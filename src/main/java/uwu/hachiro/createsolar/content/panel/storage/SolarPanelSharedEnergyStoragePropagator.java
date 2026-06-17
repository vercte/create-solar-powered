package uwu.hachiro.createsolar.content.panel.storage;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import uwu.hachiro.createsolar.content.panel.SolarPanelBlockEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SolarPanelSharedEnergyStoragePropagator {
    public static SolarPanelSharedEnergyStorage propagateStartingAt(Level level, BlockPos pos) {
        SolarPanelSharedEnergyStorage storage = new SolarPanelSharedEnergyStorage();
        List<BlockPos> positions = new ArrayList<>();

        SolarPanelSharedEnergyStorage propagated = propagateTo(level, pos, positions, storage);

        return propagated;
    }

    private static SolarPanelSharedEnergyStorage propagateTo(Level level, BlockPos pos, List<BlockPos> visited, SolarPanelSharedEnergyStorage storage) {
        visited.add(pos);
        SolarPanelSharedEnergyStorage result = storage;
        if(level.getBlockEntity(pos) instanceof SolarPanelBlockEntity panel) {
            SolarPanelSharedEnergyStorage nextStorage = panel.getSharedEnergyStorage();
            if(nextStorage != null && nextStorage != result) {
                result.combineInto(nextStorage);
                Minecraft.getInstance().player.sendSystemMessage(Component.literal("got that shit!! " + UUID.randomUUID()));
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
