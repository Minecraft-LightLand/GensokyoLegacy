package dev.xkmc.gensokyolegacy.content.entity.characters.fairy;

import dev.xkmc.gensokyolegacy.compat.touhoulittlemaid.TouhouSpellCards;
import dev.xkmc.gensokyolegacy.content.entity.behavior.combat.YoukaiCombatManager;
import dev.xkmc.gensokyolegacy.content.entity.behavior.sensor.YoukaiFindPreySensor;
import dev.xkmc.gensokyolegacy.content.entity.behavior.task.core.TaskBoard;
import dev.xkmc.gensokyolegacy.content.entity.behavior.task.play.ItemPickupTask;
import dev.xkmc.gensokyolegacy.content.entity.behavior.task.play.YoukaiHuntTask;
import dev.xkmc.gensokyolegacy.content.entity.module.AbstractYoukaiModule;
import dev.xkmc.gensokyolegacy.content.entity.module.FeedModule;
import dev.xkmc.gensokyolegacy.content.entity.module.HomeModule;
import dev.xkmc.gensokyolegacy.init.registrate.GLBrains;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.youkaishomecoming.init.food.YHFood;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.FollowTemptation;
import net.tslat.smartbrainlib.api.core.sensor.custom.NearbyItemsSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.ItemTemptingSensor;

import java.util.List;

@SerialClass
public class CirnoEntity extends FairyEntity {

	public static AttributeSupplier.Builder createAttributes() {
		return FairyEntity.createAttributes()
				.add(Attributes.MAX_HEALTH, 40)
				.add(Attributes.ATTACK_DAMAGE, 6);
	}

	public CirnoEntity(EntityType<? extends CirnoEntity> type, Level level) {
		super(type, level);
	}

	@Override
	public boolean canFreeze() {
		return false;
	}

	@Override
	protected YoukaiCombatManager createCombatManager() {
		return new CirnoCombatManager(this);
	}

	@Override
	protected List<AbstractYoukaiModule> createModules() {
		return List.of(new HomeModule(this), new FeedModule(this));
	}

	@Override
	protected void constructTaskBoard(TaskBoard board) {
		super.constructTaskBoard(board);
		board.addFirst(250, new FollowTemptation<>(), Activity.IDLE, Activity.PLAY, GLBrains.AT_HOME.get());
		board.addFirst(400, new ItemPickupTask(), Activity.PLAY);
		board.addFirst(450, new YoukaiHuntTask(6), Activity.PLAY);

		board.addSensor(new ItemTemptingSensor<CirnoEntity>().setRadius(16, 8)
				.temptedWith((self, stack) -> stack.is(YHFood.MILK_POPSICLE.item)).setScanRate(e -> 20));
		board.addSensor(new NearbyItemsSensor<CirnoEntity>().setRadius(12, 8)
				.setScanRate(e -> e.getActivity() == Activity.PLAY ? 20 : 60));
		board.addSensor(new YoukaiFindPreySensor<>(a -> a == Activity.PLAY, (self, e) -> e instanceof Frog));
	}

	@Override
	public void initSpellCard() {
		TouhouSpellCards.setCirno(this);
	}

	@Override
	public boolean wantsToPickUp(ItemStack stack) {
		return super.wantsToPickUp(stack);
	}

	@Override
	protected void pickUpItem(ItemEntity itemEntity) {
		super.pickUpItem(itemEntity);
	}

}
