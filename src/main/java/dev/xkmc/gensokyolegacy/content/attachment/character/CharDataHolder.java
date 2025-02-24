package dev.xkmc.gensokyolegacy.content.attachment.character;

import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.gensokyolegacy.init.registrate.GLMeta;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public record CharDataHolder(CharacterData data, Player player, EntityType<?> type, @Nullable YoukaiEntity e) {

	public static CharDataHolder get(Player player, YoukaiEntity e) {
		return GLMeta.CHAR.type().getOrCreate(player).get(player, e);
	}

	public static CharDataHolder getUnbounded(Player player, EntityType<?> e) {
		return GLMeta.CHAR.type().getOrCreate(player).getUnbounded(player, e);
	}

	public void onHurt(DamageSource source, float amount) {
		if (e == null) return;
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
		data.gainReputation(v, CharacterData.MAX);
		sync();
		return v;
	}

	public void gain(int v, int max) {
		data.gainReputation(v, max);
		sync();
	}

	public void sync() {
		if (player instanceof ServerPlayer sp)
			GensokyoLegacy.HANDLER.toClientPlayer(new CharDataToClient(type, player.getUUID(), data), sp);
	}

}
