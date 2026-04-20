
package dev.xkmc.alchemy.content.fluid;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.builders.FluidBuilder;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

public class VirtualFluidBuilder<T extends BaseFlowingFluid, P> extends FluidBuilder<T, P> {

	public static final ResourceLocation WATER_FLOW = ResourceLocation.withDefaultNamespace("block/water_flow");
	public static final ResourceLocation WATER_STILL = ResourceLocation.withDefaultNamespace("block/water_still");

	public static <T extends MixableFluid> FluidBuilder<T, L2Registrate> virtualFluid(
			L2Registrate reg,
			String id, ResourceLocation flow, ResourceLocation still,
			FluidBuilder.FluidTypeFactory typeFactory, NonNullFunction<BaseFlowingFluid.Properties, T> factory) {
		return reg.entry(id, (c) -> new VirtualFluidBuilder<>(reg, reg, id, c, still, flow, typeFactory, factory));
	}

	public static <T extends MixableFluid> FluidBuilder<T, L2Registrate> water(
			L2Registrate reg, String id, FluidBuilder.FluidTypeFactory typeFactory,
			NonNullFunction<BaseFlowingFluid.Properties, T> factory) {
		return virtualFluid(reg, id, WATER_FLOW, WATER_STILL, typeFactory, factory);
	}

	public VirtualFluidBuilder(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback,
							   ResourceLocation stillTexture, ResourceLocation flowingTexture,
							   FluidBuilder.FluidTypeFactory typeFactory, NonNullFunction<BaseFlowingFluid.Properties, T> factory) {
		super(owner, parent, name, callback, stillTexture, flowingTexture, typeFactory, factory);
		this.source(factory);
	}

	public NonNullSupplier<T> asSupplier() {
		return this::getEntry;
	}
}
