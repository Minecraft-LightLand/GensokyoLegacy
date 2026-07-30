package dev.xkmc.gensokyolegacy.content.rpg.core;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.tterrag.registrate.util.OneTimeEventReceiver;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.l2core.init.reg.simple.CdcReg;
import dev.xkmc.l2core.init.reg.simple.CdcVal;
import dev.xkmc.l2serial.util.Wrappers;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.function.Supplier;

public record CodecRegistryInstance<T extends CodecElement<?>>(
		ResourceKey<Registry<MapCodec<? extends T>>> key,
		Supplier<Registry<MapCodec<? extends T>>> registry,
		CdcReg<T> reg
) {

	public static <T extends CodecElement<?>> CodecRegistryInstance<T> of(String id) {
		ResourceKey<Registry<MapCodec<? extends T>>> key = ResourceKey.createRegistryKey(GensokyoLegacy.loc(id));
		RegistryBuilder<MapCodec<? extends T>> ans = new RegistryBuilder<>(key);
		Registry<MapCodec<? extends T>> reg = ans.create();
		OneTimeEventReceiver.addModListener(GensokyoLegacy.REGISTRATE, NewRegistryEvent.class, (e) -> e.register(reg));
		return new CodecRegistryInstance<>(key, () -> reg, CdcReg.of(GensokyoLegacy.REG, reg));
	}

	public Codec<T> codec() {
		return registry.get().byNameCodec().dispatch(x -> Wrappers.cast(x.codec()), e -> e);
	}

	public <E extends T> CdcVal<E> reg(String id, MapCodec<E> codec) {
		return reg.reg(id, codec);
	}

}
