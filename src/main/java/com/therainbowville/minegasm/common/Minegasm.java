package com.therainbowville.minegasm.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.therainbowville.minegasm.client.ClientEventHandler;
import com.therainbowville.minegasm.client.ToyController;
import com.therainbowville.minegasm.config.MinegasmConfig;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;

public class Minegasm implements ModInitializer {

	public static Logger LOGGER = LogManager.getLogger(Minegasm.class);

	public static final String MOD_ID = "minegasm";
	public static final String MOD_NAME = "Minegasm";

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing");
		MinegasmConfig.loadConfig();
		LOGGER.info(MinegasmConfig.INSTANCE.toString());
		AttackEntityCallback.EVENT.register(ClientEventHandler::onAttack);

		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
			dispatcher.register(ClientCommandManager.literal("minegasm-reconnect").executes(o -> {
				ToyController.connectDevice();
				return 0;
			}));
		});
	}

}
