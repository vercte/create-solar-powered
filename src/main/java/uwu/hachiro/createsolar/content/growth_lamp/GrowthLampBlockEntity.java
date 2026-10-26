package uwu.hachiro.createsolar.content.growth_lamp;

import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;
import uwu.hachiro.createsolar.SolarBlockEntities;

import java.util.List;

public class GrowthLampBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {
    private final IEnergyStorage capability;

    protected GrowthLampBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.capability = new EnergyStorage(1000);
    }

    public GrowthLampBlockEntity(BlockPos pos, BlockState state) {
        this(SolarBlockEntities.GROWTH_LAMP.get(), pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }

    @Nullable
    public static IEnergyStorage getCapability(GrowthLampBlockEntity be, Direction side) {
        return side == Direction.DOWN || side == null ? be.capability : null;
    }
}
