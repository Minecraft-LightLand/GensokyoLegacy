package dev.xkmc.gensokyolegacy.content.effect;

import dev.xkmc.gensokyolegacy.content.attachment.role.RolePlayHandler;
import dev.xkmc.gensokyolegacy.content.mechanics.role.RoleCategory;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.gensokyolegacy.init.data.GLModConfig;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

public class HigiEffect extends MobEffect {

	public HigiEffect(MobEffectCategory category, int color) {
		super(category, color);
		var uuid = GensokyoLegacy.loc("higi");
		addAttributeModifier(Attributes.ATTACK_DAMAGE, uuid, 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
		addAttributeModifier(Attributes.MOVEMENT_SPEED, uuid, 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
	}

	@Override
	public boolean applyEffectTick(LivingEntity e, int lv) {
		if (!e.level().isClientSide) {
			float amount = 1;
			int period = GLModConfig.SERVER.higiHealingPeriod.get() >> lv;
			if (e instanceof Player pl && RolePlayHandler.is(pl, RoleCategory.FAIRY)) period /= 3;
			if (period < 10) period = 10;
			if (e.tickCount % period == 0)
				e.heal(amount);
		}
		return true;
	}

	@Override
	public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
		return true;
	}

}
