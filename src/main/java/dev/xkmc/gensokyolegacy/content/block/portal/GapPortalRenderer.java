//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package dev.xkmc.gensokyolegacy.content.block.portal;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GapPortalRenderer implements BlockEntityRenderer<GapPortalBlockEntity> {
    public static final ResourceLocation END_SKY_LOCATION = ResourceLocation.withDefaultNamespace("textures/environment/end_sky.png");
    public static final ResourceLocation END_PORTAL_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/end_portal.png");

    public GapPortalRenderer(BlockEntityRendererProvider.Context ctx) {
    }

    public void render(GapPortalBlockEntity e, float pTick, PoseStack pose, MultiBufferSource source, int light, int overlay) {

    }

    protected RenderType renderType() {
        return RenderType.endPortal();
    }
}
