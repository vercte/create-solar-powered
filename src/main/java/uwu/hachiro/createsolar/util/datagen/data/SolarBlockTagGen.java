package uwu.hachiro.createsolar.util.datagen.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uwu.hachiro.createsolar.CreateSolarPowered;
import uwu.hachiro.createsolar.SolarBlocks;

import java.util.concurrent.CompletableFuture;

public class SolarBlockTagGen extends BlockTagsProvider {
    public SolarBlockTagGen(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, CreateSolarPowered.ID, existingFileHelper);
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider provider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(SolarBlocks.SOLAR_PANEL.get())
                .add(SolarBlocks.SCULK_PANEL.get())
                .add(SolarBlocks.GROWTH_LAMP.get());

        tag(BlockTags.MINEABLE_WITH_SHOVEL)
                .add(SolarBlocks.CHARGED_SOUL_SAND.get());

        tag(BlockTags.SOUL_SPEED_BLOCKS)
                .add(SolarBlocks.CHARGED_SOUL_SAND.get());
    }
}
