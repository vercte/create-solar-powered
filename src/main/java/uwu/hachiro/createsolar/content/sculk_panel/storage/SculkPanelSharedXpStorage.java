package uwu.hachiro.createsolar.content.sculk_panel.storage;

import uwu.hachiro.createsolar.content.sculk_panel.SculkPanelBlock;
import uwu.hachiro.createsolar.content.sculk_panel.SculkPanelBlockEntity;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;

public class SculkPanelSharedXpStorage {
    private final Set<WeakReference<SculkPanelBlockEntity>> panels;
    private boolean locked;

    public SculkPanelSharedXpStorage() {
        panels = new HashSet<>();
        locked = false;
    }

    public void combineInto(SculkPanelSharedXpStorage newStorage) {
        getSet().forEach(newStorage::assimilate);
        panels.clear();
        locked = true;
    }

    public void assimilate(SculkPanelBlockEntity panel) {
        assert panel.getLevel() != null;
        if (locked()) throw new UnsupportedOperationException("Cannot use locked SculkPanelSharedXpStorage");

        for (SculkPanelBlockEntity be : getSet()) {
            if (be == panel) return;
        }

        panel.setSharedXpStorage(this);
        panels.add(new WeakReference<>(panel));
    }

    public void remove(SculkPanelBlockEntity panel) {
        panels.removeIf(reference -> panel == reference.get());
    }

    private boolean locked() {
        return locked;
    }

    public int getTotalXp() {
        if (locked()) throw new UnsupportedOperationException("Cannot use locked SculkPanelSharedXpStorage");

        int sum = 0;
        for (SculkPanelBlockEntity storage : getSet()) sum += storage.getStoredXp();
        return sum;
    }

    public int collectXp() {
        if (locked()) throw new UnsupportedOperationException("Cannot use locked SculkPanelSharedXpStorage");

        int total = 0;
        for (SculkPanelBlockEntity panel : getSet()) {
            total += panel.collectXp();
        }
        return total;
    }

    public void addXp(int amount) {
        if (locked()) throw new UnsupportedOperationException("Cannot use locked SculkPanelSharedXpStorage");
        if (amount <= 0) return;

        Set<SculkPanelBlockEntity> set = getSet();
        if (set.isEmpty()) return;

        int perPanel = amount / set.size();
        int remainder = amount % set.size();
        for (SculkPanelBlockEntity panel : set) {
            panel.addXp(perPanel + (remainder > 0 ? 1 : 0));
            if (remainder > 0) remainder--;
        }
    }

    public int getPanelCount() {
        return getSet().size();
    }

    private Set<SculkPanelBlockEntity> getSet() {
        Set<SculkPanelBlockEntity> output = new HashSet<>();
        panels.removeIf(ref -> {
            SculkPanelBlockEntity be = ref.get();
            if (be != null) output.add(be);
            return be == null;
        });
        return output;
    }
}
