package uwu.hachiro.createsolar;

import com.simibubi.create.content.processing.recipe.StandardProcessingRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import uwu.hachiro.createsolar.content.sculk_panel.SoulChargingRecipe;

import java.util.function.Supplier;

public class SolarRecipeTypes {
    private static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, CreateSolarPowered.ID);
    private static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, CreateSolarPowered.ID);

    public static final Supplier<RecipeType<SoulChargingRecipe>> SOUL_CHARGING = RECIPE_TYPES.register(
            "soul_charging",
            () -> RecipeType.simple(CreateSolarPowered.at("soul_charging"))
    );

    public static final Supplier<RecipeSerializer<SoulChargingRecipe>> SOUL_CHARGING_SERIALIZER = RECIPE_SERIALIZERS.register(
            "soul_charging",
            () -> new StandardProcessingRecipe.Serializer<>(SoulChargingRecipe::new)
    );

    public static void loadAndRegister(IEventBus bus) {
        RECIPE_TYPES.register(bus);
        RECIPE_SERIALIZERS.register(bus);
    }
}
