package dev.xkmc.gap.init.registrate;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.xkmc.gap.content.block.bellow.BellowBlock;
import dev.xkmc.gap.content.block.bellow.BellowBlockEntity;
import dev.xkmc.gap.content.block.pot.PotBlock;
import dev.xkmc.gap.content.block.pot.PotBlockEntity;
import dev.xkmc.gap.content.block.pot.PotRenderer;
import dev.xkmc.gap.content.block.pot.recipe.PotRecipe;
import dev.xkmc.gap.content.block.pot.recipe.PotRecipeInput;
import dev.xkmc.gap.content.block.pot.recipe.SimplePotRecipe;
import dev.xkmc.gap.content.block.stove.StoveBlock;
import dev.xkmc.gap.content.data.FluidCap;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.l2core.init.reg.datapack.DataMapReg;
import dev.xkmc.l2core.init.reg.simple.SR;
import dev.xkmc.l2core.init.reg.simple.Val;
import dev.xkmc.l2core.serial.recipe.BaseRecipe;
import dev.xkmc.l2modularblock.core.BlockTemplates;
import dev.xkmc.l2modularblock.core.DelegateBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluid;

public class GapRegistries {

	private static final SR<RecipeType<?>> RT = SR.of(GensokyoLegacy.REG, Registries.RECIPE_TYPE);
	public static final Val<RecipeType<PotRecipe<?>>> RT_POT = RT.reg("pot", RecipeType::simple);

	private static final SR<RecipeSerializer<?>> RS = SR.of(GensokyoLegacy.REG, Registries.RECIPE_SERIALIZER);
	public static final Val<BaseRecipe.RecType<SimplePotRecipe, PotRecipe<?>, PotRecipeInput>> RS_POT =
			RS.reg("pot", () -> new BaseRecipe.RecType<>(SimplePotRecipe.class, RT_POT));

	public static final DataMapReg<Fluid, FluidCap> FLUID_CAP =
			GensokyoLegacy.REG.dataMap("fluid_cap", Registries.FLUID, FluidCap.class);

	public static final BlockEntry<DelegateBlock> POT, STOVE, BELLOW;
	public static final BlockEntityEntry<PotBlockEntity> POT_BE;
	public static final BlockEntityEntry<BellowBlockEntity> BELLOW_BE;

	static {
		var reg = GensokyoLegacy.REGISTRATE;
		POT = reg.block("pot", p ->
						DelegateBlock.newBaseBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE),
								new PotBlock(), PotBlock.TE))
				.blockstate(PotBlock::buildModel)
				.simpleItem().tag(BlockTags.MINEABLE_WITH_PICKAXE)
				.register();

		POT_BE = reg.blockEntity("pot", PotBlockEntity::new)
				.validBlock(POT).renderer(() -> PotRenderer::new).register();

		STOVE = reg.block("stove", p ->
						DelegateBlock.newBaseBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE),
								BlockTemplates.HORIZONTAL))
				.blockstate(StoveBlock::buildModel)
				.simpleItem().tag(BlockTags.MINEABLE_WITH_PICKAXE, GapTagGen.HEAT_SOURCE)
				.register();

		BELLOW = reg.block("bellow", p ->
						DelegateBlock.newBaseBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS),
								BlockTemplates.HORIZONTAL, BlockTemplates.TRIGGER, new BellowBlock(), BellowBlock.TE))
				.blockstate(BellowBlock::buildModel)
				.simpleItem().tag(BlockTags.MINEABLE_WITH_AXE)
				.register();

		BELLOW_BE = reg.blockEntity("bellow", BellowBlockEntity::new)
				.validBlock(BELLOW).register();
	}

	public static void register() {

	}

}
