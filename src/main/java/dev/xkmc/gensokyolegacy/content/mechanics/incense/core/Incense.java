package dev.xkmc.gensokyolegacy.content.mechanics.incense.core;

import dev.xkmc.gensokyolegacy.init.registrate.GLMechanics;
import dev.xkmc.l2core.init.reg.registrate.NamedEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Predicate;

public class Incense extends NamedEntry<Incense> {

	public Incense() {
		super(GLMechanics.INCENSE);
	}

	public ItemStack asStack() {
		return BuiltInRegistries.ITEM.get(getRegistryName()).getDefaultInstance();
	}

	public void tick(Level level, BlockPos pos, int radius) {
	}

	public List<LivingEntity> getEntities(Level level, BlockPos pos, int radius, Predicate<LivingEntity> pred) {
		Vec3 center = pos.getCenter();
		return level.getEntities(EntityTypeTest.forClass(LivingEntity.class),
				AABB.encapsulatingFullBlocks(pos, pos).inflate(radius + 2), e -> pred.test(e) &&
						center.distanceTo(e.position().add(0, e.getBbHeight() / 2, 0)) <
								radius + e.getBbWidth() / 2 + e.getBbHeight() / 2);
	}

	public void addParticles(Level level, BlockPos pos, int radius, int count, ParticleOptions opt) {
		Vec3 center = pos.getCenter();
		var r = level.getRandom();
		for (int i = 0; i < count; i++) {
			double rad = Math.pow(r.nextDouble(), 1 / 3f);
			Vec3 dir = new Vec3(
					r.nextGaussian(),
					r.nextGaussian(),
					r.nextGaussian()
			).normalize();
			Vec3 p = center.add(dir.scale(rad * radius));
			level.addParticle(opt, p.x, p.y, p.z, 0, 0, 0);
		}

	}

	public IncenseData getData(Level level) {
		var data = GLMechanics.INCENSE_DATA.get(level.registryAccess(), holder());
		return data == null ? IncenseData.DEF : data;
	}

	public int getRadius(Level level) {
		return getData(level).radius();
	}

	public int getBrightness(Level level) {
		return getData(level).light();
	}

}
