package dev.xkmc.gensokyolegacy.init.data.dimension;

import net.minecraft.world.level.biome.Climate;

public class GLParamDiv {

	public static GLParamDiv positive(float threshold) {
		return new GLParamDiv(true, false, threshold);
	}

	public static GLParamDiv trinary(float threshold) {
		return new GLParamDiv(false, true, threshold);
	}

	public static GLParamDiv polar() {
		return new GLParamDiv(false, false, 0);
	}

	private final boolean positive;
	private final boolean trinary;
	private final float threshold;

	private GLParamDiv(boolean positive, boolean trinary, float threshold) {
		this.positive = positive;
		this.trinary = trinary;
		this.threshold = threshold;
	}

	public Climate.Parameter get(int sign) {
		if (positive) {
			return sign == 0 ? span(0, threshold - 0.05f) : span(threshold + 0.05f, 1);
		}
		if (!trinary) {
			return sign < 0 ? span(-1, -0.05f) : span(0.05f, 1);
		}
		if (sign == 0) {
			return span(-threshold + 0.05f, threshold - 0.05f);
		}
		return sign < 0 ? span(-1, -threshold - 0.05f) : span(threshold + 0.05f, 1);
	}

	public Climate.Parameter all() {
		return positive ? span(0, 1) : span(-1, 1);
	}

	public Climate.Parameter tip(float tip) {
		if (positive) {
			return tip > 0.5 ? span(tip, 1) : span(0, tip);
		}
		return tip > 0 ? span(tip, 1) : span(-1, tip);
	}

	public static Climate.Parameter span(float min, float max) {
		if (min > max) {
			return new Climate.Parameter(Climate.quantizeCoord(max), Climate.quantizeCoord(min));
		} else {
			return new Climate.Parameter(Climate.quantizeCoord(min), Climate.quantizeCoord(max));
		}
	}

}
