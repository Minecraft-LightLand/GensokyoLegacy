package dev.xkmc.gensokyolegacy.init.data.rpg;

import com.tterrag.registrate.providers.ProviderType;
import dev.xkmc.gensokyolegacy.content.rpg.action.DialogAction;
import dev.xkmc.gensokyolegacy.content.rpg.core.CodecRegistry;
import dev.xkmc.gensokyolegacy.content.rpg.dialog.Dialog;
import dev.xkmc.gensokyolegacy.content.rpg.dialog.DialogOption;
import dev.xkmc.gensokyolegacy.content.rpg.dialog.DialogStarter;
import dev.xkmc.gensokyolegacy.content.rpg.dialog.SimpleDialogOption;
import dev.xkmc.gensokyolegacy.content.rpg.quest.Quest;
import dev.xkmc.gensokyolegacy.content.rpg.core.IngredientEntry;
import dev.xkmc.gensokyolegacy.content.rpg.reward.LootTableReward;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.l2core.init.reg.ench.DataGenHolder;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class QuestDialogData {

	private final Map<ResourceKey<Dialog>, DataGenHolder<Dialog>> dialogRegistry = new LinkedHashMap<>();
	private final Map<ResourceKey<DialogStarter>, DataGenHolder<DialogStarter>> starterRegistry = new LinkedHashMap<>();
	private final Map<ResourceKey<Quest>, DataGenHolder<Quest>> questRegistry = new LinkedHashMap<>();

	private String prefix = "";

	private final String modid;
	private final L2Registrate reg;

	public QuestDialogData(String modid, L2Registrate reg) {
		this.modid = modid;
		this.reg = reg;
		reg.getDataGenInitializer().add(CodecRegistry.DIALOG.key(), ctx ->
				dialogRegistry.forEach((k, v) -> ctx.register(k, v.value())));
		reg.getDataGenInitializer().add(CodecRegistry.STARTER.key(), ctx ->
				starterRegistry.forEach((k, v) -> ctx.register(k, v.value())));
		reg.getDataGenInitializer().add(CodecRegistry.QUEST.key(), ctx ->
				questRegistry.forEach((k, v) -> ctx.register(k, v.value())));
	}

	public QuestDialogData() {
		this(GensokyoLegacy.MODID, GensokyoLegacy.REGISTRATE);
	}

	public ResourceLocation loc(String id) {
		return ResourceLocation.fromNamespaceAndPath(modid, id);
	}

	public void prefix(String prefix) {
		this.prefix = prefix;
	}

	public String starterText(String id, String text) {
		return text("starter", id, text);
	}

	public String optionText(String id, String text) {
		return text("option", id, text);
	}

	public String reqText(String id, String text) {
		return text("requirement", id, text);
	}

	public String questTitle(String text) {
		return text("quest", "title", text);
	}

	public String questDesc(String text) {
		return text("quest", "desc", text);
	}

	private String text(String type, String id, String text) {
		String full = modid + "/" + prefix + "/" + type + "/" + id;
		reg.addRawLang(full, text);
		return full;
	}

	public Holder<Dialog> dialog(String id, String text, DialogOption<?>... options) {
		id = prefix + "/" + id;
		var key = ResourceKey.create(CodecRegistry.DIALOG.key(), loc(id));
		var holder = new DataGenHolder<>(key, new Dialog(text("dialog", id, text), List.of(options)));
		dialogRegistry.put(key, holder);
		return holder;
	}

	public Holder<DialogStarter> starter(String id, DialogStarter dialog) {
		var key = ResourceKey.create(CodecRegistry.STARTER.key(), loc(id));
		var holder = new DataGenHolder<>(key, dialog);
		starterRegistry.put(key, holder);
		return holder;
	}

	public Holder<Quest> quest(String id, Quest quest) {
		var key = ResourceKey.create(CodecRegistry.QUEST.key(), loc(id));
		var holder = new DataGenHolder<>(key, quest);
		questRegistry.put(key, holder);
		return holder;
	}

	public LootTableReward loot(String id, LootTable.Builder loot) {
		var key = ResourceKey.create(Registries.LOOT_TABLE, loc(id));
		reg.addDataGenerator(ProviderType.LOOT, pvd -> pvd.addLootAction(
				LootContextParamSets.ADVANCEMENT_REWARD,
				c -> c.accept(key, loot)));
		return new LootTableReward(key.location());
	}

	public SimpleDialogOption option(String id, String text) {
		return new SimpleDialogOption(List.of(), optionText(id, text), List.of(), Optional.empty());
	}

	public SimpleDialogOption option(String id, String text, DialogAction<?> action) {
		return new SimpleDialogOption(List.of(), optionText(id, text), List.of(action), Optional.empty());
	}

	public SimpleDialogOption option(String id, String text, Holder<Dialog> next) {
		return new SimpleDialogOption(List.of(), optionText(id, text), List.of(), Optional.of(next));
	}

	public IngredientEntry item(ItemLike item, int count) {
		return new IngredientEntry(Ingredient.of(item), count, Optional.empty());
	}

	public LootPool.Builder lootItem(ItemLike item, int count) {
		return LootPool.lootPool().add(LootItem.lootTableItem(item)
				.apply(SetItemCountFunction.setCount(ConstantValue.exactly(count))));
	}

}
