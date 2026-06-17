package uwu.hachiro.createsolar.content.panel.storage;

import net.neoforged.neoforge.energy.IEnergyStorage;
import uwu.hachiro.createsolar.SolarConfig;
import uwu.hachiro.createsolar.content.panel.SolarPanelBlockEntity;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;

public class SolarPanelSharedEnergyStorage implements IEnergyStorage {
    private final Set<WeakReference<SolarPanelBlockEntity>> storages;
    private boolean locked;

    public SolarPanelSharedEnergyStorage() {
        storages = new HashSet<>();
        locked = false;
    }

    public void combineInto(SolarPanelSharedEnergyStorage newStorage) {
        getSet().forEach(be -> {
            be.setSharedEnergyStorage(newStorage);
            newStorage.storages.add(new WeakReference<>(be));
        });

        storages.clear();
        locked = true;
    }

    public void assimilate(SolarPanelBlockEntity entity) {
        assert entity.getLevel() != null;
        if(locked()) throw new UnsupportedOperationException("Cannot use locked SolarPanelSharedEnergyStorage");

        entity.setSharedEnergyStorage(this);
        storages.add(new WeakReference<>(entity));
    }

    private boolean locked() {
        return locked;
    }

    public int getPanels() {
        return storages.size();
    }

    @Override
    public int receiveEnergy(int amount, boolean simulate) {
        return 0;
    }

    @Override
    public int extractEnergy(int amount, boolean simulate) {
        return extractEnergy(amount, simulate, null);
    }

    public int extractEnergy(int amount, boolean simulate, SolarPanelBlockEntity ignored) {
        if(locked()) throw new UnsupportedOperationException("Cannot use locked SolarPanelSharedEnergyStorage");

        int left = amount;
        for(SolarPanelBlockEntity panel : getSet()) {
            if(panel == ignored) continue;
            if(left <= 0) break;
            left -= panel.extractEnergy(left, simulate);
        }
        return amount - left;
    }

    @Override
    public int getEnergyStored() {
        if(locked()) throw new UnsupportedOperationException("Cannot use locked SolarPanelSharedEnergyStorage");

        int sum = 0;
        for(SolarPanelBlockEntity storage : getSet()) sum += storage.getEnergyStored();
        return sum;
    }

    private Set<SolarPanelBlockEntity> getSet() {
        Set<WeakReference<SolarPanelBlockEntity>> toRemove = new HashSet<>();
        Set<SolarPanelBlockEntity> output = new HashSet<>();
        for(WeakReference<SolarPanelBlockEntity> weakReference : storages) {
            if(weakReference.get() != null) {
                output.add(weakReference.get());
            } else toRemove.add(weakReference);
        }

        toRemove.forEach(storages::remove);

        return output;
    }

    @Override
    public int getMaxEnergyStored() {
        return getPanels() * SolarConfig.SOLAR_PANEL_MAX_ENERGY_STORED.get();
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
