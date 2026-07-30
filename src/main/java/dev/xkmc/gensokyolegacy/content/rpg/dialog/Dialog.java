package dev.xkmc.gensokyolegacy.content.rpg.dialog;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.gensokyolegacy.content.rpg.core.CodecRegistry;
import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryFileCodec;

import java.util.List;

public record Dialog(
		List<DialogOption<?>> options
) {

	public static final Codec<Dialog> CODEC = RecordCodecBuilder.create(i -> i.group(
			CodecRegistry.OPTION.codec().listOf().fieldOf("options").forGetter(Dialog::options)
	).apply(i, Dialog::new));

	public static final Codec<Holder<Dialog>> HOLDER = RegistryFileCodec.create(CodecRegistry.DIALOG.key(), CODEC);

}
