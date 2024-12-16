package dev.xkmc.gensokyolegacy.content.client.structure;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.OptionalDouble;

public class StructureOutlineRenderer {

	public static class LineRenderType extends RenderType {

		public LineRenderType(String p_173178_, VertexFormat p_173179_, VertexFormat.Mode p_173180_, int p_173181_, boolean p_173182_, boolean p_173183_, Runnable p_173184_, Runnable p_173185_) {
			super(p_173178_, p_173179_, p_173180_, p_173181_, p_173182_, p_173183_, p_173184_, p_173185_);
		}

		public static RenderType OUTLINE = create("highlight_lines",
				DefaultVertexFormat.POSITION_COLOR_NORMAL, VertexFormat.Mode.LINES, 256, false, false,
				RenderType.CompositeState.builder()
						.setShaderState(RENDERTYPE_LINES_SHADER)
						.setLineState(new RenderStateShard.LineStateShard(OptionalDouble.empty()))
						.setLayeringState(VIEW_OFFSET_Z_LAYERING)
						.setTransparencyState(TRANSLUCENT_TRANSPARENCY)
						.setOutputState(ITEM_ENTITY_TARGET)
						.setWriteMaskState(COLOR_DEPTH_WRITE)
						.setCullState(NO_CULL)
						.setDepthTestState(RenderStateShard.NO_DEPTH_TEST)
						.createCompositeState(false)
		);

	}

	public static void renderOutline(PoseStack pose, Vec3 camera) {
		Level level = Minecraft.getInstance().level;
		Player player = Minecraft.getInstance().player;
		var data = StructureInfoClientManager.fetch();
		if (data == null || level == null || player == null) return;
		var buffer = Minecraft.getInstance().renderBuffers().bufferSource();
		var vc = buffer.getBuffer(RenderType.lines());
		renderBox(pose, vc, data.structure(), camera.toVector3f(), 1, 1, 1, 1);
		renderBox(pose, vc, data.house(), camera.toVector3f(), 0.5f, 1, 1, 1);
		vc = buffer.getBuffer(LineRenderType.OUTLINE);
		renderBox(pose, vc, data.room(), camera.toVector3f(), 1, 0.5f, 0.5f, 1);
	}

	private static void renderBox(
			PoseStack pose, VertexConsumer vc, StructureBoundUpdateToClient.Box box,
			Vector3f pos, float r, float g, float b, float a
	) {
		renderCube(pose, vc, box.x0(), box.y0(), box.z0(), box.x1(), box.y1(), box.z1(), -pos.x, -pos.y, -pos.z, r, g, b, a);
	}

	public static void renderCube(
			PoseStack pose, VertexConsumer vc,
			float x0, float y0, float z0,
			float x1, float y1, float z1,
			float dx, float dy, float dz,
			float r, float g, float b, float a) {
		renderShape(pose, vc, x0, y0, z0, x1, y0, z0, dx, dy, dz, r, g, b, a);
		renderShape(pose, vc, x0, y0, z0, x0, y1, z0, dx, dy, dz, r, g, b, a);
		renderShape(pose, vc, x0, y0, z0, x0, y0, z1, dx, dy, dz, r, g, b, a);
		renderShape(pose, vc, x1, y0, z0, x1, y1, z0, dx, dy, dz, r, g, b, a);
		renderShape(pose, vc, x1, y0, z0, x1, y0, z1, dx, dy, dz, r, g, b, a);
		renderShape(pose, vc, x0, y1, z0, x1, y1, z0, dx, dy, dz, r, g, b, a);
		renderShape(pose, vc, x0, y1, z0, x0, y1, z1, dx, dy, dz, r, g, b, a);
		renderShape(pose, vc, x0, y0, z1, x1, y0, z1, dx, dy, dz, r, g, b, a);
		renderShape(pose, vc, x0, y0, z1, x0, y1, z1, dx, dy, dz, r, g, b, a);
		renderShape(pose, vc, x1, y1, z0, x1, y1, z1, dx, dy, dz, r, g, b, a);
		renderShape(pose, vc, x1, y0, z1, x1, y1, z1, dx, dy, dz, r, g, b, a);
		renderShape(pose, vc, x0, y1, z1, x1, y1, z1, dx, dy, dz, r, g, b, a);
	}

	private static void renderShape(
			PoseStack pose, VertexConsumer vc,
			float x0, float y0, float z0,
			float x1, float y1, float z1,
			float dx, float dy, float dz,
			float r, float g, float b, float a
	) {
		PoseStack.Pose mat = pose.last();
		float rx = x1 - x0;
		float ry = y1 - y0;
		float rz = z1 - z0;
		float len = Mth.sqrt(rx * rx + ry * ry + rz * rz);
		rx /= len;
		ry /= len;
		rz /= len;
		vc.addVertex(mat, x0 + dx, y0 + dy, z0 + dz).setColor(r, g, b, a).setNormal(mat, rx, ry, rz);
		vc.addVertex(mat, x1 + dx, y1 + dy, z1 + dz).setColor(r, g, b, a).setNormal(mat, rx, ry, rz);
	}


}
