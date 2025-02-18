package dev.xkmc.gensokyolegacy.init.data.dimension;

import net.minecraft.world.level.levelgen.SurfaceRules;

import java.util.ArrayList;
import java.util.List;

public class SurfaceRuleBuilder {

	private final SurfaceRules.ConditionSource condition;
	private final GLSurfaceParent self;
	private final List<SurfaceRules.RuleSource> list;

	SurfaceRuleBuilder(SurfaceRules.ConditionSource condition, GLSurfaceParent self) {
		this.condition = condition;
		this.self = self;
		this.list = new ArrayList<>();
	}

	public void add(SurfaceRules.RuleSource rule) {
		list.add(rule);
	}

	public SurfaceRules.RuleSource buildRules() {
		return SurfaceRules.ifTrue(condition, buildRulesImpl());
	}

	private SurfaceRules.RuleSource buildRulesImpl() {
		int total = self.surfaceList.size() + list.size();
		if (total == 0) throw new IllegalStateException("No rules to build");
		if (total == 1) {
			if (list.isEmpty())
				return self.surfaceList.getFirst().buildRules();
			else return list.getFirst();
		}
		List<SurfaceRules.RuleSource> list = new ArrayList<>();
		for (var e : self.surfaceList) {
			list.add(e.buildRules());
		}
		list.addAll(this.list);
		return SurfaceRules.sequence(list.toArray(SurfaceRules.RuleSource[]::new));
	}

}
