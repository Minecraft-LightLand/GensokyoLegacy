package dev.xkmc.gensokyolegacy.init.data.structure;

import com.tterrag.registrate.providers.loot.RegistrateLootTableProvider;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class GLStructureLootGen {

    private static ResourceKey<LootTable> key(String id) {
        return ResourceKey.create(Registries.LOOT_TABLE, GensokyoLegacy.loc(id));
    }

    public static void genLoot(RegistrateLootTableProvider pvd) {

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
