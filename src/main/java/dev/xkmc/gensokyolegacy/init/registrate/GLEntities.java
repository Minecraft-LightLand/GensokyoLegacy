package dev.xkmc.gensokyolegacy.init.registrate;

import com.tterrag.registrate.util.entry.EntityEntry;
import dev.xkmc.gensokyolegacy.content.entity.characters.boss.KoishiEntity;
import dev.xkmc.gensokyolegacy.content.entity.characters.boss.YukariEntity;
import dev.xkmc.gensokyolegacy.content.entity.characters.fairy.CirnoEntity;
import dev.xkmc.gensokyolegacy.content.entity.characters.fairy.CirnoRenderer;
import dev.xkmc.gensokyolegacy.content.entity.characters.maiden.ReimuEntity;
import dev.xkmc.gensokyolegacy.content.entity.characters.maiden.ReimuRenderer;
import dev.xkmc.gensokyolegacy.content.entity.characters.maiden.SanaeEntity;
import dev.xkmc.gensokyolegacy.content.entity.characters.rumia.RumiaEntity;
import dev.xkmc.gensokyolegacy.content.entity.characters.rumia.RumiaRenderer;
import dev.xkmc.gensokyolegacy.content.entity.misc.FairyIce;
import dev.xkmc.gensokyolegacy.content.entity.misc.FrozenFrog;
import dev.xkmc.gensokyolegacy.content.entity.youkai.BossYoukaiEntity;
import dev.xkmc.gensokyolegacy.content.entity.youkai.GeneralYoukaiRenderer;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.gensokyolegacy.init.data.loot.EntityLootGen;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.entity.MobCategory;

public class GLEntities {

	public static final EntityEntry<RumiaEntity> RUMIA;
	public static final EntityEntry<ReimuEntity> REIMU;
	public static final EntityEntry<CirnoEntity> CIRNO;
	public static final EntityEntry<YukariEntity> YUKARI;
	public static final EntityEntry<SanaeEntity> SANAE;
	public static final EntityEntry<KoishiEntity> KOISHI;

	public static final EntityEntry<FrozenFrog> FROZEN_FROG;
	public static final EntityEntry<FairyIce> FAIRY_ICE;

	static {

		RUMIA = GensokyoLegacy.REGISTRATE
				.entity("rumia", RumiaEntity::new, MobCategory.MONSTER)
				.properties(e -> e.sized(0.4F, 1.7f).clientTrackingRange(10))
				.attributes(RumiaEntity::createAttributes)
				.renderer(() -> RumiaRenderer::new)
				.spawnEgg(0x413734, 0xA55064).build()
				.loot(EntityLootGen::noLoot)
				.register();

		REIMU = GensokyoLegacy.REGISTRATE
				.entity("reimu", ReimuEntity::new, MobCategory.MONSTER)
				.properties(e -> e.sized(0.4F, 1.8f).clientTrackingRange(10))
				.attributes(BossYoukaiEntity::createAttributes)
				.renderer(() -> ReimuRenderer::new)
				.spawnEgg(0xa93937, 0xfaf5f2).build()
				.loot(EntityLootGen::noLoot)
				.register();


		CIRNO = GensokyoLegacy.REGISTRATE
				.entity("cirno", CirnoEntity::new, MobCategory.MONSTER)
				.properties(e -> e.sized(0.4F, 1.8f).clientTrackingRange(10))
				.attributes(CirnoEntity::createAttributes)
				.renderer(() -> CirnoRenderer::new)
				.spawnEgg(0x5676af, 0xb6ecf1).build()
				.loot(EntityLootGen::noLoot)
				.register();


		YUKARI = GensokyoLegacy.REGISTRATE
				.entity("yukari", YukariEntity::new, MobCategory.MONSTER)
				.properties(e -> e.sized(0.4F, 1.8f).clientTrackingRange(10))
				.attributes(BossYoukaiEntity::createAttributes)
				.renderer(() -> GeneralYoukaiRenderer::new)
				.spawnEgg(0x4B1442, 0xFFFFFF).build()
				.loot(EntityLootGen::noLoot)
				.register();

		SANAE = GensokyoLegacy.REGISTRATE
				.entity("kochiya_sanae", SanaeEntity::new, MobCategory.MONSTER)
				.properties(e -> e.sized(0.4F, 1.8f).clientTrackingRange(10))
				.attributes(BossYoukaiEntity::createAttributes)
				.renderer(() -> GeneralYoukaiRenderer::new)
				.spawnEgg(0x4eaff9, 0xFFFFFF).build()
				.loot(EntityLootGen::noLoot)
				.register();

		KOISHI = GensokyoLegacy.REGISTRATE
				.entity("komeiji_koishi", KoishiEntity::new, MobCategory.MONSTER)
				.properties(e -> e.sized(0.4F, 1.8f).clientTrackingRange(10))
				.attributes(BossYoukaiEntity::createAttributes)
				.renderer(() -> GeneralYoukaiRenderer::new)
				.spawnEgg(0x88BA7F, 0x645856).build()
				.loot(EntityLootGen::noLoot)
				.register();

		FROZEN_FROG = GensokyoLegacy.REGISTRATE
				.<FrozenFrog>entity("frozen_frog", FrozenFrog::new, MobCategory.MISC)
				.properties(p -> p.sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10))
				.renderer(() -> ThrownItemRenderer::new)
				.register();

		FAIRY_ICE = GensokyoLegacy.REGISTRATE
				.<FairyIce>entity("fairy_ice_crystal", FairyIce::new, MobCategory.MISC)
				.properties(p -> p.sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10))
				.renderer(() -> ThrownItemRenderer::new)
				.register();

	}

	public static void register() {

	}

}
