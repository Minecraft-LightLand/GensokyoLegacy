package dev.xkmc.gensokyolegacy.init.data.loot;

import dev.xkmc.gensokyolegacy.content.food.reg.GLFood;
import dev.xkmc.gensokyolegacy.content.mechanics.role.core.RoleCategory;
import dev.xkmc.gensokyolegacy.content.mechanics.role.loot.RoleCategoryLootCondition;
import dev.xkmc.gensokyolegacy.content.mechanics.role.loot.RoleProgressLootCondition;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.gensokyolegacy.init.data.GLTagGen;
import dev.xkmc.gensokyolegacy.init.registrate.GLEntities;
import dev.xkmc.gensokyolegacy.init.registrate.GLItems;
import dev.xkmc.gensokyolegacy.init.registrate.GLMechanics;
import dev.xkmc.youkaishomecoming.mixin.AddItemModifierAccessor;
import dev.xkmc.youkaishomecoming.mixin.AddLootTableModifierAccessor;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.FrogVariant;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.LootModifier;
import vectorwing.farmersdelight.common.loot.modifier.AddItemModifier;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.util.concurrent.CompletableFuture;

public class GLGLMProvider extends GlobalLootModifierProvider {

	public static void register() {

	}

	public GLGLMProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries, GensokyoLegacy.MODID);
	}

	@Override
	protected void start() {
		add("cirno_frozen_frog_cold", create(GLItems.FROZEN_FROG_COLD.get(), 1,
				killedByCirno(), frog(FrogVariant.COLD)));
		add("cirno_frozen_frog_warm", create(GLItems.FROZEN_FROG_WARM.get(), 1,
				killedByCirno(), frog(FrogVariant.WARM)));
		add("cirno_frozen_frog_temperate", create(GLItems.FROZEN_FROG_TEMPERATE.get(), 1,
				killedByCirno(), frog(FrogVariant.TEMPERATE)));

		add("scavenging_flesh", create(GLFood.FLESH.item.get(), 1,
				killedByKnife(), fire(false), isFleshSource(), killedByYoukai()));
		add("scavenging_flesh_cooked", create(GLFood.COOKED_FLESH.item.get(), 1,
				killedByKnife(), fire(true), isFleshSource(), killedByYoukai()));


		add("rumia_scavenging_flesh", create(GLFood.FLESH.item.get(), 1,
				killedByRumia(), fire(false), isFleshSource()));
		add("rumia_scavenging_flesh_cooked", create(GLFood.COOKED_FLESH.item.get(), 1,
				killedByRumia(), fire(true), isFleshSource()));
		add("rumia_scavenging_skeleton_skull", create(Items.SKELETON_SKULL, 1,
				killedByRumia(), entity(GLTagGen.SKULL_SOURCE)));
		add("rumia_scavenging_wither_skeleton_skull", create(Items.WITHER_SKELETON_SKULL, 1,
				killedByRumia(), entity(GLTagGen.WITHER_SOURCE)));
		add("rumia_scavenging_zombie_head", create(Items.ZOMBIE_HEAD, 1,
				killedByRumia(), entity(GLTagGen.ZOMBIE_SOURCE)));
		add("rumia_scavenging_creeper_head", create(Items.CREEPER_HEAD, 1,
				killedByRumia(), entity(GLTagGen.CREEPER_SOURCE)));
		add("rumia_scavenging_piglin_head", create(Items.PIGLIN_HEAD, 1,
				killedByRumia(), entity(GLTagGen.PIGLIN_SOURCE)));

	}

	private static LootModifier loot(ResourceLocation id, LootItemCondition... cond) {
		return AddLootTableModifierAccessor.createAddLootTableModifier(cond, id);
	}

	private static LootItemCondition isFleshSource() {
		return LootItemEntityPropertyCondition.hasProperties(
				LootContext.EntityTarget.THIS,
				EntityPredicate.Builder.entity().entityType(
						EntityTypePredicate.of(GLTagGen.FLESH_SOURCE))).build();
	}

	private static LootItemCondition killer(EntityType<?> type) {
		return LootItemEntityPropertyCondition.hasProperties(
				LootContext.EntityTarget.ATTACKER,
				EntityPredicate.Builder.entity().entityType(
						EntityTypePredicate.of(type))).build();
	}


	private static LootItemCondition entity(EntityType<?> type) {
		return LootItemEntityPropertyCondition.hasProperties(
				LootContext.EntityTarget.THIS,
				EntityPredicate.Builder.entity().entityType(
						EntityTypePredicate.of(type))).build();
	}

	private LootItemCondition frog(ResourceKey<FrogVariant> type) {
		return LootItemEntityPropertyCondition.hasProperties(
				LootContext.EntityTarget.THIS,
				EntityPredicate.Builder.entity().of(EntityType.FROG)
						.subPredicate(EntitySubPredicates.FROG.createPredicate(
								HolderSet.direct(registries.holderOrThrow(type))))
		).build();
	}

	private static LootItemCondition entity(TagKey<EntityType<?>> tag) {
		return LootItemEntityPropertyCondition.hasProperties(
				LootContext.EntityTarget.THIS,
				EntityPredicate.Builder.entity().entityType(
						EntityTypePredicate.of(tag))).build();
	}

	private static LootItemCondition killedByKnife() {
		return LootItemEntityPropertyCondition.hasProperties(
				LootContext.EntityTarget.ATTACKER,
				EntityPredicate.Builder.entity().equipment(
						EntityEquipmentPredicate.Builder.equipment().mainhand(
										ItemPredicate.Builder.item().of(ModTags.KNIVES))
								.build()).build()).build();
	}

	private static LootItemCondition killedByRumia() {
		return LootItemEntityPropertyCondition.hasProperties(
				LootContext.EntityTarget.ATTACKER,
				EntityPredicate.Builder.entity().equipment(
						EntityEquipmentPredicate.Builder.equipment().head(
										ItemPredicate.Builder.item().of(GLItems.RUMIA_HAIRBAND))
								.build()).build()).build();
	}

	private static LootItemCondition killedByCirno() {
		return LootItemEntityPropertyCondition.hasProperties(
						LootContext.EntityTarget.ATTACKER,
						EntityPredicate.Builder.entity().entityType(
								EntityTypePredicate.of(GLEntities.CIRNO.get())))
				.or(LootItemEntityPropertyCondition.hasProperties(
						LootContext.EntityTarget.ATTACKER,
						EntityPredicate.Builder.entity().equipment(
								EntityEquipmentPredicate.Builder.equipment()
										.head(ItemPredicate.Builder.item().of(GLItems.CIRNO_HAIRBAND.get())).build()
						).build()))
				.or(new RoleProgressLootCondition(GLMechanics.ICE_FAIRY.get(), 1000))
				.build();
	}


	private static LootItemCondition killedByYoukai() {
		return new RoleCategoryLootCondition(RoleCategory.YOUKAI);
	}

	private static LootItemCondition fire(boolean fire) {
		return LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS,
				EntityPredicate.Builder.entity().flags(
						EntityFlagsPredicate.Builder.flags().setOnFire(fire)).build()).build();
	}

	private static AddItemModifier create(Item item, int count, LootItemCondition... conditions) {
		var ans = AddItemModifierAccessor.create(conditions, item, count);
		assert ans != null;
		return ans;
	}

}
