package uwu.hachiro.createsolar;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import uwu.hachiro.createsolar.content.panel.SolarPanelBlockEntity;

public class SolarBlockEntities {
    private static final CreateRegistrate REGISTRATE = CreateSolarPowered.registrate();

    public static final BlockEntityEntry<SolarPanelBlockEntity> SOLAR_PANEL = REGISTRATE.blockEntity("solar_panel", SolarPanelBlockEntity::new)
            .validBlock(SolarBlocks.SOLAR_PANEL)
            .register();

    public static void loadAndRegister() {}
}
