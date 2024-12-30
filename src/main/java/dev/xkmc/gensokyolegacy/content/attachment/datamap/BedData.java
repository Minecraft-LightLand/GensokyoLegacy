package dev.xkmc.gensokyolegacy.content.attachment.datamap;

import dev.xkmc.gensokyolegacy.init.registrate.GLMeta;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

public record BedData(EntityType<?> type) {

	@Nullable
	public static BedData of(Block key) {
		return BuiltInRegistries.BLOCK.wrapAsHolder(key).getData(GLMeta.BED_DATA.reg());
	}

}
