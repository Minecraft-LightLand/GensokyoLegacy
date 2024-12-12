package dev.xkmc.gensokyolegacy.content.item;

import dev.xkmc.gensokyolegacy.content.block.bed.YoukaiBedBlockEntity;
import dev.xkmc.gensokyolegacy.init.data.GLLang;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.util.List;

public class DebugWand extends Item {

	public DebugWand(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
		return super.use(level, player, usedHand);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		var level = context.getLevel();
		var pos = context.getClickedPos();
		if (level.getBlockEntity(pos) instanceof YoukaiBedBlockEntity be) {
			if (context.getPlayer() instanceof ServerPlayer sp)
				be.debugClick(sp);
			return InteractionResult.SUCCESS;
		}
		return super.useOn(context);
	}

	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity
			interactionTarget, InteractionHand usedHand) {
		return super.interactLivingEntity(stack, player, interactionTarget, usedHand);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> list, TooltipFlag flag) {
		list.add(GLLang.ITEM_WAND_BED.get().withStyle(ChatFormatting.GRAY));
	}

}
