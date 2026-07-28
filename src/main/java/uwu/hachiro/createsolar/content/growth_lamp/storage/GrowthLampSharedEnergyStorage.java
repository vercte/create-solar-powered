package uwu.hachiro.createsolar.content.growth_lamp.storage;

import net.neoforged.neoforge.energy.IEnergyStorage;
import uwu.hachiro.createsolar.SolarConfig;
import uwu.hachiro.createsolar.content.growth_lamp.GrowthLampBlockEntity;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;

public class GrowthLampSharedEnergyStorage implements IEnergyStorage {
    private final Set<WeakReference<GrowthLampBlockEntity>> lamps;
    private boolean locked;

    public GrowthLampSharedEnergyStorage() {
        lamps = new HashSet<>();
        locked = false;
    }

    public void combineInto(GrowthLampSharedEnergyStorage newStorage) {
        getSet().forEach(newStorage::assimilate);
        lamps.clear();
        locked = true;
    }

    public void assimilate(GrowthLampBlockEntity lamp) {
        assert lamp.getLevel() != null;
        if (locked()) throw new UnsupportedOperationException("Cannot use locked GrowthLampSharedEnergyStorage");

        for (GrowthLampBlockEntity be : getSet()) {
            if (be == lamp) return;
        }

        lamp.setSharedEnergyStorage(this);
        lamps.add(new WeakReference<>(lamp));
    }

    public void remove(GrowthLampBlockEntity lamp) {
        lamps.removeIf(reference -> lamp == reference.get());
    }

    private boolean locked() {
        return locked;
    }

    public int getLampCount() {
        return getSet().size();
    }

    @Override
    public int receiveEnergy(int amount, boolean simulate) {
        if (locked()) throw new UnsupportedOperationException("Cannot use locked GrowthLampSharedEnergyStorage");

        Set<GrowthLampBlockEntity> set = getSet();
        if (set.isEmpty()) return 0;

        int perLamp = amount / set.size();
        int remainder = amount % set.size();
        int accepted = 0;

        for (GrowthLampBlockEntity lamp : set) {
            int toAdd = perLamp + (remainder > 0 ? 1 : 0);
            if (remainder > 0) remainder--;
            int added = lamp.receiveEnergy(toAdd, simulate);
            accepted += added;
        }

        return accepted;
    }

    @Override
    public int extractEnergy(int amount, boolean simulate) {
        return 0;
    }

    @Override
    public int getEnergyStored() {
        if (locked()) throw new UnsupportedOperationException("Cannot use locked GrowthLampSharedEnergyStorage");

        int sum = 0;
        for (GrowthLampBlockEntity lamp : getSet()) sum += lamp.getEnergyStored();
        return sum;
    }

    @Override
    public int getMaxEnergyStored() {
        return getLampCount() * SolarConfig.GROWTH_LAMP_MAX_ENERGY_STORED.get();
    }

    @Override
    public boolean canExtract() {
        return false;
    }

    @Override
    public boolean canReceive() {
        return true;
    }

    private Set<GrowthLampBlockEntity> getSet() {
        Set<GrowthLampBlockEntity> output = new HashSet<>();
        lamps.removeIf(ref -> {
            GrowthLampBlockEntity be = ref.get();
            if (be != null) output.add(be);
            return be == null;
        });
        return output;
    }
}
