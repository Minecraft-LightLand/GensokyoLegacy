package dev.xkmc.gensokyolegacy.content.block.bed;

import dev.xkmc.gensokyolegacy.content.attachment.datamap.BedData;
import dev.xkmc.gensokyolegacy.content.attachment.datamap.CharacterConfig;
import dev.xkmc.gensokyolegacy.content.attachment.index.IndexStorage;
import dev.xkmc.gensokyolegacy.content.attachment.index.StructureKey;
import dev.xkmc.gensokyolegacy.content.client.debug.BedInfoToClient;
import dev.xkmc.gensokyolegacy.init.registrate.GLMisc;
import dev.xkmc.l2core.base.tile.BaseBlockEntity;
import dev.xkmc.l2modularblock.tile_api.TickableBlockEntity;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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
					if (start.getPieces().isEmpty()) continue;
					var root = start.getPieces().getFirst().getLocatorPosition();
					var data = id.get().getData(GLMisc.STRUCTURE_DATA.reg());
					var bed = BedData.of(getBlockState().getBlock());
					if (data != null && bed != null && data.entities().contains(bed.type()))
						key = new StructureKey(id.get().key(), sl.dimension(), root);
				}
			}
			if (key != null && getBlockState().getValue(BedBlock.PART) == BedPart.HEAD) {
				var data = BedData.of(getBlockState().getBlock());
				if (data != null) {
					var config = CharacterConfig.of(data.type());
					if (config != null && config.structure().equals(key.getStructure().location())) {
						IndexStorage.get(sl).getOrCreate(key).data().blockTick(sl, data, this, key);
					}
				}
			}
		}
	}

	public ResourceLocation getTexture() {
		return BuiltInRegistries.BLOCK.getKey(getBlockState().getBlock()).withPrefix("entity/bed/");
	}

	public void debugClick(ServerPlayer sp) {
		if (key == null || getBlockState().getValue(BedBlock.PART) == BedPart.FOOT) return;
		var data = BedData.of(getBlockState().getBlock());
		if (data == null) return;
		var sl = sp.serverLevel();
		var config = CharacterConfig.of(data.type());
		if (config == null || !config.structure().equals(key.getStructure().location())) return;
		var bed = IndexStorage.get(sl).getOrCreate(key).bedOf(data.type());
		if (bed == null) return;
		bed.onDebugClick(sp, config);
	}

	public BedInfoToClient getDebugPacket() {
		if (key == null || !(level instanceof ServerLevel sl)) {
			return new BedInfoToClient(null, null, 0, 0);
		}
		var data = BedData.of(getBlockState().getBlock());
		if (data != null) {
			var config = CharacterConfig.of(data.type());
			if (config != null && config.structure().equals(key.getStructure().location())) {
				var bed = IndexStorage.get(sl).getOrCreate(key).bedOf(data.type());
				if (bed != null) {
					return bed.getDebugPacket(sl, config, key);
				}
			}
		}
		return new BedInfoToClient(key, null, 0, 0);
	}

}
