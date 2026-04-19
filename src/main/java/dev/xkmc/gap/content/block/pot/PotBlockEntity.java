package dev.xkmc.gap.content.block.pot;

import dev.xkmc.l2core.base.tile.BaseBlockEntity;
import dev.xkmc.l2core.base.tile.BaseContainerListener;
import dev.xkmc.l2modularblock.tile_api.TickableBlockEntity;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.LinkedHashMap;
import java.util.Map;

@SerialClass
public class PotBlockEntity extends BaseBlockEntity implements BaseContainerListener, TickableBlockEntity {

    @SerialField
    public final PotItemHandler items = new PotItemHandler();

    @SerialField
    public final PotTank fluids = new PotTank(1250);

    @SerialField
    protected final Map<ResourceLocation, PotRecipeProgress> matchedRecipe = new LinkedHashMap<>();

    private boolean shouldCheckRecipe = true;

    public PotBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        items.add(this);
        fluids.add(this);
    }

    @Override
    public void tick() {
        var level = getLevel();
        if (level instanceof ServerLevel sl) {
            if (shouldCheckRecipe) {
                shouldCheckRecipe = false;
                checkRecipe(sl, PotRecipeTriggerType.NONE);
            }
            matchedRecipe.entrySet().removeIf(e -> e.getValue().removeOnUpdate(sl, this, e.getKey()));
        }
    }

    public void checkRecipe(ServerLevel level, PotRecipeTriggerType type) {
        matchedRecipe.entrySet().removeIf(e -> e.getValue().removeOnValidate(level, this, e.getKey()));
        //TODO check for new recipes
    }

    public PotHeatState getHeat() {
        return PotHeatState.NONE; // TODO
    }

    @Override
    public void notifyTile() {
        shouldCheckRecipe = true;
        sync();
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider pvd) {
        super.loadAdditional(tag, pvd);
        shouldCheckRecipe = true;
    }

}
