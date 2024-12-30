package dev.xkmc.gensokyolegacy.init.registrate;

import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.danmakuapi.content.item.SpellItem;
import dev.xkmc.danmakuapi.init.data.DanmakuTagGen;
import dev.xkmc.danmakuapi.init.registrate.DanmakuItems;
import dev.xkmc.gensokyolegacy.compat.food.GLFood;
import dev.xkmc.gensokyolegacy.compat.food.GLFoodType;
import dev.xkmc.gensokyolegacy.compat.food.GLSake;
import dev.xkmc.gensokyolegacy.compat.food.flesh.FleshBlockItem;
import dev.xkmc.gensokyolegacy.compat.food.flesh.FleshFeastBlock;
import dev.xkmc.gensokyolegacy.compat.food.flesh.FleshSimpleItem;
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
import dev.xkmc.gensokyolegacy.content.spell.item.MarisaItemSpell;
import dev.xkmc.gensokyolegacy.content.spell.item.ReimuItemSpell;
import dev.xkmc.gensokyolegacy.content.spell.item.SanaeItemSpell;
import dev.xkmc.gensokyolegacy.content.spell.item.YukariItemSpellLaser;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.gensokyolegacy.init.data.GLTagGen;
import dev.xkmc.l2core.init.reg.registrate.SimpleEntry;
import dev.xkmc.youkaishomecoming.content.item.fluid.BottledFluid;
import dev.xkmc.youkaishomecoming.init.food.CakeEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.animal.FrogVariant;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import vectorwing.farmersdelight.common.block.FeastBlock;

public class GLItems {

	public static final SimpleEntry<CreativeModeTab> TAB;

	public static final ItemEntry<FairyIceItem> FAIRY_ICE_CRYSTAL;
	public static final ItemEntry<FrozenFrogItem> FROZEN_FROG_COLD, FROZEN_FROG_WARM, FROZEN_FROG_TEMPERATE;
	public static final ItemEntry<MistletoeBranch> MISTLETOE_BRANCH;
	public static final BottledFluid<BloodBottleItem> BLOOD_BOTTLE;

	public static final ItemEntry<SpellItem> REIMU_SPELL, MARISA_SPELL, SANAE_SPELL, YUKARI_SPELL, MYSTIA_SPELL;

	public static final ItemEntry<StrawHatItem> STRAW_HAT;
	public static final ItemEntry<SuwakoHatItem> SUWAKO_HAT;
	public static final ItemEntry<KoishiHatItem> KOISHI_HAT;
	public static final ItemEntry<RumiaHairbandItem> RUMIA_HAIRBAND;
	public static final ItemEntry<ReimuHairbandItem> REIMU_HAIRBAND;
	public static final ItemEntry<CirnoHairbandItem> CIRNO_HAIRBAND;
	public static final ItemEntry<CirnoWingsItem> CIRNO_WINGS;

	public static final ItemEntry<FleshSimpleItem> RAW_FLESH_FEAST;
	public static final BlockEntry<FleshFeastBlock> FLESH_FEAST;
	public static final CakeEntry RED_VELVET;

	public static final ItemEntry<DebugGlasses> DEBUG_GLASSES;
	public static final ItemEntry<DebugWand> DEBUG_WAND;

	static {
		TAB = GensokyoLegacy.REGISTRATE.buildModCreativeTab("ingredients", "Gensokyo Legacy - Ingredients",
				e -> e.icon(GLItems.FAIRY_ICE_CRYSTAL::asStack));

		FAIRY_ICE_CRYSTAL = GensokyoLegacy.REGISTRATE.item("fairy_ice_crystal", FairyIceItem::new)
				.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/ingredient/" + ctx.getName())))
				.register();
		FROZEN_FROG_COLD = GensokyoLegacy.REGISTRATE.item("frozen_frog_cold",
						p -> new FrozenFrogItem(p.stacksTo(16), FrogVariant.COLD))
				.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/ingredient/" + ctx.getName())))
				.register();
		FROZEN_FROG_WARM = GensokyoLegacy.REGISTRATE.item("frozen_frog_warm",
						p -> new FrozenFrogItem(p.stacksTo(16), FrogVariant.WARM))
				.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/ingredient/" + ctx.getName())))
				.register();
		FROZEN_FROG_TEMPERATE = GensokyoLegacy.REGISTRATE.item("frozen_frog_temperate",
						p -> new FrozenFrogItem(p.stacksTo(16), FrogVariant.TEMPERATE))
				.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/ingredient/" + ctx.getName())))
				.register();
		MISTLETOE_BRANCH = GensokyoLegacy.REGISTRATE.item("mistletoe_branch", MistletoeBranch::new)
				.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/ingredient/" + ctx.getName())))
				.register();
		BLOOD_BOTTLE = new BottledFluid<>("blood", 0xff772221, () -> Items.GLASS_BOTTLE, "ingredient", BloodBottleItem::new);

		// spell cards
		{
			REIMU_SPELL = GensokyoLegacy.REGISTRATE
					.item("spell_reimu", p -> new SpellItem(
							p.stacksTo(1), ReimuItemSpell::new, true,
							() -> DanmakuItems.Bullet.CIRCLE.get(DyeColor.RED).get()))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/spell/" + ctx.getName())))
					.lang("Reimu's Spellcard \"Innate Dream\"")
					.tag(DanmakuTagGen.PRESET_SPELL)
					.register();

			MARISA_SPELL = GensokyoLegacy.REGISTRATE
					.item("spell_marisa", p -> new SpellItem(
							p.stacksTo(1), MarisaItemSpell::new, false,
							() -> DanmakuItems.Laser.LASER.get(DyeColor.WHITE).get()))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/spell/" + ctx.getName())))
					.lang("Marisa's Spellcard \"Master Spark\"")
					.tag(DanmakuTagGen.PRESET_SPELL)
					.register();

			SANAE_SPELL = GensokyoLegacy.REGISTRATE
					.item("spell_sanae", p -> new SpellItem(
							p.stacksTo(1), SanaeItemSpell::new, false,
							() -> DanmakuItems.Bullet.SPARK.get(DyeColor.GREEN).get()))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/spell/" + ctx.getName())))
					.lang("Sanae's Spellcard \"Inherited Ritual\"")
					.tag(DanmakuTagGen.PRESET_SPELL)
					.register();

			YUKARI_SPELL = GensokyoLegacy.REGISTRATE
					.item("spell_yukari", p -> new SpellItem(
							p.stacksTo(1), YukariItemSpellLaser::new, false,
							() -> DanmakuItems.Laser.LASER.get(DyeColor.RED).get()))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/spell/" + ctx.getName())))
					.lang("Barrier \"Mesh of Light & Darkness\"")
					.tag(DanmakuTagGen.PRESET_SPELL)
					.register();

			MYSTIA_SPELL = GensokyoLegacy.REGISTRATE
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
			STRAW_HAT = GensokyoLegacy.REGISTRATE
					.item("straw_hat", p -> new StrawHatItem(p.rarity(Rarity.UNCOMMON)))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/curio/" + ctx.getName())))
					.clientExtension(() -> () -> new HatModel(SuwakoHatModel.STRAW))
					.register();

			SUWAKO_HAT = GensokyoLegacy.REGISTRATE
					.item("suwako_hat", p -> new SuwakoHatItem(p.rarity(Rarity.EPIC)))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/curio/" + ctx.getName())))
					.clientExtension(() -> () -> new HatModel(SuwakoHatModel.SUWAKO))
					.tag(ItemTags.HEAD_ARMOR, GLTagGen.TOUHOU_HAT)
					.register();

			KOISHI_HAT = GensokyoLegacy.REGISTRATE
					.item("koishi_hat", p -> new KoishiHatItem(p.rarity(Rarity.EPIC)))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/curio/" + ctx.getName())))
					.clientExtension(() -> () -> new HatModel(KoishiHatModel.HAT))
					.tag(ItemTags.HEAD_ARMOR, GLTagGen.TOUHOU_HAT)
					.register();

			RUMIA_HAIRBAND = GensokyoLegacy.REGISTRATE
					.item("rumia_hairband", p -> new RumiaHairbandItem(p.rarity(Rarity.EPIC)))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/curio/" + ctx.getName())))
					.clientExtension(() -> () -> new RumiaHairbandModel(RumiaModel.HAIRBAND))
					.tag(ItemTags.HEAD_ARMOR, GLTagGen.TOUHOU_HAT)
					.register();

			REIMU_HAIRBAND = GensokyoLegacy.REGISTRATE
					.item("reimu_hairband", p -> new ReimuHairbandItem(p.rarity(Rarity.EPIC)))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/curio/" + ctx.getName())))
					.clientExtension(() -> () -> new ReimuHairbandModel(ReimuModel.HAIRBAND))
					.tag(ItemTags.HEAD_ARMOR, GLTagGen.TOUHOU_HAT)
					.register();


			CIRNO_HAIRBAND = GensokyoLegacy.REGISTRATE
					.item("cirno_hairband", p -> new CirnoHairbandItem(p.rarity(Rarity.EPIC)))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/curio/" + ctx.getName())))
					.clientExtension(() -> () -> new CirnoHairbandModel(CirnoModel.HAT))
					.tag(ItemTags.HEAD_ARMOR, GLTagGen.TOUHOU_HAT)
					.register();

			var back = ItemTags.create(ResourceLocation.fromNamespaceAndPath("curios", "back"));

			CIRNO_WINGS = GensokyoLegacy.REGISTRATE
					.item("cirno_wings", p -> new CirnoWingsItem(p.rarity(Rarity.EPIC)))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/curio/" + ctx.getName())))
					.tag(back, GLTagGen.TOUHOU_WINGS)
					.register();

		}

		DEBUG_GLASSES = GensokyoLegacy.REGISTRATE.item("debug_glasses", p -> new DebugGlasses(p.stacksTo(1)))
				.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/debug/" + ctx.getName())))
				.defaultLang().register();

		DEBUG_WAND = GensokyoLegacy.REGISTRATE.item("debug_wand", p -> new DebugWand(p.stacksTo(1)))
				.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/debug/" + ctx.getName())))
				.defaultLang().register();

		GLBlocks.register();

		GLFood.register();

		{


			RAW_FLESH_FEAST = GensokyoLegacy.REGISTRATE.item("raw_flesh_feast", FleshSimpleItem::new)
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/feast/" + ctx.getName())))
					.lang("Raw %1$s Feast")
					.register();

			FLESH_FEAST = GensokyoLegacy.REGISTRATE.block("flesh_feast", p ->
							new FleshFeastBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BROWN_WOOL),
									GLFood.BOWL_OF_FLESH_FEAST.item))
					.blockstate((ctx, pvd) -> pvd.horizontalBlock(ctx.get(), state ->
							FleshFeastBlock.Model.values()[state.getValue(FeastBlock.SERVINGS)].build(pvd)))
					.lang("%1$s Feast")
					.item(FleshBlockItem::new).model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/feast/" + ctx.getName()))).build()
					.loot(FleshFeastBlock::builtLoot)
					.register();

			RED_VELVET = new CakeEntry("red_velvet", MapColor.COLOR_RED, GLFoodType.FLESH, 1, 0.8f, true);
		}
		GLSake.register();
	}

	public static void register() {

	}

}
