package dev.xkmc.gensokyolegacy.content.attachment.index;

import dev.xkmc.gensokyolegacy.content.attachment.datamap.CharacterConfig;
import dev.xkmc.gensokyolegacy.content.block.bed.YoukaiBedBlockEntity;
import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

import java.util.UUID;

@SerialClass
public class BedRefData {

	@SerialField
	private UUID entityId = Util.NIL_UUID;
	@SerialField
	private BlockPos bedPos = null;
	@SerialField
	private long lastEntityTickedTime = 0;
	@SerialField
	private long lostEntityTick = 0;

	void blockTick(CharacterConfig config, ServerLevel sl, YoukaiBedBlockEntity be) {
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
			var entity = config.create(sl, bedPos);
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

}
