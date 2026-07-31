package dev.xkmc.gensokyolegacy.content.rpg.quest;

import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import dev.xkmc.gensokyolegacy.content.rpg.core.CodecRegistry;
import dev.xkmc.gensokyolegacy.content.rpg.network.QuestStatusToClient;
import dev.xkmc.gensokyolegacy.content.rpg.trigger.QuestTrigger;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.l2core.capability.player.PlayerCapabilityTemplate;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.LinkedHashMap;

@SerialClass
public class QuestAttachment extends PlayerCapabilityTemplate<QuestAttachment> {

	@SerialField
	public final LinkedHashMap<ResourceLocation, QuestData> data = new LinkedHashMap<>();

	public QuestData getData(ResourceLocation id) {
		return data.computeIfAbsent(id, k -> new QuestData());
	}

	public void start(ServerPlayer sp, Holder<Quest> quest) {
		var id = quest.unwrapKey().orElseThrow().location();
		var data = getData(id);
		if (data.canStart(sp, quest.value())) {
			data.start(sp);
			GensokyoLegacy.HANDLER.toClientPlayer(new QuestStatusToClient(id, data), sp);
		}
	}

	public void complete(ServerPlayer sp, Holder<Quest> quest, YoukaiEntity ch) {
		var id = quest.unwrapKey().orElseThrow().location();
		var data = getData(id);
		if (data.isCompletable(sp, quest.value())) {
			data.complete(sp, quest.value(), ch);
			GensokyoLegacy.HANDLER.toClientPlayer(new QuestStatusToClient(id, data), sp);
		}
	}

	public void replace(ResourceLocation id, QuestData val) {
		data.put(id, val);
	}

	public <T extends Record & QuestTrigger<T>> void dispatch(ServerPlayer sp, T trigger) {
		for (var e : data.entrySet()) {
			var opt = sp.level().registryAccess().holder(ResourceKey.create(CodecRegistry.Keys.QUEST, e.getKey()));
			if (opt.isEmpty()) continue;
			var quest = opt.get().value();
			boolean updated = false;
			for (var req : quest.requirements().entrySet()) {
				var prog = e.getValue().progress;
				var key = req.getKey();
				if (prog.getOrDefault(key, 0) >= req.getValue().getMaxProgress())
					continue;
				int add = req.getValue().rawMatch(trigger);
				if (add > 0) {
					prog.compute(key, (k, v) -> (v == null ? 0 : v) + add);
					updated = true;
				}
			}
			if (updated) {
				GensokyoLegacy.HANDLER.toClientPlayer(new QuestStatusToClient(e.getKey(), e.getValue(), true), sp);
			}
		}
	}

}
