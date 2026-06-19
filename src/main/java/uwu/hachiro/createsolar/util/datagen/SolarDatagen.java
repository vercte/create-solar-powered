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

    public static void gatherExtraLang(final GatherDataEvent event) {
        CreateSolarPowered.registrate().addDataGenerator(ProviderType.LANG, p -> {
            p.add(SolarCreativeTabs.BASE.get(), "Create: Solar Powered");
            p.add("createsolar.tooltip.solar_panel.info", "Solar Panel Info:");
            p.add("createsolar.tooltip.solar_panel.efficiency", "Solar Power Efficiency:");
            p.add("createsolar.tooltip.solar_panel.generated", "Energy Generated:");
            p.add("createsolar.tooltip.solar_panel.postamble", " at current sun strength");
        });
    }
}
