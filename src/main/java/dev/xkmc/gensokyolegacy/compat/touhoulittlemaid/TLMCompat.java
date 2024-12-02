package dev.xkmc.gensokyolegacy.compat.touhoulittlemaid;

import com.github.tartaricacid.touhoulittlemaid.item.ItemGarageKit;
import dev.xkmc.gensokyolegacy.content.entity.youkai.GeneralYoukaiEntity;
import net.minecraft.world.InteractionResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

public class TLMCompat {

	@SubscribeEvent
	public static void onInteract(PlayerInteractEvent.EntityInteract event) {
		if (event.getTarget() instanceof GeneralYoukaiEntity e && event.getEntity().isCreative()) {
			if (event.getItemStack().getItem() instanceof ItemGarageKit) {
				if (!event.getTarget().level().isClientSide()) {
					String id = ItemGarageKit.getMaidData(event.getItemStack()).getUnsafe().getString("model_id");
					TouhouSpellCards.setSpell(e, id);
				}
				event.setCancellationResult(InteractionResult.SUCCESS);
				event.setCanceled(true);
			}
		}
	}

}
