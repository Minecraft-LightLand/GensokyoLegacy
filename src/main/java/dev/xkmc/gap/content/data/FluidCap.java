package dev.xkmc.gap.content.data;

import org.jetbrains.annotations.Nullable;

public record FluidCap(
		int max, boolean hide, @Nullable FluidCanceller canceller
) {
}
