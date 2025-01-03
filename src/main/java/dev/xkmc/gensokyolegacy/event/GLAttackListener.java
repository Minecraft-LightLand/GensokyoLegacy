package dev.xkmc.gensokyolegacy.event;

import dev.xkmc.danmakuapi.init.data.DanmakuDamageTypes;
import dev.xkmc.gensokyolegacy.content.attachment.role.RolePlayHandler;
import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import dev.xkmc.gensokyolegacy.content.item.character.TouhouHatItem;
import dev.xkmc.gensokyolegacy.content.mechanics.role.core.RoleCategory;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.gensokyolegacy.init.data.GLModConfig;
import dev.xkmc.l2core.events.SchedulerHandler;
import dev.xkmc.l2damagetracker.contents.attack.AttackListener;
import dev.xkmc.l2damagetracker.contents.attack.DamageData;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class GLAttackListener implements AttackListener {

	@Override
	public void onDamage(DamageData.Defence data) {
		if (data.getSource().is(DanmakuDamageTypes.DANMAKU) && data.getSource().getEntity() instanceof YoukaiEntity) {

			LivingEntity le = data.getTarget();
			double min = le instanceof Player ?
					GLModConfig.SERVER.danmakuPlayerPHPDamage.get() :
					GLModConfig.SERVER.danmakuMinPHPDamage.get();
			data.addDealtModifier(DamageModifier.nonlinearMiddle(460,
					f -> Math.max(f, le.getMaxHealth() * (float) min),
					GensokyoLegacy.loc("youkai_damage")
			));
		}
		if (data.getTarget() instanceof AbstractVillager e &&
				GLModConfig.SERVER.reimuSummonKill.get() &&
				data.getSource().getEntity() instanceof ServerPlayer sp &&
				RolePlayHandler.is(sp, RoleCategory.YOUKAI)
		) {
			SchedulerHandler.schedule(() -> {
				if (e.isAlive()) {
					ReimuEventHandlers.hurtWarn(sp);
				}
			});
		}
	}

	@Override
	public void onDamageFinalized(DamageData.DefenceMax data) {
		var attacker = data.getAttacker();
		if (attacker == null) return;
		ItemStack head = attacker.getItemBySlot(EquipmentSlot.HEAD);
		if (head.getItem() instanceof TouhouHatItem hat) {
			hat.onHurtTarget(head, data.getSource(), data.getTarget());
		}
	}

}
