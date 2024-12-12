package dev.xkmc.gensokyolegacy.content.attachment.index;

import dev.xkmc.gensokyolegacy.content.attachment.datamap.BedData;
import dev.xkmc.gensokyolegacy.content.attachment.datamap.CharacterConfig;
import dev.xkmc.gensokyolegacy.content.block.bed.YoukaiBedBlockEntity;
import dev.xkmc.gensokyolegacy.content.client.debug.BedInfoToClient;
import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import dev.xkmc.gensokyolegacy.init.data.GLLang;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@SerialClass
public class BedRefData {

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

	public void onDebugClick(ServerPlayer sp, CharacterConfig config) {
		if (sp.serverLevel().getEntity(entityId) instanceof YoukaiEntity e) {
			e.discard();
		}
		entityId = Util.NIL_UUID;
		lastEntityTickedTime = sp.level().getGameTime() - config.respawnTime();
		sp.sendSystemMessage(GLLang.MSG_RESET.get());
	}

	public BedInfoToClient getDebugPacket(ServerLevel sl, CharacterConfig config, StructureKey key) {
		if (entityId.equals(Util.NIL_UUID)) {
			return new BedInfoToClient(key, null, 0, lastEntityTickedTime + config.respawnTime());
		}
		if (!(sl.getEntity(entityId) instanceof YoukaiEntity youkai)) {
			return new BedInfoToClient(key, null, lastEntityTickedTime, 0);
		}
		return new BedInfoToClient(key, youkai.blockPosition(), 0, 0);
	}

}