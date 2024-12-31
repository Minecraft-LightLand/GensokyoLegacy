package dev.xkmc.gensokyolegacy.compat.food.flesh;

import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import dev.xkmc.gensokyolegacy.content.attachment.role.RolePlayHandler;
import dev.xkmc.gensokyolegacy.init.data.GLLang;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.InvertedLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import vectorwing.farmersdelight.common.block.FeastBlock;

import java.util.Locale;
import java.util.function.Supplier;

public class FleshFeastBlock extends FeastBlock {

	private static final int[] HEIGHT = {4, 5, 6, 7, 9};
	protected static final VoxelShape[] SHAPE_BY_BITE;

	static {
		SHAPE_BY_BITE = new VoxelShape[5];
		for (int i = 0; i < 5; i++) {
			SHAPE_BY_BITE[i] = Shapes.or(
					Block.box(1.0, 0.0, 1.0, 15.0, 2.0, 15.0),
					Shapes.join(Block.box(4.0, 2.0, 4.0, 12.0, 8.0, 12.0),
							Block.box(5.0, 2.0, 5.0, 11.0, 9.0, 11.0), BooleanOp.ONLY_FIRST),
					Block.box(5.0, 2.0, 5.0, 11.0, HEIGHT[i], 11.0)
			);
		}
	}

	public FleshFeastBlock(Properties properties, Supplier<Item> servingItem) {
		super(properties, servingItem, true);
	}

	@Override
	public MutableComponent getName() {
		return Component.translatable(this.getDescriptionId(),
				RolePlayHandler.showInfo() ?
						GLLang.FLESH_NAME_YOUKAI.get() :
						GLLang.FLESH_NAME_HUMAN.get()
		);
	}

	@Override
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		return SHAPE_BY_BITE[pState.getValue(FeastBlock.SERVINGS)];
	}

	public static void builtLoot(RegistrateBlockLootTables pvd, FleshFeastBlock block) {
		pvd.add(block, LootTable.lootTable()
				.withPool(LootPool.lootPool().add(LootItem.lootTableItem(block.asItem())
						.when(ExplosionCondition.survivesExplosion())
						.when(getServe(block))))
				.withPool(LootPool.lootPool().add(LootItem.lootTableItem(Items.BOWL))
						.when(ExplosionCondition.survivesExplosion())
						.when(InvertedLootItemCondition.invert(getServe(block))))
				.withPool(LootPool.lootPool().add(LootItem.lootTableItem(Items.SKELETON_SKULL))
						.when(ExplosionCondition.survivesExplosion())
						.when(InvertedLootItemCondition.invert(getServe(block)))));
	}

	private static <T extends FeastBlock> LootItemBlockStatePropertyCondition.Builder getServe(T block) {
		return LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).
				setProperties(StatePropertiesPredicate.Builder.properties()
						.hasProperty(block.getServingsProperty(), block.getMaxServings()));
	}

	public enum Model {
		LEFTOVER(2, 4, 0, 0),
		STAGE3(2, 3, 0, 0),
		STAGE2(2, 2, 1, 0),
		STAGE1(2, 2, 1, 1),
		STAGE0(1, 1, 1, 1);
		public final int skeleton, brain, misc, meat;

		Model(int skeleton, int brain, int misc, int meat) {
			this.skeleton = skeleton;
			this.brain = brain;
			this.misc = misc;
			this.meat = meat;
		}

		public ModelFile build(RegistrateBlockstateProvider pvd) {
			String name = name().toLowerCase(Locale.ROOT);
			var ans = pvd.models().getBuilder("flesh_feast_" + name)
					.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/flesh_feast_" + name)));
			ans.texture("tray_top", YoukaisHomecoming.loc("block/tray_top"));
			ans.texture("particle", YoukaisHomecoming.loc("block/tray_top"));
			ans.texture("tray_bottom", YoukaisHomecoming.loc("block/tray_bottom"));
			if (skeleton > 0) ans.texture("skeleton", pvd.modLoc("block/flesh_skeleton_" + skeleton));
			if (brain > 0) ans.texture("brain", pvd.modLoc("block/flesh_brain_" + brain));
			if (misc > 0) ans.texture("misc", pvd.modLoc("block/flesh_misc_" + misc));
			if (meat > 0) ans.texture("meat", pvd.modLoc("block/flesh_meat_" + meat));
			ans.renderType("cutout");
			return ans;
		}
	}

}
