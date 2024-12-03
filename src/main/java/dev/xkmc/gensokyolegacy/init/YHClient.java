package dev.xkmc.gensokyolegacy.init;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import dev.xkmc.gensokyolegacy.compat.touhoulittlemaid.TLMRenderHandler;
import dev.xkmc.gensokyolegacy.content.entity.characters.reimu.ReimuModel;
import dev.xkmc.gensokyolegacy.content.entity.characters.rumia.BlackBallModel;
import dev.xkmc.gensokyolegacy.content.entity.characters.rumia.RumiaModel;
import dev.xkmc.youkaishomecoming.content.entity.lampery.LampreyModel;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.common.NeoForge;

@EventBusSubscriber(value = Dist.CLIENT, modid = GensokyoLegacy.MODID, bus = EventBusSubscriber.Bus.MOD)
public class YHClient {

	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent event) {
		if (ModList.get().isLoaded(TouhouLittleMaid.MOD_ID)) {
			NeoForge.EVENT_BUS.register(TLMRenderHandler.class);
		}
	}

	@SubscribeEvent
	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(LampreyModel.LAYER_LOCATION, LampreyModel::createBodyLayer);
		event.registerLayerDefinition(RumiaModel.LAYER_LOCATION, RumiaModel::createBodyLayer);
		event.registerLayerDefinition(RumiaModel.HAIRBAND, RumiaModel::createHairbandLayer);
		event.registerLayerDefinition(BlackBallModel.LAYER_LOCATION, BlackBallModel::createBodyLayer);
		event.registerLayerDefinition(ReimuModel.LAYER_LOCATION, ReimuModel::createBodyLayer);
		event.registerLayerDefinition(ReimuModel.HAT_LOCATION, ReimuModel::createHatLayer);
	}


	@SubscribeEvent
	public static void addLayer(EntityRenderersEvent.AddLayers event) {
		if (ModList.get().isLoaded(TouhouLittleMaid.MOD_ID)) {
			TLMRenderHandler.addLayers(event);
		}
	}

}
