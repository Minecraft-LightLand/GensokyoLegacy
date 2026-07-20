package dev.xkmc.gensokyolegacy.content.block.furniture;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import dev.xkmc.gensokyolegacy.init.data.GLRecipeGen;
import dev.xkmc.gensokyolegacy.init.registrate.GLDecoBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.model.generators.ModelFile;

import java.util.List;
import java.util.function.Supplier;

public class WoodChairBlock extends Block {

	public static final VoxelShape SHAPE = Block.box(1, 0, 1, 15, 10, 15);

	public WoodChairBlock(Properties pProperties) {
		super(pProperties);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		return SHAPE;
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		if (player.isShiftKeyDown()) {
			return InteractionResult.PASS;
		}
		List<ChairEntity> seats = level.getEntitiesOfClass(ChairEntity.class, new AABB(pos));
		if (!seats.isEmpty()) {
			ChairEntity seat = seats.getFirst();
			List<Entity> passengers = seat.getPassengers();
			if (!passengers.isEmpty() && passengers.getFirst() instanceof Player) {
				return InteractionResult.PASS;
			}
			if (!level.isClientSide) {
				seat.ejectPassengers();
				player.startRiding(seat);
			}
			return InteractionResult.SUCCESS;
		}
		if (!level.isClientSide) {
			sitDown(level, pos, player);
		}
		return InteractionResult.SUCCESS;
	}

	public static void sitDown(Level world, BlockPos pos, Entity entity) {
		if (!world.isClientSide) {
			ChairEntity seat = new ChairEntity(world, pos);
			seat.setPos(pos.getX() + 0.5F, pos.getY(), pos.getZ() + 0.5F);
			world.addFreshEntity(seat);
			entity.startRiding(seat, true);
		}
	}

	public static boolean isSeatOccupied(Level world, BlockPos pos) {
		return !world.getEntitiesOfClass(ChairEntity.class, new AABB(pos)).isEmpty();
	}

	public static void buildStates(DataGenContext<Block, WoodChairBlock> ctx, RegistrateBlockstateProvider pvd) {
		pvd.simpleBlock(ctx.get(), pvd.models().getBuilder("block/" + ctx.getName())
				.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/wooden_dining_chair")))
				.texture("all", pvd.modLoc("block/wood/" + ctx.getName()))
				.texture("particle", pvd.mcLoc("block/birch_planks"))
				.renderType("cutout"));
	}

	public static void genRecipe(RegistrateRecipeProvider pvd, GLDecoBlocks.WoodType base, Supplier<? extends ItemLike> self) {
		GLRecipeGen.unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, self.get(), 4)::unlockedBy, base.plank.asItem())
				.pattern("PCP").pattern("PSP")
				.define('C', Items.WHITE_WOOL)
				.define('P', base.plank)
				.define('S', Items.STICK)
				.save(pvd);
	}

}
