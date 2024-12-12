package dev.xkmc.gensokyolegacy.content.client.debug;

import dev.xkmc.gensokyolegacy.content.block.bed.YoukaiBedBlockEntity;
import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import dev.xkmc.gensokyolegacy.init.registrate.GLItems;
import dev.xkmc.l2itemselector.overlay.OverlayUtil;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

import java.util.ArrayList;
import java.util.List;

public class DebugOverlay implements LayeredDraw.Layer {

	@Override
	public void render(GuiGraphics g, DeltaTracker delta) {
		var player = Minecraft.getInstance().player;
		if (player == null) return;
		var level = player.level();
		var hit = Minecraft.getInstance().hitResult;
		if (!player.getItemBySlot(EquipmentSlot.HEAD).is(GLItems.DEBUG_GLASSES)) return;
		List<Component> lines = new ArrayList<>();
		if (hit instanceof BlockHitResult block) {
			if (level.getBlockEntity(block.getBlockPos()) instanceof YoukaiBedBlockEntity bed) {
				if (bed.getBlockState().getValue(BedBlock.PART) == BedPart.HEAD)
					BedInfoClientManager.tooltip(lines, player.level().getGameTime(), bed);
			}
		} else if (hit instanceof EntityHitResult ehit) {
			if (ehit.getEntity() instanceof YoukaiEntity youkai) {
				CharacterInfoClientManager.tooltip(lines, player.level().getGameTime(), youkai);
			}
		}
		if (lines.isEmpty()) return;
		int sw = g.guiWidth();
		int sh = g.guiHeight();
		new OverlayUtil(g, (int) (sw * 0.6), (int) (sh * 0.5), (int) (sw * 0.3))
				.renderLongText(Minecraft.getInstance().font, lines);
	}

}
