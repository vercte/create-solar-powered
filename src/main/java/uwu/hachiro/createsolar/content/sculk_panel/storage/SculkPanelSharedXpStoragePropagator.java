package uwu.hachiro.createsolar.content.sculk_panel.storage;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import uwu.hachiro.createsolar.content.sculk_panel.SculkPanelBlockEntity;

import java.util.*;

public class SculkPanelSharedXpStoragePropagator {
    public static void trySplit(Level level, BlockPos pos) {
        Map<Direction, Set<SculkPanelBlockEntity>> discovered = new HashMap<>();
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            Set<BlockPos> visited = new HashSet<>();
            visited.add(pos);

            BlockPos next = pos.relative(dir);
            visited.add(next);
            Set<SculkPanelBlockEntity> discoveredHere = crawlForSplitHere(visited, level, next);
            if (!discoveredHere.isEmpty()) discovered.put(dir, discoveredHere);
        }

        List<Set<SculkPanelBlockEntity>> list = new ArrayList<>(discovered.values());
        boolean allEqual = list.stream().allMatch(s -> s.equals(list.getFirst()));
        if (allEqual) return;

        discovered.forEach((d, s) -> {
            s.forEach(p -> p.setSharedXpStorage(null));
            propagateStartingAt(level, pos.relative(d), pos);
        });
    }

    private static Set<SculkPanelBlockEntity> crawlForSplitHere(Set<BlockPos> visited, Level level, BlockPos pos) {
        Set<SculkPanelBlockEntity> total = new HashSet<>();
        if (!(level.getBlockEntity(pos) instanceof SculkPanelBlockEntity be)) return total;

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
    public static SculkPanelSharedXpStorage propagateStartingAt(Level level, BlockPos pos) {
        return propagateStartingAt(level, pos, null);
    }

    public static SculkPanelSharedXpStorage propagateStartingAt(Level level, BlockPos pos, BlockPos exclude) {
        SculkPanelSharedXpStorage storage = new SculkPanelSharedXpStorage();
        Set<BlockPos> visited = new HashSet<>();
        visited.add(exclude);

        return propagateTo(level, pos, visited, storage);
    }

    private static SculkPanelSharedXpStorage propagateTo(Level level, BlockPos pos, Set<BlockPos> visited, SculkPanelSharedXpStorage storage) {
        visited.add(pos);
        SculkPanelSharedXpStorage result = storage;
        if (level.getBlockEntity(pos) instanceof SculkPanelBlockEntity panel) {
            SculkPanelSharedXpStorage nextStorage = panel.getSharedXpStorage();
            if (nextStorage != null && nextStorage != result) {
                result.combineInto(nextStorage);
                result = nextStorage;
            } else result.assimilate(panel);

            for (Direction dir : Direction.Plane.HORIZONTAL) {
                BlockPos next = pos.relative(dir);
                if (visited.contains(next)) continue;
                result = propagateTo(level, next, visited, result);
            }
        }
        return result;
    }
}
