//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package dev.xkmc.gensokyolegacy.content.block.portal;

import dev.xkmc.l2modularblock.core.DelegateEntityBlockImpl;
import dev.xkmc.l2modularblock.one.EntityInsideBlockMethod;
import dev.xkmc.l2modularblock.one.RenderShapeBlockMethod;
import dev.xkmc.l2modularblock.type.BlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Portal;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.portal.DimensionTransition;
import org.jetbrains.annotations.Nullable;

public class BasePortalBlock extends DelegateEntityBlockImpl implements Portal {

	public record PortalMethod() implements EntityInsideBlockMethod, RenderShapeBlockMethod {

		public void entityInside(BlockState state, Level level, BlockPos pos, Entity e) {
			if (e.canUsePortal(false) && state.getBlock() instanceof Portal portal) {
				e.setAsInsidePortal(portal, pos);
			}
		}

		@Override
		public RenderShape getRenderShape(BlockState p_49232_) {
			return RenderShape.ENTITYBLOCK_ANIMATED;
		}

	}

	protected BasePortalBlock(Properties p, BlockMethod... impl) {
		super(p, impl);
	}

	public @Nullable DimensionTransition getPortalDestination(ServerLevel level, Entity e, BlockPos pos) {
		if (level.getBlockEntity(pos) instanceof IPortalBlockEntity be) {
			return be.getPortalDestination(level, e, pos);
		}
		return null;
	}

	protected boolean canBeReplaced(BlockState state, Fluid fluid) {
		return false;
	}

}
