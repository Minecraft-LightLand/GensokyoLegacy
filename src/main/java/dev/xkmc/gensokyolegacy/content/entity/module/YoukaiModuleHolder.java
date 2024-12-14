package dev.xkmc.gensokyolegacy.content.entity.module;

import dev.xkmc.l2serial.util.Wrappers;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class YoukaiModuleHolder implements Iterable<AbstractYoukaiModule> {

	private final List<AbstractYoukaiModule> modules;
	private final Map<Class<?>, Optional<?>> cache = new HashMap<>();
	private boolean hasPickup;

	public YoukaiModuleHolder(List<AbstractYoukaiModule> modules) {
		this.modules = modules;
		hasPickup = false;
		for (var e : modules) {
			if (e instanceof IPickupModule) {
				hasPickup = true;
				break;
			}
		}
	}

	@NotNull
	@Override
	public Iterator<AbstractYoukaiModule> iterator() {
		return modules.iterator();
	}

	public <T> Optional<T> getModule(Class<T> cls) {
		var hit = cache.get(cls);
		if (hit == null) {
			Optional<T> ans = Wrappers.cast(modules.stream().filter(cls::isInstance).findFirst());
			cache.put(cls, ans);
			return ans;
		}
		return Wrappers.cast(hit);
	}

	public boolean hasPickup() {
		return hasPickup;
	}

}
