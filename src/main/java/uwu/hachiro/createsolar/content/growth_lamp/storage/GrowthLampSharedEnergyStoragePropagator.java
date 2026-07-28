package uwu.hachiro.createsolar.content.growth_lamp.storage;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import uwu.hachiro.createsolar.content.growth_lamp.GrowthLampBlockEntity;

import java.util.*;

public class GrowthLampSharedEnergyStoragePropagator {
    public static void trySplit(Level level, BlockPos pos) {
        Map<Direction, Set<GrowthLampBlockEntity>> discovered = new HashMap<>();
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            Set<BlockPos> visited = new HashSet<>();
            visited.add(pos);

            BlockPos next = pos.relative(dir);
            visited.add(next);
            Set<GrowthLampBlockEntity> discoveredHere = crawlForSplitHere(visited, level, next);
            if (!discoveredHere.isEmpty()) discovered.put(dir, discoveredHere);
        }

        List<Set<GrowthLampBlockEntity>> list = new ArrayList<>(discovered.values());
        boolean allEqual = list.stream().allMatch(s -> s.equals(list.getFirst()));
        if (allEqual) return;

        discovered.forEach((d, s) -> {
            s.forEach(p -> p.setSharedEnergyStorage(null));

            propagateStartingAt(level, pos.relative(d), pos);
        });
    }

    private static Set<GrowthLampBlockEntity> crawlForSplitHere(Set<BlockPos> visited, Level level, BlockPos pos) {
        Set<GrowthLampBlockEntity> total = new HashSet<>();
        if (!(level.getBlockEntity(pos) instanceof GrowthLampBlockEntity be)) return total;

        total.add(be);
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            BlockPos next = pos.relative(dir);
            if (visited.contains(next)) continue;

            visited.add(next);
            total.addAll(crawlForSplitHere(visited, level, next));
        }
        return total;
    }

    @SuppressWarnings("UnusedReturnValue")
    public static GrowthLampSharedEnergyStorage propagateStartingAt(Level level, BlockPos pos) {
        return propagateStartingAt(level, pos, null);
    }

    public static GrowthLampSharedEnergyStorage propagateStartingAt(Level level, BlockPos pos, BlockPos exclude) {
        GrowthLampSharedEnergyStorage storage = new GrowthLampSharedEnergyStorage();
        Set<BlockPos> visited = new HashSet<>();
        visited.add(exclude);

        return propagateTo(level, pos, visited, storage);
    }

    private static GrowthLampSharedEnergyStorage propagateTo(Level level, BlockPos pos, Set<BlockPos> visited, GrowthLampSharedEnergyStorage storage) {
        visited.add(pos);
        GrowthLampSharedEnergyStorage result = storage;
        if (level.getBlockEntity(pos) instanceof GrowthLampBlockEntity lamp) {
            GrowthLampSharedEnergyStorage nextStorage = lamp.getSharedEnergyStorage();
            if (nextStorage != null && nextStorage != result) {
                result.combineInto(nextStorage);
                result = nextStorage;
            } else result.assimilate(lamp);

            for (Direction dir : Direction.Plane.HORIZONTAL) {
                BlockPos next = pos.relative(dir);
                if (visited.contains(next)) continue;
                result = propagateTo(level, next, visited, result);
            }
        }
        return result;
    }
}
