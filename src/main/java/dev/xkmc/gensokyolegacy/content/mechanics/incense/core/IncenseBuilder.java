package dev.xkmc.gensokyolegacy.content.mechanics.incense.core;

import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import com.tterrag.registrate.util.nullness.NonnullType;
import dev.xkmc.gensokyolegacy.init.data.GLTagGen;
import dev.xkmc.gensokyolegacy.init.registrate.GLMechanics;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.l2core.init.reg.registrate.NamedEntry;
import dev.xkmc.l2serial.util.Wrappers;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

public class IncenseBuilder<T extends Incense, P> extends AbstractBuilder<Incense, T, P, IncenseBuilder<T, P>> {

	private final NonNullSupplier<T> sup;

	public IncenseBuilder(L2Registrate owner, P parent, String name, BuilderCallback callback, NonNullSupplier<T> sup) {
		super(owner, parent, name, callback, GLMechanics.INCENSE.key());
		this.sup = sup;
	}

	@Override
	protected IncenseEntry<T> createEntryWrapper(DeferredHolder<Incense, T> delegate) {
		return new IncenseEntry<>(Wrappers.cast(this.getOwner()), delegate);
	}

	@Override
	public IncenseEntry<T> register() {
		return Wrappers.cast(super.register());
	}

	public IncenseBuilder<T, P> lang(String name) {
		return this.lang(NamedEntry::getDescriptionId, name);
	}

	public <I extends IncenseItem> ItemBuilder<I, IncenseBuilder<T, P>> item(NonNullFunction<Item.Properties, I> sup) {
		return getOwner().item(this, getName(), sup)
				.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/incense/" + ctx.getName())))
				.setData(ProviderType.LANG, NonNullBiConsumer.noop())
				.tag(GLTagGen.INCENSE_ITEM);
	}

	@NonnullType
	@NotNull
	protected T createEntry() {
		return this.sup.get();
	}

	public IncenseBuilder<T, P> desc(String s) {
		getOwner().addRawLang("trait." + getOwner().getModid() + "." + getName() + ".desc", s);
		return this;
	}

}
