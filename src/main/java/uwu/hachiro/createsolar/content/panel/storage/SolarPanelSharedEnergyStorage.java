package uwu.hachiro.createsolar.content.panel.storage;

import com.mojang.logging.LogUtils;
import net.neoforged.neoforge.energy.IEnergyStorage;
import uwu.hachiro.createsolar.SolarConfig;
import uwu.hachiro.createsolar.content.panel.SolarPanelBlockEntity;

import java.util.WeakHashMap;

public class SolarPanelSharedEnergyStorage implements IEnergyStorage {
    private final WeakHashMap<SolarPanelBlockEntity, SolarPanelEnergyStorage> storages;
    private boolean client = false;
    private boolean locked;

    public SolarPanelSharedEnergyStorage() {
        storages = WeakHashMap.newWeakHashMap(16);
        locked = false;
    }

    public void combineInto(SolarPanelSharedEnergyStorage storage) {
        storage.storages.putAll(storages);
        storages.keySet().forEach(b -> b.setSharedEnergyStorage(storage));
        storage.storages.clear();
        locked = true;
    }

    public void assimilate(SolarPanelBlockEntity entity) {
        client = entity.getLevel().isClientSide();
        if(locked()) throw new UnsupportedOperationException("Cannot use locked SolarPanelSharedEnergyStorage");
        entity.setSharedEnergyStorage(this);
        storages.put(entity, entity.getEnergyStorage());
    }

    private boolean locked() {
        return locked;
    }

    public int getPanels() {
        return storages.size();
    }

    @Override
    public int receiveEnergy(int toReceive, boolean simulate) {
        return 0;
    }

    @Override
    public int extractEnergy(int toExtract, boolean simulate) {
        if(locked()) throw new UnsupportedOperationException("Cannot use locked SolarPanelSharedEnergyStorage");

        int left = toExtract;
        for(SolarPanelEnergyStorage storage : storages.values()) {
            if(left <= 0) break;
            left -= storage.extractEnergy(left, simulate);
        }
        return toExtract - left;
    }

    @Override
    public int getEnergyStored() {
        if(locked()) throw new UnsupportedOperationException("Cannot use locked SolarPanelSharedEnergyStorage");

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

    @Override
    public String toString() {
        return "⚡&" + Integer.toHexString(hashCode());
    }
}
