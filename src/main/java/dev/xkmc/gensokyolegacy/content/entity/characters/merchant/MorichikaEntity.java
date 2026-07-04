package dev.xkmc.gensokyolegacy.content.entity.characters.merchant;

import dev.xkmc.gensokyolegacy.content.entity.youkai.GeneralYoukaiEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class MorichikaEntity extends GeneralYoukaiEntity implements GeoEntity {

	protected static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");

	private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

	public MorichikaEntity(EntityType<? extends GeneralYoukaiEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	@Override
	public boolean mayFly() {
		return false;
	}

	protected <E extends MorichikaEntity> PlayState idleAnimController(final AnimationState<E> event) {
		return event.setAndContinue(IDLE);
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
		//controllers.add(new AnimationController<>(this, "Flying", 5, this::idleAnimController));
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.geoCache;
	}

}
