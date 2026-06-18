package uwu.hachiro.createsolar.content.panel.storage;

import net.neoforged.neoforge.energy.IEnergyStorage;
import uwu.hachiro.createsolar.SolarConfig;
import uwu.hachiro.createsolar.content.panel.SolarPanelBlockEntity;

public class SolarPanelExposedEnergyStorage implements IEnergyStorage {
    private final SolarPanelBlockEntity parent;

    public SolarPanelExposedEnergyStorage(SolarPanelBlockEntity parent) {
        this.parent = parent;
    }

    @Override
    public int receiveEnergy(int amount, boolean simulate) {
        return 0;
    }

    @Override
    public int extractEnergy(int amount, boolean simulate) {
        int fromParent = parent.extractEnergy(amount, simulate);
        if(fromParent < amount && getShared() != null) {
            int fromShared = getShared().extractEnergy(amount - fromParent, simulate, parent);
            return fromParent + fromShared;
        }

        return fromParent;
    }

    @Override
    public int getEnergyStored() {
        return getShared() != null ? getShared().getEnergyStored() : parent.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored() {
        return getShared() != null ? getShared().getMaxEnergyStored() : SolarConfig.SOLAR_PANEL_MAX_ENERGY_STORED.getAsInt();
    }

    @Override
    public boolean canExtract() {
        return true;
    }

    @Override
    public boolean canReceive() {
        return false;
    }

    private SolarPanelSharedEnergyStorage getShared() {
        return parent.getSharedEnergyStorage();
    }
}
