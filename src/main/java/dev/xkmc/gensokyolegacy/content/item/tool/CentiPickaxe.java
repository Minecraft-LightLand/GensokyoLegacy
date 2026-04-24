package dev.xkmc.gensokyolegacy.content.item.tool;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class CentiPickaxe extends PickaxeItem {

	public CentiPickaxe(Properties p) {
		super(Tiers.GOLD, p);
	}

	public ItemStack getDefaultInstance(HolderLookup.Provider access) {
		ItemStack ans = super.getDefaultInstance();
		ans.enchant(access.holderOrThrow(Enchantments.EFFICIENCY), 5);
		ans.enchant(access.holderOrThrow(Enchantments.FORTUNE), 3);
		ans.enchant(access.holderOrThrow(Enchantments.MENDING), 1);
		return ans;
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
		if (entity instanceof LivingEntity le) {
			if (le.getMainHandItem() == stack || le.getOffhandItem() == stack) {
				if (le.tickCount % 20 == 4) {
					le.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 40, 1, true, false, true), le);
				}
			}
		}
	}

	@Override
	public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, @Nullable T entity, Consumer<Item> onBroken) {
		int dmg = stack.getDamageValue();
		int max = stack.getMaxDamage();
		int rem = max - dmg;
		if (rem <= amount && entity instanceof LivingEntity le) {
			var hp = le.getHealth();
			if (hp > amount) {
				le.setHealth(hp - amount);
				if (le.getHealth() == hp - amount) {
					return 0;
				}
				return amount - (int) (hp - le.getHealth());
			}
		}
		return super.damageItem(stack, amount, entity, onBroken);
	}

}
