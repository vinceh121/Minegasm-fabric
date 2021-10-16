package me.vinceh121.minegasm.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.therainbowville.minegasm.client.ClientEventHandler;

import net.minecraft.block.BlockState;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixins {
	@Shadow
	public ServerPlayerEntity player;
	@Shadow
	public ServerWorld world;

	@Inject(method = "tryBreakBlock(Lnet/minecraft/util/math/BlockPos;)Z", at = @At("HEAD"))
	private void onBreak(BlockPos pos, CallbackInfoReturnable<Boolean> ci) {
		BlockState state = world.getBlockState(pos);
		ClientEventHandler.onBreak(player, state);
	}
}
