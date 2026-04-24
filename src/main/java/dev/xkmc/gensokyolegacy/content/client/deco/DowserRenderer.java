package dev.xkmc.gensokyolegacy.content.client.deco;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.gensokyolegacy.content.client.structure.StructureOutlineRenderer;
import dev.xkmc.gensokyolegacy.content.item.tool.Dowser;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Set;

public class DowserRenderer {

	private static Level level;
	private static Set<BlockPos> pos;

	public static void init(Player player, Set<BlockPos> set) {
		if (level != player.level()) {
			pos = null;
		}
		level = player.level();
		if (pos == null) pos = set;
		else pos.addAll(set);
	}

	public static void tickClient() {
		var pl = Minecraft.getInstance().player;
		if (pl == null) return;
		var cl = Minecraft.getInstance().level;
		if (cl != level) {
			level = null;
			pos = null;
			return;
		}
		if (level == null || pos == null) return;
		pos.removeIf(e -> {
			if (!level.isLoaded(e)) return true;
			return !Dowser.isValid(level.getBlockEntity(e));
		});
	}

	public static void renderOutline(PoseStack pose, Vec3 camera) {
		var cl = Minecraft.getInstance().level;
		if (cl != level) {
			level = null;
			pos = null;
			return;
		}
		if (level == null || pos == null) return;
		StructureOutlineRenderer.renderPosSet(pose, camera, pos);
	}

}
