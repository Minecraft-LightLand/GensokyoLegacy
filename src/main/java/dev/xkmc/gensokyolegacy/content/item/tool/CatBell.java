package dev.xkmc.gensokyolegacy.content.item.tool;

import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.l2serial.network.SerialPacketBase;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CatBell extends Item {

	public CatBell(Properties properties) {
		super(properties.durability(16));
	}

	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity le, InteractionHand usedHand) {
		if (le instanceof Cat cat) {
			if (cat.getOwner() != player) return InteractionResult.FAIL;
			if (player instanceof ServerPlayer sp) {
				cat.addTag("CatBell");
				cat.startRiding(player, true);
				GensokyoLegacy.HANDLER.toClientPlayer(new MountToClient(cat.getId(), player.getId(), true), sp);
			}
			return InteractionResult.SUCCESS;
		}
		return super.interactLivingEntity(stack, player, le, usedHand);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (player.hasPassenger(e -> e instanceof Cat))
			return InteractionResultHolder.fail(stack);
		if (player instanceof ServerPlayer sp) {
			var cat = new Cat(EntityType.CAT, sp.serverLevel());
			cat.finalizeSpawn(sp.serverLevel(), sp.serverLevel().getCurrentDifficultyAt(sp.blockPosition()),
					MobSpawnType.MOB_SUMMONED, null);
			cat.moveTo(sp.position());
			cat.addTag("CatBell");
			cat.setTame(true, true);
			cat.setOwnerUUID(sp.getUUID());
			cat.startRiding(player, true);
			sp.serverLevel().addFreshEntity(cat);
			if (!player.isCreative()) {
				stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
			}
			player.getCooldowns().addCooldown(this, 100);
		}
		return InteractionResultHolder.success(stack);
	}


	public record MountToClient(
			int catId, int playerId, boolean ride
	) implements SerialPacketBase<MountToClient> {

		@Override
		public void handle(Player player) {
			var cat = player.level().getEntity(catId);
			var pl = player.level().getEntity(playerId);
			if (cat != null && pl != null) {
				if (ride) {
					cat.startRiding(pl);
				} else {
					cat.unRide();
				}
			}
		}

	}

}
