package uwu.hachiro.createsolar.util.datagen.data;

import com.simibubi.create.AllItems;
import com.simibubi.create.api.data.recipe.CrushingRecipeGen;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Blocks;
import uwu.hachiro.createsolar.CreateSolarPowered;
import uwu.hachiro.createsolar.SolarBlocks;
import uwu.hachiro.createsolar.SolarItems;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
public class SolarCrushingRecipeGen extends CrushingRecipeGen {
    public SolarCrushingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, CreateSolarPowered.ID);
    }

    GeneratedRecipe CHARGED_SOUL_SAND = create("charged_soul_sand", b -> b
            .require(SolarBlocks.CHARGED_SOUL_SAND)
            .output(SolarItems.SOUL_SHARD, 2)
            .output(0.75f, SolarItems.SOUL_SHARD)
            .output(0.25f, SolarItems.SOUL_SHARD)
            .output(0.5f, Blocks.SOUL_SOIL)
    );

    GeneratedRecipe EXPERIENCE_NUGGET_FROM_SOUL_SHARD = create("experience_nugget_from_soul_shard", b -> b
            .require(SolarItems.SOUL_SHARD)
            .output(AllItems.EXP_NUGGET)
            .output(0.5f, AllItems.EXP_NUGGET)
    );
}
