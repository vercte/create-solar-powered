package uwu.hachiro.createsolar.util.datagen.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;
import uwu.hachiro.createsolar.SolarItems;

import java.util.concurrent.CompletableFuture;

public class SolarItemTagGen extends ItemTagsProvider {
    public SolarItemTagGen(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider, CompletableFuture.completedFuture(null));
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider provider) {
        tag(Tags.Items.HIDDEN_FROM_RECIPE_VIEWERS)
                .add(SolarItems.CANNY.get())
                .add(SolarItems.UNCANNY.get());
    }
}
