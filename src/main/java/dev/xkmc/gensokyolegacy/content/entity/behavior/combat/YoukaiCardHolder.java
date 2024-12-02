package dev.xkmc.gensokyolegacy.content.entity.behavior.combat;

import dev.xkmc.danmakuapi.api.DanmakuBullet;
import dev.xkmc.danmakuapi.api.DanmakuLaser;
import dev.xkmc.danmakuapi.api.IDanmakuEntity;
import dev.xkmc.danmakuapi.content.entity.ItemBulletEntity;
import dev.xkmc.danmakuapi.content.entity.ItemLaserEntity;
import dev.xkmc.danmakuapi.content.spell.spellcard.CardHolder;
import dev.xkmc.danmakuapi.init.data.DanmakuDamageTypes;
import dev.xkmc.danmakuapi.init.registrate.DanmakuEntities;
import dev.xkmc.fastprojectileapi.entity.SimplifiedProjectile;
import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public record YoukaiCardHolder(YoukaiEntity self) implements CardHolder {

	private Vec3 position() {
		return self.position();
	}

	@Nullable
	private LivingEntity getTarget() {
		return self.getTarget();
	}

	private Level level() {
		return self.level();
	}

	private float getDamage() {
		return (float) self.getAttributeValue(Attributes.ATTACK_DAMAGE);
	}


	@Override
	public DamageSource getDanmakuDamageSource(IDanmakuEntity danmaku) {
		if (self.spellCard != null) return self.spellCard.card.getDanmakuDamageSource(danmaku);
		return DanmakuDamageTypes.danmaku(danmaku);
	}

	@Override
	public Vec3 center() {
		return position().add(0, self.getBbHeight() / 2, 0);
	}

	@Override
	public Vec3 forward() {
		var target = target();
		if (target != null) {
			return target.subtract(center()).normalize();
		}
		return self.getForward();
	}

	@Override
	public @Nullable Vec3 target() {
		var le = getTarget();
		if (le == null) return null;
		return le.position().add(0, le.getBbHeight() / 2, 0);
	}

	@Override
	public @Nullable Vec3 targetVelocity() {
		var le = getTarget();
		if (le == null) return null;
		return le.getDeltaMovement();
	}

	@Override
	public RandomSource random() {
		return self.getRandom();
	}

	@Override
	public ItemBulletEntity prepareDanmaku(int life, Vec3 vec, DanmakuBullet type, DyeColor color) {
		ItemBulletEntity danmaku = new ItemBulletEntity(DanmakuEntities.ITEM_DANMAKU.get(), self, level());
		danmaku.setPos(center());
		danmaku.setItem(type.get(color).asStack());
		danmaku.setup(getDamage(), life, true, true, vec);
		return danmaku;
	}

	@Override
	public ItemLaserEntity prepareLaser(int life, Vec3 pos, Vec3 vec, int len, DanmakuLaser type, DyeColor color) {
		ItemLaserEntity danmaku = new ItemLaserEntity(DanmakuEntities.ITEM_LASER.get(), self, level());
		danmaku.setItem(type.get(color).asStack());
		danmaku.setup(getDamage(), life, len, true, vec);
		danmaku.setPos(pos);
		return danmaku;
	}

	@Override
	public void shoot(SimplifiedProjectile danmaku) {
		level().addFreshEntity(danmaku);
	}

}
