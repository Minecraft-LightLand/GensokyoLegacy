package dev.xkmc.gensokyolegacy.compat.touhoulittlemaid;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import dev.xkmc.danmakuapi.content.spell.spellcard.SpellCard;
import dev.xkmc.danmakuapi.content.spell.spellcard.SpellCardWrapper;
import dev.xkmc.gensokyolegacy.content.entity.characters.maiden.MaidenEntity;
import dev.xkmc.gensokyolegacy.content.entity.youkai.GeneralYoukaiEntity;
import dev.xkmc.gensokyolegacy.content.spell.card.*;
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
		registerSpell("touhou_little_maid:hakurei_reimu", ReimuSpell::new);
		registerSpell("touhou_little_maid:yukari_yakumo", YukariSpell::new);
		registerSpell("touhou_little_maid:cirno", CirnoSpell::new);
		registerSpell("touhou_little_maid:kochiya_sanae", SanaeSpell::new);
		registerSpell("touhou_little_maid:komeiji_koishi", KoishiSpell::new);
		registerSpell("touhou_little_maid:kirisame_marisa", MarisaSpell::new);

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
