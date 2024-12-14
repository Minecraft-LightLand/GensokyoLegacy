package dev.xkmc.gensokyolegacy.content.attachment.chunk;

import dev.xkmc.gensokyolegacy.content.attachment.index.StructureKey;
import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import dev.xkmc.gensokyolegacy.init.registrate.GLMisc;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.ai.util.RandomPos;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public record HomeHolder(
		ServerLevel level, LevelChunk chunk, StructureKey key, StructureAttachment attachment, HomeData data
) {

	@Nullable
	public static HomeHolder of(ServerLevel level, StructureKey key) {
		if (!level.isLoaded(key.pos())) return null;
		var chunk = level.getChunkAt(key.pos());
		var att = chunk.getData(GLMisc.STRUCTURE.get());
		var home = att.data.computeIfAbsent(key, k -> new HomeData());
		return new HomeHolder(level, chunk, key, att, home);
	}

	public boolean isValid() {
		return level.isLoaded(key.pos()) && data.checkInit(this);
	}

	@Nullable
	public Vec3 getRandomPosInRoom(YoukaiEntity e) {
		if (!data.checkInit(this)) return null;
		var rand = e.getRandom();
		var bound = data.getBound();
		return RandomPos.generateRandomPos(e, () -> {
			int x = rand.nextInt(bound.minX() + 1, bound.maxX());
			int y = rand.nextInt(bound.minY(), bound.maxY() - 1);
			int z = rand.nextInt(bound.minZ() + 1, bound.maxZ());
			var ans = new BlockPos(x, y, z);
			if (!e.getNavigation().isStableDestination(ans)) return null;
			ans = LandRandomPos.movePosUpOutOfSolid(e, ans);
			if (ans != null && bound.isInside(ans)) return ans;
			return null;
		});
	}

	@Nullable
	public BlockPos getContainersAround(BlockPos pos) {
		if (!data.checkInit(this)) return null;
		return data.getContainerAround(level, pos, 3, 1, 32);
	}

}
