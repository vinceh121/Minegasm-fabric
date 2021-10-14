package me.vinceh121.minegasm.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.authlib.GameProfile;
import com.therainbowville.minegasm.client.ClientEventHandler;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixins {
	@Shadow
	public abstract GameProfile getGameProfile();

	@Inject(at = @At("INVOKE"), method = "applyDamage(Lnet/minecraft/entity/damage/DamageSourceF;)V")
	private void playerReceivedDamage(DamageSource source, float health, CallbackInfo ci) {
		ClientEventHandler.onHurt(getGameProfile());
	}
	@Inject(at = @At("TAIL"), method = "tick()V")
	private void playerTick(CallbackInfo ci) {
		ClientEventHandler.onPlayerTick((PlayerEntity)(Object)this);
	}
}
