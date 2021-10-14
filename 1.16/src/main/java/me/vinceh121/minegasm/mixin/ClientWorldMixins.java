package me.vinceh121.minegasm.mixin;

import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.therainbowville.minegasm.client.ClientEventHandler;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

@Mixin(ClientWorld.class)
public class ClientWorldMixins {
	@Inject(at = @At("TAIL"), method = "<init>()V")
	public void onWorldLoad(ClientPlayNetworkHandler clientPlayNetworkHandler, ClientWorld.Properties properties,
			RegistryKey<World> registryKey, DimensionType dimensionType, int i, Supplier<Profiler> supplier,
			WorldRenderer worldRenderer, boolean bl, long l, CallbackInfo ci) {
		ClientEventHandler.onWorldLoaded((World)(Object)this);
	}
}
