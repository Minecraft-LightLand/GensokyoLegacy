package dev.xkmc.gensokyolegacy.content.entity.characters.fairy;

import dev.xkmc.gensokyolegacy.content.entity.behavior.combat.YoukaiCombatManager;
import dev.xkmc.gensokyolegacy.content.entity.youkai.GeneralYoukaiEntity;
import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.youkaishomecoming.init.food.YHFood;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

@SerialClass
public class FairyEntity extends GeneralYoukaiEntity {

	public static AttributeSupplier.Builder createAttributes() {
		return YoukaiEntity.createAttributes()
				.add(Attributes.MAX_HEALTH, 20)
				.add(Attributes.ATTACK_DAMAGE, 4);
	}

	public FairyEntity(EntityType<? extends FairyEntity> type, Level level) {
		super(type, level);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		goalSelector.addGoal(5, new TemptGoal(this, 1,
				Ingredient.of(YHFood.MILK_POPSICLE.item.get()), false));
	}

	@Override
	protected YoukaiCombatManager createCombatManager() {
		return new FairyCombatManager(this);
	}

}
