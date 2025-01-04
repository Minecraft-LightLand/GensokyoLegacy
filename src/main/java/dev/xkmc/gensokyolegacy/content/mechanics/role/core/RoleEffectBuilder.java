package dev.xkmc.gensokyolegacy.content.mechanics.role.core;

import java.util.ArrayList;
import java.util.List;

public class RoleEffectBuilder {

	private final List<List<RoleEffect>> table = new ArrayList<>();

	public RoleEffectBuilder() {
		for (int i = 0; i < 5; i++)
			table.add(new ArrayList<>());
	}

	public void build(Role role) {
		for (int i = 0; i < 5; i++) {
			role.add(i, table.get(i).toArray(RoleEffect[]::new));
		}
	}

}
