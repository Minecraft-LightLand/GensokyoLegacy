package dev.xkmc.gensokyolegacy.content.entity.behavior.task.home;

import dev.xkmc.gensokyolegacy.content.entity.youkai.SmartYoukaiEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class YoukaiStayInRoomTask<E extends SmartYoukaiEntity> extends AbstractStayTask<E> {

	protected @Nullable Vec3 getTargetPos(E entity) {
		if (home == null || !home.isValid()) return null;
		return home.getRandomPosInRoom(entity);
	}

}