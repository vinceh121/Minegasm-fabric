package com.therainbowville.minegasm.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class MinegasmModMenu implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return this::makeConfig;
	}

	private Screen makeConfig(Screen parent) {
		ConfigBuilder builder = ConfigBuilder.create()
				.setParentScreen(parent)
				.setTitle(Text.literal("Minegasm Settings"))
				.setSavingRunnable(MinegasmConfig::saveConfig);

		ConfigEntryBuilder entryBuild = builder.entryBuilder();

		builder.getOrCreateCategory(Text.literal("General Settings"))
				.addEntry(entryBuild.startStrField(Text.literal("Server URL"), MinegasmConfig.INSTANCE.serverUrl)
						.setDefaultValue("ws://localhost:12345/buttplug")
						.setSaveConsumer(s -> MinegasmConfig.INSTANCE.serverUrl = s)
						.build())
				.addEntry(entryBuild.startBooleanToggle(Text.literal("Vibrate"), MinegasmConfig.INSTANCE.vibrate)
						.setDefaultValue(true)
						.setSaveConsumer(v -> MinegasmConfig.INSTANCE.vibrate = v)
						.build())
				.addEntry(entryBuild
						.startEnumSelector(Text.literal("Gameplay Mode"), GameplayMode.class,
								MinegasmConfig.INSTANCE.mode)
						.setDefaultValue(GameplayMode.NORMAL)
						.setSaveConsumer(m -> MinegasmConfig.INSTANCE.mode = m)
						.build())
				.addEntry(entryBuild
						.startIntSlider(Text.literal("Attack Intensity"), MinegasmConfig.INSTANCE.attackIntensity, 0,
								100)
						.setDefaultValue(60)
						.setSaveConsumer(i -> MinegasmConfig.INSTANCE.attackIntensity = i)
						.build())
				.addEntry(entryBuild
						.startIntSlider(Text.literal("Hurt Intensity"), MinegasmConfig.INSTANCE.hurtIntensity, 0,
								100)
						.setDefaultValue(0)
						.setSaveConsumer(i -> MinegasmConfig.INSTANCE.hurtIntensity = i)
						.build())
				.addEntry(entryBuild
						.startIntSlider(Text.literal("Mine Intensity"), MinegasmConfig.INSTANCE.mineIntensity, 0,
								100)
						.setDefaultValue(60)
						.setSaveConsumer(i -> MinegasmConfig.INSTANCE.mineIntensity = i)
						.build())
				.addEntry(entryBuild
						.startIntSlider(Text.literal("XP Changed Intensity"),
								MinegasmConfig.INSTANCE.xpChangeIntensity, 0, 100)
						.setDefaultValue(0)
						.setSaveConsumer(i -> MinegasmConfig.INSTANCE.xpChangeIntensity = i)
						.build())
				.addEntry(entryBuild
						.startIntSlider(Text.literal("Harvest Intensity"), MinegasmConfig.INSTANCE.harvestIntensity,
								0, 100)
						.setDefaultValue(0)
						.setSaveConsumer(i -> MinegasmConfig.INSTANCE.harvestIntensity = i)
						.build())
				.addEntry(entryBuild
						.startIntSlider(Text.literal("Vitality Intensity"),
								MinegasmConfig.INSTANCE.vitalityIntensity, 0, 100)
						.setDefaultValue(0)
						.setSaveConsumer(i -> MinegasmConfig.INSTANCE.vitalityIntensity = i)
						.build());

		return builder.build();
	}

}
