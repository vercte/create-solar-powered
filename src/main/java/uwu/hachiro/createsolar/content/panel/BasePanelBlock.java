package uwu.hachiro.createsolar.content.panel;

import net.createmod.catnip.placement.IPlacementHelper;
import net.createmod.catnip.placement.PlacementHelpers;
import net.createmod.catnip.placement.PlacementOffset;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public abstract class BasePanelBlock extends Block {
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
    private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 8, 16);

    public BasePanelBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(ACTIVE, false));
    }

    @Override
    @NotNull
    protected ItemInteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        IPlacementHelper placementHelper = PlacementHelpers.get(getPlacementHelperId());
        if (!player.isShiftKeyDown() && player.mayBuild()) {
            if (placementHelper.matchesItem(stack)) {
                placementHelper.getOffset(player, level, state, pos, hitResult)
                        .placeInWorld(level, (BlockItem) stack.getItem(), player, hand, hitResult);
                return ItemInteractionResult.SUCCESS;
            }
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
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

    protected abstract int getPlacementHelperId();

    @MethodsReturnNonnullByDefault
    protected static class PanelPlacementHelper<T extends BasePanelBlock> implements IPlacementHelper {
        Supplier<T> block;

        public PanelPlacementHelper(Supplier<T> block) {
            this.block = block;
        }

        @Override
        public Predicate<ItemStack> getItemPredicate() {
            return s -> (s.getItem() instanceof BlockItem b) && b.getBlock() == block.get();
        }

        @Override
        public Predicate<BlockState> getStatePredicate() {
            return s -> s.is(block.get());
        }

        @Override
        public PlacementOffset getOffset(@NotNull Player player, @NotNull Level world, @NotNull BlockState state, @NotNull BlockPos pos, BlockHitResult ray) {
            List<Direction> directions = IPlacementHelper.orderedByDistanceExceptAxis(
                    pos, ray.getLocation(),
                    Direction.Axis.Y,
                    dir -> world.getBlockState(pos.relative(dir))
                            .canBeReplaced()
            );

            if (directions.isEmpty())
                return PlacementOffset.fail();
            else {
                return PlacementOffset.success(pos.relative(directions.getFirst()));
            }
        }
    }
}
