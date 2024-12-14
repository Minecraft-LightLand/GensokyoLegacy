package dev.xkmc.gensokyolegacy.init.data;

import com.tterrag.registrate.providers.loot.RegistrateLootTableProvider;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.gensokyolegacy.init.registrate.GLItems;
import dev.xkmc.youkaishomecoming.init.food.YHFood;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import vectorwing.farmersdelight.common.registry.ModItems;

public class GLLootGen {

	public static final ResourceKey<LootTable> CIRNO_POT = key("chests/cirno_nest/pot");
	public static final ResourceKey<LootTable> CIRNO_CABINET = key("chests/cirno_nest/cabinet");

	private static ResourceKey<LootTable> key(String id) {
		return ResourceKey.create(Registries.LOOT_TABLE, GensokyoLegacy.loc(id));
	}

	public static void genLoot(RegistrateLootTableProvider pvd) {
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
		{
			pvd.addLootAction(LootContextParamSets.CHEST, cons -> cons.accept(CIRNO_POT, LootTable.lootTable()
					.withPool(getPool(1, 1)
							.add(getItem(YHItems.ICE_CUBE.get(), 4, 8)))
			));

			pvd.addLootAction(LootContextParamSets.CHEST, cons -> cons.accept(CIRNO_CABINET, LootTable.lootTable()
					.withPool(popsicle).withPool(mochi).withPool(dango)
					.withPool(getPool(3, 1)
							.add(getItem(GLItems.FROZEN_FROG_COLD.get(), 1, 1))
							.add(getItem(GLItems.FROZEN_FROG_TEMPERATE.get(), 1, 1))
							.add(getItem(GLItems.FROZEN_FROG_WARM.get(), 1, 1))
							.add(getItem(GLItems.FAIRY_ICE_CRYSTAL.get(), 1, 1)))
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
