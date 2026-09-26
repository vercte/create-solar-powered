package uwu.hachiro.createsolar.util.datagen.assets;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.loaders.SeparateTransformsModelBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import uwu.hachiro.createsolar.CreateSolarPowered;
import uwu.hachiro.createsolar.SolarBlocks;
import uwu.hachiro.createsolar.SolarItems;

public class SolarItemModelGen extends ItemModelProvider {
    public SolarItemModelGen(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, CreateSolarPowered.ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        ResourceLocation sunglasses = CreateSolarPowered.at("item/sunglasses");
        withExistingParent(sunglasses.toString(), "item/generated")
                .customLoader(SeparateTransformsModelBuilder::begin)
                .base(
                        nested().parent(new ModelFile.UncheckedModelFile("item/generated"))
                                .texture("layer0", CreateSolarPowered.at("item/sunglasses"))
                ).perspective(
                        ItemDisplayContext.HEAD,
                        nested().parent(getExistingFile(CreateSolarPowered.at("item/sunglasses_worn")))
                );

        simpleBlockItem(SolarBlocks.SOLAR_PANEL.get());
        simpleBlockItem(SolarBlocks.SCULK_PANEL.get());
        simpleBlockItem(SolarBlocks.GROWTH_LAMP.get());
        simpleBlockItem(SolarBlocks.CHARGED_SOUL_SAND.get());

        basicItem(SolarItems.SOUL_SHARD.get());
    }
}
