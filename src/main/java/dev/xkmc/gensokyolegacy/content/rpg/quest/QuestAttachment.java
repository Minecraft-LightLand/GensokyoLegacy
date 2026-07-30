package dev.xkmc.gensokyolegacy.content.rpg.quest;

import dev.xkmc.l2core.capability.player.PlayerCapabilityTemplate;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;

@SerialClass
public class QuestAttachment extends PlayerCapabilityTemplate<QuestAttachment> {

	@SerialField
	public final LinkedHashMap<ResourceLocation, QuestData> data = new LinkedHashMap<>();

	public @Nullable QuestData getData(ResourceLocation id) {
		return data.get(id);
	}

}
