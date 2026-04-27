package dev.xkmc.gensokyolegacy.event;

import dev.xkmc.gensokyolegacy.content.entity.characters.rumia.RumiaEntity;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.gensokyolegacy.init.registrate.GLEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent;

@EventBusSubscriber(modid = GensokyoLegacy.MODID)
public class GLGeneralEventHandlers {

	@SubscribeEvent
	public static void onShieldBlock(LivingShieldBlockEvent event) {
		if (event.getBlocked() && event.getDamageSource().getDirectEntity() instanceof RumiaEntity rumia) {
			rumia.state.onBlocked();
		}
	}

	public static double getGravity(LivingEntity le, double original) {
		var ins = le.getEffect(GLEffects.FLOATING);
		if (ins != null && !le.onGround() && !le.isShiftKeyDown()) {
			int h = 2;
			double dist;
			if (le.onGround()) dist = 0;
			else {
				var pos = le.position();
				var ans = le.level().clip(new ClipContext(pos, pos.add(0, -h * 2, 0),
						ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY, le));
				dist = ans.getLocation().distanceTo(pos);
			}
			return original * Math.clamp((dist - h) / h, -1, 1) + le.getDeltaMovement().y() * 0.1;
		}
		return original;
	}

	public static float getFlyingSpeed(LivingEntity le, float original) {
		if (le.onGround() || le instanceof Player player && player.getAbilities().flying) return original;
		var ins = le.getEffect(GLEffects.FLOATING);
		if (ins != null) {
			return Math.max(le.getSpeed() * 1f, original);
		}
		return original;
	}

}