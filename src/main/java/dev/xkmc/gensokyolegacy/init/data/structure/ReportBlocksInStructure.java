package dev.xkmc.gensokyolegacy.init.data.structure;

import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Comparator;

public class ReportBlocksInStructure {

	private static final Logger LOGGER = LogManager.getLogger();

	public static void report() {
		try {
			//report("cirno_nest.nbt");
			//report("hakurei_shrine/shrine.nbt");
		} catch (Exception e) {
			LOGGER.throwing(e);
		}
	}

	public static void report(String path) throws IOException {
		File file = new File("../../src/main/resources/data/gensokyolegacy/structure/" + path);
		var tag = NbtIo.readCompressed(new FileInputStream(file), NbtAccounter.unlimitedHeap());
		Object2IntLinkedOpenHashMap<ResourceLocation> count = new Object2IntLinkedOpenHashMap<>();
		ListTag palette = tag.getList("palette", 10);
		ResourceLocation[] ids = new ResourceLocation[palette.size()];
		for (int i = 0; i < palette.size(); i++) {
			ids[i] = ResourceLocation.parse(palette.getCompound(i).getString("Name"));
		}
		ListTag blocks = tag.getList("blocks", 10);
		for (int i = 0; i < blocks.size(); i++) {
			int state = blocks.getCompound(i).getInt("state");
			count.computeInt(ids[state], (k, c) -> (c == null ? 0 : c) + 1);
		}
		System.out.println("--- Report for Structure <" + path + "> ---");
		count.object2IntEntrySet().stream().sorted(Comparator.comparingInt(e -> -e.getIntValue()))
				.forEach(e -> System.out.println(e.getKey() + " - " + e.getIntValue()));
		System.out.println("---------");
	}

}
