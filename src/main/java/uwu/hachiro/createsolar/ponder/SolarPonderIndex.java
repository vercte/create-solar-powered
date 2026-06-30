package uwu.hachiro.createsolar.ponder;

import com.tterrag.registrate.util.entry.ItemProviderEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;

import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.resources.ResourceLocation;
import uwu.hachiro.createsolar.CreateSolarPowered;
import uwu.hachiro.createsolar.SolarBlocks;

public class SolarPonderIndex implements PonderPlugin {

    @Override
    public String getModId() {
        return CreateSolarPowered.ID;
    }

    @Override
    public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        PonderSceneRegistrationHelper<ItemProviderEntry<?, ?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);

        HELPER.addStoryBoard(SolarBlocks.SOLAR_PANEL, "solar_panel/intro", SolarPonderScenes::solarPanel);
        HELPER.addStoryBoard(SolarBlocks.SCULK_PANEL, "sculk_panel/intro", SolarPonderScenes::sculkPanel);
    }
}
