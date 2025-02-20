package dev.xkmc.gensokyolegacy.content.worldgen.infmaze.leaf;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.gensokyolegacy.content.worldgen.infmaze.dim3d.MazeCell3D;
import dev.xkmc.gensokyolegacy.content.worldgen.infmaze.init.CellContent;
import dev.xkmc.gensokyolegacy.content.worldgen.infmaze.init.LeafManager;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.levelgen.RandomState;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public record RoomLeafManager(Map<LeafType, SimpleWeightedRandomList<CellContent>> map) implements LeafManager {

	public static final Codec<RoomLeafManager> CODEC = DataEntry.CODEC.listOf().xmap(DataEntry::build, RoomLeafManager::revert);

	@Nullable
	@Override
	public CellContent getLeaf(RandomSource random, MazeCell3D cell) {
		if (map.isEmpty()) return null;
		var type = LeafType.of(cell);
		if (type == null) return null;
		var list = map.get(type);
		if (list == null) return null;
		var entry = list.getRandom(random).orElse(null);
		if (entry == null) return null;
		return entry.data();
	}

	@Override
	public void decoratePath(RandomState random, MazeCell3D cell) {

	}

	private List<DataEntry> revert() {
		return map.entrySet().stream().map(e -> new DataEntry(e.getKey().type(), e.getKey().scale(),
				e.getValue().unwrap().stream().map(x -> new DataEntry.Entry(x.data(), x.weight().asInt())).toList()
		)).toList();
	}

	public record DataEntry(GateType type, int scale, List<Entry> list) {

		public static final Codec<DataEntry> CODEC = RecordCodecBuilder.create(i -> i.group(
				GateType.CODEC.fieldOf("type").forGetter(DataEntry::type),
				Codec.INT.fieldOf("scale").forGetter(DataEntry::scale),
				Entry.CODEC.listOf().fieldOf("rooms").forGetter(DataEntry::list)
		).apply(i, DataEntry::new));

		public static RoomLeafManager build(List<DataEntry> data) {
			ImmutableMap.Builder<LeafType, SimpleWeightedRandomList<CellContent>> builder = ImmutableMap.builder();
			for (var ent : data) {
				SimpleWeightedRandomList.Builder<CellContent> list = SimpleWeightedRandomList.builder();
				var key = new LeafType(ent.type(), ent.scale());
				for (var pair : ent.list) {
					list.add(pair.content, pair.weight);
				}
				builder.put(key, list.build());
			}
			return new RoomLeafManager(builder.build());
		}

		public record Entry(CellContent content, int weight) {

			public static final Codec<Entry> CODEC = RecordCodecBuilder.create(i -> i.group(
					CellContent.CODEC.forGetter(Entry::content),
					Codec.INT.fieldOf("weight").forGetter(Entry::weight)
			).apply(i, Entry::new));

		}

	}

	public static class Builder {

		private final Map<LeafType, List<Pair<CellContent, Integer>>> map = new TreeMap<>();

		public Builder addCell(CellContent cell, LeafType type, int weight) {
			map.computeIfAbsent(type, k -> new ArrayList<>()).add(Pair.of(cell, weight));
			return this;
		}

		public RoomLeafManager build() {
			ImmutableMap.Builder<LeafType, SimpleWeightedRandomList<CellContent>> builder = ImmutableMap.builder();
			for (var ent : map.entrySet()) {
				SimpleWeightedRandomList.Builder<CellContent> list = SimpleWeightedRandomList.builder();
				for (var pair : ent.getValue()) {
					list.add(pair.getFirst(), pair.getSecond());
				}
				builder.put(ent.getKey(), list.build());
			}
			return new RoomLeafManager(builder.build());
		}

	}


}
