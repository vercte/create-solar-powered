package uwu.hachiro.createsolar;

import net.neoforged.neoforge.common.ModConfigSpec;

public class SolarConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    static {
        BUILDER.comment(" The config version. DO NOT CHANGE!\n ...Unless you really, really want to, I guess.");
    }

    private static final ModConfigSpec.IntValue CONFIG_VERSION = BUILDER.defineInRange("version", 1, 1, 1);

    static {
        BUILDER.comment("## Solar Panel Config");
        BUILDER.push("solar_panel");
    }

    public static final ModConfigSpec.IntValue SOLAR_PANEL_MAX_OUTPUT = BUILDER
            .comment(" Maximum FE generated in one update interval at full sunlight.")
            .defineInRange("max_output", 250, 1, 500);

    public static final ModConfigSpec.IntValue SOLAR_PANEL_UPDATE_INTERVAL = BUILDER
            .comment(" The number of ticks between energy updates.")
            .defineInRange("update_interval", 20, 1, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue SOLAR_PANEL_MAX_ENERGY_STORED = BUILDER
            .comment(" Internal energy buffer capacity (FE).")
            .defineInRange("max_energy_stored", 1000, 1, Integer.MAX_VALUE);

    static {
        BUILDER.comment("# Altitude dependence for the solar panels");
        BUILDER.push("altitude_dependence");
    }

    public static final ModConfigSpec.BooleanValue SOLAR_PANEL_ALTITUDE_DEPENDENCE = BUILDER
            .comment(" Whether solar panel output depends on altitude.\n Default: false")
            .define("enabled", false);

    public static final ModConfigSpec.IntValue SOLAR_PANEL_ALTITUDE_MIN_Y = BUILDER
            .comment(" Minimum Y level for altitude calculation")
            .defineInRange("min_y", -64, Integer.MIN_VALUE, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue SOLAR_PANEL_ALTITUDE_MAX_Y = BUILDER
            .comment(" Minimum Y level for altitude calculation")
            .defineInRange("max_y", 61, Integer.MIN_VALUE, Integer.MAX_VALUE);

    public static final ModConfigSpec.DoubleValue SOLAR_PANEL_ALTITUDE_MIN_FACTOR = BUILDER
            .comment(" Output multiplier at minimum altitude.")
            .defineInRange("altitude_min_factor", 0.5, 0.0, 1.0);

    public static final ModConfigSpec.DoubleValue SOLAR_PANEL_ALTITUDE_MAX_FACTOR = BUILDER
            .comment(" Output multiplier at maximum altitude).")
            .defineInRange("altitude_max_factor", 1.0, 0.0, 1.0);

    static {
        BUILDER.pop();

        BUILDER.comment("# Temperature dependence for the solar panels");
        BUILDER.push("temperature_dependence");
    }

    public static final ModConfigSpec.BooleanValue SOLAR_PANEL_TEMP_DEPENDENCE_ENABLED = BUILDER
            .comment(" Whether solar panel output depends on biome temperature.\n Default: false")
            .define("temperature_dependence_enabled", false);

    public static final ModConfigSpec.DoubleValue SOLAR_PANEL_TEMP_MIN_VALUE = BUILDER
            .comment(" Biome temperature at which temperature_min_factor applies.")
            .defineInRange("temperature_min_value", 0.0, -0.5, 2.0);

    public static final ModConfigSpec.DoubleValue SOLAR_PANEL_TEMP_MAX_VALUE = BUILDER
            .comment(" Biome temperature at which temperature_max_factor applies.")
            .defineInRange("temperature_max_value", 1.2, -0.5, 2.0);

    public static final ModConfigSpec.DoubleValue SOLAR_PANEL_TEMP_MIN_FACTOR = BUILDER
            .comment(" Output multiplier at temperature_min_value.")
            .defineInRange("temperature_min_factor", 0.7, 0.0, 1.0);

    public static final ModConfigSpec.DoubleValue SOLAR_PANEL_TEMP_MAX_FACTOR = BUILDER
            .comment(" Output multiplier at temperature_max_value.")
            .defineInRange("temperature_max_factor", 1.0, 0.0, 1.0);

    static final ModConfigSpec SPEC = BUILDER.build();
}
