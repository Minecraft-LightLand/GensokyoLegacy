package dev.xkmc.gensokyolegacy.content.entity.characters.fairy;

import dev.xkmc.gensokyolegacy.content.entity.behavior.combat.YoukaiCombatManager;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

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

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData) {
		//TouhouSpellCards.setCirno(this);
		return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData);
	}

	public static boolean checkCirnoSpawnRules(EntityType<CirnoEntity> e, ServerLevelAccessor level, MobSpawnType type, BlockPos pos, RandomSource rand) {
		if (!checkMobSpawnRules(e, level, type, pos, rand)) return false;
		//if (!YHModConfig.COMMON.cirnoSpawn.get()) return false;
		var aabb = AABB.ofSize(pos.getCenter(), 48, 24, 48);
		if (!level.getEntitiesOfClass(CirnoEntity.class, aabb).isEmpty()) return false;
		var player = level.getNearestPlayer(pos.getX(), pos.getY(), pos.getZ(), 128, false);
		if (player == null) return false;
		return true;//EffectEventHandlers.isCharacter(player);
	}

}
