package dev.xkmc.gensokyolegacy.content.entity.characters.maiden;

import dev.xkmc.gensokyolegacy.content.entity.behavior.combat.DefaultCombatManager;
import dev.xkmc.gensokyolegacy.content.entity.behavior.combat.TargetKind;
import dev.xkmc.gensokyolegacy.content.entity.youkai.GeneralYoukaiEntity;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.npc.Villager;

class MaidenCombatManager extends DefaultCombatManager {

	public MaidenCombatManager(MaidenEntity e) {
		super(e, GeneralYoukaiEntity.SPELL);
	}

	@Override
	public TargetKind targetKind(LivingEntity le) {
		if (le.getType().is(EntityTypeTags.RAIDERS))
			return TargetKind.PRAY;
		if (le instanceof Mob mob && (mob.getTarget() instanceof Villager || mob.getLastHurtMob() instanceof Villager))
			return TargetKind.PRAY;
		return super.targetKind(le);
	}

}
