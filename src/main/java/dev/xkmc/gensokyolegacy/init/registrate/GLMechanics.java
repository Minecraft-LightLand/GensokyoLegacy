package dev.xkmc.gensokyolegacy.init.registrate;

import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.gensokyolegacy.content.mechanics.incense.core.*;
import dev.xkmc.gensokyolegacy.content.mechanics.incense.simple.CleanseIncense;
import dev.xkmc.gensokyolegacy.content.mechanics.incense.simple.DarknessIncense;
import dev.xkmc.gensokyolegacy.content.mechanics.incense.simple.GlowingIncense;
import dev.xkmc.gensokyolegacy.content.mechanics.incense.simple.HealIncense;
import dev.xkmc.gensokyolegacy.content.mechanics.ritual.Ritual;
import dev.xkmc.gensokyolegacy.content.mechanics.role.core.Role;
import dev.xkmc.gensokyolegacy.content.mechanics.role.effect.RoleDataMap;
import dev.xkmc.gensokyolegacy.content.mechanics.role.simple.*;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.l2core.init.reg.datapack.DataMapReg;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.l2core.init.reg.simple.Val;
import dev.xkmc.l2serial.serialization.codec.CodecAdaptor;
import net.neoforged.neoforge.registries.datamaps.DataMapType;

public class GLMechanics {

	public static final L2Registrate.RegistryInstance<Role> ROLES = GensokyoLegacy.REGISTRATE.newRegistry("role", Role.class);
	public static final L2Registrate.RegistryInstance<Incense> INCENSE = GensokyoLegacy.REGISTRATE.newRegistry("incense", Incense.class);
	public static final L2Registrate.RegistryInstance<Ritual> RITUAL = GensokyoLegacy.REGISTRATE.newRegistry("ritual", Ritual.class);

	public static final DataMapReg<Incense, IncenseData> INCENSE_DATA =
			GensokyoLegacy.REG.dataMap(DataMapType.builder(GensokyoLegacy.loc("incense_data"),
					INCENSE.key(), new CodecAdaptor<>(IncenseData.class)).build());
	public static final DataMapReg<Role, RoleDataMap> ROLE_ATTRIBUTE =
			GensokyoLegacy.REG.dataMap(DataMapType.builder(GensokyoLegacy.loc("role_attribute"),
					ROLES.key(), new CodecAdaptor<>(RoleDataMap.class)).build());

	public static final Val<VampireRole> VAMPIRE = regRole("vampire", VampireRole::new);
	public static final Val<RumiaRole> RUMIA = regRole("dark_youkai", RumiaRole::new);
	public static final Val<NatureFairyRole> NATURE_FAIRY = regRole("nature_fairy", NatureFairyRole::new);
	public static final Val<IceFairyRole> ICE_FAIRY = regRole("ice_fairy", IceFairyRole::new);
	public static final Val<HellFairyRole> HELL_FAIRY = regRole("hell_fairy", HellFairyRole::new);
	public static final Val<MaidenRole> MAIDEN = regRole("maiden", MaidenRole::new);
	public static final Val<MagicianRole> MAGICIAN = regRole("magician", MagicianRole::new);

	public static final IncenseEntry<HealIncense> HEAL = regIncense("heal_incense", HealIncense::new)
			.dataMap(INCENSE_DATA.reg(), new IncenseData(8, 15, 0xffff0000)).register();
	public static final IncenseEntry<CleanseIncense> CLEANSE = regIncense("cleanse_incense", CleanseIncense::new)
			.dataMap(INCENSE_DATA.reg(), new IncenseData(8, 12, 0xffffff7f)).register();
	public static final IncenseEntry<GlowingIncense> GLOW = regIncense("glow_incense", GlowingIncense::new)
			.dataMap(INCENSE_DATA.reg(), new IncenseData(16, 15, 0xffffffff)).register();
	public static final IncenseEntry<DarknessIncense> DARK = regIncense("darkness_incense", DarknessIncense::new)
			.dataMap(INCENSE_DATA.reg(), new IncenseData(6, 1, 0xff000000)).register();

	public static <T extends Role> Val<T> regRole(String id, NonNullSupplier<T> sup) {
		return new Val.Registrate<>(GensokyoLegacy.REGISTRATE.generic(ROLES, id, sup).defaultLang().register());
	}

	public static <T extends Incense> IncenseBuilder<T, L2Registrate> regIncense(String id, NonNullSupplier<T> sup) {
		var reg = GensokyoLegacy.REGISTRATE;
		return reg.entry(id, cb -> new IncenseBuilder<>(reg, reg, id, cb, sup))
				.item(IncenseItem::new).build();
	}

	public static void register() {

	}

}
