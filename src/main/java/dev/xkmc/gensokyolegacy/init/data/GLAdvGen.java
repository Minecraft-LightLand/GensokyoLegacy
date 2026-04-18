package dev.xkmc.gensokyolegacy.init.data;

import com.tterrag.registrate.providers.RegistrateAdvancementProvider;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.gensokyolegacy.init.registrate.GLCriteriaTriggers;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public class GLAdvGen {
	public static final ResourceLocation KOISHI_FIRST = GensokyoLegacy.loc("koishi_first");

	public static void genAdv(RegistrateAdvancementProvider pvd) {
		pvd.accept(Advancement.Builder.advancement().addCriterion("koishi_first",
				GLCriteriaTriggers.KOISHI_FIRST.get().createCriterion(new PlayerTrigger.TriggerInstance(Optional.empty()))
		).build(KOISHI_FIRST));
	}

}
