package dev.xkmc.gensokyolegacy.content.mechanics.incense.core;

import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import net.neoforged.neoforge.registries.DeferredHolder;

public class IncenseEntry<T extends Incense> extends RegistryEntry<Incense, T> {

	public IncenseEntry(L2Registrate owner, DeferredHolder<Incense, T> delegate) {
		super(owner, delegate);
	}

}
