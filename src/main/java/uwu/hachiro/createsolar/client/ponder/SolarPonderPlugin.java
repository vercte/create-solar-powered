package uwu.hachiro.createsolar.client.ponder;

import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredItem;
import org.jetbrains.annotations.NotNull;
import uwu.hachiro.createsolar.CreateSolarPowered;
import uwu.hachiro.createsolar.SolarItems;

public class SolarPonderPlugin implements PonderPlugin {
    @Override
    @NotNull
    public String getModId() {
        return CreateSolarPowered.ID;
    }

    @Override
    public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        PonderSceneRegistrationHelper<DeferredItem<?>> HELPER = helper.withKeyFunction(DeferredItem::getId);

        HELPER.forComponents(SolarItems.SOLAR_PANEL)
                .addStoryBoard("solar_panel/solar_panel", PanelScenes::solarPanel)
                .addStoryBoard("solar_panel/array", PanelScenes::array);

    }
}
