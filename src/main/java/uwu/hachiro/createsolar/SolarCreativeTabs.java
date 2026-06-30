package uwu.hachiro.createsolar;

import com.simibubi.create.AllCreativeModeTabs;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class SolarCreativeTabs {
    private static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, CreateSolarPowered.ID);

    private static final ResourceLocation CREATE_ADDITION_TAB_LOCATION = ResourceLocation.fromNamespaceAndPath("createaddition", "createaddition");

    public static final Supplier<CreativeModeTab> BASE = CREATIVE_TABS.register(
            "base",
            () -> CreativeModeTab.builder()
                    .withTabsBefore(
                            AllCreativeModeTabs.BASE_CREATIVE_TAB.getKey(),
                            ResourceKey.create(Registries.CREATIVE_MODE_TAB, CREATE_ADDITION_TAB_LOCATION) // specified like this because it doesn't care if it's loaded :D
                    )
                    .title(Component.translatable("itemGroup.createsolar.base"))
                    .displayItems(SolarCreativeTabs::displayItems)
                    .icon(SolarBlocks.SOLAR_PANEL::asStack)
                    .build()
    );

    private static void displayItems(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
        output.accept(SolarBlocks.SOLAR_PANEL);
        output.accept(SolarBlocks.SCULK_PANEL);
        output.accept(SolarBlocks.GROWTH_LAMP);
    }

    public static void loadAndRegister(IEventBus bus) {
        CREATIVE_TABS.register(bus);
    }
}
