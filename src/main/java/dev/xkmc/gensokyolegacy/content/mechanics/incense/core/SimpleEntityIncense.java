package dev.xkmc.gensokyolegacy.content.mechanics.incense.core;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Predicate;

public class SimpleEntityIncense extends Incense {

	@Override
	public void tickClient(Level level, BlockPos pos, int radius) {
		super.tickClient(level, pos, radius);
		addParticlesInRange(level, pos, radius, 5, getParticleOptions());
	}

	public ParticleOptions getParticleOptions() {
		return ParticleTypes.COMPOSTER;
	}

	public List<LivingEntity> getEntities(Level level, BlockPos pos, int radius, Predicate<LivingEntity> pred) {
		Vec3 center = pos.getCenter();
		return level.getEntities(EntityTypeTest.forClass(LivingEntity.class),
				AABB.encapsulatingFullBlocks(pos, pos).inflate(radius + 2), e -> pred.test(e) &&
						center.distanceTo(e.position().add(0, e.getBbHeight() / 2, 0)) <
								radius + e.getBbWidth() / 2 + e.getBbHeight() / 2);
	}

}
