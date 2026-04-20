package dev.xkmc.gap.content.block.pot;

import dev.xkmc.gap.content.data.FluidCap;
import dev.xkmc.gap.init.registrate.GapRegistries;
import dev.xkmc.l2core.base.tile.BaseTank;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

@SerialClass
public class PotTank extends BaseTank {

	private final int max;
	private Supplier<Level> levelAccess;

	public PotTank(int max) {
		super(8, max);
		this.max = max;
	}

	public void setLevelAccess(Supplier<Level> access) {
		levelAccess = access;
	}

	@Nullable
	public FluidCap getCap(FluidStack stack) {
		if (levelAccess == null) return null;
		var level = levelAccess.get();
		if (level == null) return null;
		var access = level.registryAccess();
		return GapRegistries.FLUID_CAP.get(access, stack.getFluidHolder());
	}

	public int getMax(FluidStack stack) {
		var cap = getCap(stack);
		if (cap == null) return max;
		return cap.max();
	}

	@Override
	public int fill(FluidStack resource, FluidAction action) {
		int amount = resource.getAmount();
		int filled = 0;
		var cap = getCap(resource);
		if (cap != null && cap.canceller() != null) {
			for (int i = 0; i < getTanks(); i++) {
				var old = getFluidInTank(i);
				if (old.isEmpty()) continue;
				if (old.getFluid() == cap.canceller().fluid().value()) {
					int mayCancel = Math.min(amount, old.getAmount());
					if (mayCancel <= 0) return 0;
					if (action.execute()) {
						old.shrink(mayCancel);

					}
					amount -= mayCancel;
					filled += mayCancel;
				}
			}
		}
		if (amount <= 0) return filled;
		for (int i = 0; i < getTanks(); i++) {
			var old = getFluidInTank(i);
			if (old.isEmpty()) continue;
			if (FluidStack.isSameFluidSameComponents(resource, old)) {
				int max = getMax(resource);
				int mayFill = Math.min(amount, max - old.getAmount());
				if (mayFill <= 0) return 0;
				if (action.execute()) {
					old.grow(mayFill);
				}
				return filled + mayFill;
			}
		}
		return filled + super.fill(resource.copyWithAmount(amount), action);
	}

}
