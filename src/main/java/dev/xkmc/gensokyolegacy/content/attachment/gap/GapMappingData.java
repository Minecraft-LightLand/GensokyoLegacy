package dev.xkmc.gensokyolegacy.content.attachment.gap;

import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.l2core.capability.level.BaseSavedData;
import dev.xkmc.l2serial.serialization.codec.TagCodec;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.UUID;

@SerialClass
public class GapMappingData extends BaseSavedData<GapMappingData> {

	private static final String ID = GensokyoLegacy.MODID + "_gap_mapping";
	private static final Factory<GapMappingData> FACTORY = new Factory<>(GapMappingData::new, GapMappingData::new);

	public static GapMappingData get(ServerLevel level) {
		var ans = level.getServer().overworld().getDataStorage().computeIfAbsent(FACTORY, ID);
		ans.level = level;
		return ans;
	}

	private ServerLevel level;

	@SerialField
	public final LinkedHashMap<UUID, GapMapping> mapping = new LinkedHashMap<>();

	public GapMappingData() {
		super(GapMappingData.class);
	}

	private GapMappingData(CompoundTag tag, HolderLookup.Provider pvd) {
		super(GapMappingData.class);
		new TagCodec(pvd).fromTag(tag, GapMappingData.class, this);
	}

	public @Nullable GapMapping get(UUID id) {
		return mapping.get(id);
	}

	public void set(UUID id, GapMapping data) {
		var prev = mapping.put(id, data);
	}

	public void remove(UUID key) {
		mapping.remove(key);
	}

}
