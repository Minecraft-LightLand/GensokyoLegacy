package dev.xkmc.gensokyolegacy.event;

import dev.xkmc.gensokyolegacy.content.entity.characters.maiden.MaidenEntity;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.gensokyolegacy.init.registrate.GLEffects;
import dev.xkmc.gensokyolegacy.init.registrate.GLItems;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = GensokyoLegacy.MODID, bus = EventBusSubscriber.Bus.GAME)
public class KoishiEventHandlers {


	@SubscribeEvent
	public static void onAttack(LivingIncomingDamageEvent event) {
		if (event.getSource().getEntity() instanceof LivingEntity le) {
			if (le instanceof Player player) {
				removeKoishi(player);
			}
		}
	}

	@SubscribeEvent
	public static void onTick(PlayerTickEvent event) {
		var e = event.getEntity();
		if (e.getLastHurtMob() instanceof MaidenEntity ||
				e.getLastHurtByMob() instanceof MaidenEntity) {
			removeKoishi(e);
		}
	}

	public static void removeKoishi(LivingEntity le) {
		if (le instanceof Player player) {
			if (player.hasEffect(GLEffects.UNCONSCIOUS)) {
				player.removeEffect(GLEffects.UNCONSCIOUS);
			}
			var hat = GLItems.KOISHI_HAT.get();
			if (player.getItemBySlot(EquipmentSlot.HEAD).is(hat)) {
				if (player.getCooldowns().getCooldownPercent(hat, 0) < 0.5)
					player.getCooldowns().addCooldown(hat, 200);
			}
		}
	}

}
