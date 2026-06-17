package uwu.hachiro.createsolar.content.panel.storage;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import uwu.hachiro.createsolar.content.panel.SolarPanelBlockEntity;

import java.util.*;

public class SolarPanelSharedEnergyStoragePropagator {
    public static void trySplit(Level level, BlockPos pos) {
        Map<Direction, Set<SolarPanelBlockEntity>> discovered = new HashMap<>();
        for(Direction dir : Direction.Plane.HORIZONTAL) {
            Set<BlockPos> visited = new HashSet<>();

            visited.add(pos);

            BlockPos next = pos.relative(dir);
            visited.add(next);
            Set<SolarPanelBlockEntity> discoveredHere = crawlForSplitHere(visited, level, next);
            if(!discoveredHere.isEmpty()) discovered.put(dir, discoveredHere);
        }

        List<Set<SolarPanelBlockEntity>> list = new ArrayList<>(discovered.values());
        boolean allEqual = list.stream().allMatch(s -> s.equals(list.getFirst()));
        if(allEqual) return;

        discovered.forEach((d, s) -> {
            s.forEach(p -> p.setSharedEnergyStorage(null));

            propagateStartingAt(level, pos.relative(d), pos);
        });
    }

    private static Set<SolarPanelBlockEntity> crawlForSplitHere(Set<BlockPos> visited, Level level, BlockPos pos) {
        Set<SolarPanelBlockEntity> total = new HashSet<>();
        if(!(level.getBlockEntity(pos) instanceof SolarPanelBlockEntity be)) return total;

        total.add(be);
        for(Direction dir : Direction.Plane.HORIZONTAL) {
            BlockPos next = pos.relative(dir);
            if(visited.contains(next)) continue;

            visited.add(next);
            total.addAll(crawlForSplitHere(visited, level, next));
        }
        return total;
    }

    @SuppressWarnings("UnusedReturnValue")
    public static SolarPanelSharedEnergyStorage propagateStartingAt(Level level, BlockPos pos) {
        return propagateStartingAt(level, pos, null);
    }

    public static SolarPanelSharedEnergyStorage propagateStartingAt(Level level, BlockPos pos, BlockPos exclude) {
        SolarPanelSharedEnergyStorage storage = new SolarPanelSharedEnergyStorage();
        Set<BlockPos> visited = new HashSet<>();
        visited.add(exclude);

        return propagateTo(level, pos, visited, storage);
    }

    private static SolarPanelSharedEnergyStorage propagateTo(Level level, BlockPos pos, Set<BlockPos> visited, SolarPanelSharedEnergyStorage storage) {
        visited.add(pos);
        SolarPanelSharedEnergyStorage result = storage;
        if(level.getBlockEntity(pos) instanceof SolarPanelBlockEntity panel) {
            SolarPanelSharedEnergyStorage nextStorage = panel.getSharedEnergyStorage();
            if(nextStorage != null && nextStorage != result) {
                result.combineInto(nextStorage);
                result = nextStorage;
            } else result.assimilate(panel);

            for(Direction dir : Direction.Plane.HORIZONTAL) {
                BlockPos next = pos.relative(dir);
                if(visited.contains(next)) continue;
                result = propagateTo(level, next, visited, result);
            }
        }
        return result;
    }
}
