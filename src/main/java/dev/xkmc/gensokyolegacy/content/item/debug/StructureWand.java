package dev.xkmc.gensokyolegacy.content.item.debug;

import dev.xkmc.gensokyolegacy.content.attachment.home.core.HomeSearchUtil;
import dev.xkmc.gensokyolegacy.content.attachment.home.custom.CustomHomeData;
import dev.xkmc.gensokyolegacy.content.attachment.home.custom.CustomHomeHolder;
import dev.xkmc.gensokyolegacy.content.block.bed.YoukaiBedBlockEntity;
import dev.xkmc.gensokyolegacy.init.registrate.GLMeta;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.properties.BedPart;

import java.util.Optional;

public class StructureWand extends Item {
	private static final int SEARCH_RADIUS = 7; // 15x15x15 means radius of 7 in each direction
	private static final int SEARCH_RADIUS_Y = 7;

	public StructureWand(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
		return super.use(level, player, usedHand);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		if (context.getPlayer() instanceof ServerPlayer sp && context.getLevel() instanceof ServerLevel level) {
			var pos = context.getClickedPos();

			// Check if there's at least one container and one chair in 15x15x15 area
			var bed = hasBlockInArea(level, pos, (sl, p) ->
					sl.getBlockEntity(p) instanceof YoukaiBedBlockEntity be &&
							be.getBlockState().getValue(BedBlock.PART) == BedPart.HEAD);
			if (bed.isEmpty() || !(level.getBlockEntity(bed.get()) instanceof YoukaiBedBlockEntity be)) {
				sp.sendSystemMessage(Component.literal("Youkai Bed not found!"));
				return InteractionResult.FAIL;
			}
			if (be.linked()) {
				sp.sendSystemMessage(Component.literal("Youkai Bed is already linked to a structure!"));
				return InteractionResult.FAIL;
			}
			var container = hasBlockInArea(level, pos, HomeSearchUtil::isValidChest);
			var chair = hasBlockInArea(level, pos, HomeSearchUtil::isValidChair);
			if (container.isEmpty() || chair.isEmpty()) {
				sp.sendSystemMessage(Component.literal(
						"Missing required blocks. Container: " + container + ", Chair: " + chair));
				return InteractionResult.FAIL;

			}

			// Find the structure at this position
			CustomHomeHolder holder = CustomHomeHolder.create(level, pos);
			if (holder == null) return InteractionResult.FAIL;

			// Create new HomeData and put it into StructureAttachment
			var attachment = holder.chunk().getData(GLMeta.STRUCTURE.get());
			var homeData = new CustomHomeData();
			attachment.custom.put(pos, homeData);
			homeData.checkInit(holder);
			holder.chunk().setUnsaved(true);
			be.linkTo(holder.key());
			sp.sendSystemMessage(Component.literal("Home registered successfully!"));
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

	/**
	 * Check if there's at least one block in the 15x15x15 area around the position
	 * that matches the given predicate
	 */
	private Optional<BlockPos> hasBlockInArea(ServerLevel level, BlockPos center, java.util.function.BiPredicate<ServerLevel, BlockPos> predicate) {
		BlockPos minPos = center.offset(-SEARCH_RADIUS, -SEARCH_RADIUS_Y, -SEARCH_RADIUS);
		BlockPos maxPos = center.offset(SEARCH_RADIUS, SEARCH_RADIUS_Y, SEARCH_RADIUS);

		for (int x = minPos.getX(); x <= maxPos.getX(); x++) {
			for (int y = minPos.getY(); y <= maxPos.getY(); y++) {
				for (int z = minPos.getZ(); z <= maxPos.getZ(); z++) {
					BlockPos checkPos = new BlockPos(x, y, z);
					if (level.isLoaded(checkPos) && predicate.test(level, checkPos)) {
						return Optional.of(checkPos);
					}
				}
			}
		}
		return Optional.empty();
	}

}
