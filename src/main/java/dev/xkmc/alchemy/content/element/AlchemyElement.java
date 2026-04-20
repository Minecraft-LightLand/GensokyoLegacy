package dev.xkmc.alchemy.content.element;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.tterrag.registrate.util.entry.FluidEntry;
import dev.xkmc.alchemy.content.fluid.MixableFluid;
import dev.xkmc.alchemy.content.fluid.MixableFluidType;
import dev.xkmc.alchemy.content.fluid.VirtualFluidBuilder;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

public class AlchemyElement {

	private final FluidEntry<MixableFluid> pos, neg;

	public AlchemyElement(L2Registrate reg, String pid, int pc, String nid, int nc) {
		this.pos = VirtualFluidBuilder.water(reg, pid + "_essence",
						(p, s, f) -> new MixableFluidType(p, s, f, pc),
						MixableFluid::new)
				.lang(RegistrateLangProvider.toEnglishName(pid + "_essence"))
				.register();
		this.neg = VirtualFluidBuilder.water(reg, nid + "_essence",
						(p, s, f) -> new MixableFluidType(p, s, f, nc),
						MixableFluid::new)
				.lang(RegistrateLangProvider.toEnglishName(nid + "_essence"))
				.register();
	}

	public AlchemyElement(L2Registrate reg, String pid, int pc) {
		this.pos = VirtualFluidBuilder.water(reg, pid + "_essence",
						(p, s, f) -> new MixableFluidType(p, s, f, pc),
						MixableFluid::new)
				.lang(RegistrateLangProvider.toEnglishName(pid + "_essence"))
				.register();
		this.neg = VirtualFluidBuilder.water(reg, pid + "_canceller",
						(p, s, f) -> new MixableFluidType(p, s, f, pc),
						MixableFluid::new)
				.lang(RegistrateLangProvider.toEnglishName(pid + "_canceller"))
				.register();
	}

	public MixableFluid getPos() {
		return pos.getSource();
	}

	public MixableFluid getNeg() {
		return neg.getSource();
	}

}
