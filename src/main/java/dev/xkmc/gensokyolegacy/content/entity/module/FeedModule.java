package dev.xkmc.gensokyolegacy.content.entity.module;

import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiFlags;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;

@SerialClass
public class FeedModule extends IYoukaiModules {

	private static final ResourceLocation ID = GensokyoLegacy.loc("feed");

	@SerialField
	private int feedCoolDown;

	public FeedModule(YoukaiEntity self) {
		super(ID, self);
	}

	public int getFavor(ItemStack food, FoodProperties prop) {
		return 10;
	}

	public void onFeed(ItemStack food, FoodProperties prop, Player player, int favor) {
		var data = self.getData(player);
		if (data.isEmpty()) return;
		int heart = data.get().feed(food, favor);
		if (heart > 0) {
			self.level().broadcastEntityEvent(self, EntityEvent.IN_LOVE_HEARTS);
		}
	}

	@Override
	public InteractionResult interact(Player player, InteractionHand hand, YoukaiEntity e) {
		ItemStack stack = player.getItemInHand(hand);
		var food = stack.getFoodProperties(e);
		if (food == null) return InteractionResult.PASS;
		if (feedCoolDown > 0) return InteractionResult.PASS;
		int favor = getFavor(stack, food);
		if (favor < 0) return InteractionResult.PASS;
		onFeed(stack, food, player, favor);
		ItemStack remain = stack.getCraftingRemainingItem();
		stack.shrink(1);
		feedCoolDown += food.nutrition() * 100;
		if (stack.getUseAnimation() == UseAnim.DRINK)
			e.playSound(stack.getDrinkingSound());
		else e.playSound(stack.getEatingSound());
		if (!remain.isEmpty())
			player.getInventory().placeItemBackInInventory(remain);
		return InteractionResult.SUCCESS;
	}

	@Override
	public void tickServer(YoukaiEntity e) {
		if (feedCoolDown > 0) feedCoolDown--;
		e.setFlag(YoukaiFlags.FED, feedCoolDown > 0);
	}

	@Override
	public void tickClient(YoukaiEntity e) {
		if (!e.getFlag(YoukaiFlags.FED)) return;
		int chance = e.isInvisible() ? 15 : 2;
		if (e.getRandom().nextInt(chance) != 0) return;
		e.level().addParticle(
				ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, MobEffects.SATURATION.value().getColor()),
				e.getRandomX(0.5D), e.getRandomY(), e.getRandomZ(0.5D), 0, 0, 0
		);
	}

}
