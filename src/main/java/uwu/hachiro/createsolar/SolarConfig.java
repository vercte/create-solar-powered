package uwu.hachiro.createsolar;

import net.neoforged.neoforge.common.ModConfigSpec;

public class SolarConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    static {
        BUILDER.comment("## Solar Panel Config");
        BUILDER.push("solar_panel");
    }

    public static final ModConfigSpec.IntValue SOLAR_PANEL_MAX_OUTPUT = BUILDER
            .comment("Maximum FE generated in one update interval at full sunlight.")
            .defineInRange("max_output", 300, 1, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue SOLAR_PANEL_UPDATE_INTERVAL = BUILDER
            .comment("The number of ticks between energy updates.")
            .defineInRange("update_interval", 20, 1, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue SOLAR_PANEL_MAX_ENERGY_STORED = BUILDER
            .comment("Internal energy buffer capacity (FE).")
            .defineInRange("max_energy_stored", 1000, 1, Integer.MAX_VALUE);


    static {
        BUILDER.pop();
        BUILDER.comment("## Sculk Panel Config");
        BUILDER.push("sculk_panel");
    }

    public static final ModConfigSpec.IntValue SCULK_PANEL_UPDATE_INTERVAL = BUILDER
            .comment("The number of ticks between energy updates.")
            .defineInRange("update_interval", 20, 1, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue SCULK_PANEL_CYCLE_REQUIREMENT = BUILDER
            .comment("The amount of charge units that a sculk panel requires to charge the block below. Typically, a full moon with clear weather generates 250.")
            .defineInRange("cycle_requirement", 125, 1, Integer.MAX_VALUE);

    static {
        BUILDER.pop();
        BUILDER.comment("## Growth Lamp Config");
        BUILDER.push("growth_lamp");
    }

    public static final ModConfigSpec.IntValue GROWTH_LAMP_MAX_ENERGY_STORED = BUILDER
            .comment("Internal energy buffer capacity (FE).")
            .defineInRange("max_energy_stored", 1000, 1, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue GROWTH_LAMP_MIN_ACTIVATION_ENERGY = BUILDER
            .comment("Minimum FE required to activate the lamp.")
            .defineInRange("min_activation_energy", 125, 1, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue GROWTH_LAMP_CONSUMPTION = BUILDER
            .comment("FE consumed per tick while active.")
            .defineInRange("consumption", 3, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue GROWTH_LAMP_INTERVAL = BUILDER
            .comment("Ticks between bonemeal operations.")
            .defineInRange("interval", 40, 1, Integer.MAX_VALUE);

    static final ModConfigSpec SPEC = BUILDER.build();
}
