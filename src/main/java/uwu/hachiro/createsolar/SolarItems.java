package uwu.hachiro.createsolar;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.Item;

public class SolarItems {
    private static final CreateRegistrate REGISTRATE = CreateSolarPowered.registrate();

    public static final ItemEntry<Item> SOUL_SHARD = REGISTRATE.item("soul_shard", Item::new)
            .register();

    public static void loadAndRegister() {}
}
