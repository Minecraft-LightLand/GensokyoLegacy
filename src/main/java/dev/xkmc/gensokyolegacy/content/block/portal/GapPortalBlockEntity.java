package dev.xkmc.gensokyolegacy.content.block.portal;

import dev.xkmc.gensokyolegacy.content.attachment.gap.GapMapping;
import dev.xkmc.gensokyolegacy.content.attachment.gap.GapMappingData;
import dev.xkmc.gensokyolegacy.content.dimension.GLDimensionGen;
import dev.xkmc.gensokyolegacy.init.registrate.GLBlocks;
import dev.xkmc.gensokyolegacy.init.registrate.GLItems;
import dev.xkmc.l2core.base.tile.BaseBlockEntity;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@SerialClass
public class GapPortalBlockEntity extends BaseBlockEntity implements IPortalBlockEntity {

	public static boolean isInGap(Level level) {
		return level.dimension().location().equals(GLDimensionGen.GAP.location());
	}

	@SerialField
	@Nullable
	public UUID id;

	public GapPortalBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public @Nullable DimensionTransition getPortalDestination(ServerLevel level, Entity e, BlockPos pos) {
		if (getBlockState().getValue(BlockStateProperties.HALF) == Half.TOP) {
			if (level.getBlockEntity(pos.below()) instanceof GapPortalBlockEntity be) {
				return be.getPortalDestination(level, e, pos.below());
			}
		}
		if (id == null) return null;
		var data = GapMappingData.get(level).get(id);
		if (data == null) return null;
		boolean inGap = isInGap(level);
		var targetPos = inGap ? data.worldPos() : data.gapPos();
		ResourceKey<Level> dim = inGap ? ResourceKey.create(Registries.DIMENSION, data.dimension()) : GLDimensionGen.GAP;
		ServerLevel targetLevel = level.getServer().getLevel(dim);
		if (targetLevel == null) return null;
		if (!inGap) {
			createEndPlatform(targetLevel, targetPos, id);
		}
		Vec3 vec3 = targetPos.getBottomCenter();
		return new DimensionTransition(targetLevel, vec3, e.getDeltaMovement(), e.getYRot(), e.getXRot(),
				DimensionTransition.PLAY_PORTAL_SOUND.then(DimensionTransition.PLACE_PORTAL_TICKET));
	}

	public void setPlacedBy(ItemStack stack) {
		if (!(level instanceof ServerLevel sl)) return;
		if (!stack.has(GLItems.DC_UUID)) {
			var id = UUID.randomUUID();
			stack.set(GLItems.DC_UUID, id);
		}
		id = stack.get(GLItems.DC_UUID);
		initData();
	}

	public void initData() {
		if (!(level instanceof ServerLevel sl)) return;
		if (id == null) return;
		var data = GapMappingData.get(sl);
		var prev = data.get(id);
		var dimId = sl.dimension().location();
		if (isInGap(sl)) {
			if (prev == null) {
				var dim = sl.getServer().overworld();
				var pos = dim.getSharedSpawnPos();
				data.set(id, new GapMapping(pos, getBlockPos(), dim.dimension().location()));
			} else if (!prev.gapPos().equals(getBlockPos())) {
				if (sl.isLoaded(prev.gapPos())) {
					sl.destroyBlock(prev.gapPos(), false);
				}
				data.set(id, prev.updateGap(sl, getBlockPos()));
			}
		} else {
			if (prev == null) {
				int minY = sl.getMinBuildHeight();
				int maxY = sl.getMaxBuildHeight();
				var pos = getBlockPos();
				var gapDim = sl.getServer().getLevel(GLDimensionGen.GAP);
				var gy0 = gapDim != null ? gapDim.getMinBuildHeight() : 0;
				var gy1 = gapDim != null ? gapDim.getMaxBuildHeight() : 256;
				int gy = (int) (1d * (pos.getY() - minY) / (maxY - minY) * (gy1 - gy0)) + gy0;
				data.set(id, new GapMapping(getBlockPos(), new BlockPos(pos.getX(), gy, pos.getZ()), dimId));
			} else if (!prev.worldPos().equals(getBlockPos()) || !prev.dimension().equals(dimId)) {
				var other = sl.getServer().getLevel(ResourceKey.create(Registries.DIMENSION, prev.dimension()));
				if (other != null && other.isLoaded(prev.gapPos())) {
					other.destroyBlock(prev.worldPos(), false);
				}
				data.set(id, prev.updateWorld(sl, getBlockPos()));
			}
		}
	}

	@Override
	public void onLoad() {
		super.onLoad();
		if (!(level instanceof ServerLevel sl)) return;
		if (id == null) return;
		var data = GapMappingData.get(sl);
		var prev = data.get(id);
		if (prev == null) return;
		var dimId = sl.dimension().location();
		if (isInGap(sl)) {
			if (!prev.gapPos().equals(getBlockPos())) {
				sl.destroyBlock(getBlockPos(), false);
			}
		} else {
			if (!prev.worldPos().equals(getBlockPos()) || !prev.dimension().equals(dimId)) {
				sl.destroyBlock(getBlockPos(), false);
			}
		}
	}

	public static void createEndPlatform(ServerLevelAccessor sl, BlockPos pos, UUID id) {
		if (sl.getBlockEntity(pos) instanceof GapPortalBlockEntity) return;
		BlockPos.MutableBlockPos m = pos.mutable();
		for (int i = -2; i <= 2; ++i) {
			for (int j = -2; j <= 2; ++j) {
				BlockPos blockpos = m.set(pos).move(j, -1, i);
				Block block = Math.abs(i) <= 1 && Math.abs(j) <= 1 ? Blocks.CRYING_OBSIDIAN : Blocks.OBSIDIAN;
				if (sl.getBlockState(blockpos).isAir()) {
					sl.setBlock(blockpos, block.defaultBlockState(), 3);
				}
			}
		}
		sl.setBlock(pos, GLBlocks.GAP_PORTAL.getDefaultState(), 3);
		sl.setBlock(pos.above(), GLBlocks.GAP_PORTAL.getDefaultState().setValue(BlockStateProperties.HALF, Half.TOP), 3);
		if (sl.getBlockEntity(pos) instanceof GapPortalBlockEntity be) {
			be.id = id;
			be.initData();
		}
	}

	public ItemStack getItem() {
		var ans = GLBlocks.GAP_PORTAL.asStack();
		if (id != null) {
			ans.set(GLItems.DC_UUID, id);
		}
		return ans;
	}

}
