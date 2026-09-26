package uwu.hachiro.createsolar.util.datagen.assets;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.simibubi.create.foundation.utility.FilesHelper;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import uwu.hachiro.createsolar.CreateSolarPowered;
import uwu.hachiro.createsolar.SolarBlocks;
import uwu.hachiro.createsolar.SolarItems;

import java.util.Map;

public class SolarLangGen extends LanguageProvider {
    public SolarLangGen(PackOutput output) {
        super(output, CreateSolarPowered.ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        addBlock(SolarBlocks.SOLAR_PANEL, "Solar Panel");
        addBlock(SolarBlocks.SCULK_PANEL, "Sculk Panel");
        addBlock(SolarBlocks.GROWTH_LAMP, "Growth Lamp");
        addBlock(SolarBlocks.CHARGED_SOUL_SAND, "Charged Soul Sand");

        addItem(SolarItems.SOUL_SHARD, "Soul Shard");
        addItem(SolarItems.SUNGLASSES, "Sunglasses");

        gatherInterfaceTranslations();
    }

    private void gatherInterfaceTranslations() {
        String interfacePath = "assets/createsolar/lang/default/interface.json"; // code STOLEN. TAKEN. THIEVED from Create
        JsonElement jsonElement = FilesHelper.loadJsonResource(interfacePath);   // except its legal teehee :3c
        if (jsonElement == null) {
            throw new IllegalStateException(String.format("Could not find interface lang file: %s", interfacePath));
        }

        JsonObject jsonObject = jsonElement.getAsJsonObject();
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue().getAsString();
            add(key, value);
        }
    }
}
