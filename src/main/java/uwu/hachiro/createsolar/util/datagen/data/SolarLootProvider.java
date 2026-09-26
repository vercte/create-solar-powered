package uwu.hachiro.createsolar.util.datagen.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class SolarLootProvider extends LootTableProvider {
    public SolarLootProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, Set.of(), List.of(
                new SubProviderEntry(SolarBlockLootProvider::new, LootContextParamSets.BLOCK)
        ), provider);
    }
}
