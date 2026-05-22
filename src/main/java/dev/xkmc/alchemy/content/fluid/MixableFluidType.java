package dev.xkmc.alchemy.content.fluid;

import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidType;

import java.util.function.Consumer;

public class MixableFluidType extends FluidType {
	final Identifier stillTexture;
	final Identifier flowingTexture;
	final int color;

	public MixableFluidType(FluidType.Properties properties, Identifier stillTexture, Identifier flowingTexture, int color) {
		super(properties);
		this.stillTexture = stillTexture;
		this.flowingTexture = flowingTexture;
		this.color = color;
	}

	public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
		consumer.accept(new ClientMixableFluid(this));
	}

	public int getColor() {
		return color;
	}

}