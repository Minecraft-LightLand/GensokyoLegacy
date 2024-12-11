package dev.xkmc.gensokyolegacy.init.data;

import com.tterrag.registrate.providers.loot.RegistrateEntityLootTables;
import dev.xkmc.danmakuapi.init.data.DanmakuDamageTypes;
import dev.xkmc.danmakuapi.init.registrate.DanmakuItems;
import dev.xkmc.gensokyolegacy.content.entity.characters.fairy.CirnoEntity;
import dev.xkmc.gensokyolegacy.content.entity.characters.maiden.ReimuEntity;
import dev.xkmc.gensokyolegacy.content.entity.characters.rumia.RumiaEntity;
import dev.xkmc.l2core.serial.loot.LootHelper;
import net.minecraft.advancements.critereon.DamageSourcePredicate;
import net.minecraft.advancements.critereon.EntityFlagsPredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.TagPredicate;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SmeltItemFunction;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class EntityLootGen {

	public static <T extends LivingEntity> void noLoot(RegistrateEntityLootTables pvd, EntityType<T> type) {
		pvd.add(type, LootTable.lootTable());
	}

	public static void rumia(RegistrateEntityLootTables pvd, EntityType<RumiaEntity> type) {
		pvd.add(type, LootTable.lootTable()
				.withPool(LootPool.lootPool().add(getItem(DanmakuItems.Bullet.CIRCLE.get(DyeColor.RED).get(), 5, 10))
						.apply(lootCount(pvd, 1f))
						.when(byPlayer()))
				.withPool(LootPool.lootPool().add(getItem(DanmakuItems.Bullet.CIRCLE.get(DyeColor.BLACK).get(), 3, 6))
						.apply(lootCount(pvd, 1f))
						.when(byPlayer()))
		);
	}

	public static void reimu(RegistrateEntityLootTables pvd, EntityType<ReimuEntity> type) {
		pvd.add(type, LootTable.lootTable()
				.withPool(LootPool.lootPool().add(getItem(DanmakuItems.REIMU_SPELL.get(), 1, 1))
						.when(byPlayer()))
		);
	}

	public static void cirno(RegistrateEntityLootTables pvd, EntityType<CirnoEntity> type) {
		pvd.add(type, LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.add(getItem(DanmakuItems.Bullet.MENTOS.get(DyeColor.LIGHT_BLUE).get(), 1, 1)
								.setWeight(6))
						.when(lootChance(pvd, 0.3f))
						.when(danmakuKill())
						.when(byPlayer()))
		);
	}

	private static LootItemFunction.Builder onFire() {
		return SmeltItemFunction.smelted()
				.when(LootItemEntityPropertyCondition.hasProperties(
						LootContext.EntityTarget.THIS,
						EntityPredicate.Builder.entity()
								.flags(EntityFlagsPredicate.Builder.flags()
										.setOnFire(true))));
	}


	public static LootPoolSingletonContainer.Builder<?> getItem(Item item, int min, int max) {
		return LootItem.lootTableItem(item).apply(SetItemCountFunction.setCount(UniformGenerator.between((float) min, (float) max)));
	}

	private static LootItemCondition.Builder byPlayer() {
		return LootItemKilledByPlayerCondition.killedByPlayer();
	}

	private static LootItemCondition.Builder danmakuKill() {
		return DamageSourceCondition.hasDamageSource(DamageSourcePredicate.Builder.damageType()
				.tag(TagPredicate.is(DanmakuDamageTypes.DANMAKU_TYPE)));
	}

	private static LootItemFunction.Builder lootCount(RegistrateEntityLootTables pvd, float factor) {
		return EnchantedCountIncreaseFunction.lootingMultiplier(pvd.getRegistries(), UniformGenerator.between(factor * 0.5f, factor));
	}

	private static LootItemCondition.Builder lootChance(RegistrateEntityLootTables pvd, float base) {
		return LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(pvd.getRegistries(), base, base * 0.2f);
	}

}
