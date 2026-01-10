package dev.xkmc.gensokyolegacy.content.attachment.home.core;

import dev.xkmc.gensokyolegacy.content.attachment.home.structure.HomeHolder;
import dev.xkmc.gensokyolegacy.content.attachment.index.StructureKey;
import dev.xkmc.gensokyolegacy.content.entity.youkai.SmartYoukaiEntity;
import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import dev.xkmc.l2serial.network.SimplePacketBase;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public interface IHomeHolder {

	@Nullable
	static IHomeHolder of(ServerLevel sl, SmartYoukaiEntity entity) {
		var key = StructureKey.of(entity);
		if (key.isPresent()) return HomeHolder.of(sl, key.get());
		return null;
	}

	@Nullable
	static IHomeHolder find(ServerLevel sl, BlockPos pos) {
		return HomeHolder.find(sl, pos);
	}

	@Nullable
	static IHomeHolder of(ServerLevel sl, StructureKey key) {
		return HomeHolder.of(sl, key);
	}

	StructureKey key();

	boolean isValid();

	boolean supportEntity(EntityType<?> type);

	boolean isInRoom(BlockPos pos);

	@Nullable
	Vec3 getRandomPosInRoom(YoukaiEntity e);

	@Nullable
	Vec3 getRandomPosInBound(YoukaiEntity e);

	@Nullable
	BlockPos getContainersAround(BlockPos pos);

	@Nullable
	BlockPos getChairsAround(BlockPos pos);

	@Nullable
	BlockPos getWanderCenter();

	int getWanderBaseRadius();


	void tick();

	SimplePacketBase toBoundPacket();

	SimplePacketBase getUpdatePacket();

}
