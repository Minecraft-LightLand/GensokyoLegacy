package dev.xkmc.gensokyolegacy.content.item.character;

import dev.xkmc.danmakuapi.api.IDanmakuEntity;
import dev.xkmc.danmakuapi.init.data.DanmakuDamageTypes;
import dev.xkmc.gensokyolegacy.content.attachment.role.RolePlayHandler;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.gensokyolegacy.init.data.GLLang;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ReimuHairbandItem extends TouhouHatItem {

	public ReimuHairbandItem(Properties properties) {
		super(properties, TouhouMat.REIMU_HAIRBAND);
	}

	@Override
	public @Nullable ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel) {
		return GensokyoLegacy.loc("textures/entity/reimu.png");
	}

	@Override
	public DamageSource modifyDamageType(ItemStack stack, LivingEntity le, IDanmakuEntity danmaku, DamageSource type) {
		return DanmakuDamageTypes.abyssal(danmaku);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext level, List<Component> list, TooltipFlag flag) {
		RolePlayHandler.addTooltips(list, GLLang.OBTAIN_REIMU_HAIRBAND.get(), GLLang.USAGE_REIMU_HAIRBAND.get());
	}

}
