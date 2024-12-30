package dev.xkmc.gensokyolegacy.content.item.character;

import dev.xkmc.gensokyolegacy.content.attachment.role.RolePlayHandler;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.gensokyolegacy.init.registrate.GLCriteriaTriggers;
import dev.xkmc.gensokyolegacy.init.registrate.GLMeta;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class StrawHatItem extends TouhouHatItem {

	public StrawHatItem(Item.Properties properties) {
		super(properties, TouhouMat.STRAW_HAT);
	}

	@Override
	public @Nullable ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel) {
		return GensokyoLegacy.loc("textures/model/straw_hat.png");
	}

	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
		if (!(target instanceof Frog frog))
			return InteractionResult.PASS;
		if (!RolePlayHandler.startTransition(player))
			return InteractionResult.FAIL;
		if (!GLMeta.FROG_GOD.type().isProper(frog))
			return InteractionResult.FAIL;
		var cap = GLMeta.FROG_GOD.type().getOrCreate(frog);
		if (cap.hasHat) {
			return InteractionResult.FAIL;
		} else {
			if (player instanceof ServerPlayer sp) {
				cap.hasHat = true;
				cap.syncToClient(frog);
				GLCriteriaTriggers.SUWAKO_WEAR.get().trigger(sp);
			}
			return InteractionResult.SUCCESS;
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext level, List<Component> list, TooltipFlag flag) {
		RolePlayHandler.addTooltips(list, null, YHLangData.USAGE_STRAW_HAT.get(RolePlayHandler.tooltipStart()));
	}

}
