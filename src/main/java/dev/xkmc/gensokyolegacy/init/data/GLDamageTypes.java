package dev.xkmc.gensokyolegacy.init.data;

import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.l2damagetracker.init.data.DamageTypeAndTagsGen;
import dev.xkmc.l2damagetracker.init.data.L2DamageTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class GLDamageTypes extends DamageTypeAndTagsGen {

	public static final ResourceKey<DamageType> KOISHI = create("koishi_attack"
			, "Koishi stabbed %s in the back", "%2$s stabbed %1$s in the back");
	public static final ResourceKey<DamageType> RUMIA = create("rumia_attack",
			"%s is eaten by Rumia", "%s is eaten by %s");

	public GLDamageTypes(L2Registrate reg) {
		super(reg);
		new DamageTypeHolder(KOISHI, new DamageType("koishi_attack", 0.1f))
				.add(L2DamageTypes.BYPASS_MAGIC);
		new DamageTypeHolder(RUMIA, new DamageType("rumia_attack", 0.1f))
				.add(L2DamageTypes.BYPASS_MAGIC);
	}

	public static DamageSource koishi(LivingEntity target, Vec3 source) {
		return new DamageSource(target.level().registryAccess()
				.registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(KOISHI),
				source);
	}

	public static DamageSource rumia(LivingEntity self) {
		return new DamageSource(self.level().registryAccess()
				.registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(RUMIA), self);
	}

	private static ResourceKey<DamageType> create(String id, String msg) {
		return create(id, msg, msg);
	}

	private static ResourceKey<DamageType> create(String id, String msg, String player) {
		return create(id, msg, player, player);
	}

	private static ResourceKey<DamageType> create(String id, String msg, String player, String item) {
		GensokyoLegacy.REGISTRATE.addRawLang("death.attack." + id, msg);
		GensokyoLegacy.REGISTRATE.addRawLang("death.attack." + id + ".player", player);
		GensokyoLegacy.REGISTRATE.addRawLang("death.attack." + id + ".item", item);
		return create(id);
	}

	private static ResourceKey<DamageType> create(String id) {
		return ResourceKey.create(Registries.DAMAGE_TYPE, GensokyoLegacy.loc(id));
	}

}
