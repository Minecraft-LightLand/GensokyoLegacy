package dev.xkmc.gensokyolegacy.content.entity.characters.fairy;

import dev.xkmc.danmakuapi.init.data.DanmakuDamageTypes;
import dev.xkmc.gensokyolegacy.compat.touhoulittlemaid.TouhouConditionalSpawns;
import dev.xkmc.gensokyolegacy.content.entity.behavior.combat.YoukaiCombatManager;
import dev.xkmc.gensokyolegacy.content.entity.youkai.GeneralYoukaiEntity;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

@SerialClass
public class FairyEntity extends GeneralYoukaiEntity {

	public static AttributeSupplier.Builder createAttributes() {
		return GeneralYoukaiEntity.createAttributes()
				.add(Attributes.MAX_HEALTH, 20)
				.add(Attributes.ATTACK_DAMAGE, 6);
	}

	public FairyEntity(EntityType<? extends GeneralYoukaiEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	@Override
	protected YoukaiCombatManager createCombatManager() {
		return new FairyCombatManager(this);
	}

	@Override
	protected void onKilledBy(LivingEntity le, DamageSource source) {
		super.onKilledBy(le, source);
		if (!source.is(DanmakuDamageTypes.DANMAKU_TYPE))
			TouhouConditionalSpawns.triggetFairyReinforcement(this, le, position());
	}
}
