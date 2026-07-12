package me.vinceh121.minegasm.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.therainbowville.minegasm.client.ClientEventHandler;

import net.minecraft.client.player.LocalPlayer;

@Mixin(LocalPlayer.class)
public class ClientPlayerEntityMixins {
	@Inject(at = @At("HEAD"), method = "setExperienceValues(FII)V")
	private void onXpAdded(float progress, int total, int level, CallbackInfo ci) {
		LocalPlayer thos = (LocalPlayer) (Object) this;
		ClientEventHandler.onXpChange(thos, level - thos.experienceLevel);
	}

	@Inject(at = @At("HEAD"), method = "tickDeath()V")
	private void onDeath(CallbackInfo ci) { // this gets called every tick when an entity is dead, but still present,
											// shouldn't be a problem
		ClientEventHandler.onDeath((LocalPlayer) (Object) this);
	}

	@Inject(at = @At("HEAD"), method = "respawn()V")
	private void onRespawn(CallbackInfo ci) {
		ClientEventHandler.onRespawn();
	}
}
