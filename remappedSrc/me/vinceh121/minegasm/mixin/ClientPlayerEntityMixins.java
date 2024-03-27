package me.vinceh121.minegasm.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.therainbowville.minegasm.client.ClientEventHandler;

import net.minecraft.client.network.ClientPlayerEntity;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixins {
	@Inject(at = @At("HEAD"), method = "setExperience(FII)V")
	private void onXpAdded(float progress, int total, int level, CallbackInfo ci) {
		ClientPlayerEntity thos = (ClientPlayerEntity) (Object) this;
		ClientEventHandler.onXpChange(thos, level - thos.experienceLevel);
	}

	@Inject(at = @At("HEAD"), method = "updatePostDeath()V")
	private void onDeath(CallbackInfo ci) { // this gets called every tick when an entity is dead, but still present,
											// shouldn't be a problem
		ClientEventHandler.onDeath((ClientPlayerEntity) (Object) this);
	}

	@Inject(at = @At("HEAD"), method = "requestRespawn()V")
	private void onRespawn(CallbackInfo ci) {
		ClientEventHandler.onRespawn();
	}
}
