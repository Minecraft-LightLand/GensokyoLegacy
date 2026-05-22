package dev.xkmc.gensokyolegacy.content.entity.characters.maiden;

import com.geckolib.animatable.GeoAnimatable;
import com.geckolib.animatable.GeoEntity;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.animation.AnimationController;
import com.geckolib.animation.RawAnimation;
import com.geckolib.animation.object.PlayState;
import com.geckolib.animation.state.AnimationTest;
import com.geckolib.util.GeckoLibUtil;
import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiFeatureSet;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

@SerialClass
public class ReimuEntity extends MaidenEntity implements GeoEntity {
	protected static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");

	private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

	public ReimuEntity(EntityType<? extends ReimuEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	@Override
	public YoukaiFeatureSet getFeatures() {
		return YoukaiFeatureSet.MAIDEN;
	}

	protected <E extends GeoAnimatable> PlayState idleAnimController(final AnimationTest<E> event) {
		return event.setAndContinue(IDLE);
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
		controllers.add(new AnimationController<>( "Flying", 5, this::idleAnimController));
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.geoCache;
	}
}
