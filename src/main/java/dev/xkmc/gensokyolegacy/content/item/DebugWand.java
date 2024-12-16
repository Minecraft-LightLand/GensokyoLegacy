package dev.xkmc.gensokyolegacy.content.item;

import dev.xkmc.gensokyolegacy.content.attachment.character.CharacterData;
import dev.xkmc.gensokyolegacy.content.block.bed.YoukaiBedBlockEntity;
import dev.xkmc.gensokyolegacy.content.client.debug.InfoUpdateClientManager;
import dev.xkmc.gensokyolegacy.content.client.structure.StructureBoundUpdateToClient;
import dev.xkmc.gensokyolegacy.content.client.structure.StructureInfoClientManager;
import dev.xkmc.gensokyolegacy.content.client.structure.StructureRepairManager;
import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import dev.xkmc.gensokyolegacy.init.data.GLLang;
import dev.xkmc.gensokyolegacy.init.registrate.GLMisc;
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
			else InfoUpdateClientManager.clearCache();
		} else if (context.getPlayer() != null) {
			if (context.getPlayer().isShiftKeyDown()) {
				if (level.isClientSide()) {
					StructureRepairManager.openScreen();
				}
				return InteractionResult.SUCCESS;
			} else if (context.getPlayer() instanceof ServerPlayer sp) {
				StructureBoundUpdateToClient.clickBlockInServer(sp, pos);
			} else if (StructureInfoClientManager.clearCache()) {
				return InteractionResult.FAIL;
			}
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity e, InteractionHand hand) {
		if (e instanceof YoukaiEntity) {
			if (!player.level().isClientSide()) {
				GLMisc.CHAR.type().getOrCreate(player).replace(e.getType(), new CharacterData());
			} else InfoUpdateClientManager.clearCache();
			return InteractionResult.SUCCESS;
		}
		return super.interactLivingEntity(stack, player, e, hand);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> list, TooltipFlag flag) {
		list.add(GLLang.ITEM_WAND_BED.get().withStyle(ChatFormatting.GRAY));
		list.add(GLLang.ITEM_WAND_BLOCK.get().withStyle(ChatFormatting.GRAY));
		list.add(GLLang.ITEM_WAND_STRUCTURE.get().withStyle(ChatFormatting.GRAY));
		list.add(GLLang.ITEM_WAND_CHARACTER.get().withStyle(ChatFormatting.GRAY));
	}

}
