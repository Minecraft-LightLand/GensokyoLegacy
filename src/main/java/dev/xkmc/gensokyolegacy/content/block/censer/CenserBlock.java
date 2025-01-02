package dev.xkmc.gensokyolegacy.content.block.censer;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import dev.xkmc.gensokyolegacy.init.registrate.GLBlocks;
import dev.xkmc.l2modularblock.core.BlockTemplates;
import dev.xkmc.l2modularblock.core.DelegateBlock;
import dev.xkmc.l2modularblock.impl.BlockEntityBlockMethodImpl;
import dev.xkmc.l2modularblock.mult.*;
import dev.xkmc.l2modularblock.one.LightBlockMethod;
import dev.xkmc.l2modularblock.one.ShapeBlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import org.jetbrains.annotations.Nullable;

public class CenserBlock {

	private static final BooleanProperty LIT = BlockStateProperties.LIT;

	public record States() implements LightBlockMethod,
			DefaultStateBlockMethod, CreateBlockStateBlockMethod, PlacementBlockMethod,
			ChangeStateExtraBlockMethod, ToolModifyBlockMethod {

		@Override
		public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
			builder.add(LIT);
		}

		@Override
		public BlockState getDefaultState(BlockState state) {
			return state.setValue(LIT, false);
		}

		@Override
		public BlockState getStateForPlacement(BlockState state, BlockPlaceContext blockPlaceContext) {
			return state.setValue(LIT, false);
		}

		@Override
		public int getLightValue(BlockState state, BlockGetter blockGetter, BlockPos blockPos) {
			return state.getValue(LIT) ? 15 : 0;
		}

		@Override
		public BlockState changeState(Block block, BlockState state, BlockState old, LevelAccessor level, BlockPos pos) {
			boolean flag = state.getValue(LIT);
			if (flag) {
				level.playSound(null, pos, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 1.0F, 1.0F);
			}
			return state.setValue(LIT, false);
		}

		@Override
		public @Nullable BlockState getToolModifiedState(
				Block block, @Nullable BlockState current, BlockState old,
				UseOnContext ctx, ItemAbility ability, boolean simulate
		) {
			if (ability != ItemAbilities.FIRESTARTER_LIGHT) return current;
			if (old.getValue(LIT) || old.getValue(BlockStateProperties.WATERLOGGED)) {
				return null;
			}
			return old.setValue(LIT, true);
		}
	}

	public record Shapes() implements ShapeBlockMethod {

	}

	public record Click() implements UseItemOnBlockMethod {

		@Override
		public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
			if (player.isShiftKeyDown()) {
				if (state.getValue(LIT)) {
					if (!level.isClientSide()) {
						level.setBlockAndUpdate(pos, state.setValue(LIT, false));
					}
				} else {
					if (!level.isClientSide() && level.getBlockEntity(pos) instanceof CenserBlockEntity be) {
						be.popItems();
					}
				}
				return ItemInteractionResult.SUCCESS;
			}
			if (!state.getValue(LIT) && stack.canPerformAction(ItemAbilities.FIRESTARTER_LIGHT))
				return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
			if (level.getBlockEntity(pos) instanceof CenserBlockEntity be) {
				if (be.tryAddItem(stack)) {
					return ItemInteractionResult.SUCCESS;
				}
			}
			return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		}

	}

	public static DelegateBlock create() {
		return DelegateBlock.newBaseBlock(
				BlockBehaviour.Properties.of().mapColor(MapColor.GOLD).sound(SoundType.METAL)
						.requiresCorrectToolForDrops().strength(0.5f).noOcclusion(),
				new Click(), new States(), new Shapes(), BlockTemplates.WATER,
				new BlockEntityBlockMethodImpl<>(GLBlocks.CENSER_BE, CenserBlockEntity.class)
		);
	}

	public static void buildStates(DataGenContext<Block, DelegateBlock> ctx, RegistrateBlockstateProvider pvd) {
		pvd.simpleBlock(ctx.get(), pvd.models().getBuilder(ctx.getName())
				.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/censer")))
				.texture("0", pvd.modLoc("block/censer"))
				.renderType("cutout"));
	}

}
