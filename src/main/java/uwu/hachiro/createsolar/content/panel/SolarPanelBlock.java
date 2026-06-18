package uwu.hachiro.createsolar.content.panel;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
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
import uwu.hachiro.createsolar.SolarParticles;
import uwu.hachiro.createsolar.content.panel.storage.SolarPanelSharedEnergyStoragePropagator;

public class SolarPanelBlock extends Block implements IWrenchable, IBE<SolarPanelBlockEntity> {
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
    private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 8, 16);

    public SolarPanelBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(ACTIVE, false));
    }

    @Override
    public void setPlacedBy(@NotNull Level level, @NotNull BlockPos blockPos, @NotNull BlockState state, @Nullable LivingEntity entity, @NotNull ItemStack stack) {
        SolarPanelSharedEnergyStoragePropagator.propagateStartingAt(level, blockPos);
    }

    @Override
    protected void onRemove(@NotNull BlockState original, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull BlockState replacement, boolean isMoving) {
        if(!original.is(replacement.getBlock())) {
            BlockEntity be = level.getBlockEntity(blockPos);
            if(be instanceof SolarPanelBlockEntity panel && panel.getSharedEnergyStorage() != null) {
                SolarPanelSharedEnergyStoragePropagator.trySplit(level, blockPos);
                panel.getSharedEnergyStorage().remove(panel);
            }
        }
        super.onRemove(original, level, blockPos, replacement, isMoving);
    }

    @Override
    public boolean canConnectRedstone(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @Nullable Direction direction) {
        return true;
    }

    @Override
    public int getSignal(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull Direction direction) {
        return getRedstoneSignal(level, pos);
    }

    @Override
    public int getDirectSignal(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull Direction direction) {
        return getRedstoneSignal(level, pos);
    }

    private int getRedstoneSignal(BlockGetter level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof SolarPanelBlockEntity be) return be.getRedstoneOutput();
        return 0;
    }

    @Override
    protected void createBlockStateDefinition(@NotNull StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(ACTIVE);
    }

    @Override
    public void animateTick(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, RandomSource random) {
        if(!state.getValue(ACTIVE)) return;
        if (random.nextInt(4) == 0) {
            int amount = random.nextInt(2);
            for(int i = 0; i < amount; i++) {
                double xd = (14d/16) - random.nextDouble() * (12d/16);
                double zd = (14d/16) - random.nextDouble() * (12d/16);
                level.addParticle(
                        SolarParticles.SPARKLE.get(),
                        pos.getX() + xd,
                        pos.getY() + 0.6,
                        pos.getZ() + zd,
                        random.nextGaussian() * 0.005,
                        random.nextGaussian() * 0.005,
                        random.nextGaussian() * 0.005
                );
            }
        }
    }

    @Override
    @NotNull
    public VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected boolean useShapeForLightOcclusion(@NotNull BlockState p_56395_) {
        return true;
    }

    @Override
    public Class<SolarPanelBlockEntity> getBlockEntityClass() {
        return SolarPanelBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends SolarPanelBlockEntity> getBlockEntityType() {
        return SolarBlockEntities.SOLAR_PANEL.get();
    }
}
