package dev.xkmc.gensokyolegacy.content.item.tool;

import dev.xkmc.gensokyolegacy.content.client.deco.DowserRenderer;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.l2serial.network.SerialPacketBase;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.ProtoChunk;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class Dowser extends Item {

	public static boolean isValid(@Nullable BlockEntity be) {
		if (!(be instanceof RandomizableContainerBlockEntity e)) return false;
		var lv = be.getLevel();
		if (lv instanceof ServerLevel && e.getLootTable() == null) return false;
		return true;
	}

	public static LinkedHashSet<BlockPos> search(ServerLevel level, BlockPos pos, int r) {
		var cpos = new ChunkPos(pos.getX() >> 4, pos.getZ() >> 4);
		LinkedHashSet<BlockPos> ans = new LinkedHashSet<>();
		for (int ix = -r; ix <= r; ix++) {
			for (int iz = -r; iz <= r; iz++) {
				var chunk = level.getChunk(cpos.x + ix, cpos.z + iz, ChunkStatus.FULL, false);
				if (chunk == null) continue;
				Map<BlockPos, BlockEntity> bss = chunk instanceof ProtoChunk proto ? proto.getBlockEntities() :
						chunk instanceof LevelChunk lc ? lc.getBlockEntities() : Map.of();
				for (var ent : bss.entrySet()) {
					if (isValid(ent.getValue())) {
						ans.add(ent.getKey());
					}
				}
			}
		}
		return ans;
	}

	public Dowser(Properties properties) {
		super(properties.durability(64));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (player instanceof ServerPlayer sp) {
			var blocks = search(sp.serverLevel(), player.blockPosition(), 4);
			GensokyoLegacy.HANDLER.toClientPlayer(new DowserToClient(blocks), sp);
			if (!player.isCreative()) {
				stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
			}
			player.getCooldowns().addCooldown(this, 20);
		}
		return InteractionResultHolder.success(stack);
	}

	public record DowserToClient(
			LinkedHashSet<BlockPos> pos
	) implements SerialPacketBase<DowserToClient> {

		@Override
		public void handle(Player player) {
			DowserPacketHandler.handle(player, pos);
		}

	}

	public static class DowserPacketHandler {

		public static void handle(Player player, Set<BlockPos> pos) {
			DowserRenderer.init(player, pos);
		}

	}

}
