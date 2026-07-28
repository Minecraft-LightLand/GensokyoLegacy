package dev.xkmc.gensokyolegacy.content.block.misc;

import dev.xkmc.l2modularblock.mult.PlacementBlockMethod;
import dev.xkmc.l2modularblock.mult.SurviveBlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public record SurviveImpl() implements SurviveBlockMethod, PlacementBlockMethod {

	@Override
	public @Nullable BlockState getStateForPlacement(BlockState state, BlockPlaceContext ctx) {
		if (!canSurvive(state, ctx.getLevel(), ctx.getClickedPos()))
			return null;
		return state;
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		return Block.canSupportCenter(level, pos.below(), Direction.UP);
	}

}
