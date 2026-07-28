package uwu.hachiro.createsolar.content.sculk_panel;

import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import uwu.hachiro.createsolar.SolarConfig;
import uwu.hachiro.createsolar.content.panel.SolarPanelCalculations;
import uwu.hachiro.createsolar.content.sculk_panel.storage.SculkPanelSharedXpStorage;
import uwu.hachiro.createsolar.content.sculk_panel.storage.SculkPanelSharedXpStoragePropagator;
import uwu.hachiro.createsolar.util.SolarLang;

import java.text.DecimalFormat;
import java.util.List;

import static uwu.hachiro.createsolar.content.sculk_panel.SculkPanelBlock.ACTIVE;

public class SculkPanelBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {
    private SculkPanelSharedXpStorage sharedXpStorage;
    private int storedXp;
    private int nextUpdate;
    private boolean active;
    private int lastRedstoneOutput;

    private static final DecimalFormat XP_FORMAT = new DecimalFormat("0.00");

    public SculkPanelBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.sharedXpStorage = null;
        this.storedXp = 0;
        this.nextUpdate = SolarConfig.SOLAR_PANEL_UPDATE_INTERVAL.get();
        this.active = false;
        this.lastRedstoneOutput = 0;
    }

    @Override
    public void tick() {
        super.tick();

        assert level != null;
        if (level.isClientSide()) return;

        if (nextUpdate-- > 0) return;
        nextUpdate = SolarConfig.SOLAR_PANEL_UPDATE_INTERVAL.get();

        if (sharedXpStorage == null) {
            SculkPanelSharedXpStoragePropagator.propagateStartingAt(level, worldPosition);
        }

        int output = calculateOutput();
        boolean nowActive = output > 0;
        if (active != nowActive) {
            active = nowActive;
            level.setBlockAndUpdate(worldPosition, getBlockState().setValue(ACTIVE, nowActive));
        }

        int totalXp = getTotalXp();
        int maxXp = getMaxXp();
        int redstoneOutput = (int)(Mth.clamp((double)totalXp / maxXp, 0, 1) * 15);
        if (redstoneOutput != lastRedstoneOutput) {
            lastRedstoneOutput = redstoneOutput;
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
            level.updateNeighborsAt(worldPosition, getBlockState().getBlock());
        }

        if (sharedXpStorage != null) {
            sharedXpStorage.addXp(output);
        } else {
            addXp(output);
        }
    }

    public int calculateOutput() {
        assert level != null;
        if (!level.canSeeSky(worldPosition)) return 0;

        double moonFactor = SculkPanelCalculations.getMoonFactor(level);
        if (moonFactor == 0) return 0;

        double phaseFactor = SculkPanelCalculations.getPhaseFactor(level);
        double weatherFactor = SolarPanelCalculations.getWeatherFactor(level);
        double altitudeFactor = SolarPanelCalculations.getAltitudeFactor(worldPosition.getY());
        double temperatureFactor = SculkPanelCalculations.getTemperatureFactor(level, worldPosition);

        double finalFactor = moonFactor * phaseFactor * weatherFactor * altitudeFactor * temperatureFactor;
        return (int)(SolarConfig.SOLAR_PANEL_MAX_OUTPUT.get() * finalFactor);
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        SolarLang.builder().add(Component.translatable("createsolar.tooltip.sculk_panel.info")
                        .withStyle(ChatFormatting.WHITE))
                .forGoggles(tooltip);

        int output = calculateOutput();
        int efficiency = (int)((double)output / SolarConfig.SOLAR_PANEL_MAX_OUTPUT.getAsInt() * 100);
        SolarLang.builder().add(Component.translatable("createsolar.tooltip.sculk_panel.efficiency").withStyle(ChatFormatting.GRAY))
                .forGoggles(tooltip);
        SolarLang.builder().add(Component.literal(efficiency + "% \uD83C\uDF19").withStyle(ChatFormatting.DARK_AQUA))
                .add(Component.translatable("createsolar.tooltip.sculk_panel.moon_postamble").withStyle(ChatFormatting.DARK_GRAY))
                .forGoggles(tooltip);

        double perTick = (double)output / SolarConfig.SOLAR_PANEL_UPDATE_INTERVAL.getAsInt();
        SolarLang.builder().add(Component.translatable("createsolar.tooltip.sculk_panel.generated").withStyle(ChatFormatting.GRAY))
                .forGoggles(tooltip);
        SolarLang.builder().add(Component.literal(XP_FORMAT.format(perTick) + " XP/t").withStyle(ChatFormatting.GREEN))
                .add(Component.translatable("createsolar.tooltip.sculk_panel.postamble").withStyle(ChatFormatting.DARK_GRAY))
                .forGoggles(tooltip);

        int totalXp = getTotalXp();
        int maxXp = getMaxXp();
        int panels = sharedXpStorage != null ? sharedXpStorage.getPanelCount() : 1;
        SolarLang.builder().add(Component.translatable("createsolar.tooltip.sculk_panel.stored", totalXp, maxXp, panels)
                        .withStyle(ChatFormatting.GRAY))
                .forGoggles(tooltip);

        return true;
    }

    // region Shared XP Storage
    @Nullable
    public SculkPanelSharedXpStorage getSharedXpStorage() {
        return sharedXpStorage;
    }

    public void setSharedXpStorage(SculkPanelSharedXpStorage storage) {
        this.sharedXpStorage = storage;
    }

    public int getTotalXp() {
        if (sharedXpStorage != null) return sharedXpStorage.getTotalXp();
        return storedXp;
    }

    private int getMaxXp() {
        if (sharedXpStorage != null) return sharedXpStorage.getPanelCount() * SculkPanelBlock.MAX_XP_PER_PANEL;
        return SculkPanelBlock.MAX_XP_PER_PANEL;
    }

    public int getStoredXp() {
        return storedXp;
    }

    public void addXp(int amount) {
        assert level != null;
        int toAdd = Math.min(amount, SculkPanelBlock.MAX_XP_PER_PANEL - storedXp);
        if (toAdd > 0) {
            storedXp += toAdd;
            notifyUpdate();
        }
    }

    public int collectXp() {
        assert level != null;
        int xp = storedXp;
        storedXp = 0;
        notifyUpdate();
        return xp;
    }
    // endregion

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {}

    @Override
    public void write(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(tag, registries, clientPacket);
        tag.putInt("StoredXp", storedXp);
        tag.putInt("NextUpdate", nextUpdate);
        tag.putInt("LastRedstoneOutput", lastRedstoneOutput);
        tag.putBoolean("Active", active);
    }

    @Override
    protected void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(tag, registries, clientPacket);
        this.storedXp = tag.getInt("StoredXp");
        this.nextUpdate = tag.getInt("NextUpdate");
        this.lastRedstoneOutput = tag.getInt("LastRedstoneOutput");
        this.active = tag.getBoolean("Active");
    }
}
