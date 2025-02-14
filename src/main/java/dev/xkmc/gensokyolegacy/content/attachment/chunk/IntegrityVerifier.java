package dev.xkmc.gensokyolegacy.content.attachment.chunk;

import dev.xkmc.gensokyolegacy.content.attachment.datamap.StructureConfig;
import dev.xkmc.gensokyolegacy.content.block.bed.YoukaiBedBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

import java.util.ArrayList;
import java.util.List;

public class IntegrityVerifier {

	private final ServerLevel level;
	private final StructureBound bound;
	private final StructureCache template;
	private final StructureConfig config;
	private final BoundingBox roomBound;
	private final AbnormalCache abnormal;

	public IntegrityVerifier(HomeHolder holder, BoundingBox house, BoundingBox room, StructureCache template, AbnormalCache set) {
		this.level = holder.level();
		this.config = holder.config();
		this.roomBound = room;
		this.bound = new StructureBound(house);
		this.template = template;
		this.abnormal = set;
	}

	public boolean isValid() {
		return bound.getSize() == template.raster().length;
	}

	public boolean tick() {
		var rand = level.getRandom();
		var pos = new BlockPos.MutableBlockPos();
		int count = 0;
		boolean detected = false;
		for (int i = 0; i < PerformanceConstants.VERIFY_SCAN; i++) {
			int step;
			if (i % 2 == 0) {
				step = rand.nextInt(bound.getSize());
				bound.resolve(pos, step);
			} else {
				pos.set(
						roomBound.minX() + rand.nextInt(roomBound.getXSpan()),
						roomBound.minY() + rand.nextInt(roomBound.getYSpan()),
						roomBound.minZ() + rand.nextInt(roomBound.getZSpan())
				);
				step = bound.compute(pos);
			}
			if (!level.isLoaded(pos))
				continue;
			int sid = template.raster()[step];
			boolean inRoom = roomBound.isInside(pos);
			count++;
			var state = level.getBlockState(pos);
			var pal = template.palette()[sid];
			if (state.getBlock() != pal.getBlock()) {
				process(pos, step, state, pal, inRoom);
				detected = true;
			}
			if (count >= PerformanceConstants.VERIFY_FETCH)
				break;
		}
		return detected;
	}

	private void process(BlockPos pos, int step, BlockState current, BlockState ref, boolean inRoom) {
		if (inRoom) {
			boolean curCollide = current.getCollisionShape(level, pos).isEmpty();
			boolean refCollide = ref.getCollisionShape(level, pos).isEmpty();
			if (!refCollide && curCollide) {
				abnormal.addAir(step);
				return;
			}
		}
		boolean isPrimary = config.isPrimary(ref);
		boolean wouldFix = isPrimary || config.wouldFix(ref);
		if (inRoom && wouldFix || isPrimary)
			abnormal.addPrimary(step);
		else if (wouldFix)
			abnormal.addSecondary(step);
		else if (current.getBlock() != ref.getBlock()) {
			if (!ref.blocksMotion() && current.blocksMotion()) {
				abnormal.addSecondary(step);
			}
		}
	}

	public List<BlockFix> popFix(int count, FixStage stage) {
		var pos = new BlockPos.MutableBlockPos();
		bound.resolve(pos, 0);
		if (!level.isLoaded(pos)) return List.of();
		bound.resolve(pos, bound.getSize() - 1);
		if (!level.isLoaded(pos)) return List.of();
		List<BlockFix> ans = new ArrayList<>();
		int step = 0;
		while (step < count) {
			int[] fetch = abnormal.pop(count - step, stage);
			if (fetch == null || fetch.length == 0) break;
			step += fetch.length;
			for (var e : fetch) {
				bound.resolve(pos, e);
				var state = template.stateAt(e);
				if (state.getBlock() instanceof YoukaiBedBlock) continue;
				if (level.getBlockState(pos).getBlock() instanceof YoukaiBedBlock) continue;
				if (!state.isAir() && !config.isPrimary(state) && !config.wouldFix(state))
					state = Blocks.AIR.defaultBlockState();
				ans.add(new BlockFix(pos.immutable(), state));
			}
		}
		return ans;
	}
}
