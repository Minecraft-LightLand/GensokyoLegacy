package dev.xkmc.gensokyolegacy.content.client.structure;

import dev.xkmc.gensokyolegacy.content.attachment.home.structure.FixStage;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import net.minecraft.client.Minecraft;

public class StructureRepairManager {

	public static void openScreen() {
		var data = StructureInfoClientManager.info;
		if (data == null) return;
		if (data.key().isCustom()) {
			Minecraft.getInstance().setScreen(new StructureCustomizeScreen());
		} else {
			Minecraft.getInstance().setScreen(new StructureFixScreen());
		}
	}

	public static void onScan() {
		var data = StructureInfoClientManager.info;
		if (data == null || !data.key().isCustom()) return;
		GensokyoLegacy.HANDLER.toServer(new StructureEditToServer(data.key(), data.key().pos(), StructureEditToServer.Edit.SCAN));
	}

	public static void onDelete() {
		var data = StructureInfoClientManager.info;
		if (data == null || !data.key().isCustom()) return;
		StructureInfoClientManager.clearStructure();
		GensokyoLegacy.HANDLER.toServer(new StructureEditToServer(data.key(), data.key().pos(), StructureEditToServer.Edit.DELETE));
	}

	public static void onGenerateAir() {
		var data = StructureInfoClientManager.info;
		if (data == null || data.remove() == 0) return;
		GensokyoLegacy.HANDLER.toServer(new StructureRepairToServer(data.key(), FixStage.PATH));
	}

	public static void onGeneratePrimary() {
		var data = StructureInfoClientManager.info;
		if (data == null || data.remove() == 0 && data.primary() == 0) return;
		GensokyoLegacy.HANDLER.toServer(new StructureRepairToServer(data.key(), FixStage.PRIMARY));
	}

	public static void onGenerateSecondary() {
		var data = StructureInfoClientManager.info;
		if (data == null || data.remove() == 0 && data.primary() == 0 && data.secondary() == 0) return;
		GensokyoLegacy.HANDLER.toServer(new StructureRepairToServer(data.key(), FixStage.SECONDARY));
	}

	public static void onGenerateAll() {
		var data = StructureInfoClientManager.info;
		if (data == null || data.remove() == 0 && data.primary() == 0 && data.secondary() == 0) return;
		GensokyoLegacy.HANDLER.toServer(new StructureRepairToServer(data.key(), FixStage.ALL));
	}

}
