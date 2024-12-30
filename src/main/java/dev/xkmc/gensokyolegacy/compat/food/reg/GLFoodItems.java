package dev.xkmc.gensokyolegacy.compat.food.reg;

import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.gensokyolegacy.compat.food.flesh.FleshBlockItem;
import dev.xkmc.gensokyolegacy.compat.food.flesh.FleshFeastBlock;
import dev.xkmc.gensokyolegacy.compat.food.flesh.FleshSimpleItem;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.l2core.init.reg.registrate.SimpleEntry;
import dev.xkmc.youkaishomecoming.init.food.CakeEntry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import vectorwing.farmersdelight.common.block.FeastBlock;

public class GLFoodItems {

	public static final SimpleEntry<CreativeModeTab> TAB;

	public static final ItemEntry<Item> CAN;
	public static final ItemEntry<FleshSimpleItem> RAW_FLESH_FEAST;
	public static final BlockEntry<FleshFeastBlock> FLESH_FEAST;
	public static final CakeEntry RED_VELVET;

	static {
		var reg = GensokyoLegacy.REGISTRATE;

		TAB = reg.buildModCreativeTab("food", "Gensokyo Legacy - Foods",
				e -> e.icon(GLFood.FLESH_CHOCOLATE_MOUSSE.item::asStack));

		CAN = reg.item("can", Item::new).register();
		GLFood.register();

		{


			RAW_FLESH_FEAST = reg.item("raw_flesh_feast", FleshSimpleItem::new)
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/feast/" + ctx.getName())))
					.lang("Raw %1$s Feast")
					.register();

			FLESH_FEAST = reg.block("flesh_feast", p ->
							new FleshFeastBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BROWN_WOOL),
									GLFood.BOWL_OF_FLESH_FEAST.item))
					.blockstate((ctx, pvd) -> pvd.horizontalBlock(ctx.get(), state ->
							FleshFeastBlock.Model.values()[state.getValue(FeastBlock.SERVINGS)].build(pvd)))
					.lang("%1$s Feast")
					.item(FleshBlockItem::new).model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/feast/" + ctx.getName()))).build()
					.loot(FleshFeastBlock::builtLoot)
					.register();

			RED_VELVET = new CakeEntry(reg, "red_velvet", MapColor.COLOR_RED, GLFoodType.FLESH, 1, 0.8f, true);
		}
		GLDish.register();
		GLSake.register();
	}

	public static void register() {

	}

}
