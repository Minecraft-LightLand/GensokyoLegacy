package dev.xkmc.gensokyolegacy.content.rpg.dialog;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;

import java.util.Optional;

public record SimpleDialogOption(
		String text,
		Optional<Holder<Dialog>> next
) implements DialogOption<SimpleDialogOption> {

	public static final MapCodec<SimpleDialogOption> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.STRING.fieldOf("text").forGetter(SimpleDialogOption::text),
			Dialog.HOLDER.optionalFieldOf("next").forGetter(SimpleDialogOption::next)
	).apply(i, SimpleDialogOption::new));

	@Override
	public MapCodec<SimpleDialogOption> codec() {
		return CODEC;
	}

}
