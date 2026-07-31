package dev.xkmc.gensokyolegacy.compat.jei;

import dev.xkmc.gensokyolegacy.content.ui.dialog.FirstDialogScreen;
import dev.xkmc.gensokyolegacy.content.ui.dialog.SimpleDialogScreen;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class GLJEIPlugin implements IModPlugin {

	public static final ResourceLocation ID = GensokyoLegacy.loc("main");

	@Override
	public ResourceLocation getPluginUid() {
		return ID;
	}

	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registration) {
		registration.addGuiScreenHandler(FirstDialogScreen.class, e -> null);
		registration.addGuiScreenHandler(SimpleDialogScreen.class, e -> null);
	}

}
