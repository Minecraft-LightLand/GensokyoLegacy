package dev.xkmc.gensokyolegacy.event;

import dev.xkmc.danmakuapi.api.DanmakuDamageEvent;
import dev.xkmc.gensokyolegacy.content.attachment.misc.FrogGodCapability;
import dev.xkmc.gensokyolegacy.content.attachment.role.RolePlayHandler;
import dev.xkmc.gensokyolegacy.content.item.character.TouhouHatItem;
import dev.xkmc.gensokyolegacy.content.mechanics.role.core.RoleCategory;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.gensokyolegacy.init.data.GLDamageTypes;
import dev.xkmc.gensokyolegacy.init.data.GLModConfig;
import dev.xkmc.gensokyolegacy.init.data.GLTagGen;
import dev.xkmc.gensokyolegacy.init.registrate.GLItems;
import dev.xkmc.gensokyolegacy.init.registrate.GLMeta;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import vectorwing.farmersdelight.common.tag.CommonTags;

@EventBusSubscriber(modid = GensokyoLegacy.MODID, bus = EventBusSubscriber.Bus.GAME)
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
	public static void collectBlood(LivingDeathEvent event) {
		if (!event.getEntity().getType().is(GLTagGen.FLESH_SOURCE)) return;
		if (event.getSource().getEntity() instanceof Player le) {
			if (le.getMainHandItem().is(CommonTags.TOOLS_KNIFE) &&
					RolePlayHandler.is(le, RoleCategory.YOUKAI))
				spawnBlood(le);
			if (le.getItemBySlot(EquipmentSlot.HEAD).is(GLItems.RUMIA_HAIRBAND.get()))
				spawnBlood(le);
		}
	}

	private static void spawnBlood(LivingEntity le) {
		if (!le.getOffhandItem().is(Items.GLASS_BOTTLE)) return;
		le.getOffhandItem().shrink(1);
		if (le instanceof Player player) {
			player.getInventory().add(GLItems.BLOOD_BOTTLE.asStack(1));
		} else {
			le.spawnAtLocation(GLItems.BLOOD_BOTTLE.asStack(1));
		}
	}

	@SubscribeEvent
	public static void startTracking(PlayerEvent.StartTracking event) {
		if (event.getTarget() instanceof Frog frog) {
			FrogGodCapability.startTracking(frog, event.getEntity());
		}
	}

	@SubscribeEvent
	public static void onEntityKilled(LivingDeathEvent event) {
		if (event.getEntity() instanceof Villager && GLModConfig.SERVER.reimuSummonKill.get()) {
			if (event.getSource().getEntity() instanceof Player le) {
				if (RolePlayHandler.is(le, RoleCategory.YOUKAI))
					ReimuEventHandlers.triggerReimuResponse(le, 16, false);
			}
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

}
