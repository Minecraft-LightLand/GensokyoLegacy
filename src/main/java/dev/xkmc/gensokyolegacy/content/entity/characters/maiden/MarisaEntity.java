package dev.xkmc.gensokyolegacy.content.entity.characters.maiden;

import dev.xkmc.danmakuapi.init.data.DanmakuDamageTypes;
import dev.xkmc.gensokyolegacy.compat.touhoulittlemaid.TouhouConditionalSpawns;
import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiFeatureSet;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

public class MarisaEntity extends MaidenEntity implements GeoEntity {
    protected static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public MarisaEntity(EntityType<? extends MaidenEntity> pEntityType, Level pLevel) {
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

    protected <E extends MarisaEntity> PlayState idleAnimController(final AnimationState<E> event) {
        return event.setAndContinue(IDLE);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "Flying", 5, this::idleAnimController));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }
}
