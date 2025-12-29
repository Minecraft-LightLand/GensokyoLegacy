package dev.xkmc.gensokyolegacy.event;

import dev.xkmc.gensokyolegacy.content.attachment.dream.DreamChunkHolder;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.gensokyolegacy.init.data.dimension.GLDimensionGen;
import dev.xkmc.gensokyolegacy.init.registrate.GLMeta;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = GensokyoLegacy.MODID)
public class DreamWorldEventHandlers {

	@SubscribeEvent
	public static void onLevelTick(LevelTickEvent.Post event) {
		var level = event.getLevel();
		if (!(level instanceof ServerLevel sl)) return;
		if (!sl.dimensionTypeRegistration().unwrapKey().orElseThrow().equals(GLDimensionGen.DT_DREAM)) return;
		for (var e : sl.getChunkSource().chunkMap.getChunks()) {
			var chunk = e.getTickingChunk();
			if (chunk == null) continue;
			var data = GLMeta.DREAM.type().getOrCreate(chunk);
			var holder = new DreamChunkHolder(chunk, data);
			holder.tick();
		}
	}

	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		var e = event.getEntity();
		if (!(e instanceof ServerPlayer sp)) return;
		var sl = sp.serverLevel();
		if (!sl.dimensionTypeRegistration().unwrapKey().orElseThrow().equals(GLDimensionGen.DT_DREAM)) return;
		var pos = sp.blockPosition();
		var holder = DreamChunkHolder.of(sl, pos.getX() >> 4, pos.getZ() >> 4);
		if (holder == null) return;
		holder.alive(sl, pos);
	}

}
