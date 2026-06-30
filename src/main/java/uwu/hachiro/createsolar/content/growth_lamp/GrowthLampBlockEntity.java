package uwu.hachiro.createsolar.content.growth_lamp;

import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;
import uwu.hachiro.createsolar.SolarConfig;
import uwu.hachiro.createsolar.content.growth_lamp.storage.GrowthLampSharedEnergyStorage;
import uwu.hachiro.createsolar.util.SolarLang;

import java.util.List;

import static uwu.hachiro.createsolar.content.growth_lamp.GrowthLampBlock.ACTIVE;

public class GrowthLampBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation, IEnergyStorage {
    private GrowthLampSharedEnergyStorage sharedStorage;
    private int energy;
    private boolean active;
    private int nextOperationTick;

    private static final int RADIUS = 5;
    private static final int BONE_MEAL_CHANCE = 40;

    public GrowthLampBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.sharedStorage = null;
        this.energy = 0;
        this.active = false;
        this.nextOperationTick = 0;
    }

    @Override
    public void tick() {
        super.tick();

        assert level != null;
        if (level.isClientSide()) return;

        IEnergyStorage input = level.getCapability(
                Capabilities.EnergyStorage.BLOCK,
                worldPosition, Direction.DOWN);

        if (input != null && input.getEnergyStored() > 0) {
            int received = input.extractEnergy(1, false);
            energy += received;
            if (energy > SolarConfig.GROWTH_LAMP_MAX_ENERGY_STORED.get()) {
                energy = SolarConfig.GROWTH_LAMP_MAX_ENERGY_STORED.get();
            }
            notifyUpdate();
        }

        if (active) {
            if (energy < SolarConfig.GROWTH_LAMP_CONSUMPTION.get()) {
                if (energy > 0) {
                    energy = 0;
                    notifyUpdate();
                }
                setActive(false);
                return;
            }

            energy -= SolarConfig.GROWTH_LAMP_CONSUMPTION.get();
            notifyUpdate();

            if (--nextOperationTick == 0) {
                applyBoneMeal((ServerLevel) level);
                nextOperationTick = SolarConfig.GROWTH_LAMP_INTERVAL.get();
            }
        } else {
            if (energy >= SolarConfig.GROWTH_LAMP_MIN_ACTIVATION_ENERGY.get()) {
                setActive(true);
                applyBoneMeal((ServerLevel) level);
                nextOperationTick = SolarConfig.GROWTH_LAMP_INTERVAL.get();
            }
        }
    }

    private void applyBoneMeal(ServerLevel level) {
        BlockPos.betweenClosedStream(
                worldPosition.offset(-RADIUS, -RADIUS, -RADIUS),
                worldPosition.offset(RADIUS, RADIUS, RADIUS)
        ).forEach(pos -> {
            if (level.random.nextInt(BONE_MEAL_CHANCE) == 0) {
                BlockState state = level.getBlockState(pos);
                if (state.getBlock() instanceof BonemealableBlock growable) {
                    if (growable.isValidBonemealTarget(level, pos, state)) {
                        if (growable.isBonemealSuccess(level, level.random, pos, state)) {
                            growable.performBonemeal(level, level.random, pos, state);
                            level.sendParticles(ParticleTypes.HAPPY_VILLAGER,
                                    pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                                    5, 0.3, 0.3, 0.3, 0);
                            level.playSound(null, pos, SoundEvents.BONE_MEAL_USE,
                                    SoundSource.BLOCKS, 1.0F, 1.0F);
                        }
                    }
                }
            }
        });
    }


    public void setActive(boolean active) {
        this.active = active;
        assert level != null;
        level.setBlockAndUpdate(worldPosition, getBlockState().setValue(ACTIVE, active));
        notifyUpdate();
    }

    // region Energy

    @Nullable
    public GrowthLampSharedEnergyStorage getSharedEnergyStorage() {
        return sharedStorage;
    }

    public void setSharedEnergyStorage(GrowthLampSharedEnergyStorage storage) {
        this.sharedStorage = storage;
    }

    @Override
    public int getEnergyStored() {
        return energy;
    }

    @Override
    public int getMaxEnergyStored() {
        return SolarConfig.GROWTH_LAMP_MAX_ENERGY_STORED.get();
    }

    @Override
    public int receiveEnergy(int amount, boolean simulate) {
        int max = SolarConfig.GROWTH_LAMP_MAX_ENERGY_STORED.get();
        int toAdd = Math.min(amount, max - energy);
        if (!simulate) energy += toAdd;
        return toAdd;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return 0;
    }

    @Override
    public boolean canExtract() {
        return false;
    }

    @Override
    public boolean canReceive() {
        return true;
    }

    @Nullable
    @SuppressWarnings("unused")
    public static IEnergyStorage getCapability(GrowthLampBlockEntity be, Direction side) {
        return be;
    }

    // endregion

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        SolarLang.builder().add(Component.translatable("createsolar.tooltip.growth_lamp.info")
                        .withStyle(ChatFormatting.WHITE))
                .forGoggles(tooltip);

        String status = active
                ? Component.translatable("createsolar.tooltip.growth_lamp.active").getString()
                : Component.translatable("createsolar.tooltip.growth_lamp.inactive").getString();
        SolarLang.builder().add(Component.literal(status).withStyle(active ? ChatFormatting.GREEN : ChatFormatting.RED))
                .forGoggles(tooltip);

        int stored = getEnergyStored();
        int max = SolarConfig.GROWTH_LAMP_MAX_ENERGY_STORED.get();
        SolarLang.builder().add(Component.translatable("createsolar.tooltip.growth_lamp.energy", stored, max)
                        .withStyle(ChatFormatting.GRAY))
                .forGoggles(tooltip);

        return true;
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {}

    @Override
    public void write(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(tag, registries, clientPacket);
        tag.putInt("Energy", energy);
        tag.putBoolean("Active", active);
        tag.putInt("NextOperationTick", nextOperationTick);
    }

    @Override
    protected void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(tag, registries, clientPacket);
        this.energy = tag.getInt("Energy");
        this.active = tag.getBoolean("Active");
        this.nextOperationTick = tag.getInt("NextOperationTick");
    }
}