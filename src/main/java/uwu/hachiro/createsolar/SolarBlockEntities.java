package uwu.hachiro.createsolar;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import uwu.hachiro.createsolar.content.sculk_panel.SculkPanelBlockEntity;
import uwu.hachiro.createsolar.content.solar_panel.SolarPanelBlockEntity;
import uwu.hachiro.createsolar.content.growth_lamp.GrowthLampBlockEntity;

import java.util.function.Supplier;

@SuppressWarnings("DataFlowIssue")
public class SolarBlockEntities {
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, CreateSolarPowered.ID);

    public static final Supplier<BlockEntityType<SolarPanelBlockEntity>> SOLAR_PANEL = BLOCK_ENTITIES.register(
            "solar_panel",
            () -> BlockEntityType.Builder.of(SolarPanelBlockEntity::new, SolarBlocks.SOLAR_PANEL.get()).build(null)
        );

    public static final Supplier<BlockEntityType<SculkPanelBlockEntity>> SCULK_PANEL = BLOCK_ENTITIES.register(
            "sculk_panel",
            () -> BlockEntityType.Builder.of(SculkPanelBlockEntity::new, SolarBlocks.SCULK_PANEL.get()).build(null)
    );

    public static final Supplier<BlockEntityType<GrowthLampBlockEntity>> GROWTH_LAMP = BLOCK_ENTITIES.register(
            "growth_lamp",
            () -> BlockEntityType.Builder.of(GrowthLampBlockEntity::new, SolarBlocks.GROWTH_LAMP.get()).build(null)
    );

    public static void loadAndRegister(IEventBus bus) {
        BLOCK_ENTITIES.register(bus);
    }
}
