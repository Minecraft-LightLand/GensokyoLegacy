package dev.xkmc.gensokyolegacy.content.entity.characters.fairy;

import dev.xkmc.gensokyolegacy.compat.touhoulittlemaid.TouhouSpellCards;
import dev.xkmc.gensokyolegacy.content.entity.behavior.combat.YoukaiCombatManager;
import dev.xkmc.gensokyolegacy.content.entity.behavior.sensor.YoukaiFindPreySensor;
import dev.xkmc.gensokyolegacy.content.entity.behavior.task.combat.YoukaiSearchTargetTask;
import dev.xkmc.gensokyolegacy.content.entity.behavior.task.core.TaskBoard;
import dev.xkmc.gensokyolegacy.content.entity.behavior.task.home.YoukaiCraftTask;
import dev.xkmc.gensokyolegacy.content.entity.behavior.task.play.ItemPickupTask;
import dev.xkmc.gensokyolegacy.content.entity.behavior.task.play.YoukaiHuntTask;
import dev.xkmc.gensokyolegacy.content.entity.module.*;
import dev.xkmc.gensokyolegacy.content.entity.youkai.GeneralYoukaiEntity;
import dev.xkmc.gensokyolegacy.content.item.FrozenFrogItem;
import dev.xkmc.gensokyolegacy.init.registrate.GLBrains;
import dev.xkmc.gensokyolegacy.init.registrate.GLItems;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.youkaishomecoming.init.food.YHFood;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.FollowTemptation;
import net.tslat.smartbrainlib.api.core.sensor.custom.NearbyItemsSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.ItemTemptingSensor;

import java.util.List;

@SerialClass
public class CirnoEntity extends GeneralYoukaiEntity {

	public static AttributeSupplier.Builder createAttributes() {
		return GeneralYoukaiEntity.createAttributes()
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
		return List.of(
				new HomeModule(this),
				new FeedModule(this),
				new TalkModule(this),
				new CountPickupModule(this, e -> e.getItem() instanceof FrozenFrogItem)
		);
	}

	@Override
	protected void constructTaskBoard(TaskBoard board) {
		super.constructTaskBoard(board);
		board.addFirst(50, new FollowTemptation<>(), Activity.IDLE, Activity.PLAY, GLBrains.AT_HOME.get());
		board.addFirst(200, new ItemPickupTask(), Activity.IDLE, Activity.PLAY);
		board.addFirst(250, new YoukaiHuntTask(6), GLBrains.HUNT.get());

		board.addRandom(new YoukaiCraftTask<>(this::doCraft, 60, 12000), GLBrains.AT_HOME.get());

		board.addBehaviorActivity(YoukaiSearchTargetTask.class, GLBrains.HUNT.get());

		board.addSensor(new ItemTemptingSensor<CirnoEntity>().setRadius(16, 8)
				.temptedWith((self, stack) -> stack.is(YHFood.MILK_POPSICLE.item)).setScanRate(e -> 20));
		board.addSensor(new NearbyItemsSensor<CirnoEntity>().setRadius(18, 6)
				.setScanRate(e -> e.playOrHunt() ? 20 : 60));
		board.addSensor(new YoukaiFindPreySensor<>(CirnoEntity::playOrHunt));

		board.addPrioritizedActivity(GLBrains.HUNT.get(), GLBrains.MEM_PREY.get(), 200);
	}

	@Override
	public void initSpellCard() {
		TouhouSpellCards.setCirno(this);
	}

	private boolean playOrHunt() {
		var a = getActivity();
		return a == Activity.PLAY || a == GLBrains.HUNT.get();
	}

	private ItemStack doCraft(boolean simulate) {
		var module = getModule(CountPickupModule.class);
		if (module.isEmpty()) return ItemStack.EMPTY;
		if (module.get().getCount() < 3) return ItemStack.EMPTY;
		if (!simulate) {
			module.get().consume(3);
		}
		return GLItems.FAIRY_ICE_CRYSTAL.asStack();
	}

	@Override
	public String getBrainDebugInfo() {
		int frogPickup = getModule(CountPickupModule.class)
				.map(CountPickupModule::getCount).orElse(0);
		if (frogPickup == 0) return super.getBrainDebugInfo();
		return super.getBrainDebugInfo() + "\n" + frogPickup + " frogs";
	}

}
