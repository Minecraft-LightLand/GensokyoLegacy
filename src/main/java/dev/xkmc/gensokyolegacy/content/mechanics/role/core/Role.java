package dev.xkmc.gensokyolegacy.content.mechanics.role.core;

import dev.xkmc.gensokyolegacy.content.attachment.role.PlayerRoleHolder;
import dev.xkmc.gensokyolegacy.content.mechanics.role.effect.AttackEffect;
import dev.xkmc.gensokyolegacy.content.mechanics.role.effect.RoleDataMap;
import dev.xkmc.gensokyolegacy.content.mechanics.role.effect.RoleEffect;
import dev.xkmc.gensokyolegacy.init.registrate.GLMechanics;
import dev.xkmc.gensokyolegacy.init.registrate.GLMeta;
import dev.xkmc.l2core.init.reg.registrate.NamedEntry;
import dev.xkmc.l2damagetracker.contents.attack.DamageData;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class Role extends NamedEntry<Role> {

	private final RoleCategory category;
	private final RoleEffect[][] effects;

	public Role(RoleCategory category) {
		super(GLMechanics.ROLES);
		this.category = category;
		this.effects = new RoleEffect[5][0];
	}

	public Role add(int stage, RoleEffect... effects) {
		this.effects[stage] = effects;
		return this;
	}

	public Component getName() {
		return getDesc();
	}

	public void startOrAdvance(Player player, int max, int point) {
		GLMeta.ABILITY.type().getOrCreate(player).getOrCreate(player, this).advance(max, point);
	}

	public void advanceIfStarted(Player player, int max, int point) {
		var data = GLMeta.ABILITY.type().getOrCreate(player).getData(player, this);
		if (data != null) data.advance(max, point);
	}

	public RoleCategory getCategory() {
		return category;
	}

	@Nullable
	private RoleDataMap getData(Level level) {
		return GLMechanics.ROLE_ATTRIBUTE.get(level.registryAccess(), holder());
	}

	public void start(PlayerRoleHolder player, int stage) {
		for (var e : effects[stage])
			e.start(player, stage);
		var attr = getData(player.player().level());
		if (attr != null) attr.start(player, stage);
	}

	public void tick(PlayerRoleHolder player, int stage) {
		for (var e : effects[stage])
			e.tick(player, stage);
	}

	public void end(PlayerRoleHolder player, int stage) {
		for (var e : effects[stage])
			e.end(player, stage);
		var attr = getData(player.player().level());
		if (attr != null) attr.end(player, stage);
	}

	public boolean onAttack(DamageData.Attack cache, int stage) {
		var attr = getData(cache.getTarget().level());
		if (attr != null && attr.immune(cache, stage))
			return true;
		for (var e : effects[stage]) {
			if (e instanceof AttackEffect att) {
				if (att.immune(cache, stage)) {
					return true;
				}
			}
		}
		return false;
	}

	public void onDamage(DamageData.Defence cache, int stage) {
		var attr = getData(cache.getTarget().level());
		if (attr != null) attr.onDamage(this, cache, stage);
		for (var e : effects[stage]) {
			if (e instanceof AttackEffect att) {
				att.onDamage(this, cache, stage);
			}
		}
	}

}
