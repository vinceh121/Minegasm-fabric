package me.vinceh121.minegasm.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.therainbowville.minegasm.client.ClientEventHandler;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixins {
	@Shadow
	@Final
	public MinecraftClient client;

	@Inject(method = "breakBlock(Lnet/minecraft/util/math/BlockPos;)Z", at = @At("HEAD"))
	private void onBreak(BlockPos pos, CallbackInfoReturnable<Boolean> ci) {
		BlockState state = this.client.world.getBlockState(pos);
		ClientEventHandler.onBreak(client.player, state);
	}

	@Inject(at = @At("RETURN"), method = "updateBlockBreakingProgress(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;)Z")
	private void onHarvestCheck(BlockPos pos, Direction dic, CallbackInfoReturnable<Boolean> ci) {
		BlockState state = this.client.world.getBlockState(pos);

		ClientEventHandler.onHarvest(this.client.player, state, this.client.player.canHarvest(state));
	}
}
