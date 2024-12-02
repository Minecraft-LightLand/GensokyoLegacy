package dev.xkmc.gensokyolegacy.content.entity.characters.reimu;

import dev.xkmc.gensokyolegacy.content.entity.youkai.BossYoukaiEntity;
import dev.xkmc.gensokyolegacy.content.entity.behavior.combat.YoukaiCombatManager;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;

@SerialClass
public class MaidenEntity extends BossYoukaiEntity {

	public MaidenEntity(EntityType<? extends MaidenEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		if (walkNav instanceof GroundPathNavigation nav) {
			nav.setCanPassDoors(true);
			nav.setCanOpenDoors(true);
			nav.setCanFloat(true);
		}
	}

	@Override
	protected YoukaiCombatManager createCombatManager() {
		return new MaidenCombatManager(this);
	}

}
