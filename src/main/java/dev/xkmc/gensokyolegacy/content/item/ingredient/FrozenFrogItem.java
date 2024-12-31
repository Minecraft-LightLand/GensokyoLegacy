package dev.xkmc.gensokyolegacy.content.item.ingredient;

import dev.xkmc.gensokyolegacy.content.attachment.role.RolePlayHandler;
import dev.xkmc.gensokyolegacy.content.entity.misc.FrozenFrog;
import dev.xkmc.gensokyolegacy.init.data.GLLang;
import net.minecraft.Util;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.animal.FrogVariant;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class FrozenFrogItem extends Item implements ProjectileItem {

	public final ResourceKey<FrogVariant> variant;

	public FrozenFrogItem(Properties pProperties, ResourceKey<FrogVariant> variant) {
		super(pProperties);
		this.variant = variant;
	}

	public InteractionResultHolder<ItemStack> use(Level pLevel, Player player, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		pLevel.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.EGG_THROW,
				SoundSource.PLAYERS, 0.5F, 0.4F / (pLevel.getRandom().nextFloat() * 0.4F + 0.8F));
		if (!pLevel.isClientSide) {
			FrozenFrog frog = new FrozenFrog(pLevel, player);
			frog.setItem(itemstack);
			frog.shootFromRotation(player, player.getXRot(), player.getYRot(),
					0.0F, 1.5F, 1.0F);
			pLevel.addFreshEntity(frog);
		}

		player.awardStat(Stats.ITEM_USED.get(this));
		if (!player.getAbilities().instabuild) {
			itemstack.shrink(1);
		}

		return InteractionResultHolder.sidedSuccess(itemstack, pLevel.isClientSide());
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext level, List<Component> list, TooltipFlag flag) {
		RolePlayHandler.addTooltips(list, GLLang.ITEM_FROZEN_FROG_OBTAIN.get(), GLLang.ITEM_FROZEN_FROG_USAGE.get());
	}

	@Override
	public Projectile asProjectile(Level level, Position pos, ItemStack stack, Direction direction) {
		return Util.make(new FrozenFrog(level, pos.x(), pos.y(), pos.z()), e -> e.setItem(stack));
	}

}