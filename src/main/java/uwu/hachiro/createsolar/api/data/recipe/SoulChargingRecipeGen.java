package uwu.hachiro.createsolar.api.data.recipe;

import com.simibubi.create.api.data.recipe.StandardProcessingRecipeGen;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import uwu.hachiro.createsolar.content.sculk_panel.SoulChargingRecipe;

import java.util.concurrent.CompletableFuture;

public class SoulChargingRecipeGen extends StandardProcessingRecipeGen<SoulChargingRecipe> {
    public SoulChargingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, String defaultNamespace) {
        super(output, registries, defaultNamespace);
    }

    protected GeneratedRecipe charging(String name, TagKey<Item> input, Block output) {
        return create(name, b -> b.require(input).output(output));
    }

    protected GeneratedRecipe charging(String name, Block input, Block output) {
        return create(name, b -> b.require(input).output(output));
    }

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return SoulChargingRecipe.RecipeInfo.INSTANCE;
    }
}
