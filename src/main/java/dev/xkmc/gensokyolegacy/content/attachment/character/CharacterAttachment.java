package dev.xkmc.gensokyolegacy.content.attachment.character;


import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import dev.xkmc.l2core.capability.player.PlayerCapabilityTemplate;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

@SerialClass
public class CharacterAttachment extends PlayerCapabilityTemplate<CharacterAttachment> {

	@SerialField
	private final Map<EntityType<?>, CharacterData> characterData = new LinkedHashMap<>();

	@Override
	public void tick(Player player) {
		if (player.level().isClientSide()) {
			return;
		}
		if (player.level().getDayTime() == 0) {
			for (var e : characterData.values()) {
				e.dailyUpdate();
			}
		}
	}

	@Nullable
	public CharDataHolder get(Player player, YoukaiEntity e) {
		var val = characterData.computeIfAbsent(e.getType(), k -> new CharacterData());
		return new CharDataHolder(val, player, e);
	}

	public void replace(EntityType<?> target, CharacterData data) {
		characterData.put(target, data);
	}
}
