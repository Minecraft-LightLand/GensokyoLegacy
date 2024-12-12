package dev.xkmc.gensokyolegacy.content.attachment.character;

import dev.xkmc.gensokyolegacy.content.attachment.index.StructureKey;
import dev.xkmc.gensokyolegacy.content.client.debug.CharacterInfoToClient;
import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.gensokyolegacy.init.network.CharDataToClient;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public record CharDataHolder(CharacterData data, Player player, YoukaiEntity e) {

	public void onHurt(DamageSource source, float amount) {
		data.onHurtCharacter(player, e, amount, source);
		sync();
	}

	public void onKilledByCharacter() {
		data.onKilledByCharacter();
		sync();
	}

	public void onKillCharacter() {
		data.onKillCharacter();
		sync();
	}

	public int feed(ItemStack food, int favor) {
		double rate = data.foodData.feed(food);
		int v = (int) Math.round(rate * favor);
		data.reputation += v;
		sync();
		return v;
	}

	public void sync() {
		if (player instanceof ServerPlayer sp)
			GensokyoLegacy.HANDLER.toClientPlayer(new CharDataToClient(e.getType(), player.getUUID(), data), sp);
	}

	public CharacterInfoToClient getDebugPacket(@Nullable StructureKey key, @Nullable BlockPos pos) {
		return new CharacterInfoToClient(key, pos, data.reputation);
	}
}
