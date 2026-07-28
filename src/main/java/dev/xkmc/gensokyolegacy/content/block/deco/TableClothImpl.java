package dev.xkmc.gensokyolegacy.content.block.deco;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import dev.xkmc.gensokyolegacy.init.registrate.GLDecoBlocks;
import dev.xkmc.l2core.serial.loot.LootHelper;
import dev.xkmc.l2modularblock.mult.CreateBlockStateBlockMethod;
import dev.xkmc.l2modularblock.mult.DefaultStateBlockMethod;
import dev.xkmc.l2modularblock.mult.UseItemOnBlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.MultiPartBlockStateBuilder;
import net.neoforged.neoforge.common.ItemAbilities;

import java.util.Locale;

public class TableClothImpl implements CreateBlockStateBlockMethod, DefaultStateBlockMethod, UseItemOnBlockMethod {

	public enum Color implements StringRepresentable {
		NONE(Items.AIR),
		BASE(() -> GLDecoBlocks.TATAMI.asItem()),

		WHITE(Blocks.WHITE_CARPET),
		ORANGE(Blocks.ORANGE_CARPET),
		MAGENTA(Blocks.MAGENTA_CARPET),
		LIGHT_BLUE(Blocks.LIGHT_BLUE_CARPET),
		YELLOW(Blocks.YELLOW_CARPET),
		LIME(Blocks.LIME_CARPET),
		PINK(Blocks.PINK_CARPET),
		GRAY(Blocks.GRAY_CARPET),
		LIGHT_GRAY(Blocks.LIGHT_GRAY_CARPET),
		CYAN(Blocks.CYAN_CARPET),
		PURPLE(Blocks.PURPLE_CARPET),
		BLUE(Blocks.BLUE_CARPET),
		BROWN(Blocks.BROWN_CARPET),
		GREEN(Blocks.GREEN_CARPET),
		RED(Blocks.RED_CARPET),
		BLACK(Blocks.BLACK_CARPET);

		private final ItemLike item;

		Color(ItemLike item) {
			this.item = item;
		}

		@Override
		public String getSerializedName() {
			return name().toLowerCase(Locale.ROOT);
		}

	}

	public static final EnumProperty<Color> COLOR = EnumProperty.create("color", Color.class, Color.values());

	@Override
	public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(COLOR);
	}

	@Override
	public BlockState getDefaultState(BlockState state) {
		return state.setValue(COLOR, Color.NONE);
	}

	@Override
	public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player pl, InteractionHand hand, BlockHitResult result) {
		if (state.getValue(COLOR) == Color.NONE) {
			for (var e : Color.values()) {
				if (e.item.asItem() == Items.AIR) continue;
				if (stack.is(e.item.asItem())) {
					if (!level.isClientSide()) {
						level.setBlockAndUpdate(pos, state.setValue(COLOR, e));
						if (!pl.getAbilities().instabuild) {
							stack.shrink(1);
						}
					}
					return ItemInteractionResult.SUCCESS;
				}
			}
		} else if (stack.canPerformAction(ItemAbilities.SHEARS_CARVE)) {
			if (!level.isClientSide()) {
				var col = state.getValue(COLOR);
				level.setBlockAndUpdate(pos, state.setValue(COLOR, Color.NONE));
				if (!pl.getAbilities().instabuild) {
					stack.hurtAndBreak(1, pl, LivingEntity.getSlotForHand(hand));
				}
				Block.popResource(level, pos, new ItemStack(col.item, 1));
			}
			return ItemInteractionResult.SUCCESS;
		}
		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}

	public static LootTable.Builder loot(RegistrateBlockLootTables pvd, Block block) {
		LootHelper helper = new LootHelper(pvd);
		var ans = LootTable.lootTable();
		ans.withPool(LootPool.lootPool().add(LootItem.lootTableItem(block)));
		for (var e : Color.values()) {
			if (e.item.asItem() == Items.AIR) continue;
			ans.withPool(LootPool.lootPool().add(LootItem.lootTableItem(e.item)).when(helper.enumState(block, COLOR, e)));
		}
		return ans;
	}

	private static boolean built = false;

	public static void buildStates(RegistrateBlockstateProvider pvd) {
		if (built) return;
		built = true;
		for (var e : Color.values()) {
			if (e.item.asItem() == Items.AIR) continue;
			String name = e == Color.BASE ? "tablecloth" : e.getSerializedName() + "_tablecloth";
			pvd.models().getBuilder("block/" + name)
					.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/tablecloth")))
					.texture("all", "block/table/" + name);
		}
	}

	public static void buildStates(MultiPartBlockStateBuilder builder, RegistrateBlockstateProvider pvd, BlockModelBuilder table) {
		buildStates(pvd);
		for (var e : Color.values()) {
			String name = e == Color.BASE ? "tablecloth" : e.getSerializedName() + "_tablecloth";
			var file = e.item.asItem() == Items.AIR ? table : new ModelFile.UncheckedModelFile(pvd.modLoc("block/" + name));
			builder.part().modelFile(file).addModel().condition(COLOR, e).end();
		}
	}


}
