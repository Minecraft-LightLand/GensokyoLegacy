package dev.xkmc.gensokyolegacy.init.data.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.gensokyolegacy.init.registrate.GLWorldGen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity.RuleBlockEntityModifier;

import javax.annotation.Nullable;
import java.util.Map;

public class SetDataProcessor extends StructureProcessor {

	public static final MapCodec<SetDataProcessor> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.unboundedMap(BuiltInRegistries.BLOCK.byNameCodec(), RuleBlockEntityModifier.CODEC).fieldOf("map").forGetter(e -> e.map)
	).apply(i, SetDataProcessor::new));

	private final Map<Block, RuleBlockEntityModifier> map;

	public SetDataProcessor(Map<Block, RuleBlockEntityModifier> map) {
		this.map = map;
	}

	@Nullable
	@Override
	public StructureTemplate.StructureBlockInfo processBlock(
			LevelReader level,
			BlockPos offset,
			BlockPos pos,
			StructureTemplate.StructureBlockInfo blockInfo,
			StructureTemplate.StructureBlockInfo rel,
			StructurePlaceSettings settings
	) {
		RandomSource rand = RandomSource.create(Mth.getSeed(rel.pos()));
		var rule = map.get(rel.state().getBlock());
		if (rule != null) {
			return new StructureTemplate.StructureBlockInfo(rel.pos(), rel.state(), rule.apply(rand, rel.nbt()));
		}
		return rel;
	}

	@Override
	protected StructureProcessorType<?> getType() {
		return GLWorldGen.SET_DATA.get();
	}
}
