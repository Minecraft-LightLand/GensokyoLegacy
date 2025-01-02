package dev.xkmc.gensokyolegacy.content.mechanics.incense.core;

public record IncenseData(
		int radius, int light
) {

	public static final IncenseData DEF = new IncenseData(6, 15);

}
