package dev.xkmc.gensokyolegacy.content.entity.characters.rumia;

import dev.xkmc.danmakuapi.api.IDanmakuEntity;
import dev.xkmc.danmakuapi.content.entity.ItemBulletEntity;
import dev.xkmc.danmakuapi.content.item.DanmakuItem;
import dev.xkmc.gensokyolegacy.content.entity.behavior.combat.DefaultCombatManager;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import org.jetbrains.annotations.Nullable;

class RumiaCombatManager extends DefaultCombatManager {

	private static final ResourceLocation SPELL_RUMIA = GensokyoLegacy.loc("rumia");
	private static final ResourceLocation SPELL_EX_RUMIA = GensokyoLegacy.loc("ex_rumia");

	private final RumiaEntity rumia;

	public RumiaCombatManager(RumiaEntity rumia) {
		super(rumia, null);
		this.rumia = rumia;
	}

	@Override
	public boolean isInvulnerableTo(DamageSource source) {
		if (rumia.isEx()) {
			if (!source.is(DamageTypeTags.BYPASSES_INVULNERABILITY) &&
					!(source.getEntity() instanceof LivingEntity))
				return true;
		}
		return source.getEntity() instanceof RumiaEntity;
	}

	@Override
	public boolean shouldShowCircle() {
		return super.shouldShowCircle() && !rumia.isBlocked() && !rumia.isCharged();
	}

	@Override
	public @Nullable ResourceLocation getSpellCircle() {
		if (!shouldShowCircle()) {
			return null;
		}
		return rumia.isEx() ? SPELL_EX_RUMIA : SPELL_RUMIA;
	}

	@Override
	public void onDanmakuHit(LivingEntity e, IDanmakuEntity danmaku) {
		if (targetKind(e).noAdditionalEffect()) return;
		if (danmaku instanceof ItemBulletEntity d && d.getItem().getItem() instanceof DanmakuItem item) {
			if (item.color == DyeColor.BLACK) {
				e.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 1));
				if (!rumia.isEx()) return;
			}
		}
		super.onDanmakuHit(e, danmaku);
	}

}
