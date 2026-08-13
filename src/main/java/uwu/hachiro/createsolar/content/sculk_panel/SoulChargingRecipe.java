package uwu.hachiro.createsolar.content.sculk_panel;

import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;
import com.simibubi.create.content.processing.recipe.StandardProcessingRecipe;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import com.simibubi.create.foundation.utility.BlockHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.NotNull;
import uwu.hachiro.createsolar.CreateSolarPowered;
import uwu.hachiro.createsolar.SolarRecipeTypes;

import java.util.List;

public class SoulChargingRecipe extends StandardProcessingRecipe<RecipeWrapper> {
    public SoulChargingRecipe(ProcessingRecipeParams params) {
        super(RecipeInfo.INSTANCE, params);
    }

    @Override
    protected int getMaxInputCount() {
        return 1;
    }

    @Override
    protected int getMaxOutputCount() {
        return 4;
    }

    public Ingredient getProcessedItem() {
        if (ingredients.isEmpty())
            throw new IllegalStateException("Recipe has no ingredient!");
        return ingredients.getFirst();
    }

    public boolean testBlock(BlockState state) {
        return ingredients.getFirst().test(state.getBlock().asItem().getDefaultInstance());
    }

    public BlockState transformBlock(BlockState in, RandomSource randomSource) {
        ProcessingOutput mainOutput = results.getFirst();
        ItemStack output = mainOutput.rollOutput(randomSource);
        if (output.getItem() instanceof BlockItem bi)
            return BlockHelper.copyProperties(in, bi.getBlock()
                    .defaultBlockState());
        return Blocks.AIR.defaultBlockState();
    }

    @Override
    @NotNull
    public List<ItemStack> rollResults(@NotNull RandomSource randomSource) {
        return rollResults(getRollableResultsExceptBlock(), randomSource);
    }

    public List<ProcessingOutput> getRollableResultsExceptBlock() {
        ProcessingOutput mainOutput = results.getFirst();
        if (mainOutput.getStack().getItem() instanceof BlockItem)
            return results.subList(1, results.size());
        return results;
    }

    @Override
    public boolean matches(@NotNull RecipeWrapper wrapper, @NotNull Level level) {
        return getProcessedItem().test(wrapper.getItem(0));
    }

    @SuppressWarnings("unchecked")
    public static class RecipeInfo implements IRecipeTypeInfo {
        public static final RecipeInfo INSTANCE = new RecipeInfo();

        @Override
        public ResourceLocation getId() {
            return CreateSolarPowered.at("soul_charging");
        }

        @Override
        public RecipeSerializer<SoulChargingRecipe> getSerializer() {
            return SolarRecipeTypes.SOUL_CHARGING_SERIALIZER.get();
        }

        @Override
        public RecipeType<SoulChargingRecipe> getType() {
            return SolarRecipeTypes.SOUL_CHARGING.get();
        }
    }
}
