package uwu.hachiro.createsolar.util.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import uwu.hachiro.createsolar.util.datagen.assets.SolarBlockStateGen;
import uwu.hachiro.createsolar.util.datagen.assets.SolarItemModelGen;
import uwu.hachiro.createsolar.util.datagen.assets.SolarLangGen;
import uwu.hachiro.createsolar.util.datagen.assets.SolarParticleDescriptionGen;
import uwu.hachiro.createsolar.util.datagen.data.SolarBlockTagGen;
import uwu.hachiro.createsolar.util.datagen.data.SolarLootProvider;
import uwu.hachiro.createsolar.util.datagen.data.SolarRecipeProvider;
import uwu.hachiro.createsolar.util.datagen.data.SolarStandardRecipeGen;

import java.util.concurrent.CompletableFuture;

public class SolarDatagen {
    public static void gatherData(final GatherDataEvent event) {
        PackOutput output = event.getGenerator().getPackOutput();
        ExistingFileHelper helper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();

        if(event.includeClient()) {
            event.addProvider(new SolarParticleDescriptionGen(output, helper));
            event.addProvider(new SolarBlockStateGen(output, helper));
            event.addProvider(new SolarItemModelGen(output, helper));
            event.addProvider(new SolarLangGen(output));
        }

        if(event.includeServer()) {
            SolarRecipeProvider.registerAllProcessing(event.getGenerator(), output, provider);
            event.addProvider(new SolarStandardRecipeGen(output, provider));
            event.addProvider(new SolarLootProvider(output, provider));
            event.addProvider(new SolarBlockTagGen(output, provider, helper));
        }
    }
}
