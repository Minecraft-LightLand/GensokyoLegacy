package dev.xkmc.gensokyolegacy.content.block.furniture;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.model.generators.ModelFile;

public class CushionBlock extends SeatableBlock {

	public static final VoxelShape SHAPE = Block.box(2, 0, 2, 14, 2, 14);

	public CushionBlock(Properties pProperties) {
		super(pProperties, 2 / 16f);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		return SHAPE;
	}

	public static void buildStates(DataGenContext<Block, CushionBlock> ctx, RegistrateBlockstateProvider pvd) {
		pvd.simpleBlock(ctx.get(), pvd.models().getBuilder("block/" + ctx.getName())
				.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/cushion")))
				.texture("all", pvd.modLoc("block/cushion/" + ctx.getName()))
				.texture("particle", pvd.mcLoc("block/white_wool"))
				.renderType("cutout"));
	}

}
