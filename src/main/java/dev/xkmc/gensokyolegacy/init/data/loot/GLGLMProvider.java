package dev.xkmc.gensokyolegacy.init.data.loot;

import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.gensokyolegacy.init.registrate.GLEntities;
import dev.xkmc.gensokyolegacy.init.registrate.GLItems;
import dev.xkmc.l2core.serial.loot.AddItemModifier;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.FrogVariant;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;

import java.util.concurrent.CompletableFuture;

public class GLGLMProvider extends GlobalLootModifierProvider {

    public static void register() {

    }

    public GLGLMProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, GensokyoLegacy.MODID);
    }

    @Override
    protected void start() {
        add("cirno_frozen_frog_cold", create(GLItems.FROZEN_FROG_COLD.get(),
                killedByCirno(), frog(FrogVariant.COLD)));
        add("cirno_frozen_frog_warm", create(GLItems.FROZEN_FROG_WARM.get(),
                killedByCirno(), frog(FrogVariant.WARM)));
        add("cirno_frozen_frog_temperate", create(GLItems.FROZEN_FROG_TEMPERATE.get(),
                killedByCirno(), frog(FrogVariant.TEMPERATE)));


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
                //.or(new RoleProgressLootCondition(GLMechanics.ICE_FAIRY.get(), 1000))
                .build();
    }

    private static LootItemCondition fire(boolean fire) {
        return LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS,
                EntityPredicate.Builder.entity().flags(
                        EntityFlagsPredicate.Builder.flags().setOnFire(fire)).build()).build();
    }

    private static AddItemModifier create(Item item, LootItemCondition... conditions) {
        return new AddItemModifier(item, null, conditions);
    }

}
