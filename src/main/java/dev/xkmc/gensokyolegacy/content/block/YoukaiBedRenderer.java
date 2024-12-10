package dev.xkmc.gensokyolegacy.content.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BrightnessCombiner;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.BedBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;

public class YoukaiBedRenderer implements BlockEntityRenderer<YoukaiBedBlockEntity> {
	private final ModelPart headRoot;
	private final ModelPart footRoot;

	public YoukaiBedRenderer(BlockEntityRendererProvider.Context context) {
		this.headRoot = context.bakeLayer(ModelLayers.BED_HEAD);
		this.footRoot = context.bakeLayer(ModelLayers.BED_FOOT);
	}

	public void render(YoukaiBedBlockEntity be, float pTick, PoseStack pose, MultiBufferSource buffer, int light, int overlay) {
		Material material = new Material(Sheets.BED_SHEET, be.getTexture());
		Level level = be.getLevel();
		if (level != null) {
			BlockState state = be.getBlockState();
			DoubleBlockCombiner.NeighborCombineResult<? extends BedBlockEntity> neighbor = DoubleBlockCombiner.combineWithNeigbour(
					BlockEntityType.BED, BedBlock::getBlockType, BedBlock::getConnectedDirection, ChestBlock.FACING,
					state, level, be.getBlockPos(), (lv, pos) -> false
			);
			int combinedLight = neighbor.apply(new BrightnessCombiner<>()).get(light);
			this.renderPiece(pose, buffer,
					state.getValue(BedBlock.PART) == BedPart.HEAD ? this.headRoot : this.footRoot,
					state.getValue(BedBlock.FACING), material, combinedLight, overlay, false
			);
		} else {
			this.renderPiece(pose, buffer, this.headRoot, Direction.SOUTH, material, light, overlay, false);
			this.renderPiece(pose, buffer, this.footRoot, Direction.SOUTH, material, light, overlay, true);
		}
	}

	private void renderPiece(PoseStack pose, MultiBufferSource buffer, ModelPart part, Direction direction, Material material,
							 int light, int overlay, boolean foot) {
		pose.pushPose();
		pose.translate(0.0F, 0.5625F, foot ? -1.0F : 0.0F);
		pose.mulPose(Axis.XP.rotationDegrees(90.0F));
		pose.translate(0.5F, 0.5F, 0.5F);
		pose.mulPose(Axis.ZP.rotationDegrees(180.0F + direction.toYRot()));
		pose.translate(-0.5F, -0.5F, -0.5F);
		VertexConsumer vertexconsumer = material.buffer(buffer, RenderType::entitySolid);
		part.render(pose, vertexconsumer, light, overlay);
		pose.popPose();
	}

}
