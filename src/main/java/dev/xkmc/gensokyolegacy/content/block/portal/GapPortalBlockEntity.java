package dev.xkmc.gensokyolegacy.content.block.portal;

import dev.xkmc.l2core.base.tile.BaseBlockEntity;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

@SerialClass
public class GapPortalBlockEntity extends BaseBlockEntity implements IPortalBlockEntity {

	@SerialField
	public BlockPos target;

	public GapPortalBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public @Nullable DimensionTransition getPortalDestination(ServerLevel level, Entity e, BlockPos pos) {
		ResourceKey<Level> dim = level.dimension() == Level.END ? Level.OVERWORLD : Level.END;
		ServerLevel targetLevel = level.getServer().getLevel(dim);
		if (targetLevel == null) return null;
		BlockPos targetPos = targetLevel.getSharedSpawnPos();
		Vec3 vec3 = targetPos.getBottomCenter();
		return new DimensionTransition(targetLevel, vec3, e.getDeltaMovement(), e.getYRot(), e.getXRot(),
				DimensionTransition.PLAY_PORTAL_SOUND.then(DimensionTransition.PLACE_PORTAL_TICKET));
	}

}
