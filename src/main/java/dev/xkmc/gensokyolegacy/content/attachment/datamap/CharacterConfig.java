package dev.xkmc.gensokyolegacy.content.attachment.datamap;

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
		int discardTime, int respawnTime
) {

	@Nullable
	public static CharacterConfig of(EntityType<?> key) {
		return BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(key).getData(GLMisc.ENTITY_DATA.reg());
	}

	@Nullable
	public YoukaiEntity create(ServerLevel sl, BlockPos pos) {
		return null;//TODO
	}

}
