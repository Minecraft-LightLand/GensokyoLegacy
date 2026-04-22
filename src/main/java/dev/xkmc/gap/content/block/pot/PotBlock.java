package dev.xkmc.gap.content.block.pot;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import dev.xkmc.gap.init.registrate.GapRegistries;
import dev.xkmc.l2modularblock.core.DelegateBlock;
import dev.xkmc.l2modularblock.impl.BlockEntityBlockMethodImpl;
import dev.xkmc.l2modularblock.mult.AnimateTickBlockMethod;
import dev.xkmc.l2modularblock.mult.UseItemOnBlockMethod;
import dev.xkmc.l2modularblock.mult.UseWithoutItemBlockMethod;
import dev.xkmc.l2modularblock.one.ShapeBlockMethod;
import dev.xkmc.l2modularblock.type.BlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.model.generators.ModelFile;

import javax.annotation.Nullable;

public class PotBlock implements UseWithoutItemBlockMethod, UseItemOnBlockMethod, ShapeBlockMethod, AnimateTickBlockMethod {

	public static final BlockMethod TE = new BlockEntityBlockMethodImpl<>(GapRegistries.POT_BE, PotBlockEntity.class);

	private static final VoxelShape SHAPE = Block.box(1, 0, 1, 15, 14, 15);

	@Nullable
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		return SHAPE;
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {

	}

	@Override
	public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (level.getBlockEntity(pos) instanceof PotBlockEntity be) {
			if (stack.isEmpty()) {
				return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
			} else {
				if (stack.is(Items.STICK)) {
					if (level instanceof ServerLevel sl) {
						be.stir(sl);
					}
					return ItemInteractionResult.SUCCESS;
				}
				return FluidItemTile.addFluidOrItem(be, stack, level, pos, player, hand, hit);
			}
		}
		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}

	@Override
	public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
		if (level.getBlockEntity(pos) instanceof PotBlockEntity be) {
			if (player.isShiftKeyDown()) {
				if (!level.isClientSide()) {
					be.dumpInventory();
				}
				return InteractionResult.SUCCESS;
			}
		}
		return InteractionResult.PASS;
	}

	public static void buildModel(DataGenContext<Block, DelegateBlock> ctx, RegistrateBlockstateProvider pvd) {
		pvd.simpleBlock(ctx.get(), pvd.models().getBuilder("block/fermentation_tank")
				.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/utensil/fermentation_tank")))
				.texture("side", pvd.modLoc("block/utensil/iron_stockpot_side"))
				.texture("top", pvd.modLoc("block/utensil/iron_stockpot_top"))
				.texture("bottom", pvd.modLoc("block/utensil/iron_stockpot_bottom"))
				.texture("inside", pvd.modLoc("block/utensil/iron_stockpot_inside"))
				.renderType("cutout"));
	}

}
