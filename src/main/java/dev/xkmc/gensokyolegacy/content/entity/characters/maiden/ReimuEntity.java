package dev.xkmc.gensokyolegacy.content.entity.characters.maiden;

import dev.xkmc.danmakuapi.init.data.DanmakuDamageTypes;
import dev.xkmc.gensokyolegacy.compat.touhoulittlemaid.TouhouSpellCards;
import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiFeatureSet;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

@SerialClass
public class ReimuEntity extends MaidenEntity {

	public ReimuEntity(EntityType<? extends ReimuEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	@Override
	public YoukaiFeatureSet getFeatures() {
		return YoukaiFeatureSet.MAIDEN;
	}

	@Override
	public void initSpellCard() {
		TouhouSpellCards.setReimu(this);
	}

	@Override
	protected void onKilledBy(LivingEntity le, DamageSource source) {
		if (!source.is(DanmakuDamageTypes.DANMAKU_TYPE)) {
			//TODO TouhouConditionalSpawns.triggetYukari(le, position());
		}
	}

}
