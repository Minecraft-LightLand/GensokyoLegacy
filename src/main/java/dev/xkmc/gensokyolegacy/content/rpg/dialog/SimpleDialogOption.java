package dev.xkmc.gensokyolegacy.content.rpg.dialog;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.gensokyolegacy.content.rpg.action.DialogAction;
import dev.xkmc.gensokyolegacy.content.rpg.core.CodecRegistry;
import dev.xkmc.gensokyolegacy.content.rpg.quest.QuestCondition;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.Optional;

public record SimpleDialogOption(
		List<QuestCondition<?>> conditions,
		String text,
		List<DialogAction<?>> actions,
		Optional<Holder<Dialog>> next
) implements DialogOption<SimpleDialogOption> {

	public static final MapCodec<SimpleDialogOption> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			CodecRegistry.CONDITION.codec().listOf().fieldOf("conditions").forGetter(SimpleDialogOption::conditions),
			Codec.STRING.fieldOf("text").forGetter(SimpleDialogOption::text),
			CodecRegistry.ACTION.codec().listOf().fieldOf("actions").forGetter(SimpleDialogOption::actions),
			Dialog.HOLDER.optionalFieldOf("next").forGetter(SimpleDialogOption::next)
	).apply(i, SimpleDialogOption::new));

	@Override
	public MapCodec<SimpleDialogOption> codec() {
		return CODEC;
	}

	@Override
	public Component display() {
		return Component.translatable(text());
	}

}
