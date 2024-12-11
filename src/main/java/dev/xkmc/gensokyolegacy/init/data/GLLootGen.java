package dev.xkmc.gensokyolegacy.init.data;

import com.tterrag.registrate.providers.loot.RegistrateLootTableProvider;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
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

public class GLLootGen {

	public static final ResourceKey<LootTable> CIRNO_POT = key("chests/cirno_nest/pot");
	public static final ResourceKey<LootTable> CIRNO_CABINET = key("chests/cirno_nest/cabinet");

	private static ResourceKey<LootTable> key(String id) {
		return ResourceKey.create(Registries.LOOT_TABLE, GensokyoLegacy.loc(id));
	}

	public static void genLoot(RegistrateLootTableProvider pvd) {
		{
			pvd.addLootAction(LootContextParamSets.CHEST, cons -> cons.accept(CIRNO_POT, LootTable.lootTable()
					.withPool(getPool(1, 1)
							.add(getItem(YHItems.ICE_CUBE.get(), 4, 8)))
			));

			pvd.addLootAction(LootContextParamSets.CHEST, cons -> cons.accept(CIRNO_CABINET, LootTable.lootTable()
					.withPool(getPool(2, 1)
							.add(getItem(YHItems.ICE_CUBE.get(), 4, 8))
							.add(getItem(YHFood.MILK_POPSICLE.asItem(), 1, 2))
							.add(getItem(YHFood.BIG_POPSICLE.asItem(), 2, 4)))
			));
		}
	}

	public static LootPool.Builder getPool(int roll, int bonus) {
		return LootPool.lootPool().setRolls(ConstantValue.exactly((float) roll)).setBonusRolls(ConstantValue.exactly(0.0F));
	}

	public static LootPoolSingletonContainer.Builder<?> getItem(Item item, int count) {
		return LootItem.lootTableItem(item).apply(SetItemCountFunction.setCount(ConstantValue.exactly((float) count)));
	}

	public static LootPoolSingletonContainer.Builder<?> getItem(Item item, int min, int max) {
		return LootItem.lootTableItem(item).apply(SetItemCountFunction.setCount(UniformGenerator.between((float) min, (float) max)));
	}

}
