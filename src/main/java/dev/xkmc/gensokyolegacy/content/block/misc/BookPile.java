package dev.xkmc.gensokyolegacy.content.block.misc;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import dev.xkmc.l2core.serial.loot.LootHelper;
import dev.xkmc.l2modularblock.core.BlockTemplates;
import dev.xkmc.l2modularblock.core.DelegateBlock;
import dev.xkmc.l2modularblock.one.ShapeBlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import org.jetbrains.annotations.Nullable;

public class BookPile implements ShapeBlockMethod {

	public static final VoxelShape[] SHAPES = {
			Block.box(0, 0, 0, 16, 5, 16),
			Block.box(0, 0, 0, 16, 1, 16),
			Block.box(0, 0, 0, 16, 3, 16)
	};

	public static final VariantImpl VARIANT = new VariantImpl(2);

	public static DelegateBlock create(BlockBehaviour.Properties p) {
		return DelegateBlock.newBaseBlock(p, BlockTemplates.HORIZONTAL, new BookPile(), VARIANT, new SurviveImpl());
	}

	@Nullable
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		int variant = state.getValue(VARIANT.variant());
		Vec3 vec3 = state.getOffset(level, pos);
		return SHAPES[variant].move(vec3.x, vec3.y, vec3.z);
	}

	public static void buildStates(DataGenContext<Block, DelegateBlock> ctx, RegistrateBlockstateProvider pvd) {
		pvd.horizontalBlock(ctx.get(), state -> {
			int variant = state.getValue(VARIANT.variant());
			String model = "books_" + variant;
			String id = variant > 0 ? ctx.getName() + "_" + variant : ctx.getName();
			return pvd.models().getBuilder("block/" + id)
					.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/book/" + model)))
					.texture("books", pvd.modLoc("block/book/pile"));
		});
	}

	public static void buildLoot(RegistrateBlockLootTables pvd, DelegateBlock block) {
		var helper = new LootHelper(pvd);
		pvd.add(block, LootTable.lootTable().withPool(
				pvd.applyExplosionCondition(Items.BOOK, LootPool.lootPool().add(helper.item(Items.BOOK, 5)))));
	}

}
