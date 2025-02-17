package dev.xkmc.gensokyolegacy.init.data.dimension;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.DensityFunction;

public record AddNoise(
		DensityFunction main, DensityFunction noise, float slope
) implements DensityFunction {

	public static final MapCodec<AddNoise> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			DensityFunction.HOLDER_HELPER_CODEC.fieldOf("main").forGetter(AddNoise::main),
			DensityFunction.HOLDER_HELPER_CODEC.fieldOf("noise").forGetter(AddNoise::noise),
			Codec.FLOAT.fieldOf("slope").forGetter(AddNoise::slope)
	).apply(i, AddNoise::new));

	public static final KeyDispatchDataCodec<AddNoise> KEY_CODEC = KeyDispatchDataCodec.of(CODEC);

	@Override
	public double compute(FunctionContext context) {
		double m = main.compute(context);
		double n = noise.compute(context);
		return m + Mth.clamp(m * slope, 0, 1) * n;
	}

	@Override
	public void fillArray(double[] array, ContextProvider ctx) {
		main.fillArray(array, ctx);
		double[] n = new double[array.length];
		noise.fillArray(n, ctx);
		for (int i = 0; i < array.length; i++) {
			array[i] += Mth.clamp(array[i] * slope, 0, 1) * n[i];
		}
	}

	@Override
	public DensityFunction mapAll(DensityFunction.Visitor e) {
		return e.apply(new AddNoise(main.mapAll(e), noise.mapAll(e), slope));
	}

	@Override
	public double minValue() {
		return Math.min(main().minValue(), noise.minValue());
	}

	@Override
	public double maxValue() {
		return main.maxValue() + noise.maxValue();
	}

	@Override
	public KeyDispatchDataCodec<? extends DensityFunction> codec() {
		return KEY_CODEC;
	}

}
