package dev.xkmc.gensokyolegacy.content.attachment.chunk;

import dev.xkmc.gensokyolegacy.content.attachment.index.StructureKey;
import dev.xkmc.l2core.capability.attachment.GeneralCapabilityTemplate;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.LinkedHashMap;
import java.util.Map;

@SerialClass
public class StructureAttachment extends GeneralCapabilityTemplate<LevelChunk, StructureAttachment> {

	@SerialField
	public final Map<StructureKey, HomeData> data = new LinkedHashMap<>();

}
