package dev.xkmc.gensokyolegacy.init.registrate;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.danmakuapi.content.item.SpellItem;
import dev.xkmc.danmakuapi.init.data.DanmakuTagGen;
import dev.xkmc.danmakuapi.init.registrate.DanmakuItems;
import dev.xkmc.gensokyolegacy.content.block.mistletoe.MistletoeBranch;
import dev.xkmc.gensokyolegacy.content.client.model.*;
import dev.xkmc.gensokyolegacy.content.entity.characters.fairy.CirnoModel;
import dev.xkmc.gensokyolegacy.content.entity.characters.maiden.ReimuModel;
import dev.xkmc.gensokyolegacy.content.entity.characters.rumia.RumiaModel;
import dev.xkmc.gensokyolegacy.content.item.character.*;
import dev.xkmc.gensokyolegacy.content.item.debug.DebugGlasses;
import dev.xkmc.gensokyolegacy.content.item.debug.DebugWand;
import dev.xkmc.gensokyolegacy.content.item.ingredient.BloodBottleItem;
import dev.xkmc.gensokyolegacy.content.item.ingredient.FairyIceItem;
import dev.xkmc.gensokyolegacy.content.item.ingredient.FrozenFrogItem;
import dev.xkmc.gensokyolegacy.content.spell.item.*;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.gensokyolegacy.init.data.GLTagGen;
import dev.xkmc.l2core.init.reg.registrate.SimpleEntry;
import dev.xkmc.l2core.init.reg.simple.DCReg;
import dev.xkmc.l2core.init.reg.simple.DCVal;
import dev.xkmc.youkaishomecoming.content.item.fluid.BottledFluid;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.animal.FrogVariant;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;

public class GLItems {

	public static final SimpleEntry<CreativeModeTab> TAB;

	public static final ItemEntry<FairyIceItem> FAIRY_ICE_CRYSTAL;
	public static final ItemEntry<FrozenFrogItem> FROZEN_FROG_COLD, FROZEN_FROG_WARM, FROZEN_FROG_TEMPERATE;
	public static final ItemEntry<MistletoeBranch> MISTLETOE_BRANCH;
	public static final BottledFluid<BloodBottleItem> BLOOD_BOTTLE;

	public static final ItemEntry<SpellItem> REIMU_SPELL, MARISA_SPELL, SANAE_SPELL, YUKARI_SPELL_BUTTERFLY, YUKARI_SPELL_LASER, MYSTIA_SPELL;

	public static final ItemEntry<StrawHatItem> STRAW_HAT;
	public static final ItemEntry<SuwakoHatItem> SUWAKO_HAT;
	public static final ItemEntry<KoishiHatItem> KOISHI_HAT;
	public static final ItemEntry<RumiaHairbandItem> RUMIA_HAIRBAND;
	public static final ItemEntry<ReimuHairbandItem> REIMU_HAIRBAND;
	public static final ItemEntry<CirnoHairbandItem> CIRNO_HAIRBAND;
	public static final ItemEntry<CirnoWingsItem> CIRNO_WINGS;

	public static final ItemEntry<DebugGlasses> DEBUG_GLASSES;
	public static final ItemEntry<DebugWand> DEBUG_WAND;

	private static final DCReg DC = DCReg.of(GensokyoLegacy.REG);
	public static final DCVal<Integer> INCENSE_DUR = DC.intVal("incense_duration");
	public static final DCVal<Integer> INCENSE_RAD = DC.intVal("incense_radius");

	static {
		var reg = GensokyoLegacy.REGISTRATE;

		TAB = reg.buildModCreativeTab("ingredients", "Gensokyo Legacy - Ingredients",
				e -> e.icon(GLItems.FAIRY_ICE_CRYSTAL::asStack));

		FAIRY_ICE_CRYSTAL = reg.item("fairy_ice_crystal", FairyIceItem::new)
				.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/ingredient/" + ctx.getName())))
				.register();
		FROZEN_FROG_COLD = reg.item("frozen_frog_cold",
						p -> new FrozenFrogItem(p.stacksTo(16), FrogVariant.COLD))
				.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/ingredient/" + ctx.getName())))
				.register();
		FROZEN_FROG_WARM = reg.item("frozen_frog_warm",
						p -> new FrozenFrogItem(p.stacksTo(16), FrogVariant.WARM))
				.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/ingredient/" + ctx.getName())))
				.register();
		FROZEN_FROG_TEMPERATE = reg.item("frozen_frog_temperate",
						p -> new FrozenFrogItem(p.stacksTo(16), FrogVariant.TEMPERATE))
				.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/ingredient/" + ctx.getName())))
				.register();
		MISTLETOE_BRANCH = reg.item("mistletoe_branch", MistletoeBranch::new)
				.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/ingredient/" + ctx.getName())))
				.register();
		BLOOD_BOTTLE = new BottledFluid<>(reg, "blood", 0xff772221, () -> Items.GLASS_BOTTLE, "ingredient", BloodBottleItem::new);

		// spell cards
		{
			REIMU_SPELL = reg
					.item("spell_reimu", p -> new SpellItem(
							p.stacksTo(1), ReimuItemSpell::new, true,
							() -> DanmakuItems.Bullet.CIRCLE.get(DyeColor.RED).get()))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/spell/" + ctx.getName())))
					.lang("Reimu's Spellcard \"Innate Dream\"")
					.tag(DanmakuTagGen.PRESET_SPELL)
					.register();

			MARISA_SPELL = reg
					.item("spell_marisa", p -> new SpellItem(
							p.stacksTo(1), MarisaItemSpell::new, false,
							() -> DanmakuItems.Laser.LASER.get(DyeColor.WHITE).get()))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/spell/" + ctx.getName())))
					.lang("Marisa's Spellcard \"Master Spark\"")
					.tag(DanmakuTagGen.PRESET_SPELL)
					.register();

			SANAE_SPELL = reg
					.item("spell_sanae", p -> new SpellItem(
							p.stacksTo(1), SanaeItemSpell::new, false,
							() -> DanmakuItems.Bullet.SPARK.get(DyeColor.GREEN).get()))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/spell/" + ctx.getName())))
					.lang("Sanae's Spellcard \"Inherited Ritual\"")
					.tag(DanmakuTagGen.PRESET_SPELL)
					.register();

			YUKARI_SPELL_BUTTERFLY = reg
					.item("spell_yukari_butterfly", p -> new SpellItem(
							p.stacksTo(1), YukariItemSpellButterfly::new, false,
							() -> DanmakuItems.Bullet.BUTTERFLY.get(DyeColor.MAGENTA).get()))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/spell/spell_yukari")))
					.lang("Barrier \"Double Black Death Butterfly\"")
					.tag(DanmakuTagGen.PRESET_SPELL)
					.register();

			YUKARI_SPELL_LASER = reg
					.item("spell_yukari_laser", p -> new SpellItem(
							p.stacksTo(1), YukariItemSpellLaser::new, false,
							() -> DanmakuItems.Laser.LASER.get(DyeColor.RED).get()))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/spell/spell_yukari")))
					.lang("Barrier \"Mesh of Light & Darkness\"")
					.tag(DanmakuTagGen.PRESET_SPELL)
					.register();

			MYSTIA_SPELL = reg
					.item("spell_mystia", p -> new SpellItem(
							p.stacksTo(1), YukariItemSpellLaser::new, false,
							() -> DanmakuItems.Bullet.MENTOS.get(DyeColor.GREEN).get()))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/spell/" + ctx.getName())))
					.lang("Night Sparrow \"Midnight Chorus Master\"")
					.tag(DanmakuTagGen.PRESET_SPELL)
					.register();
		}

		// gears
		{
			STRAW_HAT = reg
					.item("straw_hat", p -> new StrawHatItem(p.rarity(Rarity.UNCOMMON)))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/curio/" + ctx.getName())))
					.clientExtension(() -> () -> new HatModel(SuwakoHatModel.STRAW))
					.register();

			SUWAKO_HAT = reg
					.item("suwako_hat", p -> new SuwakoHatItem(p.rarity(Rarity.EPIC)))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/curio/" + ctx.getName())))
					.clientExtension(() -> () -> new HatModel(SuwakoHatModel.SUWAKO))
					.tag(ItemTags.HEAD_ARMOR, GLTagGen.TOUHOU_HAT)
					.register();

			KOISHI_HAT = reg
					.item("koishi_hat", p -> new KoishiHatItem(p.rarity(Rarity.EPIC)))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/curio/" + ctx.getName())))
					.clientExtension(() -> () -> new HatModel(KoishiHatModel.HAT))
					.tag(ItemTags.HEAD_ARMOR, GLTagGen.TOUHOU_HAT)
					.register();

			RUMIA_HAIRBAND = reg
					.item("rumia_hairband", p -> new RumiaHairbandItem(p.rarity(Rarity.EPIC)))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/curio/" + ctx.getName())))
					.clientExtension(() -> () -> new RumiaHairbandModel(RumiaModel.HAIRBAND))
					.tag(ItemTags.HEAD_ARMOR, GLTagGen.TOUHOU_HAT)
					.register();

			REIMU_HAIRBAND = reg
					.item("reimu_hairband", p -> new ReimuHairbandItem(p.rarity(Rarity.EPIC)))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/curio/" + ctx.getName())))
					.clientExtension(() -> () -> new ReimuHairbandModel(ReimuModel.HAIRBAND))
					.tag(ItemTags.HEAD_ARMOR, GLTagGen.TOUHOU_HAT)
					.register();


			CIRNO_HAIRBAND = reg
					.item("cirno_hairband", p -> new CirnoHairbandItem(p.rarity(Rarity.EPIC)))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/curio/" + ctx.getName())))
					.clientExtension(() -> () -> new CirnoHairbandModel(CirnoModel.HAT))
					.tag(ItemTags.HEAD_ARMOR, GLTagGen.TOUHOU_HAT)
					.register();

			var back = ItemTags.create(ResourceLocation.fromNamespaceAndPath("curios", "back"));

			CIRNO_WINGS = reg
					.item("cirno_wings", p -> new CirnoWingsItem(p.rarity(Rarity.EPIC)))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/curio/" + ctx.getName())))
					.tag(back, GLTagGen.TOUHOU_WINGS)
					.register();

		}

		DEBUG_GLASSES = reg.item("debug_glasses", p -> new DebugGlasses(p.stacksTo(1)))
				.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/debug/" + ctx.getName())))
				.defaultLang().register();

		DEBUG_WAND = reg.item("debug_wand", p -> new DebugWand(p.stacksTo(1)))
				.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/debug/" + ctx.getName())))
				.defaultLang().register();

		GLBlocks.register();
	}

	public static void register() {

	}

}
