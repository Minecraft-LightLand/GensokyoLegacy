package dev.xkmc.gensokyolegacy.content.item.character;

import dev.xkmc.gensokyolegacy.content.attachment.role.RolePlayHandler;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.gensokyolegacy.init.data.GLLang;
import dev.xkmc.gensokyolegacy.init.data.GLModConfig;
import dev.xkmc.gensokyolegacy.init.registrate.GLEffects;
import dev.xkmc.l2core.base.effects.EffectUtil;
import dev.xkmc.l2damagetracker.init.L2DamageTracker;
import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SuwakoHatItem extends TouhouHatItem {

	public SuwakoHatItem(Item.Properties properties) {
		super(properties, TouhouMat.SUWAKO_HAT);
	}

	@Override
	protected void addModifiers(ItemAttributeModifiers.Builder builder) {
		builder.add(L2DamageTracker.MAGIC_FACTOR, new AttributeModifier(GensokyoLegacy.loc("suwako_hat"), 0.25,
				AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.HEAD);
	}

	@Override
	public @Nullable ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel) {
		return GensokyoLegacy.loc("textures/model/suwako_hat.png");
	}

	@Override
	protected void tick(ItemStack stack, Level level, Player player) {
		EffectUtil.refreshEffect(player, new MobEffectInstance(GLEffects.NATIVE, 40, 0,
				true, true), player);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext level, List<Component> list, TooltipFlag flag) {
		RolePlayHandler.addTooltips(list,
				GLLang.OBTAIN_SUWAKO_HAT.get(Component.literal("" + GLModConfig.SERVER.frogEatCountForHat.get())),
				GLLang.USAGE_SUWAKO_HAT.get(Component.translatable(GLEffects.NATIVE.get().getDescriptionId()))
		);
	}

	@Override
	public boolean support(DyeColor color) {
		return color == DyeColor.CYAN || color == DyeColor.LIME;
	}


}
