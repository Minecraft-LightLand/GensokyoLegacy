package dev.xkmc.gensokyolegacy.content.block.bed;

import dev.xkmc.gensokyolegacy.content.attachment.home.core.IHomeHolder;
import dev.xkmc.gensokyolegacy.content.attachment.datamap.BedData;
import dev.xkmc.gensokyolegacy.content.attachment.datamap.CharacterConfig;
import dev.xkmc.gensokyolegacy.content.attachment.index.BedRefData;
import dev.xkmc.gensokyolegacy.content.attachment.index.IndexStorage;
import dev.xkmc.gensokyolegacy.content.block.base.IDebugInfoBlockEntity;
import dev.xkmc.gensokyolegacy.content.block.base.LocatedBlockEntity;
import dev.xkmc.gensokyolegacy.content.client.debug.BlockInfoToClient;
import dev.xkmc.gensokyolegacy.init.data.GLLang;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;

@SerialClass
public class YoukaiBedBlockEntity extends LocatedBlockEntity implements IDebugInfoBlockEntity {

	public YoukaiBedBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void tick() {
		super.tick();
		if (level instanceof ServerLevel sl) {
			if (key != null && getBlockState().getValue(BedBlock.PART) == BedPart.HEAD) {
				var data = BedData.of(getBlockState().getBlock());
				if (data != null) {
					var config = CharacterConfig.of(data.type());
					if (config != null && config.structure().equals(key.structure())) {
						IndexStorage.get(sl).getOrCreate(key).data().blockTick(sl, data, this, key);
					}
				}
				var home = IHomeHolder.of(sl, key);
				if (home != null) {
					home.tick();
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
		var bed = BedRefData.of(sl, key, data.type());
		if (bed == null) return;
		bed.onDebugClick(sp, config);
	}

	public BlockInfoToClient getDebugPacket(ServerPlayer sp) {
		var sl = sp.serverLevel();
		if (key == null) {
			return BlockInfoToClient.of(GLLang.INFO$BED_UNBOUND.get().withStyle(ChatFormatting.RED));
		}
		var data = BedData.of(getBlockState().getBlock());
		if (data != null) {
			var config = CharacterConfig.of(data.type());
			if (config != null && config.structure().equals(key.getStructure().location())) {
				var bed = BedRefData.of(sl, key, data.type());
				if (bed != null) {
					return bed.getDebugPacket(sl, config, key);
				}
			}
		}
		return BlockInfoToClient.empty();
	}

}
