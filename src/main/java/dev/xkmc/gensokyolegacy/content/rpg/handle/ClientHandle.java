package dev.xkmc.gensokyolegacy.content.rpg.handle;

import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import dev.xkmc.gensokyolegacy.content.rpg.core.CodecRegistry;
import dev.xkmc.gensokyolegacy.content.rpg.quest.Quest;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public record ClientHandle(Component display, Optional<Holder<Quest>> quest) implements IDialogHandle {

	public static final StreamCodec<RegistryFriendlyByteBuf, ClientHandle> STREAM_CODEC = StreamCodec.composite(
			ComponentSerialization.STREAM_CODEC, ClientHandle::display,
			ByteBufCodecs.optional(ByteBufCodecs.holderRegistry(CodecRegistry.Keys.QUEST)), ClientHandle::quest,
			ClientHandle::new);

	@Override
	public void openMenu(ServerPlayer sp, YoukaiEntity character) {

	}

	@Override
	public Optional<Holder<Quest>> getQuest() {
		return quest;
	}
}
