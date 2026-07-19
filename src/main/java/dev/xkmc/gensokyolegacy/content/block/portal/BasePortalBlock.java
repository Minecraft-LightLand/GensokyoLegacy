//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package dev.xkmc.gensokyolegacy.content.block.portal;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Portal;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.portal.DimensionTransition;
import org.jetbrains.annotations.Nullable;

public abstract class BasePortalBlock extends BaseEntityBlock implements Portal {

	public BasePortalBlock(Properties properties) {
		super(properties);
	}

	protected void entityInside(BlockState state, Level level, BlockPos pos, Entity e) {
		if (e.canUsePortal(false)) {
			e.setAsInsidePortal(this, pos);
		}
	}

	public @Nullable DimensionTransition getPortalDestination(ServerLevel level, Entity e, BlockPos pos) {
		if (level.getBlockEntity(pos) instanceof IPortalBlockEntity be) {
			return be.getPortalDestination(level, e, pos);
		}
		return null;
	}

	@Override
	protected RenderShape getRenderShape(BlockState p_49232_) {
		return RenderShape.ENTITYBLOCK_ANIMATED;
	}

	protected boolean canBeReplaced(BlockState state, Fluid fluid) {
		return false;
	}

}
