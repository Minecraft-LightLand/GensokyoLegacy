package dev.xkmc.gensokyolegacy.content.trigger;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.gensokyolegacy.init.registrate.GLCriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class FeedReimuTrigger extends SimpleCriterionTrigger<FeedReimuTrigger.TriggerInstance> {
	@Override
	public Codec<FeedReimuTrigger.TriggerInstance> codec() {
		return FeedReimuTrigger.TriggerInstance.CODEC;
	}

	public void trigger(ServerPlayer player, LivingEntity le, ItemStack item) {
		trigger(player, e -> e.matches(player, le, item));
	}

	public record TriggerInstance(
			Optional<ContextAwarePredicate> player,
			Optional<EntityPredicate> entity,
			Optional<ItemPredicate> item
	) implements SimpleCriterionTrigger.SimpleInstance {
		public static final Codec<FeedReimuTrigger.TriggerInstance> CODEC = RecordCodecBuilder.create(i -> i.group(
						EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(FeedReimuTrigger.TriggerInstance::player),
						EntityPredicate.CODEC.optionalFieldOf("entity").forGetter(FeedReimuTrigger.TriggerInstance::entity),
						ItemPredicate.CODEC.optionalFieldOf("item").forGetter(FeedReimuTrigger.TriggerInstance::item)
				).apply(i, FeedReimuTrigger.TriggerInstance::new)
		);

		public static Criterion<FeedReimuTrigger.TriggerInstance> usedItem(
				@Nullable ContextAwarePredicate player, @Nullable EntityPredicate entity, @Nullable ItemPredicate item
		) {
			return GLCriteriaTriggers.FEED_REIMU.get().createCriterion(new FeedReimuTrigger.TriggerInstance(
					Optional.ofNullable(player), Optional.ofNullable(entity), Optional.ofNullable(item)));
		}

		public boolean matches(ServerPlayer player, LivingEntity le, ItemStack item) {
			return (entity().isEmpty() || entity().get().matches(player, le)) &&
					(item().isEmpty() || item().get().test(item));
		}

	}
}