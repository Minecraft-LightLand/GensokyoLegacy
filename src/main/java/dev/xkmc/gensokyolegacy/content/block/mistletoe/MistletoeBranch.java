package dev.xkmc.gensokyolegacy.content.block.mistletoe;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;

public class MistletoeBranch extends Item {

	public MistletoeBranch(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		var level = ctx.getLevel();
		var pos = ctx.getClickedPos();
		var state = level.getBlockState(pos);
		if (!MistletoeLeavesBlock.isSpreadable(level, state, pos))
			return InteractionResult.PASS;
		if (!level.isClientSide()) {
			level.setBlockAndUpdate(pos, MistletoeLeavesBlock.copyState(state));
		}
		level.playSound(ctx.getPlayer(), pos, SoundEvents.GRASS_PLACE, SoundSource.BLOCKS, 1, 0.8f);
		return InteractionResult.SUCCESS;
	}

}
