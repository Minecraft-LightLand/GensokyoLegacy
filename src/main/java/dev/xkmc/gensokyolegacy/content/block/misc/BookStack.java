package dev.xkmc.gensokyolegacy.content.block.misc;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import dev.xkmc.l2core.serial.loot.LootHelper;
import dev.xkmc.l2modularblock.core.BlockTemplates;
import dev.xkmc.l2modularblock.core.DelegateBlock;
import dev.xkmc.l2modularblock.core.VoxelBuilder;
import dev.xkmc.l2modularblock.mult.CreateBlockStateBlockMethod;
import dev.xkmc.l2modularblock.mult.DefaultStateBlockMethod;
import dev.xkmc.l2modularblock.mult.UseItemOnBlockMethod;
import dev.xkmc.l2modularblock.one.ShapeBlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.ItemAbilities;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.ATTACHED;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class BookStack implements CreateBlockStateBlockMethod, DefaultStateBlockMethod, UseItemOnBlockMethod, ShapeBlockMethod {

	public static final VoxelShape[] SHAPES = new VoxelShape[4];

	static {
		var builder = new VoxelBuilder(5, 0, 4, 11, 12, 12);
		for (int i = 0; i < 4; i++) {
			SHAPES[i] = builder.rotateFromNorth(Direction.from2DDataValue(i));
		}
	}

	public static final VariantImpl VARIANT = new VariantImpl(2);

	public static DelegateBlock create(BlockBehaviour.Properties p) {
		return DelegateBlock.newBaseBlock(p, BlockTemplates.HORIZONTAL, new BookStack(), VARIANT, new SurviveImpl());
	}

	@Nullable
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		var dir = state.getValue(HORIZONTAL_FACING);
		Vec3 vec3 = state.getOffset(level, pos);
		return SHAPES[dir.get2DDataValue()].move(vec3.x, vec3.y, vec3.z);
	}

	@Override
	public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(ATTACHED);
	}

	@Override
	public BlockState getDefaultState(BlockState state) {
		return state.setValue(ATTACHED, false);
	}

	@Override
	public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player pl, InteractionHand hand, BlockHitResult result) {
		if (stack.is(Items.STRING) && !state.getValue(ATTACHED)) {
			if (!level.isClientSide()) {
				level.setBlockAndUpdate(pos, state.setValue(ATTACHED, true));
				if (!pl.getAbilities().instabuild) {
					stack.shrink(1);
				}
			}
			return ItemInteractionResult.SUCCESS;
		}
		if (state.getValue(ATTACHED) && stack.canPerformAction(ItemAbilities.SHEARS_CARVE)) {
			if (!level.isClientSide()) {
				level.setBlockAndUpdate(pos, state.setValue(ATTACHED, false));
				if (!pl.getAbilities().instabuild) {
					Block.popResource(level, pos, new ItemStack(Items.STRING));
					stack.hurtAndBreak(1, pl, LivingEntity.getSlotForHand(hand));
				}
			}
			return ItemInteractionResult.SUCCESS;
		}
		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}

	public static void buildStates(DataGenContext<Block, DelegateBlock> ctx, RegistrateBlockstateProvider pvd) {
		pvd.horizontalBlock(ctx.get(), state -> {
			int variant = state.getValue(VARIANT.variant());
			String suffix = variant > 0 ? "_" + variant : "";
			String tex = (state.getValue(ATTACHED) ? "tied" : "stack") + "_" + variant;
			return pvd.models().getBuilder("block/book_stack" + (state.getValue(ATTACHED) ? "_tied" : "") + suffix)
					.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/book/book_stack")))
					.texture("books", pvd.modLoc("block/book/" + tex))
					.renderType("cutout");
		});
	}

	public static void buildLoot(RegistrateBlockLootTables pvd, DelegateBlock block) {
		var helper = new LootHelper(pvd);
		pvd.add(block, LootTable.lootTable().withPool(
				pvd.applyExplosionCondition(Items.BOOK, LootPool.lootPool().add(helper.item(Items.BOOK, 6)))));
	}

}
