package me.vinceh121.minegasm.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.therainbowville.minegasm.client.ClientEventHandler;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixins {
	@Inject(at = @At("INVOKE"), method = "onDeath(Lnet/minecraft/entity/damage/DamageSource;)V")
	private void onGoCommitDie(DamageSource source, CallbackInfo ci) {
		ClientEventHandler.onDeath((PlayerEntity) (Object) this);
	}
}
