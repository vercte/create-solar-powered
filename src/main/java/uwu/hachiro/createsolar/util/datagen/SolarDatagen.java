package uwu.hachiro.createsolar.util.datagen;

import com.tterrag.registrate.providers.ProviderType;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import uwu.hachiro.createsolar.CreateSolarPowered;
import uwu.hachiro.createsolar.SolarCreativeTabs;
import uwu.hachiro.createsolar.util.datagen.assets.SolarParticleDescriptionGen;
import uwu.hachiro.createsolar.util.datagen.data.SolarStandardRecipeGen;

import java.util.concurrent.CompletableFuture;

public class SolarDatagen {
    public static void gatherData(final GatherDataEvent event) {
        PackOutput output = event.getGenerator().getPackOutput();
        ExistingFileHelper helper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();

        if(event.includeClient()) {
            event.addProvider(new SolarParticleDescriptionGen(output, helper));
        }

        if(event.includeServer()) {
            event.addProvider(new SolarStandardRecipeGen(output, provider));
        }
    }

    @SuppressWarnings("unused")
    public static void gatherExtraLang(final GatherDataEvent event) {
        CreateSolarPowered.registrate().addDataGenerator(ProviderType.LANG, p -> {
            p.add(SolarCreativeTabs.BASE.get(), "Create: Solar Powered");
            p.add("createsolar.tooltip.solar_panel.info", "Solar Panel Info:");
            p.add("createsolar.tooltip.solar_panel.efficiency", "Solar Power Efficiency:");
            p.add("createsolar.tooltip.solar_panel.generated", "Energy Generated:");
            p.add("createsolar.tooltip.solar_panel.postamble", " at current sun strength");
            p.add("createsolar.tooltip.sculk_panel.info", "Sculk Panel Info:");
            p.add("createsolar.tooltip.sculk_panel.efficiency", "Lunar Power Efficiency:");
            p.add("createsolar.tooltip.sculk_panel.moon_postamble", " at current moon strength");
            p.add("createsolar.tooltip.sculk_panel.generated", "Experience Generated:");
            p.add("createsolar.tooltip.sculk_panel.postamble", " at current moon strength");
            p.add("createsolar.tooltip.sculk_panel.stored", "Stored XP: %s / %s (%s panels)");
            p.add("createsolar.ponder.solar_panel.header", "Solar Panel");
            p.add("createsolar.ponder.sculk_panel.header", "Sculk Panel");
            p.add("createsolar.ponder.solar_panel.intro", "Generating Energy from Sunlight");
            p.add("createsolar.ponder.solar_panel.output", "Extracting Energy");
            p.add("createsolar.ponder.sculk_panel.intro", "Generating Experience from Moonlight");
            p.add("createsolar.ponder.sculk_panel.collection", "Collecting Experience");
            p.add("createsolar.tooltip.growth_lamp.info", "Growth Lamp Info:");
            p.add("createsolar.tooltip.growth_lamp.active", "Active");
            p.add("createsolar.tooltip.growth_lamp.inactive", "Inactive");
            p.add("createsolar.tooltip.growth_lamp.energy", "Energy: %s / %s FE");
        });
    }
}
