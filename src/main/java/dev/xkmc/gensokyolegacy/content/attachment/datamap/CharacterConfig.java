package dev.xkmc.gensokyolegacy.content.attachment.datamap;

import dev.xkmc.gensokyolegacy.content.attachment.index.StructureKey;
import dev.xkmc.gensokyolegacy.content.entity.module.HomeModule;
import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import dev.xkmc.gensokyolegacy.init.registrate.GLMisc;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.Nullable;

public record CharacterConfig(
		ResourceLocation structure,
		int discardTime, int respawnTime, int wanderRadius,
		int xzRadius, int yRadius, int noPlayerVanishTime
) {

	@Nullable
	public static CharacterConfig of(EntityType<?> key) {
		return BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(key).getData(GLMisc.ENTITY_DATA.reg());
	}

	@Nullable
	public YoukaiEntity create(EntityType<?> type, ServerLevel sl, BlockPos pos, StructureKey key) {
		var ans = type.create(sl);
		if (!(ans instanceof YoukaiEntity youkai)) return null;
		youkai.setPos(pos.getCenter());
		youkai.getModule(HomeModule.class).ifPresent(e -> e.setHome(key));
		youkai.initSpellCard();
		youkai.restrictTo(pos, wanderRadius);
		return youkai;
	}

}
