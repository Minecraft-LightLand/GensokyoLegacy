package dev.xkmc.alchemy.content.fluid;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;

public record ClientMixableFluid(MixableFluidType type) implements IClientFluidTypeExtensions {

	public ClientMixableFluid(MixableFluidType type) {
		this.type = type;
	}

	public ResourceLocation getStillTexture() {
		return this.type.stillTexture;
	}

	public ResourceLocation getFlowingTexture() {
		return this.type.flowingTexture;
	}

	public int getTintColor() {
		return this.type.getColor();
	}

	public MixableFluidType type() {
		return this.type;
	}
}
