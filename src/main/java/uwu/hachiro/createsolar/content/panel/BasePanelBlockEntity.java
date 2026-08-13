package uwu.hachiro.createsolar.content.panel;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

import static uwu.hachiro.createsolar.content.panel.BasePanelBlock.ACTIVE;

public abstract class BasePanelBlockEntity extends SmartBlockEntity {
    protected boolean active;
    private int nextUpdate;

    public BasePanelBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);

        this.active = false;
        this.nextUpdate = getUpdateInterval();
    }

    @Override
    public void tick() {
        super.tick();

        assert level != null;
        if(level.isClientSide()) return;

        nextUpdate--;
        if(nextUpdate <= 0) {
            onUpdate();
            nextUpdate = getUpdateInterval();
        }
    }

    public abstract int getUpdateInterval();

    public abstract void onUpdate();

    public void setActive(boolean active) {
        this.active = active;

        assert level != null;

        level.setBlockAndUpdate(worldPosition, getBlockState().setValue(ACTIVE, active));
        notifyUpdate();
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }

    @Override
    public void write(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(tag, registries, clientPacket);

        tag.putBoolean("Active", active);
        tag.putInt("NextUpdate", nextUpdate);
    }

    @Override
    protected void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(tag, registries, clientPacket);

        this.active = tag.getBoolean("Active");
        this.nextUpdate = tag.getInt("NextUpdate");
    }
}
