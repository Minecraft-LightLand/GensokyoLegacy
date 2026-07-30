package dev.xkmc.gensokyolegacy.content.rpg.dialog;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.gensokyolegacy.content.rpg.core.CodecRegistry;
import dev.xkmc.gensokyolegacy.content.rpg.core.GatedEntry;
import dev.xkmc.gensokyolegacy.content.rpg.quest.QuestCondition;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.world.entity.EntityType;

import java.util.List;

public record DialogStarter(
		EntityType<?> character,
		List<QuestCondition<?>> conditions,
		Holder<Dialog> dialog
) implements GatedEntry {

	public static final Codec<DialogStarter> CODEC = RecordCodecBuilder.create(i -> i.group(
			BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("character").forGetter(DialogStarter::character),
			CodecRegistry.CONDITION.codec().listOf().fieldOf("conditions").forGetter(DialogStarter::conditions),
			Dialog.HOLDER.fieldOf("initialDialog").forGetter(DialogStarter::dialog)
	).apply(i, DialogStarter::new));

	public static final Codec<Holder<DialogStarter>> HOLDER = RegistryFileCodec.create(CodecRegistry.STARTER.key(), CODEC);

}
