package dev.xkmc.gensokyolegacy.content.entity.characters.fairy;

import dev.xkmc.gensokyolegacy.content.entity.behavior.combat.DefaultCombatManager;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

class FairyCombatManager extends DefaultCombatManager {

	private static final ResourceLocation SPELL_RUMIA = YoukaisHomecoming.loc("rumia");

	private final FairyEntity fairyEntity;

	public FairyCombatManager(FairyEntity fairyEntity) {
		super(fairyEntity, SPELL_RUMIA);
		this.fairyEntity = fairyEntity;
	}

	@Override
	public boolean shouldHurtInnocent(LivingEntity le) {
		return le instanceof Mob mob && mob.getTarget() != null;
	}

}
