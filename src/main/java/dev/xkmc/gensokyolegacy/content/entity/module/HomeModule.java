package dev.xkmc.gensokyolegacy.content.entity.module;

import dev.xkmc.gensokyolegacy.content.attachment.index.IndexStorage;
import dev.xkmc.gensokyolegacy.content.attachment.index.StructureKey;
import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.Nullable;

@SerialClass
public class HomeModule extends AbstractYoukaiModule {

	private static final ResourceLocation ID = GensokyoLegacy.loc("home");

	@Nullable
	@SerialField
	private StructureKey home;

	public HomeModule(YoukaiEntity self) {
		super(ID, self);
	}

	@Nullable
	public StructureKey home(){
		return home;
	}

	public void setHome(StructureKey key) {
		this.home = key;
	}

	@Override
	public void tickServer() {
		if (home == null) return;
		if (!(self.level() instanceof ServerLevel sl)) return;
		var ref = IndexStorage.get(sl).getOrCreate(home).bedOf(self.getType());
		if (ref == null) return;
		ref.entityTick(sl, self);
	}

	@Override
	public void onKilled() {
		if (home == null) return;
		if (!(self.level() instanceof ServerLevel sl)) return;
		var ref = IndexStorage.get(sl).getOrCreate(home).bedOf(self.getType());
		if (ref == null) return;
		ref.onEntityDie(sl, self);
	}

}
