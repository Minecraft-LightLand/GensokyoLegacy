package dev.xkmc.gensokyolegacy.init.network;

import dev.xkmc.gensokyolegacy.content.attachment.CharacterData;
import dev.xkmc.gensokyolegacy.init.registrate.GLMisc;
import dev.xkmc.l2serial.network.SerialPacketBase;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public record CharDataToClient(EntityType<?> target, UUID player, CharacterData data) implements SerialPacketBase<CharDataToClient> {

    public void handle(Player player) {
        GLMisc.CHAR.type().getOrCreate(player).replace(target, data);
    }


}
