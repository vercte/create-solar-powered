package uwu.hachiro.createsolar.content.panel.storage;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import uwu.hachiro.createsolar.SolarConfig;
import uwu.hachiro.createsolar.content.panel.SolarPanelBlockEntity;

public class SolarPanelEnergyStorage implements IEnergyStorage, INBTSerializable<Tag> {
    protected SolarPanelBlockEntity parent;
    protected int energy;

    public SolarPanelEnergyStorage(SolarPanelBlockEntity parent, int energy) {
        this.parent = parent;
        this.energy = energy;
    }

    @Override
    public int receiveEnergy(int toReceive, boolean simulate) {
        return 0;
    }

    public int insertEnergy(int toReceive) {
        if (toReceive <= 0) return 0;

        int energyReceived = Mth.clamp(getMaxEnergyStored() - this.energy, 0, toReceive);
        this.energy += energyReceived;

        parent.notifyUpdate();

        return energyReceived;
    }

    @Override
    public int extractEnergy(int toExtract, boolean simulate) {
        if (toExtract <= 0) return 0;

        int energyExtracted = Math.min(this.energy, toExtract);
        if (!simulate) {
            this.energy -= energyExtracted;
            parent.setChanged();
        }
        return energyExtracted;
    }

    @Override
    public int getEnergyStored() {
        return this.energy;
    }

    @Override
    public int getMaxEnergyStored() {
        return SolarConfig.SOLAR_PANEL_MAX_ENERGY_STORED.getAsInt();
    }

    @Override
    public boolean canExtract() {
        return true;
    }

    @Override
    public boolean canReceive() {
        return false;
    }

    @Override
    public Tag serializeNBT(@NotNull HolderLookup.Provider provider) {
        return IntTag.valueOf(this.getEnergyStored());
    }

    @Override
    public void deserializeNBT(@NotNull HolderLookup.Provider provider, @NotNull Tag nbt) {
        if (!(nbt instanceof IntTag intNbt))
            throw new IllegalArgumentException("Can not deserialize to an instance that isn't the default implementation");
        this.energy = intNbt.getAsInt();
    }
}
