package me.vinceh121.minegasm.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.therainbowville.minegasm.client.ClientEventHandler;

import net.minecraft.client.Minecraft;

@Mixin(Minecraft.class)
public class MinecraftClientMixins {
	@Inject(at = @At("TAIL"), method = "tick()V")
	private void onTickEnd(CallbackInfo ci) {
		@SuppressWarnings("resource")
		Minecraft thos = (Minecraft) (Object) this;

		ClientEventHandler.onClientTick();

		if (thos.player != null) {
			ClientEventHandler.onPlayerTick(thos.player);
		}
	}
}
