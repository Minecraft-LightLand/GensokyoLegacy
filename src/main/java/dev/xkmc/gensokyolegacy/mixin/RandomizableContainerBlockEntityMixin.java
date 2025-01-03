package dev.xkmc.gensokyolegacy.mixin;

import dev.xkmc.gensokyolegacy.event.KoishiEventHandlers;
import net.minecraft.world.RandomizableContainer;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RandomizableContainer.class)
public interface RandomizableContainerBlockEntityMixin {

	@Inject(at = @At("HEAD"), method = "unpackLootTable")
	default void gensokyolegacy$trayLoad(Player player, CallbackInfo ci) {
		if (player != null) {
			KoishiEventHandlers.removeKoishi(player);
		}
	}

}
