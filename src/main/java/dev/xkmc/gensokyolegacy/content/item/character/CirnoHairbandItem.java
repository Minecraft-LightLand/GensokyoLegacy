package dev.xkmc.gensokyolegacy.content.item.character;

import dev.xkmc.gensokyolegacy.content.attachment.role.RolePlayHandler;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.gensokyolegacy.init.data.GLLang;
import dev.xkmc.gensokyolegacy.init.registrate.GLRoles;
import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CirnoHairbandItem extends TouhouHatItem {

	public CirnoHairbandItem(Properties properties) {
		super(properties, TouhouMat.CIRNO_HAIRBAND);
	}

	@Override
	public @Nullable ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel) {
		return GensokyoLegacy.loc("textures/entity/cirno.png");
	}

	@Override
	protected void tick(ItemStack stack, Level level, Player player) {
		GLRoles.FAIRY.get().advance(player, 0.5, 1);
	}

	@Override
	public void onHurtTarget(ItemStack head, DamageSource source, LivingEntity target) {
		if (source.is(Tags.DamageTypes.IS_MAGIC)) {
			target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 2));
			if (target.canFreeze()) {
				target.setTicksFrozen(target.getTicksFrozen() + 120);
			}
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext level, List<Component> list, TooltipFlag flag) {
		RolePlayHandler.addTooltips(list, GLLang.USAGE_CIRNO_HAIRBAND.get(GLRoles.FAIRY.get().getName()), null);
	}

	@Override
	public boolean support(DyeColor color) {
		return color == DyeColor.LIGHT_BLUE;
	}

}
