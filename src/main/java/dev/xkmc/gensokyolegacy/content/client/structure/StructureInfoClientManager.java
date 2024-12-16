package dev.xkmc.gensokyolegacy.content.client.structure;

import dev.xkmc.gensokyolegacy.content.attachment.index.StructureKey;
import dev.xkmc.gensokyolegacy.content.client.debug.InfoUpdateClientManager;
import dev.xkmc.gensokyolegacy.init.data.GLLang;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class StructureInfoClientManager {

	static Level level;
	static StructureKey key;
	static StructureBoundUpdateToClient data;
	static StructureInfoUpdateToClient info;

	static long lastTime = 0;

	public static void tooltip(List<Component> lines, long gameTime, BlockPos pos) {
		if (level == null || data == null) return;
		if (!data.house().toBox().isInside(pos)) return;
		if (gameTime > lastTime + 20) {
			lastTime = gameTime;
			InfoUpdateClientManager.requestStructure(key);
		}
		if (info == null) {
			lines.add(GLLang.INFO_LOADING.get().withStyle(ChatFormatting.GRAY));
		} else {
			int total = info.remove() + info.primary() + info.secondary();
			lines.add(GLLang.INFO_STRUCTURE.get(total).withStyle(ChatFormatting.GRAY));
		}
	}

	public static void setStructure(StructureBoundUpdateToClient packet) {
		level = Minecraft.getInstance().level;
		if (level == null) return;
		key = packet.key();
		data = packet;
		if (!key.dim().equals(level.dimension().location())) {
			level = null;
		}
	}

	public static void setInfo(StructureInfoUpdateToClient packet) {
		if (level == null || key == null) return;
		if (!packet.key().equals(key)) return;
		info = packet;
	}

	@Nullable
	public static StructureBoundUpdateToClient fetch() {
		if (level == null) return null;
		if (level != Minecraft.getInstance().level) return null;
		return data;
	}

	public static boolean clearCache() {
		if (level == null) return false;
		level = null;
		return true;
	}

}
