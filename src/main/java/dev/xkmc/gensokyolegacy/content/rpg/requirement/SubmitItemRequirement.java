package dev.xkmc.gensokyolegacy.content.rpg.requirement;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.gensokyolegacy.content.rpg.core.IngredientEntry;
import dev.xkmc.gensokyolegacy.content.rpg.core.IngredientList;
import dev.xkmc.gensokyolegacy.content.rpg.quest.QuestRequirement;
import dev.xkmc.gensokyolegacy.content.rpg.trigger.EmptyTrigger;
import dev.xkmc.gensokyolegacy.init.data.GLLang;
import dev.xkmc.gensokyolegacy.util.InventoryMapper;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

public record SubmitItemRequirement(
		List<IngredientEntry> ingredients
) implements QuestRequirement<SubmitItemRequirement, EmptyTrigger>, IngredientList {

	public static final MapCodec<SubmitItemRequirement> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			IngredientEntry.CODEC.listOf().fieldOf("ingredients").forGetter(SubmitItemRequirement::ingredients)
	).apply(i, SubmitItemRequirement::new));

	@Override
	public MapCodec<SubmitItemRequirement> codec() {
		return CODEC;
	}

	@Override
	public Class<EmptyTrigger> getTrigger() {
		return EmptyTrigger.class;
	}

	@Override
	public boolean canComplete(Player pl) {
		return InventoryMapper.testCached(pl, this);
	}

	@Override
	public void doComplete(ServerPlayer sp) {
		var ans = new InventoryMapper(sp.getInventory().items, ingredients);
		ans.test();
		ans.consume();
	}

	@Override
	public List<Component> getDesc(Player player, int progress) {
		List<Component> ans = new ArrayList<>();
		for (var e : ingredients) {
			ans.add(e.getDesc(player));
		}
		if (ingredients.size() > 1) {
			boolean pass = InventoryMapper.testCached(player, this);
			ans.addFirst(pass ? GLLang.QUEST$ITEM_SUBMIT_PASS.get() : GLLang.QUEST$ITEM_SUBMIT_FAIL.get());
		}
		return ans;
	}

}
