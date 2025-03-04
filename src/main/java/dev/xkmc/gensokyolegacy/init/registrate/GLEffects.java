package dev.xkmc.gensokyolegacy.init.registrate;

import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.gensokyolegacy.content.effect.CharacterEffect;
import dev.xkmc.gensokyolegacy.content.effect.HigiEffect;
import dev.xkmc.gensokyolegacy.content.effect.NativeGodBlessEffect;
import dev.xkmc.gensokyolegacy.content.mechanics.role.core.RoleCategory;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.l2core.init.reg.registrate.LegacyHolder;
import dev.xkmc.l2core.init.reg.registrate.SimpleEntry;
import dev.xkmc.youkaishomecoming.content.effect.EmptyEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class GLEffects {

	public static final LegacyHolder<MobEffect> NATIVE = genEffect("native_god_bless",
			() -> new NativeGodBlessEffect(MobEffectCategory.BENEFICIAL, -5727850),
			"Increase movement speed and reach");

	public static final LegacyHolder<MobEffect> UNCONSCIOUS = genEffect("unconscious",
			() -> new EmptyEffect(MobEffectCategory.BENEFICIAL, -5522492),
			"You won't be targeted by mobs. Terminates when you attack or open loot chests.");

	public static final LegacyHolder<MobEffect> HIGI = genEffect("higi",
			() -> new HigiEffect(MobEffectCategory.BENEFICIAL, 0x6CA16E),
			"Boost attack damage and movement speed, heal slowly");

	public static final LegacyHolder<MobEffect> FAIRY = genEffect("fairy",
			() -> new CharacterEffect(RoleCategory.FAIRY, MobEffectCategory.NEUTRAL, 0xd0c3a5),
			"Prompt player into Fairy roles");

	public static final LegacyHolder<MobEffect> VAMPIRE = genEffect("youkai",
			() -> new CharacterEffect(RoleCategory.YOUKAI, MobEffectCategory.NEUTRAL, 0xff0000),
			"Prompt player into Youkai roles");

	private static <T extends MobEffect> LegacyHolder<MobEffect> genEffect(String name, NonNullSupplier<T> sup, String desc) {
		return new SimpleEntry<>(GensokyoLegacy.REGISTRATE.effect(name, sup, desc).lang(MobEffect::getDescriptionId).register());
	}

	public static void register() {

	}

}
