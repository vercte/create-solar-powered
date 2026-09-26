package uwu.hachiro.createsolar;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoulSandBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.model.generators.*;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import uwu.hachiro.createsolar.content.sculk_panel.SculkPanelBlock;
import uwu.hachiro.createsolar.content.solar_panel.SolarPanelBlock;
import uwu.hachiro.createsolar.content.growth_lamp.GrowthLampBlock;

import java.util.Collection;

public class SolarBlocks {
    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(CreateSolarPowered.ID);

    public static final DeferredBlock<SolarPanelBlock> SOLAR_PANEL = BLOCKS.registerBlock(
            "solar_panel",
            SolarPanelBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PURPLE)
                    .strength(1.0F)
                    .sound(SoundType.NETHERITE_BLOCK)
    );

    public static final DeferredBlock<SculkPanelBlock> SCULK_PANEL = BLOCKS.registerBlock(
            "sculk_panel",
            SculkPanelBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(1.0F)
                    .sound(SoundType.BONE_BLOCK)
    );

    public static final DeferredBlock<GrowthLampBlock> GROWTH_LAMP = BLOCKS.registerBlock(
            "growth_lamp",
            GrowthLampBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .strength(1.0F)
                    .lightLevel(s -> s.getValue(GrowthLampBlock.ACTIVE) ? 15 : 0)
    );

    public static final DeferredBlock<SoulSandBlock> CHARGED_SOUL_SAND = BLOCKS.registerBlock(
            "charged_soul_sand",
            SoulSandBlock::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.SOUL_SAND)
    );

    public static Collection<DeferredHolder<Block, ? extends Block>> getEntries() {
        return BLOCKS.getEntries();
    }

    public static void loadAndRegister(IEventBus bus) {
        BLOCKS.register(bus);
    }
}
