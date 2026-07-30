package dev.xkmc.gensokyolegacy.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.xkmc.gensokyolegacy.event.MixinHookHandlers;
import dev.xkmc.gensokyolegacy.init.registrate.GLEffects;
import net.minecraft.core.Holder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Consumer;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

	@Shadow
	public abstract boolean hasEffect(Holder<MobEffect> effect);

	@Inject(at = @At("HEAD"), method = "canBeSeenAsEnemy", cancellable = true)
	public void gensokyolegacy$canBeSeenAsEnemy$unconscious(CallbackInfoReturnable<Boolean> cir) {
		LivingEntity self = (LivingEntity) (Object) this;
		if (self instanceof Player player && hasEffect(GLEffects.UNCONSCIOUS)) {
			cir.setReturnValue(false);
		}
	}

	@WrapOperation(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getGravity()D"))
	public double gensokyoLegacy$gravity(LivingEntity instance, Operation<Double> original) {
		return MixinHookHandlers.getGravity(instance, original.call(instance));
	}

	@WrapOperation(method = "getFrictionInfluencedSpeed", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getFlyingSpeed()F"))
	public float gensokyoLegacy$flyingSpeed(LivingEntity instance, Operation<Float> original) {
		return MixinHookHandlers.getFlyingSpeed(instance, original.call(instance));
	}

	@WrapOperation(method = "dropFromLootTable", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/storage/loot/LootTable;getRandomItems(Lnet/minecraft/world/level/storage/loot/LootParams;JLjava/util/function/Consumer;)V"))
	public void gensokyoLegacy$dropIncrease(LootTable instance, LootParams params, long seed, Consumer<ItemStack> output, Operation<Void> original, @Local(argsOnly = true) DamageSource source) {
		MixinHookHandlers.alterDrops(source, seed, output, (s, c) -> original.call(instance, params, s, c));
	}

}
