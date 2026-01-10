package dev.xkmc.gensokyolegacy.content.attachment.home.custom;

import dev.xkmc.gensokyolegacy.content.attachment.home.core.HomeSearchUtil;
import dev.xkmc.gensokyolegacy.content.attachment.home.core.IHomeHolder;
import dev.xkmc.gensokyolegacy.content.attachment.home.core.StructureAttachment;
import dev.xkmc.gensokyolegacy.content.attachment.index.StructureKey;
import dev.xkmc.gensokyolegacy.content.client.structure.StructureBoundUpdateToClient;
import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import dev.xkmc.gensokyolegacy.init.registrate.GLMeta;
import dev.xkmc.l2serial.network.SimplePacketBase;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public record CustomHomeHolder(
		ServerLevel level, LevelChunk chunk, StructureKey key,
		StructureAttachment attachment, CustomHomeData data
) implements IHomeHolder {

	@Nullable
	public static CustomHomeHolder create(ServerLevel level, BlockPos pos) {
		if (!level.isLoaded(pos)) return null;
		var chunk = level.getChunkAt(pos);
		var att = chunk.getData(GLMeta.STRUCTURE.get());
		var home = new CustomHomeData();
		att.custom.put(pos, home);
		return new CustomHomeHolder(level, chunk, StructureKey.custom(level.dimension(), pos), att, home);
	}


	@Nullable
	public static CustomHomeHolder of(ServerLevel level, BlockPos pos) {
		if (!level.isLoaded(pos)) return null;
		var chunk = level.getChunkAt(pos);
		var att = chunk.getData(GLMeta.STRUCTURE.get());
		var home = att.custom.get(pos);
		if (home == null) return null;
		return new CustomHomeHolder(level, chunk, StructureKey.custom(level.dimension(), pos), att, home);
	}

	public boolean isValid() {
		return level.isLoaded(key.pos()) && data.checkInit(this);
	}

	@Override
	public boolean supportEntity(EntityType<?> type) {
		return true;
	}

	public boolean isInRoom(BlockPos pos) {
		if (!data.checkInit(this)) return false;
		return data.getRoomBound().isInside(pos);
	}

	@Nullable
	public Vec3 getRandomPosInRoom(YoukaiEntity e) {
		if (!data.checkInit(this)) return null;
		return HomeSearchUtil.getRandomPos(data.getRoomBound(), e, ans -> !data.isOutside(e.level(), ans));
	}

	@Nullable
	public Vec3 getRandomPosInBound(YoukaiEntity e) {
		if (!data.checkInit(this)) return null;
		return HomeSearchUtil.getRandomPos(data.getTotalBound(), e, ans -> true);
	}

	@Nullable
	public BlockPos getContainersAround(BlockPos pos) {
		if (!data.checkInit(this)) return null;
		return data.getContainerAround(this, pos, 3, 3, 32);
	}

	@Nullable
	public BlockPos getChairsAround(BlockPos pos) {
		if (!data.checkInit(this)) return null;
		return data.getChairAround(this, pos, 3, 3, 12);
	}

	@Nullable
	public BlockPos getWanderCenter() {
		if (!data.checkInit(this)) return null;
		var box = data.getTotalBound();
		return box.getCenter();
	}

	public int getWanderBaseRadius() {
		if (!data.checkInit(this)) return 0;
		var box = data.getTotalBound();
		return Math.min(box.getXSpan() / 2, box.getZSpan() / 2);
	}

	public void tick() {
		if (!data.checkInit(this)) return;
		data.tick(this);
	}

	@Override
	public SimplePacketBase toBoundPacket() {
		return new StructureBoundUpdateToClient(
				key(), data().getTotalBound(),
				data().getHouseBound(),
				data().getRoomBound()
		);
	}

	@Override
	public SimplePacketBase getUpdatePacket() {
		return data().getAbnormality(key);
	}

}
