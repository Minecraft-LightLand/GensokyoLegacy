package dev.xkmc.gensokyolegacy.content.block.bed;

import com.google.common.base.Suppliers;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.gensokyolegacy.init.registrate.GLBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Supplier;

public class GLBEWLR extends BlockEntityWithoutLevelRenderer {

	public static final Supplier<BlockEntityWithoutLevelRenderer> INSTANCE = Suppliers.memoize(() ->
			new GLBEWLR(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels()));

	public static final IClientItemExtensions EXTENSIONS = new IClientItemExtensions() {

		@Override
		public BlockEntityWithoutLevelRenderer getCustomRenderer() {
			return INSTANCE.get();
		}

	};

	private final EntityModelSet entityModelSet;
	private final BlockEntityRenderDispatcher beRenderer;

	private final YoukaiBedBlockEntity bed = new YoukaiBedBlockEntity(GLBlocks.BE_BED.get(), BlockPos.ZERO, GLBlocks.Beds.CIRNO.get().defaultBlockState());

	public GLBEWLR(BlockEntityRenderDispatcher dispatcher, EntityModelSet set) {
		super(dispatcher, set);
		beRenderer = dispatcher;
		entityModelSet = set;
	}

	public void onResourceManagerReload(ResourceManager manager) {
	}

	@Override
	public void renderByItem(ItemStack stack, ItemDisplayContext type, PoseStack pose,
							 MultiBufferSource buffer, int light, int overlay) {
		if (stack.getItem() instanceof BlockItem item && item.getBlock() instanceof YoukaiBedBlock block) {
			bed.setBlockState(block.defaultBlockState());
			this.beRenderer.renderItem(bed, pose, buffer, light, overlay);
		}
	}

}
