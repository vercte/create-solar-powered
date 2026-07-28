package uwu.hachiro.createsolar.content.panel;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import uwu.hachiro.createsolar.SolarConfig;

public class SolarPanelCalculations {
    private static final int TIME_GENERATION_START = 23000;
    private static final int TIME_GENERATION_LENGTH = 14000;

    // note: these two are normalized to sunrise=0
    private static final int PEAK_GENERATION_START = 5500;
    private static final int PEAK_GENERATION_END = 8500;
    private static final int PEAK_GENERATION_AFTER_DURATION = 5500;

    public static double getSunFactor(Level level) {
        int time = Math.toIntExact(level.getDayTime() % Level.TICKS_PER_DAY);

        int normalTime = 1000 + time; // time normalized, sunrise = 0, sunset = 14000
        if (time >= TIME_GENERATION_START) normalTime = time - TIME_GENERATION_START;

        if (normalTime > TIME_GENERATION_LENGTH) return 0;

        if (normalTime < PEAK_GENERATION_START) return (double)normalTime / PEAK_GENERATION_START;
        else if (normalTime <= PEAK_GENERATION_END) return 1.0;
        else {
            double timeAfterPeakEnd = normalTime - PEAK_GENERATION_END;
            return 1.0 - (timeAfterPeakEnd / PEAK_GENERATION_AFTER_DURATION);
        }
    }

    public static double getWeatherFactor(Level level) {
        if (level.isThundering()) return 0.0;
        if (level.isRaining()) return 0.5;
        return 1.0;
    }

    public static double getAltitudeFactor(int altitude) {
        if (!SolarConfig.SOLAR_PANEL_ALTITUDE_DEPENDENCE.get()) return 1.0;

        int minY = SolarConfig.SOLAR_PANEL_ALTITUDE_MIN_Y.get();
        int maxY = SolarConfig.SOLAR_PANEL_ALTITUDE_MAX_Y.get();
        int distY = maxY - minY;
        double minFactor = SolarConfig.SOLAR_PANEL_ALTITUDE_MIN_FACTOR.get();
        double maxFactor = SolarConfig.SOLAR_PANEL_ALTITUDE_MAX_FACTOR.get();
        double distFactor = maxFactor - minFactor;

        if (distY == 0) return minFactor;

        int clamped = Mth.clamp(altitude, minY, maxY);

        return Mth.clamp(
                minFactor + distFactor * (double)(clamped - minY) / distY,
                minFactor,
                maxFactor
        );
    }

    public static double getTemperatureFactor(@NotNull Level level, BlockPos blockPos) {
        if (!SolarConfig.SOLAR_PANEL_TEMP_DEPENDENCE_ENABLED.get()) return 1.0;

        float temp = level.getBiome(blockPos).value().getBaseTemperature();
        double minTemp = SolarConfig.SOLAR_PANEL_TEMP_MIN_VALUE.get();
        double maxTemp = SolarConfig.SOLAR_PANEL_TEMP_MAX_VALUE.get();
        double minFactor = SolarConfig.SOLAR_PANEL_TEMP_MIN_FACTOR.get();
        double maxFactor = SolarConfig.SOLAR_PANEL_TEMP_MAX_FACTOR.get();

        if (temp <= minTemp) return minFactor;
        if (temp >= maxTemp) return maxFactor;
        double t = (temp - minTemp) / (maxTemp - minTemp);
        return minFactor + t * (maxFactor - minFactor);
    }
}
