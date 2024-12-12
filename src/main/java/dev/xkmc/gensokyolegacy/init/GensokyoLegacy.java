package dev.xkmc.gensokyolegacy.init;

import com.tterrag.registrate.providers.ProviderType;
import dev.xkmc.danmakuapi.init.DanmakuAPI;
import dev.xkmc.gensokyolegacy.compat.touhoulittlemaid.TouhouSpellCards;
import dev.xkmc.gensokyolegacy.content.client.debug.BedInfoToClient;
import dev.xkmc.gensokyolegacy.content.client.debug.BedRequestToServer;
import dev.xkmc.gensokyolegacy.content.client.debug.CharacterInfoToClient;
import dev.xkmc.gensokyolegacy.content.client.debug.CharacterRequestToServer;
import dev.xkmc.gensokyolegacy.init.data.GLLang;
import dev.xkmc.gensokyolegacy.init.data.GLStructureGen;
import dev.xkmc.gensokyolegacy.init.data.GLTagGen;
import dev.xkmc.gensokyolegacy.init.network.CharDataToClient;
import dev.xkmc.gensokyolegacy.init.network.PathDataToClient;
import dev.xkmc.gensokyolegacy.init.registrate.GLBlocks;
import dev.xkmc.gensokyolegacy.init.registrate.GLEntities;
import dev.xkmc.gensokyolegacy.init.registrate.GLItems;
import dev.xkmc.gensokyolegacy.init.registrate.GLMisc;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.l2core.init.reg.simple.Reg;
import dev.xkmc.l2serial.network.PacketHandler;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@Mod(GensokyoLegacy.MODID)
@EventBusSubscriber(modid = GensokyoLegacy.MODID, bus = EventBusSubscriber.Bus.MOD)
public class GensokyoLegacy {

	public static final String MODID = "gensokyolegacy";
	public static final Reg REG = new Reg(MODID);
	public static final L2Registrate REGISTRATE = new L2Registrate(MODID);
	public static final PacketHandler HANDLER = new PacketHandler(MODID, 1,
			e -> e.create(CharDataToClient.class, PacketHandler.NetDir.PLAY_TO_CLIENT),
			e -> e.create(PathDataToClient.class, PacketHandler.NetDir.PLAY_TO_CLIENT),
			e -> e.create(BedRequestToServer.class, PacketHandler.NetDir.PLAY_TO_SERVER),
			e -> e.create(BedInfoToClient.class, PacketHandler.NetDir.PLAY_TO_CLIENT),
			e -> e.create(CharacterRequestToServer.class, PacketHandler.NetDir.PLAY_TO_SERVER),
			e -> e.create(CharacterInfoToClient.class, PacketHandler.NetDir.PLAY_TO_CLIENT)
	);

	public GensokyoLegacy() {
		REGISTRATE.defaultCreativeTab(DanmakuAPI.TAB.key());
		GLItems.register();
		GLMisc.register();
		GLBlocks.register();
		GLEntities.register();
		TouhouSpellCards.registerSpells();
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void gatherData(GatherDataEvent event) {
		REGISTRATE.addDataGenerator(GLTagGen.BIOME_TAG, GLTagGen::genBiomeTag);
		REGISTRATE.addDataGenerator(ProviderType.DATA_MAP, GLStructureGen::dataMap);
		REGISTRATE.addDataGenerator(ProviderType.LANG, GLLang::genLang);
		var init = REGISTRATE.getDataGenInitializer();
		GLStructureGen.init(init);
	}

	public static ResourceLocation loc(String id) {
		return ResourceLocation.fromNamespaceAndPath(MODID, id);
	}
}
