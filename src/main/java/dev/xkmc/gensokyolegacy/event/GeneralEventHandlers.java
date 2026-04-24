package dev.xkmc.gensokyolegacy.event;

import dev.xkmc.danmakuapi.api.DanmakuDamageEvent;
import dev.xkmc.gensokyolegacy.content.attachment.misc.FrogGodCapability;
import dev.xkmc.gensokyolegacy.content.item.character.TouhouHatItem;
import dev.xkmc.gensokyolegacy.content.item.tool.CatBell;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.gensokyolegacy.init.data.GLDamageTypes;
import dev.xkmc.gensokyolegacy.init.registrate.GLMeta;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

@EventBusSubscriber(modid = GensokyoLegacy.MODID)
public class GeneralEventHandlers {

	@SubscribeEvent
	public static void onShieldBlock(LivingShieldBlockEvent event) {
		if (event.getDamageSource().is(GLDamageTypes.KOISHI)) {
			if (event.getEntity() instanceof Player player) {
				GLMeta.KOISHI_ATTACK.type().getOrCreate(player).onBlock(player);
			}
		}
	}

	@SubscribeEvent
	public static void startTracking(PlayerEvent.StartTracking event) {
		if (event.getTarget() instanceof Frog frog) {
			FrogGodCapability.startTracking(frog, event.getEntity());
		}
	}

	@SubscribeEvent
	public static void onDanmakuDamageType(DanmakuDamageEvent event) {
		var le = event.getUser();
		ItemStack stack = le.getItemBySlot(EquipmentSlot.HEAD);
		if (stack.getItem() instanceof TouhouHatItem hat) {
			event.setSource(hat.modifyDamageType(stack, le, event.getBullet(), event.getSource()));
		}
	}

	@SubscribeEvent
	public static void onLivingTick(EntityTickEvent.Pre event) {
		if (event.getEntity() instanceof Cat cat && cat.level() instanceof ServerLevel) {
			if (cat.isPassenger() && cat.getVehicle() instanceof ServerPlayer player) {
				if (cat.getTags().contains("CatBell")) {
					if (player.getXRot() < -45) {
						cat.unRide();
						GensokyoLegacy.HANDLER.toClientPlayer(new CatBell.MountToClient(cat.getId(), player.getId(), false), player);
					}
				}
			}
		}
	}

}
