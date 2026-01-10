package dev.xkmc.gensokyolegacy.content.attachment.datamap;

import dev.xkmc.gensokyolegacy.content.attachment.home.core.IHomeHolder;
import dev.xkmc.gensokyolegacy.content.attachment.index.StructureKey;
import dev.xkmc.gensokyolegacy.content.entity.module.HomeModule;
import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.gensokyolegacy.init.registrate.GLMeta;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.Nullable;

public record CharacterConfig(
		ResourceLocation structure,
		int discardTime, int respawnTime, int wanderRadius, int noPlayerVanishTime
) {

	public static CharacterConfig forStructure(int discardTime, int respawnTime, int wanderRadius, int noPlayerVanishTime) {
		return new CharacterConfig(GensokyoLegacy.loc("empty"), discardTime, respawnTime, wanderRadius, noPlayerVanishTime);
	}

	public CharacterConfig withId(ResourceLocation id) {
		return new CharacterConfig(id, discardTime, respawnTime, wanderRadius, noPlayerVanishTime);
	}

	@Nullable
	public static CharacterConfig of(EntityType<?> key) {
		return BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(key).getData(GLMeta.ENTITY_DATA.reg());
	}

	@Nullable
	public YoukaiEntity create(EntityType<?> type, ServerLevel sl, BlockPos pos, StructureKey key) {
		var ans = type.create(sl);
		if (!(ans instanceof YoukaiEntity youkai)) return null;
		var home = IHomeHolder.of(sl, key);
		if (home == null) return null;
		var center = home.getWanderCenter();
		if (center == null) return null;
		youkai.setPos(pos.getCenter());
		youkai.getModule(HomeModule.class).ifPresent(e -> e.setHome(key));
		youkai.initSpellCard();
		youkai.restrictTo(center, home.getWanderBaseRadius() + wanderRadius);
		return youkai;
	}

}
