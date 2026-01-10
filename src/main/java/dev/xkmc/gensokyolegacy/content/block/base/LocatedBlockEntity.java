package dev.xkmc.gensokyolegacy.content.block.base;

import dev.xkmc.gensokyolegacy.content.attachment.datamap.BedData;
import dev.xkmc.gensokyolegacy.content.attachment.home.core.IHomeHolder;
import dev.xkmc.gensokyolegacy.content.attachment.index.StructureKey;
import dev.xkmc.l2core.base.tile.BaseBlockEntity;
import dev.xkmc.l2modularblock.tile_api.TickableBlockEntity;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

@SerialClass
public class LocatedBlockEntity extends BaseBlockEntity implements TickableBlockEntity {

	@SerialField
	protected StructureKey key;
	@SerialField
	protected boolean located = false;

	public LocatedBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void tick() {
		if (level instanceof ServerLevel sl) {
			if (!located) {
				located = true;
				var home = IHomeHolder.find(sl, getBlockPos());
				var bed = BedData.of(getBlockState().getBlock());
				if (home != null && bed != null && home.supportEntity(bed.type()))
					key = home.key();
			}
		}
	}

	public boolean linked() {
		return key != null;
	}

}
