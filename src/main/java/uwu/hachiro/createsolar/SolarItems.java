package uwu.hachiro.createsolar;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.loaders.SeparateTransformsModelBuilder;
import uwu.hachiro.createsolar.content.sunglasses.SunglassesItem;

public class SolarItems {
    private static final CreateRegistrate REGISTRATE = CreateSolarPowered.registrate();

    public static final ItemEntry<SunglassesItem> SUNGLASSES = REGISTRATE.item("sunglasses", SunglassesItem::new)
            .model((c, p) -> {
                p.withExistingParent("createsolar:sunglasses", "item/generated")
                        .customLoader(SeparateTransformsModelBuilder::begin)
                        .base(
                                p.nested().parent(new ModelFile.UncheckedModelFile("item/generated"))
                                        .texture("layer0", CreateSolarPowered.at("item/sunglasses"))
                        ).perspective(
                                ItemDisplayContext.HEAD,
                                p.nested().parent(p.getExistingFile(CreateSolarPowered.at("item/sunglasses_worn")))
                        );
            })
            .register();

    public static final ItemEntry<Item> SOUL_SHARD = REGISTRATE.item("soul_shard", Item::new)
            .register();

    public static void loadAndRegister() {}
}
