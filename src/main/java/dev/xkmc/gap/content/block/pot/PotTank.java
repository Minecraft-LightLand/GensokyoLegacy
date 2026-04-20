package dev.xkmc.gap.content.block.pot;

import dev.xkmc.l2core.base.tile.BaseTank;
import net.neoforged.neoforge.fluids.FluidStack;

public class PotTank extends BaseTank {

	private final int totalMax;

	public PotTank(int totalMax) {
		super(8, totalMax);
		this.totalMax = totalMax;
	}

	private int getTotal() {
		int sum = 0;
		for (int i = 0; i < getTanks(); i++) {
			sum += getFluidInTank(i).getAmount();
		}
		return sum;
	}

	@Override
	public int fill(FluidStack resource, FluidAction action) {
		int total = getTotal();
		if (total >= totalMax) return 0;
		if (total + resource.getAmount() > totalMax)
			resource = resource.copyWithAmount(totalMax - total);
		return super.fill(resource, action);
	}

}
