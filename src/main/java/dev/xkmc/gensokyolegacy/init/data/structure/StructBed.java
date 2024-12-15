package dev.xkmc.gensokyolegacy.init.data.structure;

import dev.xkmc.gensokyolegacy.content.attachment.datamap.CharacterConfig;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;

public record StructBed(
		Holder<EntityType<?>> entity,
		Holder<Block> bed,
		CharacterConfig data
) {
}
