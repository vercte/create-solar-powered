package uwu.hachiro.createsolar.content.solar_panel;

import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;
import uwu.hachiro.createsolar.SolarBlockEntities;
import uwu.hachiro.createsolar.SolarConfig;
import uwu.hachiro.createsolar.content.panel.BasePanelBlockEntity;
import uwu.hachiro.createsolar.content.panel.PanelCalculations;
import uwu.hachiro.createsolar.util.SolarLang;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SolarPanelBlockEntity extends BasePanelBlockEntity implements IHaveGoggleInformation {
    private int energy;
    private int lastRedstoneOutput;
    public int outputAge;
    public int outputDistance = 16; // I chose sixteen because Integer.MAX_VALUE didnt work and I wanted to

    private final Capability capability;

    private static final DecimalFormat ENERGY_FORMAT = new DecimalFormat("0.00");

    public SolarPanelBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);

        this.lastRedstoneOutput = 0;
        this.capability = new Capability();
    }

    @Override
    public void onUpdate() {
        assert level != null;

        int output = calculateOutput();
        boolean nowActive = output > 0;
        if (active != nowActive) setActive(nowActive);

        int redstoneOutput = (int)(
                Mth.clamp((double)output / SolarConfig.SOLAR_PANEL_MAX_OUTPUT.get(), 0, 1) * 15
        );
        if(redstoneOutput != lastRedstoneOutput) {
            lastRedstoneOutput = redstoneOutput;
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
            level.updateNeighborsAt(worldPosition, getBlockState().getBlock());
        }

        outputAge = Math.max(0, outputAge - 1);

        insertEnergy(output);

        if(outputAge == 0 && energy > 0) distributeEnergy();

        notifyUpdate();
    }

    public int calculateOutput() {
        assert level != null;

        if (!level.canSeeSky(worldPosition)) return 0;

        double sunFactor = SolarPanelCalculations.getSunFactor(level);
        if (sunFactor == 0) return 0;

        double weatherFactor = PanelCalculations.getWeatherFactor(level);

        double finalFactor = sunFactor * weatherFactor;
        return (int)(SolarConfig.SOLAR_PANEL_MAX_OUTPUT.get() * finalFactor);
    }

    private void distributeEnergy() {
        List<SolarPanelBlockEntity> outputs = new ArrayList<>();
        boolean outputOnly = false;

        outputDistance = 16;

        assert getLevel() != null;
        for(Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos pos = getBlockPos().relative(direction);

            Optional<SolarPanelBlockEntity> oPanel = getLevel().getBlockEntity(pos, SolarBlockEntities.SOLAR_PANEL.get());
            if(oPanel.isEmpty()) continue;
            if(outputOnly && oPanel.get().outputAge == 0) continue;
            if(oPanel.get().outputAge > 0) {
                outputOnly = true;
                outputs.removeIf(p -> p.outputAge == 0);
            }

            outputDistance = Math.min(outputDistance, oPanel.get().outputDistance + 1);

            outputs.add(oPanel.get());
        }

        outputs.removeIf(p -> p.energy >= p.getMaxEnergy() || p.outputDistance > outputDistance);

        for(int i = 0; i < outputs.size(); i++) {
            SolarPanelBlockEntity current = outputs.get(i);
            energy -= current.insertEnergy(energy / (outputs.size() - i));
            current.distributeDownwards();
            current.notifyUpdate();
        }
    }

    private void distributeDownwards() {
        assert level != null;

        IEnergyStorage energyCapability = level.getCapability(Capabilities.EnergyStorage.BLOCK, worldPosition.below(), Direction.DOWN);
        if(energyCapability != null && energyCapability.canReceive()) {
            int received = energyCapability.receiveEnergy(energy, false);
            if(received > 0) {
                outputAge = 4;
                outputDistance = 0;
            }
            energy -= received;
        }
    }

    @Nullable
    public static IEnergyStorage getCapability(SolarPanelBlockEntity be, Direction side) {
        return side == Direction.DOWN || side == null ? be.capability : null;
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        SolarLang.builder().add(Component.translatable("createsolar.tooltip.solar_panel.info")
                .withStyle(ChatFormatting.WHITE))
                .forGoggles(tooltip);

        int output = calculateOutput();
        int efficiency = (int)((double)output / SolarConfig.SOLAR_PANEL_MAX_OUTPUT.getAsInt() * 100);
        SolarLang.builder().add(Component.translatable("createsolar.tooltip.solar_panel.efficiency").withStyle(ChatFormatting.GRAY))
                .forGoggles(tooltip);
        SolarLang.builder().add(Component.literal(efficiency + "% \u2600").withStyle(ChatFormatting.YELLOW))
                .add(Component.translatable("createsolar.tooltip.solar_panel.postamble").withStyle(ChatFormatting.DARK_GRAY))
                .forGoggles(tooltip);

        double perTick = (double)output / SolarConfig.SOLAR_PANEL_UPDATE_INTERVAL.getAsInt();
        SolarLang.builder().add(Component.translatable("createsolar.tooltip.solar_panel.generated")
                        .withStyle(ChatFormatting.GRAY))
                .forGoggles(tooltip);
        SolarLang.builder().add(Component.literal(ENERGY_FORMAT.format(perTick) + "\u26A1/t").withStyle(ChatFormatting.AQUA))
                .add(Component.translatable("createsolar.tooltip.solar_panel.postamble").withStyle(ChatFormatting.DARK_GRAY))
                .forGoggles(tooltip);

        return true;
    }

    // region Energy Storage
    public int getEnergy() {
        return energy;
    }

    public int getMaxEnergy() {
        return SolarConfig.SOLAR_PANEL_MAX_ENERGY_STORED.getAsInt();
    }

    private int insertEnergy(int amount) {
        int toAdd = Math.min(amount, getMaxEnergy() - energy);
        energy += toAdd;

        return toAdd;
    }

    public int extractEnergy(int amount, boolean simulate) {
        int toExtract = Math.clamp(amount, 0, energy);
        if(!simulate) {
            energy -= toExtract;
            outputAge = 4;
            outputDistance = 0;
        }
        return toExtract;
    }
    // endregion

    @Override
    public void write(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(tag, registries, clientPacket);

        tag.putInt("LastRedstoneOutput", lastRedstoneOutput);
        tag.putInt("Energy", energy);
        tag.putInt("OutputAge", outputAge);
        tag.putInt("OutputDistance", outputDistance);
    }

    @Override
    protected void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(tag, registries, clientPacket);

        this.lastRedstoneOutput = tag.getInt("LastRedstoneOutput");
        this.energy = tag.getInt("Energy");
        this.outputAge = tag.getInt("OutputAge");
        this.outputDistance = tag.getInt("OutputDistance");
    }

    public int getRedstoneOutput() {
        return lastRedstoneOutput;
    }

    @Override
    public int getUpdateInterval() {
        return SolarConfig.SOLAR_PANEL_UPDATE_INTERVAL.get();
    }

    public class Capability implements IEnergyStorage {
        @Override
        public int receiveEnergy(int toReceive, boolean simulate) {
            return 0;
        }

        @Override
        public int extractEnergy(int toExtract, boolean simulate) {
            return SolarPanelBlockEntity.this.extractEnergy(toExtract, simulate);
        }

        @Override
        public int getEnergyStored() {
            return getEnergy();
        }

        @Override
        public int getMaxEnergyStored() {
            return getMaxEnergy();
        }

        @Override
        public boolean canExtract() {
            return true;
        }

        @Override
        public boolean canReceive() {
            return false;
        }
    }
}
