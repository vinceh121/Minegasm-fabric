package me.vinceh121.minegasm.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.therainbowville.minegasm.client.ClientEventHandler;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientLevel.ClientLevelData;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;

@Mixin(ClientLevel.class)
public class ClientWorldMixins {
	@Inject(at = @At("TAIL"), method = "<init>")
	public void onWorldLoad(ClientPacketListener connection, ClientLevelData levelData, ResourceKey<Level> dimension,
			Holder<DimensionType> dimensionType, int serverChunkRadius, int serverSimulationDistance,
			LevelRenderer levelRenderer, boolean isDebug, long biomeZoomSeed, int seaLevel, CallbackInfo ci) {
		ClientEventHandler.onWorldLoaded((Level) (Object) this);
	}

	@Inject(at = @At("TAIL"), method = "removeEntity", locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	public void onRemoveEntity(int entityId, Entity.RemovalReason removalReason, CallbackInfo ci, Entity entity) {
		ClientEventHandler.onWorldExit(entity);
	}

	@Inject(at = @At("HEAD"), method = "addEntity")
	public void onEntityAdded(Entity entity, CallbackInfo ci) {
		ClientEventHandler.onWorldEntry(entity);
	}
}
