package dev.xkmc.gensokyolegacy.content.item.character;

import dev.xkmc.gensokyolegacy.content.attachment.role.RolePlayHandler;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.gensokyolegacy.init.registrate.GLRoles;
import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RumiaHairbandItem extends TouhouHatItem {

	public RumiaHairbandItem(Properties properties) {
		super(properties, TouhouMat.RUMIA_HAIRBAND);
	}

	@Override
	protected void addModifiers(ItemAttributeModifiers.Builder builder) {
		builder.add(Attributes.ATTACK_DAMAGE, new AttributeModifier(GensokyoLegacy.loc("rumia_hairband"), 0.25,
				AttributeModifier.Operation.ADD_MULTIPLIED_BASE), EquipmentSlotGroup.HEAD);
	}

	@Override
	public @Nullable ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel) {
		return GensokyoLegacy.loc("textures/entity/rumia.png");
	}

	@Override
	protected void tick(ItemStack stack, Level level, Player player) {
		GLRoles.RUMIA.get().advance(player, 0.5, 1);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext level, List<Component> list, TooltipFlag flag) {
		RolePlayHandler.addTooltips(list, YHLangData.OBTAIN_RUMIA_HAIRBAND.get(),
				YHLangData.USAGE_RUMIA_HAIRBAND.get(GLRoles.RUMIA.get().getName()));
	}

}