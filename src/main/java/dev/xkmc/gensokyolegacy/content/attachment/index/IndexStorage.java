package dev.xkmc.gensokyolegacy.content.attachment.index;

import dev.xkmc.l2core.capability.level.BaseSavedData;
import dev.xkmc.l2serial.serialization.codec.TagCodec;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;

import java.util.LinkedHashMap;
import java.util.Map;

@SerialClass
public class IndexStorage extends BaseSavedData<IndexStorage> {

	private static final String ID = "l2backpack_dimensional";
	private static final Factory<IndexStorage> FACTORY = new Factory<>(IndexStorage::new, IndexStorage::new);

	public static IndexStorage get(ServerLevel level) {
		var ans = level.getServer().overworld().getDataStorage().computeIfAbsent(FACTORY, ID);
		ans.level = level;
		return ans;
	}

	private ServerLevel level;

	@SerialField
	private final Map<StructureKey, StructureRefData> structureData = new LinkedHashMap<>();

	public IndexStorage() {
		super(IndexStorage.class);
	}

	private IndexStorage(CompoundTag tag, HolderLookup.Provider pvd) {
		super(IndexStorage.class);
		new TagCodec(pvd).fromTag(tag, IndexStorage.class, this);
	}

	public StructureRef getOrCreate(StructureKey key) {
		return new StructureRef(this, key, structureData.computeIfAbsent(key, k -> new StructureRefData()));
	}

	public void remove(StructureKey key) {
		structureData.remove(key);
	}

}
