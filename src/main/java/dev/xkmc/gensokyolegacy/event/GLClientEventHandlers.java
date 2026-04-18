package dev.xkmc.gensokyolegacy.event;

import dev.xkmc.gensokyolegacy.content.client.deco.FurnaceItemDeco;
import dev.xkmc.gensokyolegacy.content.client.structure.StructureOutlineRenderer;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

@EventBusSubscriber(value = Dist.CLIENT, modid = GensokyoLegacy.MODID)
public class GLClientEventHandlers {

	@SubscribeEvent
	public static void renderStageEvent(RenderLevelStageEvent event) {
		if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_BLOCK_ENTITIES) {
			StructureOutlineRenderer.renderOutline(event.getPoseStack(), event.getCamera().getPosition());
		}
	}

	public static void onSlotRender(GuiGraphics g, ItemStack stack, Slot slot) {
		if (slot.container instanceof Inventory inv) {
			int index = slot.getSlotIndex();
			FurnaceItemDeco.renderSlot(g, stack, inv, index, slot.x, slot.y);
		}
	}

}