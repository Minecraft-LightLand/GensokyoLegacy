package dev.xkmc.gensokyolegacy.content.attachment.dream;

import dev.xkmc.l2core.capability.attachment.GeneralCapabilityTemplate;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.core.SectionPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;

@SerialClass
public class DreamChunkAttachment extends GeneralCapabilityTemplate<LevelChunk, DreamChunkAttachment> {

	public static final int LIFE = 100;
	public static final int SCAN_RATE = 10;
	public static final int MAX_STABILITY = 4;
	public static final double[] PERMANENT = {0, 0.3, 0.6, 1, 1};

	@SerialField
	protected DreamSectionData[] sectionData;

	public void init(LevelChunk chunk) {
		var level = chunk.getLevel();
		if (level == null) return;
		int n = chunk.getSectionsCount();
		if (sectionData == null || sectionData.length != n) {
			sectionData = new DreamSectionData[n];
			for (int i = 0; i < n; i++) {
				sectionData[i] = new DreamSectionData();
			}
		}
	}

	@Override
	public void tick(LevelChunk chunk) {
		var level = chunk.getLevel();
		if (level == null) return;
		int n = chunk.getSectionsCount();
		for (int i = 0; i < n; i++) {
			var sec = chunk.getSection(i);
			var data = sectionData[i];
			var pos = SectionPos.of(chunk.getPos(), i + chunk.getMinSection());
			if (data.tick(level, pos, sec, level.getRandom())) {
				chunk.setUnsaved(true);
			}
		}
	}

	@SerialClass
	public static class DreamSectionData {

		@SerialField
		private int stability, life;
		@SerialField
		private long pattern;

		public boolean tick(Level level, SectionPos pos, LevelChunkSection sec, RandomSource rand) {
			if (stability == 0 && sec.hasOnlyAir()) return false;
			if (life > 0) life--;
			else if (stability > 0) {
				stability--;
				life = LIFE;
			}
			if (sec.hasOnlyAir()) return true;
			if (pattern == 0) pattern = rand.nextLong();
			if (stability == 0) {
				var state = Blocks.AIR.defaultBlockState();
				for (int x = 0; x < 16; x++) {
					for (int y = 0; y < 16; y++) {
						for (int z = 0; z < 16; z++) {
							var old = sec.getBlockState(x, y, z);
							if (old != state) {
								level.setBlock(pos.origin().offset(x, y, z), state, 50, 0);
							}
						}
					}
				}
			} else if (stability < MAX_STABILITY) {
				double stay = PERMANENT[stability];
				for (int t = 0; t < SCAN_RATE; t++) {
					int x = rand.nextInt(16);
					int y = rand.nextInt(16);
					int z = rand.nextInt(16);
					var state = sec.getBlockState(x, y, z);
					if (state.isAir()) continue;
					if (state.liquid()) continue;
					int key = x | y << 4 | z << 8;
					boolean permanent = RandomSource.create(RandomSource.create(pattern ^ key).nextLong()).nextDouble() < stay;
					if (permanent) continue;
					level.removeBlock(pos.origin().offset(x, y, z), false);
				}
			}
			return true;
		}

		public void update(int sval) {
			if (sval < stability) return;
			stability = sval;
			life = LIFE;
		}
	}

}
