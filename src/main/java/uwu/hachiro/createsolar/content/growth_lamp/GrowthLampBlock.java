package uwu.hachiro.createsolar.content.growth_lamp;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uwu.hachiro.createsolar.SolarBlockEntities;
import uwu.hachiro.createsolar.content.growth_lamp.storage.GrowthLampSharedEnergyStoragePropagator;

public class GrowthLampBlock extends Block implements IWrenchable, IBE<GrowthLampBlockEntity> {
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
    private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 16, 16);

    public GrowthLampBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(ACTIVE, false));
    }

    @Override
    public void setPlacedBy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable LivingEntity entity, @NotNull ItemStack stack) {
        GrowthLampSharedEnergyStoragePropagator.propagateStartingAt(level, pos);
    }

    @Override
    protected void onRemove(@NotNull BlockState original, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState replacement, boolean isMoving) {
        if (!original.is(replacement.getBlock())) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof GrowthLampBlockEntity lamp && lamp.getSharedEnergyStorage() != null) {
                GrowthLampSharedEnergyStoragePropagator.trySplit(level, pos);
                lamp.getSharedEnergyStorage().remove(lamp);
            }
        }
        super.onRemove(original, level, pos, replacement, isMoving);
    }

    @Override
    public boolean canConnectRedstone(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @Nullable Direction direction) {
        return true;
    }

    @Override
    protected void createBlockStateDefinition(@NotNull StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(ACTIVE);
    }

    @Override
    @NotNull
    public VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected boolean useShapeForLightOcclusion(@NotNull BlockState state) {
        return true;
    }

    @Override
    public Class<GrowthLampBlockEntity> getBlockEntityClass() {
        return GrowthLampBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends GrowthLampBlockEntity> getBlockEntityType() {
        return SolarBlockEntities.GROWTH_LAMP.get();
    }
}
