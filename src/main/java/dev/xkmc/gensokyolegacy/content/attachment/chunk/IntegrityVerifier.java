package dev.xkmc.gensokyolegacy.content.attachment.chunk;

import com.mojang.datafixers.util.Pair;
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

	public void tick() {
		var rand = level.getRandom();
		var pos = new BlockPos.MutableBlockPos();
		int count = 0;
		for (int i = 0; i < PerformanceConstants.VERIFY_SCAN; i++) {
			int step = rand.nextInt(bound.getSize());
			bound.resolve(pos, step);
			if (!level.isLoaded(pos))
				continue;
			int sid = template.raster()[step];
			boolean inRoom = roomBound.isInside(pos);
			if (sid == 0 && !inRoom)
				continue;
			count++;
			var state = level.getBlockState(pos);
			var pal = template.palette()[sid];
			if (state != pal) {
				process(step, state, pal, inRoom);
			}
			if (count >= PerformanceConstants.VERIFY_FETCH)
				return;
		}
	}

	private void process(int step, BlockState current, BlockState ref, boolean inRoom) {
		if (ref.isAir()) {
			abnormal.addAir(step);
			return;
		}
		boolean isPrimary = config.isPrimary(ref);
		boolean wouldFix = isPrimary || config.wouldFix(ref);
		if (inRoom && wouldFix || isPrimary)
			abnormal.addPrimary(step);
		else if (wouldFix || !current.isAir() && current.getBlock() != ref.getBlock())
			abnormal.addSecondary(step);
	}

	public List<Pair<BlockPos, BlockState>> popFix(int count, FixStage stage) {
		var pos = new BlockPos.MutableBlockPos();
		bound.resolve(pos, 0);
		if (!level.isLoaded(pos)) return List.of();
		bound.resolve(pos, bound.getSize() - 1);
		if (!level.isLoaded(pos)) return List.of();
		List<Pair<BlockPos, BlockState>> ans = new ArrayList<>();
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
				ans.add(Pair.of(pos.immutable(), state));
			}
		}
		return ans;
	}
}
