package dev.xkmc.gensokyolegacy.content.rpg.requirement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.gensokyolegacy.content.rpg.quest.QuestRequirement;
import dev.xkmc.gensokyolegacy.content.rpg.trigger.KillTrigger;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public record KillMobRequirement(
		String text, EntityPredicate target, int count
) implements QuestRequirement<KillMobRequirement, KillTrigger> {

	public static final MapCodec<KillMobRequirement> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("text").forGetter(KillMobRequirement::text),
			EntityPredicate.CODEC.fieldOf("target").forGetter(KillMobRequirement::target),
			Codec.INT.fieldOf("count").forGetter(KillMobRequirement::count)
	).apply(i, KillMobRequirement::new));

	public KillMobRequirement(String text, EntityType<?> type, int count) {
		this(text, EntityPredicate.Builder.entity().of(type).build(), count);
	}

	public KillMobRequirement(String text, TagKey<EntityType<?>> type, int count) {
		this(text, EntityPredicate.Builder.entity().of(type).build(), count);
	}

	@Override
	public MapCodec<KillMobRequirement> codec() {
		return CODEC;
	}

	@Override
	public Class<KillTrigger> getTrigger() {
		return KillTrigger.class;
	}

	@Override
	public int match(KillTrigger trigger) {
		return target.matches(trigger.player(), trigger.target()) ? 1 : 0;
	}

	@Override
	public List<Component> getDesc(Player player, int progress) {
		return List.of(Component.literal("- ").append(Component.translatable(text)).append(": ")
				.append(Component.literal("" + progress).withStyle(progress == count ? ChatFormatting.GREEN : ChatFormatting.RED))
				.append("/").append(Component.literal("" + count).withStyle(ChatFormatting.AQUA)));
	}

	@Override
	public int getMaxProgress() {
		return count;
	}

}
