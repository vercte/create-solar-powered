package uwu.hachiro.createsolar.util.datagen.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jetbrains.annotations.NotNull;
import uwu.hachiro.createsolar.SolarBlocks;
import uwu.hachiro.createsolar.SolarItems;

import java.util.Set;

public class SolarBlockLootProvider extends BlockLootSubProvider {
    public SolarBlockLootProvider(HolderLookup.Provider provider) {
        super(Set.of(), FeatureFlags.DEFAULT_FLAGS, provider);
    }

    @Override
    @NotNull
    protected Iterable<Block> getKnownBlocks() {
        return SolarBlocks.getEntries()
                .stream()
                .map(e -> (Block)e.value())
                .toList();
    }

    @Override
    protected void generate() {
        dropSelf(SolarBlocks.SOLAR_PANEL.get());
        dropSelf(SolarBlocks.SCULK_PANEL.get());
        dropSelf(SolarBlocks.GROWTH_LAMP.get());

        add(SolarBlocks.CHARGED_SOUL_SAND.get(),
                createSilkTouchDispatchTable(SolarBlocks.CHARGED_SOUL_SAND.get(),
                        applyExplosionDecay(SolarBlocks.CHARGED_SOUL_SAND.get(),
                                LootItem.lootTableItem(SolarItems.SOUL_SHARD.get())
                                        .apply(SetItemCountFunction.setCount(
                                                UniformGenerator.between(2, 3)
                                        ))
                        )
                )
        );
    }
}
