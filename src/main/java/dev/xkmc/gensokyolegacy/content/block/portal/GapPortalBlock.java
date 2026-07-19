package dev.xkmc.gensokyolegacy.content.block.portal;

import dev.xkmc.gensokyolegacy.init.registrate.GLBlocks;
import dev.xkmc.l2modularblock.impl.BlockEntityBlockMethodImpl;
import dev.xkmc.l2modularblock.impl.DoubleBlockImpl;
import dev.xkmc.l2modularblock.mult.AnimateTickBlockMethod;
import dev.xkmc.l2modularblock.mult.SetPlacedByBlockMethod;
import dev.xkmc.l2modularblock.mult.UseItemOnBlockMethod;
import dev.xkmc.l2modularblock.one.ShapeBlockMethod;
import dev.xkmc.l2modularblock.type.BlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class GapPortalBlock implements AnimateTickBlockMethod, ShapeBlockMethod, SetPlacedByBlockMethod, UseItemOnBlockMethod {

	public static final BlockMethod TE = new BlockEntityBlockMethodImpl<>(GLBlocks.GAP_BE, GapPortalBlockEntity.class);

	public static final VoxelShape SHAPE = Block.box(6, 0, 6, 10, 16, 10);

	public static BasePortalBlock of(BlockBehaviour.Properties properties) {
		return new BasePortalBlock(properties, new BasePortalBlock.PortalMethod(), new DoubleBlockImpl(), new GapPortalBlock(), TE);
	}

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity le, ItemStack stack) {
		if (state.getValue(BlockStateProperties.HALF) == Half.BOTTOM) {
			if (level.getBlockEntity(pos) instanceof GapPortalBlockEntity be) {
				be.setPlacedBy(stack);
			}
		}
	}

	@Override
	public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player pl, InteractionHand hand, BlockHitResult result) {
		if (state.getValue(BlockStateProperties.HALF) == Half.TOP)
			pos = pos.below();
		if (stack.isEmpty() && hand == InteractionHand.MAIN_HAND && level.getBlockEntity(pos) instanceof GapPortalBlockEntity gap) {
			if (!level.isClientSide()) {
				level.removeBlock(pos, false);
				level.removeBlock(pos.above(), false);
				pl.setItemInHand(hand, gap.getItem());
			}
			return ItemInteractionResult.SUCCESS;
		}
		return ItemInteractionResult.FAIL;
	}

	@Override
	public @Nullable VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		return SHAPE;
	}

	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rand) {
		if (rand.nextInt(100) == 0) {
			level.playLocalSound(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F,
					SoundEvents.PORTAL_AMBIENT, SoundSource.BLOCKS, 0.5F, rand.nextFloat() * 0.4F + 0.8F, false);
		}

		for (int i = 0; i < 4; ++i) {
			double dx = (rand.nextFloat() - 0.5F) * 4F;
			double dz = (rand.nextFloat() - 0.5F) * 4F;

			double x = pos.getX() + 0.5f;
			double y = pos.getY() + rand.nextFloat();
			double z = pos.getZ() + 0.5f;

			level.addParticle(ParticleTypes.PORTAL, x, y, z, dx, 0, dz);
		}

	}


}
