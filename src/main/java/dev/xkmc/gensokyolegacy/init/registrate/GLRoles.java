package dev.xkmc.gensokyolegacy.init.registrate;

import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.gensokyolegacy.content.role.core.Role;
import dev.xkmc.gensokyolegacy.content.role.simple.DarknessRole;
import dev.xkmc.gensokyolegacy.content.role.simple.FairyRole;
import dev.xkmc.gensokyolegacy.content.role.simple.VampireRole;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.l2core.init.reg.simple.Val;

public class GLRoles {

	public static final L2Registrate.RegistryInstance<Role> ROLES = GensokyoLegacy.REGISTRATE.newRegistry("role", Role.class);

	public static final Val<VampireRole> VAMPIRE = reg("vampire", VampireRole::new);
	public static final Val<DarknessRole> RUMIA = reg("darkness", DarknessRole::new);
	public static final Val<FairyRole> FAIRY = reg("fairy", FairyRole::new);

	public static <T extends Role> Val<T> reg(String id, NonNullSupplier<T> sup) {
		return new Val.Registrate<>(GensokyoLegacy.REGISTRATE.generic(ROLES, id, sup).register());
	}

}
