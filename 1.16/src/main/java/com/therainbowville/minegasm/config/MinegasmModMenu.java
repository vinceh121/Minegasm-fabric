package com.therainbowville.minegasm.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.LiteralText;

public class MinegasmModMenu implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return this::makeConfig;
	}

	private Screen makeConfig(Screen parent) {
		ConfigBuilder builder = ConfigBuilder.create()
				.setParentScreen(parent)
				.setTitle(new LiteralText("Minegasm Settings"))
				.setSavingRunnable(this::saveConfig);

		ConfigEntryBuilder entryBuild = builder.entryBuilder();

		builder.getOrCreateCategory(new LiteralText("General Settings"))
				.addEntry(entryBuild.startStrField(new LiteralText("Server URL"), MinegasmConfig.serverUrl)
						.setDefaultValue("ws://localhost:12345/buttplug")
						.setSaveConsumer(s -> MinegasmConfig.serverUrl = s)
						.build())
				.addEntry(entryBuild.startBooleanToggle(new LiteralText("Vibrate"), MinegasmConfig.vibrate)
						.setDefaultValue(true)
						.setSaveConsumer(v -> MinegasmConfig.vibrate = v)
						.build())
				.addEntry(entryBuild
						.startEnumSelector(new LiteralText("Gameplay Mode"), GameplayMode.class, MinegasmConfig.mode)
						.setSaveConsumer(m -> MinegasmConfig.mode = m)
						.build())
				.addEntry(entryBuild
						.startIntSlider(new LiteralText("Attack Intensity"), MinegasmConfig.attackIntensity, 0, 100)
						.setDefaultValue(60)
						.setSaveConsumer(i -> MinegasmConfig.attackIntensity = i)
						.build())
				.addEntry(entryBuild
						.startIntSlider(new LiteralText("Hurt Intensity"), MinegasmConfig.hurtIntensity, 0, 100)
						.setDefaultValue(0)
						.setSaveConsumer(i -> MinegasmConfig.hurtIntensity = i)
						.build())
				.addEntry(entryBuild
						.startIntSlider(new LiteralText("Mine Intensity"), MinegasmConfig.mineIntensity, 0, 100)
						.setDefaultValue(60)
						.setSaveConsumer(i -> MinegasmConfig.mineIntensity = i)
						.build())
				.addEntry(entryBuild
						.startIntSlider(new LiteralText("XP Changed Intensity"), MinegasmConfig.xpChangeIntensity, 0,
								100)
						.setDefaultValue(0)
						.setSaveConsumer(i -> MinegasmConfig.xpChangeIntensity = i)
						.build())
				.addEntry(entryBuild
						.startIntSlider(new LiteralText("Harvest Intensity"), MinegasmConfig.harvestIntensity, 0, 100)
						.setDefaultValue(0)
						.setSaveConsumer(i -> MinegasmConfig.harvestIntensity = i)
						.build())
				.addEntry(entryBuild
						.startIntSlider(new LiteralText("Vitality Intensity"), MinegasmConfig.vitalityIntensity, 0, 100)
						.setDefaultValue(0)
						.setSaveConsumer(i -> MinegasmConfig.vitalityIntensity = i)
						.build());

		return builder.build();
	}

	private void saveConfig() {

	}
}
