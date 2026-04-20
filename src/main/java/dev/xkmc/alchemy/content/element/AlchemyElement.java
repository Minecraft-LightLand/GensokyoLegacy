package dev.xkmc.alchemy.content.element;

import com.tterrag.registrate.util.entry.FluidEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.alchemy.content.fluid.MixableFluid;
import dev.xkmc.alchemy.content.fluid.MixableFluidType;
import dev.xkmc.alchemy.content.fluid.VirtualFluidBuilder;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;

public class AlchemyElement {

	final FluidEntry<MixableFluid> fluid;
	final ItemEntry<ElementSymbolItem> item;

	public AlchemyElement(L2Registrate reg, String pid, int pc) {
		this.fluid = VirtualFluidBuilder.water(reg, pid,
						(p, s, f) -> new MixableFluidType(p, s, f, pc),
						MixableFluid::new)
				.register();

		this.item = reg.item(pid, p -> new ElementSymbolItem(p, this))
				.model((ctx, pvd) ->
						pvd.generated(ctx, pvd.modLoc("item/element/" + ctx.getName())))
				.register();
	}


}
