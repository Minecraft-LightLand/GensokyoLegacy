package dev.xkmc.gensokyolegacy.content.mechanics.incense.core;

import dev.xkmc.gensokyolegacy.init.registrate.GLMechanics;
import dev.xkmc.l2core.init.reg.registrate.NamedEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class Incense extends NamedEntry<Incense> {

	public Incense() {
		super(GLMechanics.INCENSE);
	}

	public ItemStack asStack() {
		return BuiltInRegistries.ITEM.get(getRegistryName()).getDefaultInstance();
	}

	public void tick(Level level, BlockPos pos, int radius) {
		if (level.isClientSide()) {
			tickClient(level, pos, radius);
		} else {
			tickServer(level, pos, radius);
		}
	}

	public void tickClient(Level level, BlockPos pos, int radius) {
		if (level.getGameTime() % 4 == 0)
			addCenserParticle(level, pos);
	}

	public void tickServer(Level level, BlockPos pos, int radius) {

	}

	public void addCenserParticle(Level level, BlockPos pos) {
		var opt = ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, getData(level).color());
		Vec3 p = pos.getCenter();
		level.addParticle(opt, p.x, p.y - 0.3, p.z, 0, 0, 0);
	}

	public void addParticlesInRange(Level level, BlockPos pos, int radius, int count, ParticleOptions opt) {
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
