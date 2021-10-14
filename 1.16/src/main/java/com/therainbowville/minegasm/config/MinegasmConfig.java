package com.therainbowville.minegasm.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.fabricmc.loader.api.FabricLoader;

public class MinegasmConfig {
	private static final ObjectMapper MAPPER = new ObjectMapper();;
	private static final Logger LOGGER = LogManager.getLogger(MinegasmConfig.class);
	public static MinegasmConfig INSTANCE;
	public String serverUrl = "ws://localhost:12345/buttplug";

	public boolean vibrate = true;
	public GameplayMode mode = GameplayMode.NORMAL;
	public int attackIntensity = 60;
	public int hurtIntensity = 0;
	public int mineIntensity = 60;
	public int xpChangeIntensity = 0;
	public int harvestIntensity = 0;
	public int vitalityIntensity = 0;

	private static File getConfigFile() {
		return FabricLoader.getInstance().getConfigDir().resolve("minegasm-fabric.json").toFile();
	}

	public static void loadConfig() {
		File config = getConfigFile();
		if (!config.exists()) {
			LOGGER.warn("Config file missing, using defaults");
			INSTANCE = new MinegasmConfig();
			return;
		}
		try (FileInputStream in = new FileInputStream(config)) {
			INSTANCE = MAPPER.readValue(in, MinegasmConfig.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void saveConfig() {
		File config = getConfigFile();
		try (FileOutputStream out = new FileOutputStream(config)) {
			MAPPER.writeValue(out, INSTANCE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return "MinegasmConfig [serverUrl="
				+ serverUrl
				+ ", vibrate="
				+ vibrate
				+ ", mode="
				+ mode
				+ ", attackIntensity="
				+ attackIntensity
				+ ", hurtIntensity="
				+ hurtIntensity
				+ ", mineIntensity="
				+ mineIntensity
				+ ", xpChangeIntensity="
				+ xpChangeIntensity
				+ ", harvestIntensity="
				+ harvestIntensity
				+ ", vitalityIntensity="
				+ vitalityIntensity
				+ "]";
	}
}
