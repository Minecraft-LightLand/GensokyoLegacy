package dev.xkmc.gensokyolegacy.init;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import dev.xkmc.gensokyolegacy.compat.touhoulittlemaid.TLMRenderHandler;
import dev.xkmc.gensokyolegacy.content.client.debug.DebugOverlay;
import dev.xkmc.gensokyolegacy.content.client.model.*;
import dev.xkmc.gensokyolegacy.content.entity.characters.fairy.CirnoModel;
import dev.xkmc.gensokyolegacy.content.entity.characters.maiden.ReimuModel;
import dev.xkmc.gensokyolegacy.content.entity.characters.rumia.BlackBallModel;
import dev.xkmc.gensokyolegacy.content.entity.characters.rumia.RumiaModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.FrogRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.common.NeoForge;

@EventBusSubscriber(value = Dist.CLIENT, modid = GensokyoLegacy.MODID)
public class GLClient {

	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent event) {
		if (ModList.get().isLoaded(TouhouLittleMaid.MOD_ID)) {
			NeoForge.EVENT_BUS.register(TLMRenderHandler.class);
		}
	}

	@SubscribeEvent
	public static void addGuiLayer(RegisterGuiLayersEvent event) {
		event.registerAbove(VanillaGuiLayers.CROSSHAIR, GensokyoLegacy.loc("debug"), new DebugOverlay());
	}

	@SubscribeEvent
	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(RumiaModel.LAYER_LOCATION, RumiaModel::createBodyLayer);
		event.registerLayerDefinition(RumiaModel.HAIRBAND, RumiaModel::createHairbandLayer);
		event.registerLayerDefinition(BlackBallModel.LAYER_LOCATION, BlackBallModel::createBodyLayer);
		event.registerLayerDefinition(ReimuModel.LAYER_LOCATION, ReimuModel::createBodyLayer);
		event.registerLayerDefinition(ReimuModel.HAIRBAND, ReimuModel::createHatLayer);
		event.registerLayerDefinition(CirnoModel.LAYER_LOCATION, CirnoModel::createBodyLayer);
		event.registerLayerDefinition(CirnoModel.HAT, CirnoModel::createHatLayer);
		event.registerLayerDefinition(CirnoModel.WINGS_LOCATION, CirnoModel::createWingsLayer);
		event.registerLayerDefinition(SuwakoHatModel.SUWAKO, SuwakoHatModel::createSuwakoHat);
		event.registerLayerDefinition(SuwakoHatModel.STRAW, SuwakoHatModel::createStrawHat);
		event.registerLayerDefinition(FrogStrawHatModel.STRAW, FrogStrawHatModel::createHat);
		event.registerLayerDefinition(KoishiHatModel.HAT, KoishiHatModel::createHat);
	}

	@SubscribeEvent
	public static void addLayer(EntityRenderersEvent.AddLayers event) {
		if (ModList.get().isLoaded(TouhouLittleMaid.MOD_ID)) {
			TLMRenderHandler.addLayers(event);
		}
		if (event.getRenderer(EntityType.FROG) instanceof FrogRenderer r) {
			r.addLayer(new FrogHatLayer<>(r, event.getEntityModels()));
		}
	}

	@SubscribeEvent
	public static void registerReloadListener(RegisterClientReloadListenersEvent event) {
		event.registerReloadListener((ResourceManagerReloadListener) m -> registerWingsLayer());
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	public static void registerWingsLayer() {
		EntityRenderDispatcher renderManager = Minecraft.getInstance().getEntityRenderDispatcher();
		var skinMap = renderManager.getSkinMap();
		for (EntityRenderer<? extends Player> renderer : skinMap.values()) {
			if (renderer instanceof LivingEntityRenderer ler) {
				addLayer(renderManager, ler);
			}
		}
		renderManager.renderers.forEach((e, r) -> {
			if (r instanceof LivingEntityRenderer ler && ler.getModel() instanceof HumanoidModel<?>) {
				addLayer(renderManager, ler);
			}
		});
	}

	private static <T extends LivingEntity, M extends HumanoidModel<T>> void addLayer(EntityRenderDispatcher manager, LivingEntityRenderer<T, M> ler) {
		var mc = Minecraft.getInstance();
		ler.addLayer(new CirnoWingsLayer<>(ler, mc.getEntityModels()));
	}

}
