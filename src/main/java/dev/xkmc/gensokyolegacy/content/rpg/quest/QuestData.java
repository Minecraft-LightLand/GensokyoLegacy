package dev.xkmc.gensokyolegacy.content.rpg.quest;

import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.server.level.ServerPlayer;

import java.util.TreeMap;

@SerialClass
public class QuestData {

	@SerialField
	public int completed;

	@SerialField
	public boolean started;

	@SerialField
	public long lastCompletion;

	@SerialField
	public final TreeMap<String, Integer> progress = new TreeMap<>();

	public boolean isCompletable(ServerPlayer sp, Quest quest) {
		for (var e : quest.requirements().entrySet()) {
			var req = e.getValue();
			if (req.getMaxProgress() > progress.getOrDefault(e.getKey(), 0))
				return false;
			if (!req.canComplete(sp))
				return false;
		}
		return true;
	}

	public boolean hasStarted(Quest quest) {
		return started;
	}

	public boolean canRestart(ServerPlayer sp, Quest quest) {
		var opt = quest.recurrence();
		if (opt.isEmpty()) {
			return completed == 0;
		}
		long time = sp.level().getGameTime();
		return time < lastCompletion || lastCompletion <= 0 || time > lastCompletion + opt.get().cooldown();
	}

}
