package com.therainbowville.minegasm.config;

import java.util.Objects;

import com.therainbowville.minegasm.common.Minegasm;

public enum GameplayMode {
	NORMAL("gui." + Minegasm.MOD_ID + ".config.mode.normal"),
	MASOCHIST("gui." + Minegasm.MOD_ID + ".config.mode.masochist"),
	HEDONIST("gui." + Minegasm.MOD_ID + ".config.mode.hedonist"),
	CUSTOM("gui." + Minegasm.MOD_ID + ".config.mode.custom");

	private final String translateKey;

	GameplayMode(String translateKey) {
		this.translateKey = Objects.requireNonNull(translateKey, "translateKey");
	}

	public String getTranslateKey() {
		return this.translateKey;
	}
}
