package dev.xkmc.gensokyolegacy.content.worldgen.infmaze.worldgen.leaf;

import dev.xkmc.gensokyolegacy.content.worldgen.infmaze.init.CellContent;

import javax.annotation.Nullable;

public record CellContentEntry(@Nullable CellContent content, int weight) {
}
