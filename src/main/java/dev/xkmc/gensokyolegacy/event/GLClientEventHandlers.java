package dev.xkmc.gensokyolegacy.event;

import dev.xkmc.gensokyolegacy.content.client.structure.StructureOutlineRenderer;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

@EventBusSubscriber(value = Dist.CLIENT, modid = GensokyoLegacy.MODID, bus = EventBusSubscriber.Bus.GAME)
public class GLClientEventHandlers {

	@SubscribeEvent
	public static void renderStageEvent(RenderLevelStageEvent event) {
		if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_BLOCK_ENTITIES) {
			StructureOutlineRenderer.renderOutline(event.getPoseStack(), event.getCamera().getPosition());
		}
	}

}