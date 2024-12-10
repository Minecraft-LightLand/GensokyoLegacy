package dev.xkmc.gensokyolegacy.content.attachment.index;

import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.Nullable;

public record StructureRef(IndexStorage storage, StructureKey key, StructureRefData data) {

	@Nullable
	public BedRefData bedOf(EntityType<?> type) {
		return data.getEntityRef(type);
	}

}
