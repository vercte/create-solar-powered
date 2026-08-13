package uwu.hachiro.createsolar.util.datagen.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Blocks;
import uwu.hachiro.createsolar.CreateSolarPowered;
import uwu.hachiro.createsolar.api.data.recipe.SoulChargingRecipeGen;

import java.util.concurrent.CompletableFuture;

public class SolarSoulChargingRecipeGen extends SoulChargingRecipeGen {
    public SolarSoulChargingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, CreateSolarPowered.ID);
    }

    GeneratedRecipe SOUL_SAND = charging("soul_sand_from_sand", ItemTags.SAND, Blocks.SOUL_SAND);
    GeneratedRecipe SCULK = charging("sculk_from_soul_sand", Blocks.SCULK, Blocks.SOUL_SAND);
}
