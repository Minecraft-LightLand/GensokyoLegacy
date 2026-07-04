package dev.xkmc.gensokyolegacy.content.block.shelf;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.gensokyolegacy.init.registrate.GLBlocks;
import dev.xkmc.l2modularblock.core.DelegateBlock;
import dev.xkmc.l2modularblock.core.VoxelBuilder;
import dev.xkmc.l2modularblock.impl.BlockEntityBlockMethodImpl;
import dev.xkmc.l2modularblock.one.ShapeBlockMethod;
import dev.xkmc.l2modularblock.type.BlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import org.jetbrains.annotations.Nullable;

public class ShelfBlock implements ShapeBlockMethod {

	public static final BlockMethod BE = new BlockEntityBlockMethodImpl<>(GLBlocks.SHELF_BE, ShelfBlockEntity.class);

	public static final VoxelShape[] SHAPES = new VoxelShape[4];

	static {
		var a = new VoxelBuilder(0, 0, 8, 16, 2, 14);
		var b = new VoxelBuilder(0, 0, 14, 16, 16, 16);
		for (int i = 0; i < 4; i++) {
			var dir = Direction.from2DDataValue(i);
			SHAPES[i] = Shapes.or(a.rotateFromNorth(dir), b.rotateFromNorth(dir));
		}
	}

	@Override
	public @Nullable VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		return SHAPES[state.getValue(BlockStateProperties.HORIZONTAL_FACING).get2DDataValue()];
	}

	public static void buildStates(DataGenContext<Block, DelegateBlock> ctx, RegistrateBlockstateProvider pvd) {
		pvd.horizontalBlock(ctx.get(), pvd.models().getBuilder(ctx.getName())
				.parent(new ModelFile.UncheckedModelFile(GensokyoLegacy.loc("custom/shelf")))
				.texture("all", "block/shelf/" + ctx.getName()));
	}

}
