package dev.xkmc.gensokyolegacy.content.attachment.datamap;

import net.minecraft.world.entity.EntityType;

import java.util.LinkedHashSet;

public record StructureConfig(LinkedHashSet<EntityType<?>> entities) {
}
