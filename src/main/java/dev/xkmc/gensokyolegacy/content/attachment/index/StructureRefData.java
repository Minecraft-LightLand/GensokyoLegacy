package dev.xkmc.gensokyolegacy.content.attachment.index;

import dev.xkmc.gensokyolegacy.content.attachment.datamap.BedData;
import dev.xkmc.gensokyolegacy.content.attachment.datamap.CharacterConfig;
import dev.xkmc.gensokyolegacy.content.block.bed.YoukaiBedBlockEntity;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

@SerialClass
public class StructureRefData {

	@SerialField
	private long lastBlockTickedTime = 0;
	@SerialField
	private long structureTick = 0;
	@SerialField
	private final Map<EntityType<?>, BedRefData> entities = new LinkedHashMap<>();

	public void blockTick(ServerLevel sl, BedData data, YoukaiBedBlockEntity be, StructureKey key) {
		long time = sl.getGameTime();
		if (time != lastBlockTickedTime) {
			structureTick++;
			lastBlockTickedTime = time;
		}
		var config = CharacterConfig.of(data.type());
		if (config != null) {
			var ref = entities.computeIfAbsent(data.type(), k -> new BedRefData());
			ref.blockTick(data, config, sl, be, key);
		}
	}

	@Nullable
	public BedRefData getEntityRef(EntityType<?> type) {
		return entities.get(type);
	}

}
