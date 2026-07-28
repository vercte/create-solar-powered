package uwu.hachiro.createsolar;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import net.createmod.catnip.lang.FontHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import uwu.hachiro.createsolar.content.growth_lamp.GrowthLampBlockEntity;
import uwu.hachiro.createsolar.content.panel.SolarPanelBlockEntity;
import uwu.hachiro.createsolar.util.datagen.SolarDatagen;

@Mod(CreateSolarPowered.ID)
public class CreateSolarPowered {
    public static final String ID = "createsolar";

    private static final CreateRegistrate REGISTRATE = CreateRegistrate.create(ID)
            .defaultCreativeTab((ResourceKey<CreativeModeTab>)null)
            .setTooltipModifierFactory(item ->
                    new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE)
                            .andThen(TooltipModifier.mapNull(KineticStats.create(item)))
            );

    public CreateSolarPowered(IEventBus bus, ModContainer modContainer) {
        REGISTRATE.registerEventListeners(bus);

        SolarBlocks.loadAndRegister();
        SolarBlockEntities.loadAndRegister();
        SolarCreativeTabs.loadAndRegister(bus);

        SolarParticles.loadAndRegister(bus);

        bus.addListener(this::registerCapabilities);
        bus.addListener(SolarDatagen::gatherData);
        bus.addListener(EventPriority.HIGHEST, SolarDatagen::gatherExtraLang);

        modContainer.registerConfig(ModConfig.Type.COMMON, SolarConfig.SPEC);
    }

    public void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.EnergyStorage.BLOCK,
                SolarBlockEntities.SOLAR_PANEL.get(),
                SolarPanelBlockEntity::getCapability
        );
        event.registerBlockEntity(
                Capabilities.EnergyStorage.BLOCK,
                SolarBlockEntities.GROWTH_LAMP.get(),
                GrowthLampBlockEntity::getCapability
        );
    }

    public static CreateRegistrate registrate() {
        return REGISTRATE;
    }

    public static ResourceLocation at(String path) {
        return ResourceLocation.fromNamespaceAndPath(ID, path);
    }
}
