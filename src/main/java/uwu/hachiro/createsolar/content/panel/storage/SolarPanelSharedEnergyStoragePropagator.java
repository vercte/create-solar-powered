package uwu.hachiro.createsolar.content.panel.storage;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import uwu.hachiro.createsolar.content.panel.SolarPanelBlockEntity;

import java.util.ArrayList;
import java.util.List;

public class SolarPanelSharedEnergyStoragePropagator {
    @SuppressWarnings("UnusedReturnValue")
    public static SolarPanelSharedEnergyStorage propagateStartingAt(Level level, BlockPos pos) {
        SolarPanelSharedEnergyStorage storage = new SolarPanelSharedEnergyStorage();
        List<BlockPos> positions = new ArrayList<>();

        return propagateTo(level, pos, positions, storage);
    }

    private static SolarPanelSharedEnergyStorage propagateTo(Level level, BlockPos pos, List<BlockPos> visited, SolarPanelSharedEnergyStorage storage) {
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
