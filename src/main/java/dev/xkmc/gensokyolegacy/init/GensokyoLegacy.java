package dev.xkmc.gensokyolegacy.init;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.tterrag.registrate.providers.ProviderType;
import dev.xkmc.alchemy.init.AlchemyRegistries;
import dev.xkmc.gap.init.registrate.GapRegistries;
import dev.xkmc.gensokyolegacy.compat.touhoulittlemaid.TLMCompat;
import dev.xkmc.gensokyolegacy.compat.touhoulittlemaid.TouhouSpellCards;
import dev.xkmc.gensokyolegacy.content.attachment.character.CharDataToClient;
import dev.xkmc.gensokyolegacy.content.attachment.misc.FrogSyncPacket;
import dev.xkmc.gensokyolegacy.content.attachment.misc.KoishiStartPacket;
import dev.xkmc.gensokyolegacy.content.client.debug.BlockInfoToClient;
import dev.xkmc.gensokyolegacy.content.client.debug.BlockRequestToServer;
import dev.xkmc.gensokyolegacy.content.client.debug.CharacterInfoToClient;
import dev.xkmc.gensokyolegacy.content.client.debug.CharacterRequestToServer;
import dev.xkmc.gensokyolegacy.content.client.structure.*;
import dev.xkmc.gensokyolegacy.content.entity.behavior.move.PathDataToClient;
import dev.xkmc.gensokyolegacy.content.entity.foundation.CombatToClient;
import dev.xkmc.gensokyolegacy.content.item.character.TouhouMat;
import dev.xkmc.gensokyolegacy.content.item.tool.CatBell;
import dev.xkmc.gensokyolegacy.content.item.tool.Dowser;
import dev.xkmc.gensokyolegacy.event.GLAttackListener;
import dev.xkmc.gensokyolegacy.event.GLClickHandler;
import dev.xkmc.gensokyolegacy.init.data.*;
import dev.xkmc.gensokyolegacy.init.data.loot.GLGLMProvider;
import dev.xkmc.gensokyolegacy.init.data.structure.GLStructureGen;
import dev.xkmc.gensokyolegacy.init.data.structure.GLStructureLootGen;
import dev.xkmc.gensokyolegacy.init.data.structure.GLStructureTagGen;
import dev.xkmc.gensokyolegacy.init.data.structure.ReportBlocksInStructure;
import dev.xkmc.gensokyolegacy.init.registrate.*;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.l2core.init.reg.simple.Reg;
import dev.xkmc.l2damagetracker.contents.attack.AttackEventHandler;
import dev.xkmc.l2serial.network.PacketHandler;
import dev.xkmc.l2serial.serialization.custom_handler.CodecHandler;
import dev.xkmc.l2serial.serialization.custom_handler.Handlers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(GensokyoLegacy.MODID)
@EventBusSubscriber(modid = GensokyoLegacy.MODID)
public class GensokyoLegacy {

	public static final Logger LOGGER = LogManager.getLogger();

	public static final String MODID = "gensokyolegacy";
	public static final Reg REG = new Reg(MODID);
	public static final L2Registrate REGISTRATE = new L2Registrate(MODID);
	public static final PacketHandler HANDLER = new PacketHandler(MODID, 1,
			e -> e.create(CharDataToClient.class, PacketHandler.NetDir.PLAY_TO_CLIENT),
			e -> e.create(PathDataToClient.class, PacketHandler.NetDir.PLAY_TO_CLIENT),
			e -> e.create(BlockRequestToServer.class, PacketHandler.NetDir.PLAY_TO_SERVER),
			e -> e.create(BlockInfoToClient.class, PacketHandler.NetDir.PLAY_TO_CLIENT),
			e -> e.create(CharacterRequestToServer.class, PacketHandler.NetDir.PLAY_TO_SERVER),
			e -> e.create(CharacterInfoToClient.class, PacketHandler.NetDir.PLAY_TO_CLIENT),
			e -> e.create(StructureBoundUpdateToClient.class, PacketHandler.NetDir.PLAY_TO_CLIENT),
			e -> e.create(CustomStructureBoundUpdateToClient.class, PacketHandler.NetDir.PLAY_TO_CLIENT),
			e -> e.create(StructureInfoRequestToServer.class, PacketHandler.NetDir.PLAY_TO_SERVER),
			e -> e.create(StructureInfoUpdateToClient.class, PacketHandler.NetDir.PLAY_TO_CLIENT),
			e -> e.create(StructureRepairToServer.class, PacketHandler.NetDir.PLAY_TO_SERVER),
			e -> e.create(StructureEditToServer.class, PacketHandler.NetDir.PLAY_TO_SERVER),
			e -> e.create(FrogSyncPacket.class, PacketHandler.NetDir.PLAY_TO_CLIENT),
			e -> e.create(KoishiStartPacket.class, PacketHandler.NetDir.PLAY_TO_CLIENT),
			e -> e.create(CombatToClient.class, PacketHandler.NetDir.PLAY_TO_CLIENT),

			e -> e.create(Dowser.DowserToClient.class, PacketHandler.NetDir.PLAY_TO_CLIENT),
			e -> e.create(CatBell.MountToClient.class, PacketHandler.NetDir.PLAY_TO_CLIENT)
	);

	public GensokyoLegacy() {
		Handlers.enableVanilla(Fluid.class, BuiltInRegistries.FLUID);
		new CodecHandler<>(FluidIngredient.class, FluidIngredient.CODEC, FluidIngredient.STREAM_CODEC);

		GLDecoBlocks.register();
		GLItems.register();
		GLEntities.register();

		GLRecipes.register();
		TouhouMat.register();
		GLMeta.register();
		GLMisc.register();
		GLWorldGen.register();
		GLBrains.register();
		GLEffects.register();
		GLSounds.register();
		GLCriteriaTriggers.register();
		GLModConfig.init();
		TouhouSpellCards.registerSpells();

		GapRegistries.register();
		AlchemyRegistries.register();

		new GLClickHandler(loc("main"));
		AttackEventHandler.register(1765, new GLAttackListener());
		if (ModList.get().isLoaded(TouhouLittleMaid.MOD_ID)) {
			NeoForge.EVENT_BUS.register(TLMCompat.class);
		}
	}

	@SubscribeEvent
	public static void registerCapabilities(RegisterCapabilitiesEvent event) {
	}

	@SubscribeEvent
	public static void commonSetup(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			DispenserBlock.registerProjectileBehavior(GLItems.FROZEN_FROG_COLD.get());
			DispenserBlock.registerProjectileBehavior(GLItems.FROZEN_FROG_WARM.get());
			DispenserBlock.registerProjectileBehavior(GLItems.FROZEN_FROG_TEMPERATE.get());
			DispenserBlock.registerProjectileBehavior(GLItems.FAIRY_ICE_CRYSTAL.get());
		});
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void gatherData(GatherDataEvent event) {
		REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, GLTagGen::onBlockTagGen);
		REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, GLTagGen::onItemTagGen);
		REGISTRATE.addDataGenerator(ProviderType.ENTITY_TAGS, GLTagGen::onEntityTagGen);
		REGISTRATE.addDataGenerator(GLStructureTagGen.BIOME_TAG, GLStructureTagGen::genBiomeTag);
		REGISTRATE.addDataGenerator(ProviderType.DATA_MAP, GLDataMapGen::dataMapGen);
		REGISTRATE.addDataGenerator(ProviderType.LANG, GLLang::genLang);
		REGISTRATE.addDataGenerator(ProviderType.RECIPE, GLRecipeGen::genRecipe);
		REGISTRATE.addDataGenerator(ProviderType.LOOT, GLStructureLootGen::genLoot);
		REGISTRATE.addDataGenerator(ProviderType.ADVANCEMENT, GLAdvGen::genAdv);
		var init = REGISTRATE.getDataGenInitializer();
		GLStructureGen.init(init);
		new GLDamageTypes(REGISTRATE).generate();

		var gen = event.getGenerator();
		gen.addProvider(event.includeServer(), new GLGLMProvider(gen.getPackOutput(), event.getLookupProvider()));

		ReportBlocksInStructure.report();
	}

	public static ResourceLocation loc(String id) {
		return ResourceLocation.fromNamespaceAndPath(MODID, id);
	}
}
