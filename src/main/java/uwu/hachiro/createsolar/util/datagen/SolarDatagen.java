package uwu.hachiro.createsolar.util.datagen;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.simibubi.create.foundation.utility.FilesHelper;
import com.tterrag.registrate.providers.ProviderType;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import uwu.hachiro.createsolar.CreateSolarPowered;
import uwu.hachiro.createsolar.util.datagen.assets.SolarParticleDescriptionGen;
import uwu.hachiro.createsolar.util.datagen.data.SolarRecipeProvider;
import uwu.hachiro.createsolar.util.datagen.data.SolarStandardRecipeGen;

import java.util.Map;
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
            SolarRecipeProvider.registerAllProcessing(event.getGenerator(), output, provider);
            event.addProvider(new SolarStandardRecipeGen(output, provider));
        }
    }

    @SuppressWarnings("unused")
    public static void gatherExtraLang(final GatherDataEvent event) {
        CreateSolarPowered.registrate().addDataGenerator(ProviderType.LANG, lang -> {
            String interfacePath = "assets/createsolar/lang/default/interface.json"; // code STOLEN. TAKEN. THIEVED from Create
            JsonElement jsonElement = FilesHelper.loadJsonResource(interfacePath);   // except its legal teehee :3c
            if (jsonElement == null) {
                throw new IllegalStateException(String.format("Could not find interface lang file: %s", interfacePath));
            }

            JsonObject jsonObject = jsonElement.getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue().getAsString();
                lang.add(key, value);
            }
        });
    }
}
