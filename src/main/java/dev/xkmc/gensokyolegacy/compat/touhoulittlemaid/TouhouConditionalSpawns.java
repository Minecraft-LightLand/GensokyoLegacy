package dev.xkmc.gensokyolegacy.compat.touhoulittlemaid;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import dev.xkmc.gensokyolegacy.content.entity.characters.fairy.FairyEntity;
import dev.xkmc.gensokyolegacy.init.data.GLModConfig;
import dev.xkmc.gensokyolegacy.init.registrate.GLEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.ModList;

import java.util.ArrayList;
import java.util.List;

public class TouhouConditionalSpawns {

	public static void triggerKoishi(LivingEntity le, Vec3 pos) {
		if (le.level().isClientSide()) return;
		if (!ModList.get().isLoaded(TouhouLittleMaid.MOD_ID)) return;
		var e = GLEntities.KOISHI.create(le.level());
		if (e == null) return;
		e.setPos(pos);
		e.setTarget(le);
		e.initSpellCard();
		le.level().addFreshEntity(e);
	}

	public static void triggetYukari(LivingEntity le, Vec3 pos) {
		if (le.level().isClientSide()) return;
		if (!ModList.get().isLoaded(TouhouLittleMaid.MOD_ID)) return;
		var e = GLEntities.YUKARI.create(le.level());
		if (e == null) return;
		e.setPos(pos);
		e.setTarget(le);
		e.initSpellCard();
		le.level().addFreshEntity(e);

	}

	public static void triggetFairyReinforcement(FairyEntity self, LivingEntity le, Vec3 pos) {
		if (le.level().isClientSide()) return;
		if (!ModList.get().isLoaded(TouhouLittleMaid.MOD_ID)) return;
		if (le.getRandom().nextDouble() > GLModConfig.SERVER.fairySummonReinforcement.get())
			return;
		List<EntityType<? extends FairyEntity>> list = new ArrayList<>(List.of(
				GLEntities.CIRNO.get(), GLEntities.SUNNY.get(), GLEntities.STAR.get(), GLEntities.LUNA.get()
		));
		list.remove(self.getType());
		var e = list.get(self.getRandom().nextInt(list.size())).create(le.level());
		if (e == null) return;
		e.setPos(pos);
		e.setTarget(le);
		e.initSpellCard();
		le.level().addFreshEntity(e);
	}

}
