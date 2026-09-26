package uwu.hachiro.createsolar;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import uwu.hachiro.createsolar.content.sunglasses.SunglassesItem;

public class SolarItems {
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(CreateSolarPowered.ID);

    public static final DeferredItem<SunglassesItem> SUNGLASSES = ITEMS.registerItem("sunglasses", SunglassesItem::new);

    public static final DeferredItem<BlockItem> SOLAR_PANEL = ITEMS.registerSimpleBlockItem(SolarBlocks.SOLAR_PANEL);
    public static final DeferredItem<BlockItem> SCULK_PANEL = ITEMS.registerSimpleBlockItem(SolarBlocks.SCULK_PANEL);
    public static final DeferredItem<BlockItem> GROWTH_LAMP = ITEMS.registerSimpleBlockItem(SolarBlocks.GROWTH_LAMP);
    public static final DeferredItem<BlockItem> CHARGED_SOUL_SAND = ITEMS.registerSimpleBlockItem(SolarBlocks.CHARGED_SOUL_SAND);
    public static final DeferredItem<Item> SOUL_SHARD = ITEMS.registerItem("soul_shard", Item::new);

    public static void loadAndRegister(IEventBus bus) {
        ITEMS.register(bus);
    }
}
