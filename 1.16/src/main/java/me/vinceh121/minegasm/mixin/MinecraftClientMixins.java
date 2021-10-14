package me.vinceh121.minegasm.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.therainbowville.minegasm.client.ClientEventHandler;

import net.minecraft.client.MinecraftClient;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixins {
	@Inject(at = @At("TAIL"), method = "tick()V")
	private void onTickEnd(CallbackInfo ci) {
		ClientEventHandler.onClientTick();
	}
}
