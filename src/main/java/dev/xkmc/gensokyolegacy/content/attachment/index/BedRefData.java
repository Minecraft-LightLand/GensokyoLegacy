package dev.xkmc.gensokyolegacy.content.attachment.index;

import dev.xkmc.gensokyolegacy.content.attachment.chunk.HomeHolder;
import dev.xkmc.gensokyolegacy.content.attachment.datamap.BedData;
import dev.xkmc.gensokyolegacy.content.attachment.datamap.CharacterConfig;
import dev.xkmc.gensokyolegacy.content.block.bed.YoukaiBedBlockEntity;
import dev.xkmc.gensokyolegacy.content.client.debug.BlockInfoToClient;
import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import dev.xkmc.gensokyolegacy.init.data.GLLang;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

@SerialClass
public class BedRefData {

	@Nullable
	public static BedRefData of(ServerLevel sl, StructureKey key, EntityType<?> type) {
		return IndexStorage.get(sl).getOrCreate(key).bedOf(type);
	}

	public static Optional<BedRefData> of(ServerLevel sl, YoukaiEntity e) {
		return StructureKey.of(e).map(k -> of(sl, k, e.getType()));
	}

	@SerialField
	private UUID entityId = Util.NIL_UUID;
	@SerialField
	private BlockPos bedPos = null;
	@SerialField
	private long lastEntityTickedTime = -1000000;
	@SerialField
	private long lostEntityTick = 0;

	@Nullable
	public BlockPos getBedPos() {
		return bedPos;
	}

	void blockTick(BedData bed, CharacterConfig config, ServerLevel sl, YoukaiBedBlockEntity be, StructureKey key) {
		var home = HomeHolder.of(sl, key);
		if (home == null || !home.isInRoom(be.getBlockPos())) {
			sl.removeBlock(be.getBlockPos(), false);
			return;
		}
		if (bedPos == null) {
			bedPos = be.getBlockPos();
		} else {
			if (!be.getBlockPos().equals(bedPos)) {
				if (sl.isLoaded(bedPos) && sl.getBlockState(bedPos).getBlock() != be.getBlockState().getBlock()) {
					sl.removeBlock(bedPos, false);
					bedPos = be.getBlockPos();
				} else {
					sl.removeBlock(be.getBlockPos(), false);
					bedPos = null;
					return;
				}
			}
		}
		long time = sl.getGameTime();
		if (time >= lastEntityTickedTime + lostEntityTick) {
			lostEntityTick++;
		} else {
			lostEntityTick = time - lastEntityTickedTime;
		}
		if (lostEntityTick > config.discardTime()) {
			entityId = Util.NIL_UUID;
		}
		if (entityId.equals(Util.NIL_UUID) && time - lastEntityTickedTime > config.respawnTime()) {
			var entity = config.create(bed.type(), sl, bedPos, key);
			if (entity != null) {
				sl.addFreshEntity(entity);
				entityId = entity.getUUID();
				lostEntityTick = 0;
				lastEntityTickedTime = sl.getGameTime();
			}
		}
	}

	public void entityTick(ServerLevel sl, YoukaiEntity self) {
		if (entityId.equals(self.getUUID())) {
			lastEntityTickedTime = sl.getGameTime();
		} else {
			self.discard();
		}
	}

	public void onEntityDie(ServerLevel sl, YoukaiEntity self) {
		entityId = Util.NIL_UUID;
	}

	public void removeEntity(ServerLevel sl, YoukaiEntity self) {
		entityId = Util.NIL_UUID;
		self.discard();
	}

	public void onDebugClick(ServerPlayer sp, CharacterConfig config) {
		if (sp.serverLevel().getEntity(entityId) instanceof YoukaiEntity e) {
			e.discard();
		}
		entityId = Util.NIL_UUID;
		lastEntityTickedTime = sp.level().getGameTime() - config.respawnTime();
		sp.sendSystemMessage(GLLang.MSG_RESET.get());
	}

	public BlockInfoToClient getDebugPacket(ServerLevel sl, CharacterConfig config, StructureKey key) {
		if (entityId.equals(Util.NIL_UUID)) {
			long diff = lastEntityTickedTime + config.respawnTime() - sl.getGameTime();
			return BlockInfoToClient.of(GLLang.INFO_BED_RESPAWN.time(diff).withStyle(ChatFormatting.GRAY));
		}
		if (!(sl.getEntity(entityId) instanceof YoukaiEntity youkai)) {
			long diff = sl.getGameTime() - lastEntityTickedTime;
			return BlockInfoToClient.of(GLLang.INFO_BED_MISSING.time(diff).withStyle(ChatFormatting.GRAY));
		}
		var p = youkai.blockPosition();
		return BlockInfoToClient.of(GLLang.INFO_BED_PRESENT.get(p.getX(), p.getY(), p.getZ()).withStyle(ChatFormatting.GRAY));
	}

}
