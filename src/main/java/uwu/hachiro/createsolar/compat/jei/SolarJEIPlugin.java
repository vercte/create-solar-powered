package uwu.hachiro.createsolar.compat.jei;

import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import uwu.hachiro.createsolar.CreateSolarPowered;
import uwu.hachiro.createsolar.SolarBlocks;
import uwu.hachiro.createsolar.SolarRecipeTypes;
import uwu.hachiro.createsolar.content.sculk_panel.SoulChargingRecipe;

@JeiPlugin
public class SolarJEIPlugin implements IModPlugin {
    private CreateRecipeCategory<SoulChargingRecipe> SOUL_CHARGING;

    @Override
    @NotNull
    public ResourceLocation getPluginUid() {
        return CreateSolarPowered.at("jei");
    }

    @Override
    public void registerCategories(@NotNull IRecipeCategoryRegistration registration) {
        SOUL_CHARGING = new CreateRecipeCategory.Builder<>(SoulChargingRecipe.class)
                .addTypedRecipes(SolarRecipeTypes.SOUL_CHARGING)
                .itemIcon(SolarBlocks.SCULK_PANEL.get())
                .catalyst(SolarBlocks.SCULK_PANEL::get)
                .emptyBackground(177, 60)
                .build(CreateSolarPowered.at("soul_charging"), SoulChargingCategory::new);

        registration.addRecipeCategories(SOUL_CHARGING);
    }

    @Override
    public void registerRecipeCatalysts(@NotNull IRecipeCatalystRegistration registration) {
        SOUL_CHARGING.registerCatalysts(registration);
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registration) {
        SOUL_CHARGING.registerRecipes(registration);
    }
}
