package dev.xkmc.gap.content.block.pot;

import dev.xkmc.l2core.base.tile.BaseBlockEntity;
import dev.xkmc.l2core.base.tile.BaseContainerListener;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

@SerialClass
public class PotBlockEntity extends BaseBlockEntity implements BaseContainerListener {

    @SerialField
    public final PotItemHandler items = new PotItemHandler();

    @SerialField
    public final PotTank fluids = new PotTank();

    public PotBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        items.add(this);
        fluids.add(this);
    }

    @Override
    public void notifyTile() {

    }



}
