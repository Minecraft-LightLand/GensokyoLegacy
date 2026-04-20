package dev.xkmc.alchemy.content.element;

import com.tterrag.registrate.providers.RegistrateDataMapProvider;
import dev.xkmc.alchemy.content.fluid.MixableFluid;
import dev.xkmc.gap.content.data.FluidCanceller;
import dev.xkmc.gap.content.data.FluidCap;
import dev.xkmc.gap.init.registrate.GapRegistries;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import net.minecraft.core.Holder;
import net.minecraft.world.level.material.Fluid;

public class AlchemyElementAxis {

	private final AlchemyElement pos, neg;

	public AlchemyElementAxis(L2Registrate reg, String pid, int pc, String nid, int nc) {
		this.pos = new AlchemyElement(reg, pid + "_essence", pc);
		this.neg = new AlchemyElement(reg, nid + "_essence", nc);
	}

	public AlchemyElementAxis(L2Registrate reg, String pid, int pc) {
		this.pos = new AlchemyElement(reg, pid + "_essence", pc);
		this.neg = new AlchemyElement(reg, pid + "_canceller", pc);
	}

	public Holder<Fluid> getPos() {
		return pos.fluid.getSource().builtInRegistryHolder();
	}

	public Holder<Fluid> getNeg() {
		return neg.fluid.getSource().builtInRegistryHolder();
	}

	public void regDataMap(RegistrateDataMapProvider pvd) {
		pvd.builder(GapRegistries.FLUID_CAP.reg())
				.add(getPos(), new FluidCap(10, new FluidCanceller(getNeg())), false)
				.add(getNeg(), new FluidCap(10, new FluidCanceller(getPos())), false);
	}

}
