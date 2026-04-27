package dev.xkmc.gensokyolegacy.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.xkmc.gensokyolegacy.event.GLClientEventHandlers;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GuiGraphics.class)
public class GuiGraphicsMixin {

	@WrapOperation(method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V", at = @At(value = "INVOKE", target = "Ljava/lang/String;valueOf(I)Ljava/lang/String;"))
	public String gensokyolegacy$bakaEffect$number(int count, Operation<String> original) {
		return original.call(GLClientEventHandlers.onStackDeco(count));
	}

	@WrapOperation(method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Ljava/lang/String;IIIZ)I"))
	public int gensokyolegacy$bakaEffect$color(GuiGraphics instance, Font font, String text, int x, int y, int color, boolean dropShadow, Operation<Integer> original) {
		return original.call(instance, font, text, x, y, GLClientEventHandlers.onDecoColor(text, color), dropShadow);
	}


}
