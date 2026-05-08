package uwu.hachiro.createsolar.content.panel;

import net.neoforged.neoforge.energy.IEnergyStorage;
import uwu.hachiro.createsolar.SolarConfig;

import java.util.WeakHashMap;

public class SolarPanelSharedEnergyStorage implements IEnergyStorage {
    private WeakHashMap<SolarPanelBlockEntity, SolarPanelEnergyStorage> storages;

    public void assimilate(SolarPanelBlockEntity entity) {
        storages.put(entity, entity.getEnergyStorage());
    }

    @Override
    public int receiveEnergy(int toReceive, boolean simulate) {
        return 0;
    }

    @Override
    public int extractEnergy(int toExtract, boolean simulate) {
        int left = toExtract;
        for(SolarPanelEnergyStorage storage : storages.values()) {
            if(left <= 0) break;
            left -= storage.extractEnergy(left, simulate);
        }
        return toExtract - left;
    }

    @Override
    public int getEnergyStored() {
        int sum = 0;
        for(SolarPanelEnergyStorage storage : storages.values()) sum += storage.getEnergyStored();
        return sum;
    }

    @Override
    public int getMaxEnergyStored() {
        return storages.size() * SolarConfig.SOLAR_PANEL_MAX_ENERGY_STORED.get();
    }

    @Override
    public boolean canExtract() {
        return true;
    }

    @Override
    public boolean canReceive() {
        return false;
    }
}
