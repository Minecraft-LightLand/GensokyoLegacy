package dev.xkmc.gensokyolegacy.init.registrate;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.danmakuapi.content.item.SpellItem;
import dev.xkmc.danmakuapi.init.DanmakuAPI;
import dev.xkmc.danmakuapi.init.registrate.DanmakuItems;
import dev.xkmc.gensokyolegacy.content.spell.item.MarisaItemSpell;
import dev.xkmc.gensokyolegacy.content.spell.item.ReimuItemSpell;
import dev.xkmc.gensokyolegacy.content.item.DebugGlasses;
import dev.xkmc.gensokyolegacy.content.item.DebugWand;
import dev.xkmc.gensokyolegacy.content.item.FairyIceItem;
import dev.xkmc.gensokyolegacy.content.item.FrozenFrogItem;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.l2core.init.reg.registrate.SimpleEntry;
import net.minecraft.world.entity.animal.FrogVariant;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;

public class GLItems {

	public static final SimpleEntry<CreativeModeTab> TAB;

	public static final ItemEntry<FairyIceItem> FAIRY_ICE_CRYSTAL;
	public static final ItemEntry<FrozenFrogItem> FROZEN_FROG_COLD, FROZEN_FROG_WARM, FROZEN_FROG_TEMPERATE;
	public static final ItemEntry<SpellItem> REIMU_SPELL, MARISA_SPELL;
	public static final ItemEntry<DebugGlasses> DEBUG_GLASSES;
	public static final ItemEntry<DebugWand> DEBUG_WAND;

	static {
		TAB = GensokyoLegacy.REGISTRATE.buildModCreativeTab("ingredients", "Gensokyo Legacy - Ingredients",
				e -> e.icon(GLItems.FAIRY_ICE_CRYSTAL::asStack));

		FAIRY_ICE_CRYSTAL = GensokyoLegacy.REGISTRATE.item("fairy_ice_crystal",
				FairyIceItem::new).register();
		FROZEN_FROG_COLD = GensokyoLegacy.REGISTRATE.item("frozen_frog_cold",
				p -> new FrozenFrogItem(p.stacksTo(16), FrogVariant.COLD)).register();
		FROZEN_FROG_WARM = GensokyoLegacy.REGISTRATE.item("frozen_frog_warm",
				p -> new FrozenFrogItem(p.stacksTo(16), FrogVariant.WARM)).register();
		FROZEN_FROG_TEMPERATE = GensokyoLegacy.REGISTRATE.item("frozen_frog_temperate",
				p -> new FrozenFrogItem(p.stacksTo(16), FrogVariant.TEMPERATE)).register();

		REIMU_SPELL = GensokyoLegacy.REGISTRATE
				.item("spell_reimu", p -> new SpellItem(
						p.stacksTo(1), ReimuItemSpell::new, true,
						() -> DanmakuItems.Bullet.CIRCLE.get(DyeColor.RED).get()))
				.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/spell/" + ctx.getName())))
				.lang("Reimu's Spellcard \"Innate Dream\"")
				.register();

		MARISA_SPELL = GensokyoLegacy.REGISTRATE
				.item("spell_marisa", p -> new SpellItem(
						p.stacksTo(1), MarisaItemSpell::new, false,
						() -> DanmakuItems.Laser.LASER.get(DyeColor.WHITE).get()))
				.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/spell/" + ctx.getName())))
				.lang("Marisa's Spellcard \"Master Spark\"")
				.register();

		DEBUG_GLASSES = GensokyoLegacy.REGISTRATE.item("debug_glasses", p -> new DebugGlasses(p.stacksTo(1)))
				.defaultModel().defaultLang().register();

		DEBUG_WAND = GensokyoLegacy.REGISTRATE.item("debug_wand", p -> new DebugWand(p.stacksTo(1)))
				.defaultModel().defaultLang().register();
	}

	public static void register() {

	}

}
