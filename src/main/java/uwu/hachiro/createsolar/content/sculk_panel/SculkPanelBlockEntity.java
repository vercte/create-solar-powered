package uwu.hachiro.createsolar.content.sculk_panel;

import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import uwu.hachiro.createsolar.SolarConfig;
import uwu.hachiro.createsolar.SolarRecipeTypes;
import uwu.hachiro.createsolar.content.panel.BasePanelBlockEntity;
import uwu.hachiro.createsolar.content.panel.PanelCalculations;
import uwu.hachiro.createsolar.util.SolarLang;

import java.util.List;
import java.util.Optional;

public class SculkPanelBlockEntity extends BasePanelBlockEntity implements IHaveGoggleInformation {
    private double chargingProgress;

    public SculkPanelBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void onUpdate() {
        assert level != null;

        double output = getCycleValue();

        boolean nowActive = output > 0;
        if (active != nowActive) setActive(nowActive);

        chargingProgress += output;

        if(chargingProgress >= SolarConfig.SCULK_PANEL_CYCLE_REQUIREMENT.get()) {
            boolean charged = chargeBlockBelow();

            if(charged) chargingProgress = 0;
        }
    }

    public double getCycleValue() {
        assert level != null;
        double moonFactor = SculkPanelCalculations.getMoonFactor(level);
        double moonBrightnessFactor = level.getMoonBrightness();
        double weatherFactor = PanelCalculations.getWeatherFactor(level);

        return moonFactor * moonBrightnessFactor * weatherFactor;
    }

    private boolean chargeBlockBelow() {
        assert level != null;

        BlockPos below = worldPosition.below();
        BlockState belowState = level.getBlockState(below);
        Optional<RecipeHolder<SoulChargingRecipe>> possiblyFoundRecipe = level.getRecipeManager()
                .getAllRecipesFor(SolarRecipeTypes.SOUL_CHARGING.get())
                .stream()
                .filter(r -> r.value().testBlock(belowState))
                .findFirst();

        if(possiblyFoundRecipe.isEmpty()) return false;

        SoulChargingRecipe recipe = possiblyFoundRecipe.get().value();
        level.playSound(null, below, SoundEvents.ZOMBIE_VILLAGER_CONVERTED, SoundSource.BLOCKS, 1, 1.45f);
        level.destroyBlock(below, false);

        BlockState transformedBlock = recipe.transformBlock(belowState, level.random);
        level.setBlock(below, transformedBlock, Block.UPDATE_ALL);
        recipe.rollResults(level.random)
                .forEach(stack -> Block.popResource(level, below, stack));

        return true;
    }

    @Override
    public int getUpdateInterval() {
        return SolarConfig.SCULK_PANEL_UPDATE_INTERVAL.get();
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        SolarLang.builder().add(Component.translatable("createsolar.tooltip.sculk_panel.info")
                .withStyle(ChatFormatting.WHITE))
                .forGoggles(tooltip);
        return true;
    }

    @Override
    public void write(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(tag, registries, clientPacket);

        tag.putDouble("ChargingProgress", chargingProgress);
    }

    @Override
    protected void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(tag, registries, clientPacket);

        chargingProgress = tag.getDouble("ChargingProgress");
    }
}
