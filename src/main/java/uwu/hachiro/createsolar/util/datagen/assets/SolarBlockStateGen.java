package uwu.hachiro.createsolar.util.datagen.assets;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.*;
import net.neoforged.neoforge.client.model.generators.loaders.CompositeModelBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import uwu.hachiro.createsolar.CreateSolarPowered;
import uwu.hachiro.createsolar.SolarBlocks;
import uwu.hachiro.createsolar.content.growth_lamp.GrowthLampBlock;
import uwu.hachiro.createsolar.content.panel.BasePanelBlock;

public class SolarBlockStateGen extends BlockStateProvider {
    public SolarBlockStateGen(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, CreateSolarPowered.ID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        registerPanelModel(SolarBlocks.SOLAR_PANEL.get());
        registerPanelModel(SolarBlocks.SCULK_PANEL.get());

        getVariantBuilder(SolarBlocks.GROWTH_LAMP.get())
                .forAllStates(s -> {
                    boolean active = s.getValue(GrowthLampBlock.ACTIVE);
                    String name = "growth_lamp" + (active ? "_active" : "");
                    ResourceLocation texture = CreateSolarPowered.at("block/growth_lamp_" + (active ? "on" : "off"));
                    return ConfiguredModel.builder()
                            .modelFile(models().cubeAll("block/" + name, texture))
                            .build();
                });

        simpleBlock(SolarBlocks.CHARGED_SOUL_SAND.get());
    }

    private void registerPanelModel(Block block) {
        getVariantBuilder(block).forAllStatesExcept(s -> {
            boolean active = s.getValue(BasePanelBlock.ACTIVE);

            String baseName = block.builtInRegistryHolder().unwrapKey().map(r -> r.location().getPath()).orElse("[unregistered]");
            String name = baseName + (active ? "_active" : "");

            ResourceLocation top = CreateSolarPowered.at(baseName + "_top").withPrefix("block/");
            ResourceLocation bottom = CreateSolarPowered.at(baseName + "_bottom").withPrefix("block/");
            ResourceLocation side = CreateSolarPowered.at(baseName + "_side" + (active ? "_active" : "")).withPrefix("block/");

            ModelFile baseBlock = models().getExistingFile(mcLoc("block/block"));
            ModelFile slab = models().getExistingFile(mcLoc("block/slab"));


            BlockModelBuilder builder = models().getBuilder(name)
                    .parent(baseBlock)
                    .texture("particle", top)
                    .customLoader((b, h) ->
                            CompositeModelBuilder.begin(b, models().existingFileHelper)
                                .child(
                                        "base",
                                        models().nested()
                                                .parent(slab)
                                                .texture("top", top)
                                                .texture("bottom", bottom)
                                                .texture("side", side)
                                                .renderType(mcLoc("solid"))
                                )
                    ).end();

            return ConfiguredModel.builder()
                    .modelFile(builder)
                    .build();
        }, BlockStateProperties.WATERLOGGED);

    }
}
