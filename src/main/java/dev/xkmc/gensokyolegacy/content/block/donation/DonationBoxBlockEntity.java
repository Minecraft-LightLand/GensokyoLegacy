package dev.xkmc.gensokyolegacy.content.block.donation;

import dev.xkmc.gensokyolegacy.content.attachment.character.CharDataHolder;
import dev.xkmc.gensokyolegacy.content.attachment.character.CharacterData;
import dev.xkmc.gensokyolegacy.content.attachment.character.ReputationState;
import dev.xkmc.gensokyolegacy.content.attachment.datamap.BedData;
import dev.xkmc.gensokyolegacy.content.block.base.IDebugInfoBlockEntity;
import dev.xkmc.gensokyolegacy.content.block.base.LocatedBlockEntity;
import dev.xkmc.gensokyolegacy.content.client.debug.BlockInfoToClient;
import dev.xkmc.gensokyolegacy.init.data.GLLang;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

@SerialClass
public class DonationBoxBlockEntity extends LocatedBlockEntity implements IDebugInfoBlockEntity {

	public DonationBoxBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public void take(@Nullable Player player, ItemStack stack) {
		if (key == null) return;
		if (player == null) return;
		var bed = BedData.of(getBlockState().getBlock());
		if (bed == null) return;
		var holder = CharDataHolder.getUnbounded(player, bed.type());
		int value = 0;
		if (stack.is(Items.EMERALD)) {
			value = 3;
		} else if (stack.is(Items.GOLD_INGOT)) {
			value = 9;
		} else if (stack.is(Items.GOLD_NUGGET)) {
			value = 1;
		} else if (stack.is(Items.GOLD_BLOCK)) {
			value = 81;
		}
		if (value > 0) {
			int current = holder.data().reputation;
			int max = CharacterData.MAX;
			if (current < max) {
				int count = Math.min(stack.getCount(), (max - current - 1) / value + 1);
				stack.shrink(count);
				holder.gain(value * count, max);
			}
		}
	}

	@Override
	public BlockInfoToClient getDebugPacket(ServerPlayer player) {
		var bed = BedData.of(getBlockState().getBlock());
		if (bed == null || key == null)
			return BlockInfoToClient.of(GLLang.INFO_BED_UNBOUND.get().withStyle(ChatFormatting.RED));
		var reputation = CharDataHolder.getUnbounded(player, bed.type()).data().reputation;
		return BlockInfoToClient.of(ReputationState.toInfo(reputation));
	}

}
