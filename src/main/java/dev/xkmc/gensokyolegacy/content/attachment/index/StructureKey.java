package dev.xkmc.gensokyolegacy.content.attachment.index;

import dev.xkmc.gensokyolegacy.content.attachment.datamap.CharacterConfig;
import dev.xkmc.gensokyolegacy.content.entity.module.HomeModule;
import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.Optional;

public record StructureKey(ResourceLocation structure, ResourceLocation dim, BlockPos pos) {

	public static final ResourceLocation CUSTOM = GensokyoLegacy.loc("custom_structure");

	public static StructureKey custom(ResourceKey<Level> dim, BlockPos pos) {
		return new StructureKey(CUSTOM, dim.location(), pos);
	}

	public StructureKey(ResourceKey<Structure> structure, ResourceKey<Level> dim, BlockPos pos) {
		this(structure.location(), dim.location(), pos);
	}

	public static Optional<StructureKey> of(YoukaiEntity e) {
		return e.getModule(HomeModule.class).map(HomeModule::home);
	}

	public ResourceKey<Structure> getStructure() {
		return ResourceKey.create(Registries.STRUCTURE, structure);
	}

	public ResourceKey<Level> getDim() {
		return ResourceKey.create(Registries.DIMENSION, dim);
	}

	public boolean support(CharacterConfig config) {
		return structure.equals(CUSTOM) || !config.structure().equals(structure);
	}
}
