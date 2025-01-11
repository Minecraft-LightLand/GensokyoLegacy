package dev.xkmc.gensokyolegacy.content.block.ritual;

import dev.xkmc.gensokyolegacy.init.data.GLTagGen;
import dev.xkmc.gensokyolegacy.init.registrate.GLMeta;
import dev.xkmc.l2core.base.tile.BaseBlockEntity;
import dev.xkmc.l2core.base.tile.BaseContainerListener;
import dev.xkmc.l2modularblock.tile_api.BlockContainer;
import dev.xkmc.l2modularblock.tile_api.TickableBlockEntity;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import dev.xkmc.youkaishomecoming.content.pot.overlay.InfoTile;
import dev.xkmc.youkaishomecoming.content.pot.overlay.TileTooltip;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

@SerialClass
public class RitualBlockEntity extends BaseBlockEntity
		implements BlockContainer, BaseContainerListener, TickableBlockEntity, InfoTile {

	@SerialField
	private final RitualItemContainer items = new RitualItemContainer(7).add(this);

	public RitualBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void tick() {

	}

	private RitualResult checkSurroundings() {
		if (level == null) return RitualResult.EMPTY;
		List<BlockState> states = new ArrayList<>();
		List<BlockPos> sites = new ArrayList<>();
		int totalMana = 0;
		for (int x = -2; x <= 2; x++) {
			for (int z = -2; z <= 2; z++) {
				int dist = x * x + z * z;
				if (dist == 0) continue;
				BlockPos pos = getBlockPos().offset(x, 0, z);
				BlockState state = level.getBlockState(pos);
				if (dist <= 3) {
					if (!state.getCollisionShape(level, pos).isEmpty()) {
						return RitualResult.EMPTY;
					}
				} else if (dist <= 5) {
					if (state.is(GLTagGen.TOTEMS)) {
						sites.add(pos);
						states.add(state);
					}
					var mana = GLMeta.BLOCK_MANA.get(level.registryAccess(), state.getBlockHolder());
					if (mana != null) totalMana += mana.mana();
				}
			}
		}
		return RitualResult.of(getBlockPos(), states, sites, totalMana);
	}

	@Override
	public void notifyTile() {
		this.setChanged();
		this.sync();
	}

	@Override
	public List<Container> getContainers() {
		return List.of(items);
	}

	@Override
	public TileTooltip getImage() {
		return new TileTooltip(List.of(), List.of());
	}

	@Override
	public List<Component> lines() {
		return List.of();
	}

}
