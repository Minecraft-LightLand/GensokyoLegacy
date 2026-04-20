package dev.xkmc.alchemy.content.fluid;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

public class MixableFluid extends BaseFlowingFluid {

	public MixableFluid(Properties properties) {
		super(properties);
	}

	public Fluid getSource() {
		return super.getSource();
	}

	public Fluid getFlowing() {
		return this;
	}

	public Item getBucket() {
		return Items.AIR;
	}

	protected BlockState createLegacyBlock(FluidState state) {
		return Blocks.AIR.defaultBlockState();
	}

	public boolean isSource(FluidState state) {
		return false;
	}

	public int getAmount(FluidState state) {
		return 0;
	}
}
