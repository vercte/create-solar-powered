package uwu.hachiro.createsolar.content.sculk_panel;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uwu.hachiro.createsolar.SolarBlockEntities;
import uwu.hachiro.createsolar.content.sculk_panel.storage.SculkPanelSharedXpStorage;
import uwu.hachiro.createsolar.content.sculk_panel.storage.SculkPanelSharedXpStoragePropagator;

public class SculkPanelBlock extends Block implements IWrenchable, IBE<SculkPanelBlockEntity> {
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
    private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 8, 16);
    public static final int MAX_XP_PER_PANEL = 1000;

    public SculkPanelBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(ACTIVE, false));
    }

    @Override
    public void setPlacedBy(@NotNull Level level, @NotNull BlockPos blockPos, @NotNull BlockState state, @Nullable LivingEntity entity, @NotNull ItemStack stack) {
        SculkPanelSharedXpStoragePropagator.propagateStartingAt(level, blockPos);
    }

    @Override
    protected void onRemove(@NotNull BlockState original, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull BlockState replacement, boolean isMoving) {
        if (!original.is(replacement.getBlock())) {
            BlockEntity be = level.getBlockEntity(blockPos);
            if (be instanceof SculkPanelBlockEntity panel && panel.getSharedXpStorage() != null) {
                SculkPanelSharedXpStoragePropagator.trySplit(level, blockPos);
                panel.getSharedXpStorage().remove(panel);
            }
        }
        super.onRemove(original, level, blockPos, replacement, isMoving);
    }

    @Override
    protected void createBlockStateDefinition(@NotNull StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(ACTIVE);
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hit) {
        if (level.isClientSide()) return InteractionResult.SUCCESS;

        if (level.getBlockEntity(pos) instanceof SculkPanelBlockEntity panel) {
            SculkPanelSharedXpStoragePropagator.propagateStartingAt(level, pos);
            SculkPanelSharedXpStorage storage = panel.getSharedXpStorage();
            if (storage != null) {
                int xp = storage.collectXp();
                if (xp > 0) {
                    ExperienceOrb.award((ServerLevel) level, player.position(), xp);
                    return InteractionResult.CONSUME;
                }
            } else {
                int xp = panel.collectXp();
                if (xp > 0) {
                    ExperienceOrb.award((ServerLevel) level, player.position(), xp);
                    return InteractionResult.CONSUME;
                }
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void animateTick(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (!state.getValue(ACTIVE)) return;

        if (random.nextFloat() < 0.15f) {
            level.addParticle(
                    ParticleTypes.SCULK_SOUL,
                    pos.getX() + random.nextDouble(),
                    pos.getY() + 0.6,
                    pos.getZ() + random.nextDouble(),
                    0, 0, 0
            );
        }
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
        if (level.getBlockEntity(pos) instanceof SculkPanelBlockEntity be) {
            int xp = be.getTotalXp();
            int max = MAX_XP_PER_PANEL;
            SculkPanelSharedXpStorage shared = be.getSharedXpStorage();
            if (shared != null) max = shared.getPanelCount() * MAX_XP_PER_PANEL;
            return (int)((double)xp / max * 15);
        }
        return 0;
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
    public Class<SculkPanelBlockEntity> getBlockEntityClass() {
        return SculkPanelBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends SculkPanelBlockEntity> getBlockEntityType() {
        return SolarBlockEntities.SCULK_PANEL.get();
    }
}
