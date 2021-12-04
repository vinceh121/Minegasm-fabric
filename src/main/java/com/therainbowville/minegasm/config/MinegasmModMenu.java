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
				.setSavingRunnable(MinegasmConfig::saveConfig);

		ConfigEntryBuilder entryBuild = builder.entryBuilder();

		builder.getOrCreateCategory(new LiteralText("General Settings"))
				.addEntry(entryBuild.startStrField(new LiteralText("Server URL"), MinegasmConfig.INSTANCE.serverUrl)
						.setDefaultValue("ws://localhost:12345/buttplug")
						.setSaveConsumer(s -> MinegasmConfig.INSTANCE.serverUrl = s)
						.build())
				.addEntry(entryBuild.startBooleanToggle(new LiteralText("Vibrate"), MinegasmConfig.INSTANCE.vibrate)
						.setDefaultValue(true)
						.setSaveConsumer(v -> MinegasmConfig.INSTANCE.vibrate = v)
						.build())
				.addEntry(entryBuild
						.startEnumSelector(new LiteralText("Gameplay Mode"), GameplayMode.class,
								MinegasmConfig.INSTANCE.mode)
						.setDefaultValue(GameplayMode.NORMAL)
						.setSaveConsumer(m -> MinegasmConfig.INSTANCE.mode = m)
						.build())
				.addEntry(entryBuild
						.startIntSlider(new LiteralText("Attack Intensity"), MinegasmConfig.INSTANCE.attackIntensity, 0,
								100)
						.setDefaultValue(60)
						.setSaveConsumer(i -> MinegasmConfig.INSTANCE.attackIntensity = i)
						.build())
				.addEntry(entryBuild
						.startIntSlider(new LiteralText("Hurt Intensity"), MinegasmConfig.INSTANCE.hurtIntensity, 0,
								100)
						.setDefaultValue(0)
						.setSaveConsumer(i -> MinegasmConfig.INSTANCE.hurtIntensity = i)
						.build())
				.addEntry(entryBuild
						.startIntSlider(new LiteralText("Mine Intensity"), MinegasmConfig.INSTANCE.mineIntensity, 0,
								100)
						.setDefaultValue(60)
						.setSaveConsumer(i -> MinegasmConfig.INSTANCE.mineIntensity = i)
						.build())
				.addEntry(entryBuild
						.startIntSlider(new LiteralText("XP Changed Intensity"),
								MinegasmConfig.INSTANCE.xpChangeIntensity, 0, 100)
						.setDefaultValue(0)
						.setSaveConsumer(i -> MinegasmConfig.INSTANCE.xpChangeIntensity = i)
						.build())
				.addEntry(entryBuild
						.startIntSlider(new LiteralText("Harvest Intensity"), MinegasmConfig.INSTANCE.harvestIntensity,
								0, 100)
						.setDefaultValue(0)
						.setSaveConsumer(i -> MinegasmConfig.INSTANCE.harvestIntensity = i)
						.build())
				.addEntry(entryBuild
						.startIntSlider(new LiteralText("Vitality Intensity"),
								MinegasmConfig.INSTANCE.vitalityIntensity, 0, 100)
						.setDefaultValue(0)
						.setSaveConsumer(i -> MinegasmConfig.INSTANCE.vitalityIntensity = i)
						.build());

		return builder.build();
	}

}
