package dev.xkmc.gensokyolegacy.content.block.bed;

import dev.xkmc.gensokyolegacy.content.attachment.datamap.BedData;
import dev.xkmc.gensokyolegacy.content.attachment.datamap.CharacterConfig;
import dev.xkmc.gensokyolegacy.content.attachment.index.IndexStorage;
import dev.xkmc.gensokyolegacy.content.attachment.index.StructureKey;
import dev.xkmc.l2core.base.tile.BaseBlockEntity;
import dev.xkmc.l2modularblock.tile_api.TickableBlockEntity;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;

@SerialClass
public class YoukaiBedBlockEntity extends BaseBlockEntity implements TickableBlockEntity {

	@SerialField
	private StructureKey key;
	@SerialField
	private boolean located = false;

	public YoukaiBedBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void tick() {
		if (level instanceof ServerLevel sl) {
			var pos = getBlockPos();
			if (!located) {
				located = true;
				var manager = sl.structureManager();
				var map = manager.getAllStructuresAt(pos);
				var reg = sl.registryAccess().registryOrThrow(Registries.STRUCTURE);
				for (var e : map.keySet()) {
					var start = manager.getStructureWithPieceAt(pos, e);
					if (!start.isValid()) continue;
					var id = reg.getHolder(reg.getId(start.getStructure()));
					if (id.isEmpty()) continue;
					key = new StructureKey(id.get().key(), sl.dimension(), pos);
				}
			}
			if (key != null && getBlockState().getValue(BedBlock.PART) == BedPart.HEAD) {
				var data = BedData.of(getBlockState().getBlock());
				if (data != null) {
					var config = CharacterConfig.of(data.type());
					if (config != null && config.structure().equals(key.getStructure().location())) {
						IndexStorage.get(sl).getOrCreate(key).data().blockTick(sl, data, this);
					}
				}
			}
		}
	}

	public ResourceLocation getTexture() {
		return BuiltInRegistries.BLOCK.getKey(getBlockState().getBlock()).withPrefix("entity/bed/");
	}

}
