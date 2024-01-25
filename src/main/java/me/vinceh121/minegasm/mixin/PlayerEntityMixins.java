package me.vinceh121.minegasm.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.therainbowville.minegasm.client.ClientEventHandler;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixins {
	@Inject(at = @At("RETURN"), method = "canHarvest(Lnet/minecraft/block/BlockState;)Z")
	private void onHarvestCheck(BlockState state, CallbackInfoReturnable<Boolean> ci) {
		ClientEventHandler.onHarvest((PlayerEntity) (Object) this, state, ci.getReturnValueZ());
	}
}
