package dev.xkmc.gensokyolegacy.event;

import dev.xkmc.gensokyolegacy.init.registrate.GLEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class MixinHookHandlers {


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
			return Math.max(le.getSpeed() * 0.3f, original);
		}
		return original;
	}

	public static void alterDrops(@Nullable DamageSource source, long seed, Consumer<ItemStack> output, BiConsumer<Long, Consumer<ItemStack>> roll) {
		if (source != null) {
			var attacker = source.getEntity();
			if (attacker instanceof LivingEntity le) {
				var ins = le.getEffect(GLEffects.LOOTING);
				if (ins != null) {
					int n = 2 + ins.getAmplifier();
					List<ItemStack> all = new ArrayList<>();
					Set<Item> set = new HashSet<>();
					for (int i = 0; i < n; i++) {
						List<ItemStack> list = new ArrayList<>();
						roll.accept(seed, list::add);
						seed = seed >> 3 ^ seed << 3;
						for (var e : list) {
							if (set.contains(e.getItem())) continue;
							set.add(e.getItem());
							all.add(e);
						}
					}
					for (var e : all) {
						output.accept(e);
					}
					return;
				}
			}
		}
		roll.accept(seed, output);
	}
}