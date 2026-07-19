package dev.xkmc.gensokyolegacy.content.block.portal;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.portal.DimensionTransition;
import org.jetbrains.annotations.Nullable;

public interface IPortalBlockEntity {

	@Nullable DimensionTransition getPortalDestination(ServerLevel level, Entity e, BlockPos pos);

}
