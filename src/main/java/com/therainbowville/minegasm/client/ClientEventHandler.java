package com.therainbowville.minegasm.client;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;

import com.mojang.authlib.GameProfile;
import com.therainbowville.minegasm.config.GameplayMode;
import com.therainbowville.minegasm.config.MinegasmConfig;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.EntityHitResult;

public class ClientEventHandler {
	private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger(ClientEventHandler.class);
	private static final int TICKS_PER_SECOND = 20;
	private static final double[] state = new double[1200];
	private static String playerName = null;
	private static int tickCounter = -1;
	private static int clientTickCounter = -1;
	private static boolean paused = false;
	private static float previousPlayerHealth = Float.NaN;

	private static void clearState() {
		playerName = null;
		tickCounter = -1;
		clientTickCounter = -1;
		Arrays.fill(state, 0);
		paused = false;
	}

	private static int getStateCounter() {
		return tickCounter / 20;
	}

	private static void setState(int start, int duration, int intensity, boolean decay) {
		if (duration <= 0) {
			return;
		}

		if (decay) {
			int safeDuration = Math.max(0, duration - 2);
			for (int i = 0; i < safeDuration; i++) {
				setState(start + i, intensity);
			}
			setState(start + safeDuration, intensity / 2);
			setState(start + safeDuration + 1, intensity / 4);
		} else {
			for (int i = 0; i < duration; i++) {
				setState(start + i, intensity);
			}
		}
	}

	private static void setState(int counter, int intensity) {
		boolean accumulate = false; // XXX reserved for future use
		setState(counter, intensity, accumulate);
	}

	private static void setState(int counter, int intensity, boolean accumulate) {
		int safeCounter = counter % state.length;
		if (accumulate) {
			state[safeCounter] = Math.min(1.0, state[safeCounter] + (intensity / 100.0));
		} else {
			state[safeCounter] = Math.min(1.0, Math.max(state[safeCounter], (intensity / 100.0)));
		}
	}

	private static int getIntensity(String type) {
		Map<String, Integer> normal = new HashMap<>();
		normal.put("attack", 60);
		normal.put("hurt", 0);
		normal.put("mine", 80);
		normal.put("xpChange", 100);
		normal.put("harvest", 0);
		normal.put("vitality", 0);

		Map<String, Integer> masochist = new HashMap<>();
		masochist.put("attack", 0);
		masochist.put("hurt", 100);
		masochist.put("mine", 0);
		masochist.put("xpChange", 0);
		masochist.put("harvest", 0);
		masochist.put("vitality", 10);

		Map<String, Integer> hedonist = new HashMap<>();
		hedonist.put("attack", 60);
		hedonist.put("hurt", 10);
		hedonist.put("mine", 80);
		hedonist.put("xpChange", 100);
		hedonist.put("harvest", 20);
		hedonist.put("vitality", 10);

		Map<String, Integer> custom = new HashMap<>();
		custom.put("attack", MinegasmConfig.INSTANCE.attackIntensity);
		custom.put("hurt", MinegasmConfig.INSTANCE.hurtIntensity);
		custom.put("mine", MinegasmConfig.INSTANCE.mineIntensity);
		custom.put("xpChange", MinegasmConfig.INSTANCE.xpChangeIntensity);
		custom.put("harvest", MinegasmConfig.INSTANCE.harvestIntensity);
		custom.put("vitality", MinegasmConfig.INSTANCE.vitalityIntensity);

		if (GameplayMode.MASOCHIST.equals(MinegasmConfig.INSTANCE.mode)) {
			return masochist.get(type);
		} else if (GameplayMode.HEDONIST.equals(MinegasmConfig.INSTANCE.mode)) {
			return hedonist.get(type);
		} else if (GameplayMode.CUSTOM.equals(MinegasmConfig.INSTANCE.mode)) {
			return custom.get(type);
		} else {
			return normal.get(type);
		}
	}

	public static InteractionResult onAttack(Player player, Level world, InteractionHand hand, Entity entity,
			EntityHitResult hitResult) {
		GameProfile profile = player.getGameProfile();

		if (profile.id().equals(getPlayerId())) {
			setState(getStateCounter(), 3, getIntensity("attack"), true);
		}
		return InteractionResult.PASS;
	}

	public static void onHurt(GameProfile profile) {
		if (profile.id().equals(getPlayerId())) {
			setState(getStateCounter(), 3, getIntensity("hurt"), true);
		}
	}

	public static void onPlayerTick(Player player) {
		GameProfile profile = player.getGameProfile();

		float playerHealth = player.getHealth();
		float playerFoodLevel = player.getFoodData().getFoodLevel();

		tickCounter = (tickCounter + 1) % (20 * (60 * TICKS_PER_SECOND)); // 20 min day cycle

		if (tickCounter % TICKS_PER_SECOND == 0) { // every 1 sec
			if (profile.id().equals(Minecraft.getInstance().getUser().getProfileId())) {
				int stateCounter = getStateCounter();

				if (GameplayMode.MASOCHIST.equals(MinegasmConfig.INSTANCE.mode)) {
					if (playerHealth > 0 && playerHealth <= 1) {
						setState(stateCounter, getIntensity("vitality"));
					}
				} else if (playerHealth >= 20 && playerFoodLevel >= 20) {
					setState(stateCounter, getIntensity("vitality"));
				}
				
				if (player.getHealth() < previousPlayerHealth) {
					onHurt(profile);
				}
				
				previousPlayerHealth = player.getHealth();

				double newVibrationLevel = state[stateCounter];
				state[stateCounter] = 0;

				LOGGER.trace("Tick " + stateCounter + ": " + newVibrationLevel);

				ToyController.setVibrationLevel(newVibrationLevel);
			}
		}

		if (tickCounter % (5 * TICKS_PER_SECOND) == 0) { // 5 secs
			LOGGER.debug("Health: " + playerHealth);
			LOGGER.debug("Food: " + playerFoodLevel);
		}
	}

	public static void onClientTick() {
		if (tickCounter >= 0) {
			if (tickCounter != clientTickCounter) {
				clientTickCounter = tickCounter;
				paused = false;
			} else {
				if (!paused) {
					paused = true;
					LOGGER.debug("Pausing");
					ToyController.setVibrationLevel(0);
				}

				if (paused) {
					LOGGER.trace("Paused");
				}
			}
		}
	}

	private static void populatePlayerInfo() {
		playerName = Minecraft.getInstance().getUser().getName();
		LOGGER.info("Current player: " + playerName + " " + getPlayerId());
	}

	public static void onWorldLoaded(Level world) {
		LOGGER.info("World loaded: " + world.toString());

		populatePlayerInfo();
	}

	public static void onDeath(Player player) {
		GameProfile profile = player.getGameProfile();

		if (profile.id().equals(getPlayerId())) {
			ToyController.setVibrationLevel(0);
		}
	}

	public static void onHarvest(Player player, BlockState blockState, boolean canHarvest) {
		GameProfile profile = player.getGameProfile();

		if (profile.id().equals(getPlayerId())) {
			Block block = blockState.getBlock();
			// ToolType. AXE, HOE, PICKAXE, SHOVEL

			float blockHardness = block.defaultBlockState().getDestroySpeed(null, null);
			// LOGGER.debug("Harvest: tool: "
			// + block.getHarvestTool(blockState)
			// + " can harvest? "
			// + event.canHarvest()
			// + " hardness: "
			// + blockHardness);

			int intensity
					= Math.toIntExact(Math.round((getIntensity("harvest") / 100.0 * (blockHardness / 50.0)) * 100));

			if (canHarvest) {
				setState(getStateCounter(), 2, intensity, false);
			}
		}
	}

	public static void onBreak(Player player, BlockState blockState) {
		GameProfile profile = player.getGameProfile();

		if (profile.id().equals(getPlayerId())) {
			Block block = blockState.getBlock();

			float blockHardness = block.defaultBlockState().getDestroySpeed(null, null);

			boolean usingAppropriateTool = player.hasCorrectToolForDrops(blockState);

			if (usingAppropriateTool) {
				int duration = Math.max(1,
						Math.min(5, Math.toIntExact(Math.round(Math.ceil(Math.log(blockHardness + 0.5))))));
				int intensity
						= Math.toIntExact(Math.round((getIntensity("mine") / 100.0 * (blockHardness / 50.0)) * 100));
				setState(getStateCounter(), duration, intensity, true);
			}
		}
	}

	public static void onRespawn() {
		clearState();
		ToyController.setVibrationLevel(0);
		populatePlayerInfo();
	}

	public static void onWorldExit(Entity entity) {
		if ((entity instanceof Player) && (playerName != null)) {
			clearState();
		}
	}

	public static void onWorldEntry(Entity entity) {
		if (entity instanceof LocalPlayer) {
			LOGGER.info("Entered world: " + entity.toString());

			if (playerName != null) {
				Player player = (Player) entity;
				GameProfile profile = player.getGameProfile();

				if (profile.id().equals(getPlayerId())) {
					LOGGER.info("Player in: " + playerName + " " + getPlayerId().toString());
					if (!ToyController.isConnected) {
						if (ToyController.connectDevice()) {
							setState(getStateCounter(), 5);
							player.sendOverlayMessage(Component.literal(String.format(
									"Connected to " + ChatFormatting.GREEN + "%s" + ChatFormatting.RESET + " [%d]",
									ToyController.getDeviceName(), ToyController.getDeviceId())));
						} else {
							player.sendSystemMessage(Component.literal(String.format(
									ChatFormatting.YELLOW + "Minegasm " + ChatFormatting.RESET + "failed to start\n%s",
									ToyController.getLastErrorMessage())));
						}
					}
				}
			}
		}
	}

	public static void onXpChange(Player player, int xpChange) {
		GameProfile profile = player.getGameProfile();

		if (profile.id().equals(getPlayerId())) {
			long duration = Math.round(Math.ceil(Math.log(xpChange + 0.5)));

			LOGGER.debug("XP CHANGE: " + xpChange);
			LOGGER.debug("duration: " + duration);

			setState(getStateCounter(), Math.toIntExact(duration), getIntensity("xpChange"), true);
		}
	}

	public static UUID getPlayerId() {
		@SuppressWarnings("resource")
		LocalPlayer player = Minecraft.getInstance().player;

		return player == null ? null : player.getUUID();
	}
}
