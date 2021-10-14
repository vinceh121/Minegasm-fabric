package com.therainbowville.minegasm.common;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.therainbowville.minegasm.client.ClientEventHandler;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;

public class Minegasm implements ModInitializer {

	public static Logger LOGGER = LogManager.getLogger();

	public static final String MOD_ID = "minegasm";
	public static final String MOD_NAME = "Minegasm";

	@Override
	public void onInitialize() {
		log(Level.INFO, "Initializing");
		AttackEntityCallback.EVENT.register(ClientEventHandler::onAttack);
	}

	public static void log(Level level, String message) {
		LOGGER.log(level, "[" + MOD_NAME + "] " + message);
	}
}
