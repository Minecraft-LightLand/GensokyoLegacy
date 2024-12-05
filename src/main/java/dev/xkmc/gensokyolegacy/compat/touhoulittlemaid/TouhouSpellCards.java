package dev.xkmc.gensokyolegacy.compat.touhoulittlemaid;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import dev.xkmc.danmakuapi.content.spell.spellcard.ListSpellCard;
import dev.xkmc.danmakuapi.content.spell.spellcard.SpellCard;
import dev.xkmc.danmakuapi.content.spell.spellcard.SpellCardWrapper;
import dev.xkmc.danmakuapi.presets.game.cirno.CirnoIceStorm;
import dev.xkmc.danmakuapi.presets.game.koishi.AncestorDream;
import dev.xkmc.danmakuapi.presets.game.reimu.StagedHoming;
import dev.xkmc.danmakuapi.presets.game.sanae.Sugiruyoru;
import dev.xkmc.danmakuapi.presets.game.youmu.YoumuSlash;
import dev.xkmc.danmakuapi.presets.game.yukari.YukariMain;
import dev.xkmc.danmakuapi.presets.game.yuyuko.YuyukoTest;
import dev.xkmc.gensokyolegacy.content.entity.characters.maiden.MaidenEntity;
import dev.xkmc.gensokyolegacy.content.entity.youkai.GeneralYoukaiEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class TouhouSpellCards {

	private static final Map<String, Supplier<SpellCard>> MAP = new ConcurrentHashMap<>();

	public static void registerSpell(String id, Supplier<SpellCard> card) {
		MAP.put(id, card);
	}

	public static void registerSpells() {
		registerSpell("touhou_little_maid:hakurei_reimu", StagedHoming::new);
		registerSpell("touhou_little_maid:yukari_yakumo", YukariMain::new);
		registerSpell("touhou_little_maid:cirno", CirnoIceStorm::new);
		registerSpell("touhou_little_maid:kochiya_sanae", Sugiruyoru::new);
		registerSpell("touhou_little_maid:komeiji_koishi", AncestorDream::new);

		registerSpell("touhou_little_maid:konpaku_youmu", () -> ListSpellCard.of(new YoumuSlash()));
		registerSpell("touhou_little_maid:saigyouji_yuyuko", () -> ListSpellCard.of(new YuyukoTest()));
	}

	public static void setSpell(GeneralYoukaiEntity e, String id) {
		e.spellCard = new SpellCardWrapper();
		e.spellCard.modelId = id;
		var sup = MAP.get(id);
		if (sup != null) e.spellCard.card = sup.get();
		e.syncModel();
		if (ModList.get().isLoaded(TouhouLittleMaid.MOD_ID)) {
			var rl = ResourceLocation.parse(id);
			var name = Component.translatable(rl.toLanguageKey("model") + ".name");
			var desc = Component.translatable(rl.toLanguageKey("model") + ".desc");
			e.setCustomName(name.append(" - ").append(desc));
		}
	}

	public static void setReimu(MaidenEntity e) {
		setSpell(e, "touhou_little_maid:hakurei_reimu");
		if (ModList.get().isLoaded(TouhouLittleMaid.MOD_ID)) {
			e.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(InitItems.HAKUREI_GOHEI.get(), 1));
		}
	}

	public static void setCirno(GeneralYoukaiEntity e) {
		setSpell(e, "touhou_little_maid:cirno");
	}

	public static void setYukari(GeneralYoukaiEntity e) {
		setSpell(e, "touhou_little_maid:yukari_yakumo");
	}

	public static void setSanae(GeneralYoukaiEntity e) {
		setSpell(e, "touhou_little_maid:kochiya_sanae");
	}

	public static void setKoishi(GeneralYoukaiEntity e) {
		setSpell(e, "touhou_little_maid:komeiji_koishi");
	}

}
