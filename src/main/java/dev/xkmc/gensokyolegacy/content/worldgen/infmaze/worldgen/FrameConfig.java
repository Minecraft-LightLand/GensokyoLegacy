package dev.xkmc.gensokyolegacy.content.worldgen.infmaze.worldgen;

import net.minecraft.world.level.block.state.BlockState;

public record FrameConfig(BlockState air, BlockState bedrock, BlockState hard, BlockState wall) {
}
