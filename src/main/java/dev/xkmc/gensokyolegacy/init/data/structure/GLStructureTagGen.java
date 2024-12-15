package dev.xkmc.gensokyolegacy.init.data.structure;

import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;

public class GLStructureTagGen {

	public static final ProviderType<RegistrateTagsProvider.Impl<Biome>> BIOME_TAG =
			ProviderType.registerDynamicTag("biome", "biome", Registries.BIOME);

	public static final TagKey<Biome> CIRNO_NEST = asTag("has_structure/cirno_nest");
	public static final TagKey<Biome> HAKUREI_SHRINE = asTag("has_structure/hakurei_shrine");

	public static TagKey<Biome> asTag(String name) {
		return TagKey.create(Registries.BIOME, GensokyoLegacy.loc(name));
	}

	public static void genBiomeTag(RegistrateTagsProvider.Impl<Biome> pvd) {
		pvd.addTag(CIRNO_NEST)
				.add(Biomes.SNOWY_PLAINS)
				.add(Biomes.ICE_SPIKES)
				.add(Biomes.FROZEN_OCEAN)
				.add(Biomes.DEEP_FROZEN_OCEAN)
				.add(Biomes.GROVE)
				.add(Biomes.FROZEN_RIVER)
				.add(Biomes.SNOWY_TAIGA)
				.add(Biomes.SNOWY_BEACH);
		pvd.addTag(HAKUREI_SHRINE)
				.add(Biomes.CHERRY_GROVE);
	}
}
