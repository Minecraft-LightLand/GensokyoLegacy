package dev.xkmc.gensokyolegacy.init.data.structure;

import dev.xkmc.gensokyolegacy.content.attachment.datamap.StructureConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

import java.util.List;

public record StructStructure(
		ResourceLocation id, TagKey<Biome> biomes, int spacing, int separation,
		StructureConfig.Builder config,
		List<StructBed> beds,
		StructBuilding building) {
}
