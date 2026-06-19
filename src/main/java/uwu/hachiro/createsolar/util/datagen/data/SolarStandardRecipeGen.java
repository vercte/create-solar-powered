package uwu.hachiro.createsolar.util.datagen.data;

import com.simibubi.create.AllItems;
import com.simibubi.create.foundation.data.recipe.CommonMetal;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;
import uwu.hachiro.createsolar.CreateSolarPowered;
import uwu.hachiro.createsolar.SolarBlocks;

import java.util.concurrent.CompletableFuture;

public class SolarStandardRecipeGen extends RecipeProvider {
    public SolarStandardRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SolarBlocks.SOLAR_PANEL, 4)
                .define('a', Tags.Items.GEMS_AMETHYST)
                .define('d', Items.DAYLIGHT_DETECTOR)
                .define('g', CommonMetal.GOLD.plates)
                .define('t', AllItems.ELECTRON_TUBE)
                .pattern("ada")
                .pattern(" g ")
                .pattern(" t ")
                .unlockedBy("has_amethyst", has(Tags.Items.GEMS_AMETHYST))
                .unlockedBy("has_gold_plate", has(CommonMetal.GOLD.plates))
                .save(output, CreateSolarPowered.at("shaped/solar_panel"));
    }
}
