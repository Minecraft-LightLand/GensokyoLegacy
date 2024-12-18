package dev.xkmc.gensokyolegacy.content.entity.module;

import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.gensokyolegacy.init.registrate.GLBrains;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.tslat.smartbrainlib.util.BrainUtils;

@SerialClass
public class TalkModule extends AbstractYoukaiModule {

	private static final ResourceLocation ID = GensokyoLegacy.loc("talk");

	private ServerPlayer talkTarget;

	public TalkModule(YoukaiEntity self) {
		super(ID, self);
	}

	@Override
	public InteractionResult interact(Player player, InteractionHand hand) {
		if (!self.mayInteract(player)) return InteractionResult.PASS;
		ItemStack stack = player.getItemInHand(hand);
		if (!stack.isEmpty()) return InteractionResult.PASS;
		if (player instanceof ServerPlayer sp) {
			if (talkTarget != null) return InteractionResult.FAIL;
			startTalking(sp);
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	public void tickServer() {
		if (talkTarget != null) {
			if (talkTarget.isRemoved() || !talkTarget.isAlive() || talkTarget.level() != self.level() ||
					talkTarget.distanceTo(self) > 5 || talkTarget.containerMenu != talkTarget.inventoryMenu) {
				stopTalking();
			}
		}
	}

	private void startTalking(ServerPlayer player) {
		talkTarget = player;
		self.setTalkTo(player, -1);

	}

	public void stopTalking() {
		if (talkTarget == null) return;
		BrainUtils.clearMemory(self, GLBrains.MEM_TALK.get());
		talkTarget = null;
	}

	public boolean isTalking() {
		return talkTarget != null;
	}

}
