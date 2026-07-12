package me.vinceh121.minegasm.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.therainbowville.minegasm.client.ClientEventHandler;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(MultiPlayerGameMode.class)
public class ClientPlayerInteractionManagerMixins {
	@Shadow
	@Final
	private Minecraft minecraft;

	@Inject(method = "destroyBlock(Lnet/minecraft/core/BlockPos;)Z", at = @At("HEAD"))
	private void onBreak(BlockPos pos, CallbackInfoReturnable<Boolean> ci) {
		BlockState state = this.minecraft.level.getBlockState(pos);
		ClientEventHandler.onBreak(minecraft.player, state);
	}

	@Inject(at = @At("RETURN"), method = "continueDestroyBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)Z")
	private void onHarvestCheck(BlockPos pos, Direction dic, CallbackInfoReturnable<Boolean> ci) {
		BlockState state = this.minecraft.level.getBlockState(pos);

		ClientEventHandler.onHarvest(this.minecraft.player, state, this.minecraft.player.hasCorrectToolForDrops(state));
	}
}
