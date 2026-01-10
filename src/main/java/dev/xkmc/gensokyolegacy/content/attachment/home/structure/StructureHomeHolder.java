package dev.xkmc.gensokyolegacy.content.attachment.home.structure;

import dev.xkmc.gensokyolegacy.content.attachment.datamap.StructureConfig;
import dev.xkmc.gensokyolegacy.content.attachment.home.core.HomeSearchUtil;
import dev.xkmc.gensokyolegacy.content.attachment.home.core.IFixableHomeHolder;
import dev.xkmc.gensokyolegacy.content.attachment.home.core.PerformanceConstants;
import dev.xkmc.gensokyolegacy.content.attachment.home.core.StructureAttachment;
import dev.xkmc.gensokyolegacy.content.attachment.index.StructureKey;
import dev.xkmc.gensokyolegacy.content.client.structure.StructureBoundUpdateToClient;
import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import dev.xkmc.gensokyolegacy.init.registrate.GLEntities;
import dev.xkmc.gensokyolegacy.init.registrate.GLMeta;
import dev.xkmc.l2serial.network.SimplePacketBase;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashSet;
import java.util.List;

public record StructureHomeHolder(
		ServerLevel level, LevelChunk chunk, StructureKey key,
		StructureConfig config, StructureAttachment attachment, StructureHomeData data
) implements IFixableHomeHolder {

	@Nullable
	public static StructureHomeHolder of(ServerLevel level, StructureKey key) {
		if (!level.isLoaded(key.pos())) return null;
		var structure = level.registryAccess().holder(key.getStructure());
		if (structure.isEmpty()) return null;
		var config = structure.get().getData(GLMeta.STRUCTURE_DATA.reg());
		if (config == null) {
			LinkedHashSet<EntityType<?>> entities = new LinkedHashSet<>();
			entities.add(GLEntities.CIRNO.get());
			//TODO 结构自定义
			config = new StructureConfig(entities,
					1, 1, 1, 0, 0, 0,
					null, null, null);
		}
		var chunk = level.getChunkAt(key.pos());
		var att = chunk.getData(GLMeta.STRUCTURE.get());
		var home = att.data.computeIfAbsent(key, k -> new StructureHomeData());
		return new StructureHomeHolder(level, chunk, key, config, att, home);
	}

	public boolean isValid() {
		return level.isLoaded(key.pos()) && data.checkInit(this);
	}

	@Override
	public boolean supportEntity(EntityType<?> type) {
		return config().entities().contains(type);
	}

	public boolean isInRoom(BlockPos pos) {
		if (!data.checkInit(this)) return false;
		return data.getRoomBound(config).isInside(pos);
	}

	@Nullable
	public Vec3 getRandomPosInRoom(YoukaiEntity e) {
		if (!data.checkInit(this)) return null;
		return HomeSearchUtil.getRandomPos(data.getRoomBound(config), e, ans -> !config.isOutside(e.level(), ans));
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

	public List<BlockFix> popFix(int count, FixStage stage) {
		if (!data.checkInit(this)) return List.of();
		return data.popFix(count, stage);
	}

	public int doFix(int count, FixStage stage) {
		if (!data.checkInit(this)) return 0;
		var list = data.popFix(count, stage);
		for (var e : list) {
			e.fix(level);
		}
		return list.size();
	}

	public boolean isBroken() {
		if (!data.checkInit(this)) return false;
		return data.getBrokenCount() >= PerformanceConstants.COMMAND_PLACE_STEP;
	}

	@Override
	public SimplePacketBase toBoundPacket() {
		return new StructureBoundUpdateToClient(
				key(), data().getTotalBound(),
				data().getHouseBound(config()),
				data().getRoomBound(config())
		);
	}

	@Override
	public SimplePacketBase getUpdatePacket() {
		return data().getAbnormality(key);
	}

}
