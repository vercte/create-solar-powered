package uwu.hachiro.createsolar.content.panel;

import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;
import uwu.hachiro.createsolar.SolarConfig;
import uwu.hachiro.createsolar.content.panel.storage.SolarPanelDefaultEnergyStorage;
import uwu.hachiro.createsolar.content.panel.storage.SolarPanelExposedEnergyStorage;
import uwu.hachiro.createsolar.content.panel.storage.SolarPanelSharedEnergyStorage;
import uwu.hachiro.createsolar.content.panel.storage.SolarPanelSharedEnergyStoragePropagator;
import uwu.hachiro.createsolar.network.packet.RequestEnergyPacketC2S;
import uwu.hachiro.createsolar.network.packet.EnergyResultPacketS2C;
import uwu.hachiro.createsolar.util.SolarLang;

import java.util.List;

import static uwu.hachiro.createsolar.content.panel.SolarPanelBlock.ACTIVE;

public class SolarPanelBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {
    private SolarPanelSharedEnergyStorage sharedStorage;
    private final SolarPanelExposedEnergyStorage exposedStorage;
    private final SolarPanelDefaultEnergyStorage defaultStorage;
    private boolean active;
    private int energy;
    private int nextUpdate;
    private int lastOutput;
    private int lastRedstoneOutput;

    public int sharedHash = -1;

    public SolarPanelBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);

        this.sharedStorage = null;
        this.exposedStorage = new SolarPanelExposedEnergyStorage(this);
        this.defaultStorage = new SolarPanelDefaultEnergyStorage(this);
        this.active = false;
        this.nextUpdate = SolarConfig.SOLAR_PANEL_UPDATE_INTERVAL.get();
        this.lastRedstoneOutput = 0;
    }

    @Override
    public void tick() {
        super.tick();

        assert level != null;

        if(level.isClientSide()) return;

        if (nextUpdate-- > 0) return;
        nextUpdate = SolarConfig.SOLAR_PANEL_UPDATE_INTERVAL.get();

        if(sharedStorage == null) {
            SolarPanelSharedEnergyStoragePropagator.propagateStartingAt(level, worldPosition);
        }

        int output = calculateOutput();
        boolean nowActive = output > 0;
        if (active != nowActive) setActive(nowActive);

        lastOutput = output;

        int redstoneOutput = (int)(
                Mth.clamp(0, (double)output / SolarConfig.SOLAR_PANEL_MAX_OUTPUT.get(), 1) * 15
        );
        if(redstoneOutput != lastRedstoneOutput) {
            lastRedstoneOutput = redstoneOutput;
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
            level.updateNeighborsAt(worldPosition, getBlockState().getBlock());
        }

        addEnergy(output);
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

    public void setActive(boolean active) {
        this.active = active;

        assert level != null;

        level.setBlockAndUpdate(worldPosition, getBlockState().setValue(ACTIVE, active));
        notifyUpdate();
    }

    public int getLastOutput() {
        return lastOutput;
    }

    public int getRedstoneOutput() {
        return lastRedstoneOutput;
    }

    @Nullable
    public static IEnergyStorage getCapability(SolarPanelBlockEntity be, Direction side) {
        if (side == Direction.DOWN) {
            if(be.sharedStorage == null) SolarPanelSharedEnergyStoragePropagator.propagateStartingAt(be.getLevel(), be.getBlockPos());
            return be.exposedStorage;
        } else if(side == null) {
            return be.sharedStorage != null ? be.sharedStorage : be.defaultStorage;
        }

        return null;
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        RequestEnergyPacketC2S.send(getBlockPos());

        SolarLang.builder().add(Component.translatable("createsolar.tooltip.solar_panel.info")
                .withStyle(ChatFormatting.WHITE))
                .forGoggles(tooltip);

        int outputPerSecond = EnergyResultPacketS2C.getLastOutput() *
                (20 / SolarConfig.SOLAR_PANEL_UPDATE_INTERVAL.getAsInt());
        int outputPercentage = (int)((double)EnergyResultPacketS2C.getLastOutput() / SolarConfig.SOLAR_PANEL_MAX_OUTPUT.getAsInt() * 100);

        SolarLang.builder().add(Component.translatable("createsolar.tooltip.solar_panel.output")
                .withStyle(ChatFormatting.GRAY))
                .forGoggles(tooltip);
        SolarLang.builder().add(Component.literal(" ")
                .append(SolarLang.formatEnergy(outputPerSecond)).append("⚡/s (" + outputPercentage + "%)")
                .withStyle(ChatFormatting.AQUA)).forGoggles(tooltip);

        SolarLang.builder().add(Component.translatable("createsolar.tooltip.energy.stored")
                .withStyle(ChatFormatting.GRAY))
                .forGoggles(tooltip);
        SolarLang.builder().add(Component.literal(" ")
                .append(SolarLang.formatEnergy(EnergyResultPacketS2C.getLastEnergy())).append("⚡")
                .withStyle(ChatFormatting.AQUA)).forGoggles(tooltip);

        SolarLang.builder().add(Component.translatable("createsolar.tooltip.energy.capacity").withStyle(ChatFormatting.GRAY))
                .forGoggles(tooltip);
        SolarLang.builder().add(Component.literal(" ")
                .append(SolarLang.formatEnergy(EnergyResultPacketS2C.getLastCapacity())).append("⚡")
                .withStyle(ChatFormatting.AQUA)).forGoggles(tooltip);

        return true;
    }

    // region Energy Storage
    @Nullable
    public SolarPanelSharedEnergyStorage getSharedEnergyStorage() {
        return sharedStorage;
    }

    public void setSharedEnergyStorage(SolarPanelSharedEnergyStorage sharedStorage) {
        this.sharedStorage = sharedStorage;
    }

    public int getEnergyStored() {
        return energy;
    }

    public void addEnergy(int amount) {
        int max = SolarConfig.SOLAR_PANEL_MAX_ENERGY_STORED.getAsInt();
        int toAdd = Math.min(amount, max - energy);
        energy += toAdd;
        notifyUpdate();
    }

    public int extractEnergy(int amount, boolean simulate) {
        int toExtract = Math.clamp(amount, 0, energy);
        if(!simulate) energy -= toExtract;
        return toExtract;
    }
    // endregion

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }

    @Override
    public void write(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(tag, registries, clientPacket);

        tag.putBoolean("Active", active);
        tag.putInt("NextUpdate", nextUpdate);
        tag.putInt("LastRedstoneOutput", lastRedstoneOutput);
        tag.putInt("Energy", energy);
    }

    @Override
    protected void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(tag, registries, clientPacket);

        this.active = tag.getBoolean("Active");
        this.nextUpdate = tag.getInt("NextUpdate");
        this.lastRedstoneOutput = tag.getInt("LastRedstoneOutput");
        this.energy = tag.getInt("Energy");
    }
}
