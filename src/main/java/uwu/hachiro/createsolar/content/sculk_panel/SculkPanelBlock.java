package uwu.hachiro.createsolar.content.sculk_panel;

import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import uwu.hachiro.createsolar.SolarBlockEntities;
import uwu.hachiro.createsolar.content.panel.BasePanelBlock;
import uwu.hachiro.createsolar.content.panel.PanelCalculations;

public class SculkPanelBlock extends BasePanelBlock implements IBE<SculkPanelBlockEntity> {
    public SculkPanelBlock(Properties properties) {
        super(properties);
    }

    private static final double PARTICLE_SPAWN_CHANCE_HIGH = 0.2;
    private static final double PARTICLE_SPAWN_CHANCE_BASE = 8.0;
    private static final double PARTICLE_X_OFFSET = 14.0 / 16.0;
    private static final double PARTICLE_X_RANGE = 12.0 / 16.0;
    private static final double PARTICLE_Y_OFFSET = 0.6;

    @Override
    public void animateTick(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if(!state.getValue(ACTIVE)) return;

        BlockEntity be = level.getBlockEntity(pos);
        if(!(be instanceof SculkPanelBlockEntity)) return;

        double efficiency = SculkPanelCalculations.getMoonFactor(level) * PanelCalculations.getWeatherFactor(level);

        if(efficiency < 0.5) return;

        boolean highEfficiency = efficiency > 0.9;
        double chance = highEfficiency ? PARTICLE_SPAWN_CHANCE_HIGH : Math.pow(efficiency, 2) / PARTICLE_SPAWN_CHANCE_BASE;
        if (random.nextDouble() < chance) {
            int amount = random.nextInt(highEfficiency ? 3 : 2);
            for(int i = 0; i < amount; i++) {
                double xd = PARTICLE_X_OFFSET - random.nextDouble() * PARTICLE_X_RANGE;
                double zd = PARTICLE_X_OFFSET - random.nextDouble() * PARTICLE_X_RANGE;
                level.addParticle(
                        ParticleTypes.SCULK_SOUL,
                        pos.getX() + xd,
                        pos.getY() + PARTICLE_Y_OFFSET,
                        pos.getZ() + zd,
                        0,
                        0.05,
                        0
                );
            }
        }
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
