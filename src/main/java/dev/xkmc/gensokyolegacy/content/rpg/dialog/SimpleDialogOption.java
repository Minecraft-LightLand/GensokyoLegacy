package dev.xkmc.gensokyolegacy.content.rpg.dialog;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.gensokyolegacy.content.rpg.action.DialogAction;
import dev.xkmc.gensokyolegacy.content.rpg.core.CodecRegistry;
import net.minecraft.core.Holder;

import java.util.List;
import java.util.Optional;

public record SimpleDialogOption(
		String text,
		Optional<Holder<Dialog>> next,
		List<DialogAction<?>> actions
) implements DialogOption<SimpleDialogOption> {

	public static final MapCodec<SimpleDialogOption> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("text").forGetter(SimpleDialogOption::text),
			Dialog.HOLDER.optionalFieldOf("next").forGetter(SimpleDialogOption::next),
			CodecRegistry.ACTION.codec().listOf().fieldOf("actions").forGetter(SimpleDialogOption::actions)
	).apply(i, SimpleDialogOption::new));

	@Override
	public MapCodec<SimpleDialogOption> codec() {
		return CODEC;
	}

}
