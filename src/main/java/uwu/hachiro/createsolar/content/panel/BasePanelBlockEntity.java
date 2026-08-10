package uwu.hachiro.createsolar.content.panel;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import static uwu.hachiro.createsolar.content.panel.BasePanelBlock.ACTIVE;

public abstract class BasePanelBlockEntity extends SmartBlockEntity {
    protected boolean active;

    public BasePanelBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);

        this.active = false;
    }

    public void setActive(boolean active) {
        this.active = active;

        assert level != null;

        level.setBlockAndUpdate(worldPosition, getBlockState().setValue(ACTIVE, active));
        notifyUpdate();
    }

    @Override
    public void write(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(tag, registries, clientPacket);

        tag.putBoolean("Active", active);
    }

    @Override
    protected void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(tag, registries, clientPacket);

        this.active = tag.getBoolean("Active");
    }
}
