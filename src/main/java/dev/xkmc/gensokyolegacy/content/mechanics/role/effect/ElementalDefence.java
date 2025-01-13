package dev.xkmc.gensokyolegacy.content.mechanics.role.effect;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;

public record ElementalDefence(String tag, int start, double base, double slope) {

	public ElementalDefence(TagKey<DamageType> tag, int start, double base, double slope) {
		this("#" + tag.location(), start, base, slope);
	}

	public boolean is(DamageSource source) {
		if (tag.startsWith("#")) {
			return source.is(TagKey.create(Registries.DAMAGE_TYPE, ResourceLocation.parse(tag.substring(1))));
		} else {
			return source.is(ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.parse(tag)));
		}
	}

}
