package dev.xkmc.gensokyolegacy.init.data.dimension;

import net.minecraft.world.level.levelgen.SurfaceRules;

import java.util.ArrayList;
import java.util.List;

public class SurfaceHolder {

	protected List<SurfaceRuleBuilder> surfaceList;

	protected SurfaceHolder(List<SurfaceRuleBuilder> surfaceList) {
		this.surfaceList = surfaceList;
	}

	public SurfaceRules.RuleSource buildRules() {
		if (surfaceList.isEmpty()) throw new IllegalStateException("No rules to build");
		if (surfaceList.size() == 1) return surfaceList.getFirst().buildRules();
		List<SurfaceRules.RuleSource> list = new ArrayList<>();
		for (var e : surfaceList) {
			list.add(e.buildRules());
		}
		return SurfaceRules.sequence(list.toArray(SurfaceRules.RuleSource[]::new));
	}

}
