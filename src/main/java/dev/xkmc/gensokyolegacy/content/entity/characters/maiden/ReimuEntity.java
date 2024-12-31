package dev.xkmc.gensokyolegacy.content.entity.characters.maiden;

import dev.xkmc.danmakuapi.init.data.DanmakuDamageTypes;
import dev.xkmc.gensokyolegacy.compat.touhoulittlemaid.TouhouConditionalSpawns;
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
	protected void onKilledBy(LivingEntity le, DamageSource source) {
		super.onKilledBy(le, source);
		if (!source.is(DanmakuDamageTypes.DANMAKU_TYPE)) {
			TouhouConditionalSpawns.triggetYukari(le, position());
		}
	}

}
