package dev.xkmc.gensokyolegacy.compat.food.flesh;

import dev.xkmc.gensokyolegacy.compat.food.GLFood;
import dev.xkmc.gensokyolegacy.content.attachment.role.RolePlayHandler;
import dev.xkmc.gensokyolegacy.content.role.core.RoleCategory;
import dev.xkmc.gensokyolegacy.event.ReimuEventHandlers;
import dev.xkmc.gensokyolegacy.init.data.GLLang;
import dev.xkmc.gensokyolegacy.init.data.GLTagGen;
import dev.xkmc.gensokyolegacy.init.registrate.GLRoles;
import dev.xkmc.youkaishomecoming.content.item.food.YHFoodItem;
import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FleshFoodItem extends YHFoodItem {

	public FleshFoodItem(Properties props) {
		super(props);
	}

	@Override
	public @Nullable FoodProperties getFoodProperties(ItemStack stack, @Nullable LivingEntity entity) {
		FoodProperties old = super.getFoodProperties(stack, entity);
		if (old == null) return null;
		int factor = 1;
		if (entity instanceof Player player) {
			double prog = RolePlayHandler.progress(player, GLRoles.VAMPIRE.get());
			if (prog > 0)
				factor++;
			if (prog >= 1)
				factor++;
		}
		var builder = new FoodProperties.Builder();
		builder.nutrition(old.nutrition() * factor);
		builder.saturationModifier(old.saturation());
		if (old.canAlwaysEat()) builder.alwaysEdible();
		if (old.eatDurationTicks() < 20) builder.fast();
		for (var ent : old.effects()) {
			if (!ent.effect().getEffect().value().isBeneficial() || factor > 1) {
				builder.effect(ent::effect, ent.probability());
			}
		}
		return builder.build();
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext level, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(stack, level, list, flag);
		double prog = RolePlayHandler.progress(GLRoles.VAMPIRE.get());
		if (prog >= 1) {
			list.add(YHLangData.FLESH_TASTE_YOUKAI.get());
		} else if (prog > 0) {
			list.add(YHLangData.FLESH_TASTE_HALF_YOUKAI.get());
		} else if (getDefaultInstance().is(GLTagGen.APPARENT_FLESH_FOOD)) {
			list.add(YHLangData.FLESH_TASTE_HUMAN.get());
		}
		if (this == GLFood.FLESH.item.get()) {
			RolePlayHandler.addTooltips(list, GLLang.OBTAIN_FLESH.get(), null);//TODO
		}
	}

	@Override
	public Component getName(ItemStack pStack) {
		return Component.translatable(this.getDescriptionId(pStack),
				RolePlayHandler.showInfo() ?
						YHLangData.FLESH_NAME_YOUKAI.get() :
						YHLangData.FLESH_NAME_HUMAN.get()
		);
	}

	public void consume(Player consumer) {
		if (consumer.level().isClientSide()) return;
		RoleCategory.YOUKAI.advanceIfStarted(consumer, 1.5, 10);
		if (getDefaultInstance().is(GLTagGen.APPARENT_FLESH_FOOD) && consumer instanceof ServerPlayer sp) {
			ReimuEventHandlers.triggerReimuResponse(sp, 24, true);
		}
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity consumer) {
		ItemStack ans = super.finishUsingItem(stack, worldIn, consumer);
		if (!(consumer instanceof Player player)) return ans;
		consume(player);
		return ans;
	}

	@Override
	public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
		super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
	}

}
