package uwu.hachiro.createsolar.content.panel;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;
import uwu.hachiro.createsolar.SolarConfig;

import java.util.List;

import static uwu.hachiro.createsolar.content.panel.SolarPanelBlock.ACTIVE;

public class SolarPanelBlockEntity extends SmartBlockEntity {
    private final SolarPanelEnergyStorage energyStorage;
    private boolean active;
    private int nextUpdate;
    private int lastRedstoneOutput;

    public SolarPanelBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);

        this.energyStorage = new SolarPanelEnergyStorage(this,0);
        this.active = false;
        this.nextUpdate = 0;
        this.lastRedstoneOutput = 0;
    }

    @Override
    public void tick() {
        super.tick();

        assert level != null;

        if(level.isClientSide()) return;

        if (nextUpdate-- > 0) return;
        nextUpdate = SolarConfig.SOLAR_PANEL_UPDATE_INTERVAL.get();

        int output = calculateOutput();
        boolean nowActive = output > 0;
        if (active != nowActive) setActive(nowActive);

        int redstoneOutput = (int)(
                Mth.clamp(0, (double)output / SolarConfig.SOLAR_PANEL_MAX_OUTPUT.get(), 1) * 15
        );
        if(redstoneOutput != lastRedstoneOutput) {
            lastRedstoneOutput = redstoneOutput;
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
            level.updateNeighborsAt(worldPosition, getBlockState().getBlock());
        }

        energyStorage.insertEnergy(output);
    }

    private int calculateOutput() {
        assert level != null;

        BlockPos pos = this.getBlockPos();
        if (!level.canSeeSky(pos)) return 0;

        double sunFactor = SolarPanelCalculations.getSunFactor(level);
        if (sunFactor == 0) return 0;

        double weatherFactor = SolarPanelCalculations.getWeatherFactor(level);
        double altitudeFactor = SolarPanelCalculations.getAltitudeFactor(pos.getY());
        double temperatureFactor = SolarPanelCalculations.getTemperatureFactor(level, pos);

        double finalFactor = sunFactor * weatherFactor * altitudeFactor * temperatureFactor;
        return (int)(SolarConfig.SOLAR_PANEL_MAX_OUTPUT.get() * finalFactor);
    }

    public void setActive(boolean to) {
        this.active = to;

        assert level != null;

        BlockPos pos = this.getBlockPos();
        this.level.setBlockAndUpdate(pos, getBlockState().setValue(ACTIVE, to));
        notifyUpdate();
    }

    public int getRedstoneOutput() {
        return lastRedstoneOutput;
    }

    @Nullable
    public static IEnergyStorage getCapability(SolarPanelBlockEntity be, Direction side) {
        if (side == Direction.DOWN) return be.energyStorage;
        return null;
    }

    SolarPanelEnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }

    @Override
    public void write(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(tag, registries, clientPacket);

        tag.putBoolean("Active", active);
        tag.putInt("NextUpdate", nextUpdate);
        tag.putInt("LastRedstoneOutput", lastRedstoneOutput);
        tag.put("Energy", energyStorage.serializeNBT(registries));
    }

    @Override
    protected void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(tag, registries, clientPacket);

        this.active = tag.getBoolean("Active");
        this.nextUpdate = tag.getInt("NextUpdate");
        this.lastRedstoneOutput = tag.getInt("LastRedstoneOutput");

        Tag energy = tag.get("Energy");
        if (energy == null) return;
        this.energyStorage.deserializeNBT(registries, energy);
    }
}
