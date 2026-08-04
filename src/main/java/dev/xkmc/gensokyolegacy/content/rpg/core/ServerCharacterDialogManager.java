package dev.xkmc.gensokyolegacy.content.rpg.core;

import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import dev.xkmc.gensokyolegacy.content.rpg.dialog.DialogStarter;
import dev.xkmc.gensokyolegacy.content.rpg.handle.DialogHandle;
import dev.xkmc.gensokyolegacy.content.rpg.handle.IDialogHandle;
import dev.xkmc.gensokyolegacy.content.rpg.handle.QuestHandle;
import dev.xkmc.gensokyolegacy.content.rpg.quest.Quest;
import dev.xkmc.gensokyolegacy.content.rpg.trade.TradeOffer;
import dev.xkmc.gensokyolegacy.init.registrate.GLMeta;
import dev.xkmc.l2core.init.reg.datapack.DatapackReg;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;

import java.util.*;

public class ServerCharacterDialogManager {

	public static final Map<EntityType<?>, ServerCharacterDialogManager> MAP = new LinkedHashMap<>();

	public static void clearCache() {
		MAP.clear();
	}

	public static ServerCharacterDialogManager get(ServerLevel sl, EntityType<?> type) {
		return MAP.computeIfAbsent(type, k -> new ServerCharacterDialogManager(sl.registryAccess(), k));
	}

	private static <T extends CharacterEntry> List<Holder<T>> getAllMatching(RegistryAccess pvd, DatapackReg<T> reg, EntityType<?> ch) {
		return reg.getAll(pvd).filter(e -> e.value().character() == ch)
				.sorted(Comparator.comparing(e -> e.unwrapKey().orElseThrow())).toList();
	}

	private final RegistryAccess pvd;
	private final EntityType<?> character;

	private final List<Holder<DialogStarter>> dialogs;
	private final List<Holder<Quest>> quests;
	private final List<Holder<TradeOffer>> offers;

	public ServerCharacterDialogManager(RegistryAccess pvd, EntityType<?> character) {
		this.pvd = pvd;
		this.character = character;
		dialogs = getAllMatching(pvd, CodecRegistry.STARTER, character);
		quests = getAllMatching(pvd, CodecRegistry.QUEST, character);
		offers = getAllMatching(pvd, CodecRegistry.TRADE, character);
	}

	public List<TradeOffer> getTradeOffers(ServerPlayer sp, YoukaiEntity ch) {
		List<TradeOffer> ans = new ArrayList<>();
		for (var e : offers) {
			if (e.value().match(sp, ch)) {
				ans.add(e.value());
			}
		}
		return ans;
	}

	public List<IDialogHandle> getInitialConversation(ServerPlayer sp, YoukaiEntity ch) {
		List<IDialogHandle> ans = new ArrayList<>();
		for (var e : dialogs) {
			if (e.value().match(sp, ch))
				ans.add(new DialogHandle(e));
		}
		var questData = GLMeta.QUEST.type().getOrCreate(sp);
		for (var e : quests) {
			var data = questData.getData(e.unwrapKey().orElseThrow().location());
			if (data.isCompletable(sp, e.value()))
				ans.add(new QuestHandle(e, e.value().completionDialog()));
			else if (data.hasStarted(e.value()))
				ans.add(new QuestHandle(e, e.value().followUpDialog()));
			else if (data.canStart(sp, e.value()) && e.value().match(sp, ch))
				ans.add(new QuestHandle(e, e.value().initialDialog()));
		}
		//TODO trade handle
		return ans;
	}

}
