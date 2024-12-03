package dev.xkmc.gensokyolegacy.init;

import com.tterrag.registrate.util.entry.EntityEntry;
import dev.xkmc.danmakuapi.init.DanmakuAPI;
import dev.xkmc.gensokyolegacy.content.entity.characters.reimu.ReimuEntity;
import dev.xkmc.gensokyolegacy.content.entity.characters.reimu.ReimuRenderer;
import dev.xkmc.gensokyolegacy.content.entity.characters.rumia.RumiaEntity;
import dev.xkmc.gensokyolegacy.content.entity.characters.rumia.RumiaRenderer;
import dev.xkmc.gensokyolegacy.content.entity.youkai.BossYoukaiEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

public class GLEntities {

	public static final EntityEntry<RumiaEntity> RUMIA;
	public static final EntityEntry<ReimuEntity> REIMU;

	static {

		GensokyoLegacy.REGISTRATE.defaultCreativeTab(DanmakuAPI.TAB.key());

		RUMIA = GensokyoLegacy.REGISTRATE
				.entity("rumia", RumiaEntity::new, MobCategory.MONSTER)
				.properties(e -> e.sized(0.4F, 1.7f).clientTrackingRange(10))
				.spawnPlacement(SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
						RumiaEntity::checkRumiaSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE)
				.attributes(RumiaEntity::createAttributes)
				.renderer(() -> RumiaRenderer::new)
				.spawnEgg(0x000000, 0x000000).build()
				//.loot(EntityLootGen::rumia)
				.register();

		REIMU = GensokyoLegacy.REGISTRATE
				.entity("shrine_maiden", ReimuEntity::new, MobCategory.MONSTER)
				.properties(e -> e.sized(0.4F, 1.8f).clientTrackingRange(10))
				.attributes(BossYoukaiEntity::createAttributes)
				.renderer(() -> ReimuRenderer::new)
				.spawnEgg(0xa93937, 0xfaf5f2).build()
				//.loot(EntityLootGen::reimu)
				.register();

	}

	public static void register() {

	}

}
