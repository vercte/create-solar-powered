package uwu.hachiro.createsolar.content.panel.storage;

import net.neoforged.neoforge.energy.IEnergyStorage;
import uwu.hachiro.createsolar.SolarConfig;
import uwu.hachiro.createsolar.content.panel.SolarPanelBlockEntity;

public class SolarPanelDefaultEnergyStorage implements IEnergyStorage {
    private final SolarPanelBlockEntity parent;

    public SolarPanelDefaultEnergyStorage(SolarPanelBlockEntity parent) {
        this.parent = parent;
    }

    @Override
    public int receiveEnergy(int i, boolean b) {
        return 0;
    }

    @Override
    public int extractEnergy(int i, boolean b) {
        return 0;
    }

    @Override
    public int getEnergyStored() {
        return parent.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored() {
        return SolarConfig.SOLAR_PANEL_MAX_ENERGY_STORED.getAsInt();
    }

    @Override
    public boolean canExtract() {
        return false;
    }

    @Override
    public boolean canReceive() {
        return false;
    }
}
