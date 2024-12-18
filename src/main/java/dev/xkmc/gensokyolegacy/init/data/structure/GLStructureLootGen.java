package dev.xkmc.gensokyolegacy.init.data.structure;

import com.tterrag.registrate.providers.loot.RegistrateLootTableProvider;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.gensokyolegacy.init.registrate.GLDecoBlocks;
import dev.xkmc.gensokyolegacy.init.registrate.GLItems;
import dev.xkmc.youkaishomecoming.init.food.YHCrops;
import dev.xkmc.youkaishomecoming.init.food.YHFood;
import dev.xkmc.youkaishomecoming.init.food.YHTea;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import vectorwing.farmersdelight.common.registry.ModItems;

public class GLStructureLootGen {

	public static final ResourceKey<LootTable> CIRNO_POT = key("chests/cirno_nest/pot");
	public static final ResourceKey<LootTable> CIRNO_CABINET = key("chests/cirno_nest/cabinet");
	public static final ResourceKey<LootTable> CIRNO_BARREL = key("chests/cirno_nest/barrel");
	public static final ResourceKey<LootTable> CIRNO_BASKET = key("chests/cirno_nest/basket");

	public static final ResourceKey<LootTable> SHRINE_CHEST = key("chests/hakurei_shrine/chest");
	public static final ResourceKey<LootTable> SHRINE_BARREL = key("chests/hakurei_shrine/barrel");
	public static final ResourceKey<LootTable> SHRINE_CABINET = key("chests/hakurei_shrine/cabinet");

	private static ResourceKey<LootTable> key(String id) {
		return ResourceKey.create(Registries.LOOT_TABLE, GensokyoLegacy.loc(id));
	}

	public static void genLoot(RegistrateLootTableProvider pvd) {
		{
			var mochi = getPool(3, 1)
					.add(getItem(YHFood.MOCHI.asItem(), 2, 4))
					.add(getItem(YHFood.MATCHA_MOCHI.asItem(), 2, 4))
					.add(getItem(YHFood.SAKURA_MOCHI.asItem(), 2, 3))
					.add(getItem(YHFood.COFFEE_MOCHI.asItem(), 1, 2));
			var dango = getPool(2, 1)
					.add(getItem(YHFood.ASSORTED_DANGO.asItem(), 1, 2))
					.add(getItem(YHFood.KINAKO_DANGO.asItem(), 1, 2))
					.add(getItem(YHFood.MITARASHI_DANGO.asItem(), 1, 2))
					.add(getItem(YHFood.TSUKIMI_DANGO.asItem(), 1, 2))
					.add(getItem(YHFood.YASHOUMA_DANGO.asItem(), 1, 2));
			var popsicle = getPool(2, 1)
					.add(getItem(ModItems.MELON_POPSICLE.get(), 1, 2))
					.add(getItem(YHFood.CANDY_APPLE.asItem(), 1, 2))
					.add(getItem(YHFood.MILK_POPSICLE.asItem(), 1, 2))
					.add(getItem(YHFood.BIG_POPSICLE.asItem(), 2, 4));
			pvd.addLootAction(LootContextParamSets.CHEST, cons -> cons.accept(CIRNO_POT, LootTable.lootTable()
					.withPool(getPool(1, 1)
							.add(getItem(YHItems.ICE_CUBE.get(), 4, 8)))
			));

			pvd.addLootAction(LootContextParamSets.CHEST, cons -> cons.accept(CIRNO_CABINET, LootTable.lootTable()
					.withPool(popsicle).withPool(mochi).withPool(dango)
			));

			pvd.addLootAction(LootContextParamSets.CHEST, cons -> cons.accept(CIRNO_BASKET, LootTable.lootTable()
					.withPool(getPool(3, 1)
							.add(getItem(GLItems.FROZEN_FROG_COLD.get(), 1, 1))
							.add(getItem(GLItems.FROZEN_FROG_TEMPERATE.get(), 1, 1))
							.add(getItem(GLItems.FROZEN_FROG_WARM.get(), 1, 1))
							.add(getItem(GLItems.FAIRY_ICE_CRYSTAL.get(), 1, 1)))
			));

			pvd.addLootAction(LootContextParamSets.CHEST, cons -> cons.accept(CIRNO_BARREL, LootTable.lootTable()
					.withPool(getPool(5, 1)
							.add(getItem(GLDecoBlocks.ICE_SET.block.get().asItem(), 4, 8))
							.add(getItem(GLDecoBlocks.ICE_SET.slab.asItem(), 2, 4))
							.add(getItem(GLDecoBlocks.ICE_SET.vertical.asItem(), 2, 4))
							.add(getItem(GLDecoBlocks.ICE_SET.stairs.asItem(), 1, 2))
							.add(getItem(ModItems.CANVAS_RUG.get(), 2, 4))
							.add(getItem(Items.BRICK, 3, 6)))
			));
		}

		{
			var misc = getPool(3, 1)
					.add(getItem(Items.REDSTONE, 2).setWeight(2))
					.add(getItem(Items.LAPIS_LAZULI, 2).setWeight(2))
					.add(getItem(Items.GLOWSTONE_DUST, 2).setWeight(2))
					.add(getItem(Items.AMETHYST_SHARD, 2).setWeight(2))
					.add(getItem(Items.COAL, 2).setWeight(4))
					.add(getItem(Items.EMERALD, 1))
					.add(getItem(Items.IRON_INGOT, 2))
					.add(getItem(Items.GOLD_NUGGET, 2));

			var crops = getPool(2, 1)
					.add(getItem(YHCrops.TEA.getSeed(), 4).setWeight(4))
					.add(getItem(YHCrops.SOYBEAN.getSeed(), 4).setWeight(4))
					.add(getItem(YHCrops.REDBEAN.getSeed(), 4).setWeight(4))
					.add(getItem(YHCrops.COFFEA.getSeed(), 4).setWeight(2))
					.add(getItem(YHCrops.UDUMBARA.getSeed(), 1).setWeight(1))
					.add(getItem(ModItems.RICE.get(), 2).setWeight(8))
					.add(getItem(ModItems.CABBAGE.get(), 2).setWeight(4))
					.add(getItem(ModItems.TOMATO.get(), 2).setWeight(4))
					.add(getItem(Items.CARROT, 2).setWeight(4))
					.add(getItem(Items.POTATO, 2).setWeight(4));

			var tea = getPool(2, 1)
					.add(getItem(YHCrops.TEA.getFruits(), 4))
					.add(getItem(YHTea.BLACK.leaves.get(), 2))
					.add(getItem(YHTea.WHITE.leaves.get(), 2))
					.add(getItem(YHTea.OOLONG.leaves.get(), 2))
					.add(getItem(YHTea.GREEN.leaves.get(), 2))
					.add(getItem(YHItems.MATCHA.get(), 2));

			var rice = getPool(3, 1)
					.add(getItem(YHFood.ONIGILI.item.get(), 2))
					.add(getItem(YHFood.SENBEI.item.get(), 2))
					.add(getItem(YHFood.SEKIBANKIYAKI.item.get(), 2))
					.add(getItem(YHFood.YAKUMO_INARI.item.get(), 2))
					.add(getItem(YHFood.BUN.item.get(), 1))
					.add(getItem(YHFood.OYAKI.item.get(), 2))
					.add(getItem(YHFood.PORK_RICE_BALL.item.get(), 1))
					.add(getItem(YHFood.OILY_BEAN_CURD.item.get(), 2))
					.add(getItem(YHFood.ROASTED_LAMPREY_FILLET.item.get(), 2));

			var dango = getPool(1, 1)
					.add(getItem(YHFood.MOCHI.item.get(), 2))
					.add(getItem(YHFood.TSUKIMI_DANGO.item.get(), 2))
					.add(getItem(YHFood.COFFEE_MOCHI.item.get(), 2))
					.add(getItem(YHFood.MATCHA_MOCHI.item.get(), 2))
					.add(getItem(YHFood.SAKURA_MOCHI.item.get(), 2))
					.add(getItem(YHFood.YASHOUMA_DANGO.item.get(), 2));

			pvd.addLootAction(LootContextParamSets.CHEST, cons -> cons.accept(SHRINE_CHEST, LootTable.lootTable()
					.withPool(misc)
			));

			pvd.addLootAction(LootContextParamSets.CHEST, cons -> cons.accept(SHRINE_BARREL, LootTable.lootTable()
					.withPool(crops).withPool(tea)
			));

			pvd.addLootAction(LootContextParamSets.CHEST, cons -> cons.accept(SHRINE_CABINET, LootTable.lootTable()
					.withPool(rice).withPool(dango)
			));
		}
	}

	public static LootPool.Builder getPool(int roll, int bonus) {
		return LootPool.lootPool().setRolls(ConstantValue.exactly((float) roll)).setBonusRolls(ConstantValue.exactly(bonus));
	}

	public static LootPoolSingletonContainer.Builder<?> getItem(Item item, int count) {
		return LootItem.lootTableItem(item).apply(SetItemCountFunction.setCount(ConstantValue.exactly((float) count)));
	}

	public static LootPoolSingletonContainer.Builder<?> getItem(Item item, int min, int max) {
		return LootItem.lootTableItem(item).apply(SetItemCountFunction.setCount(UniformGenerator.between((float) min, (float) max)));
	}

}
