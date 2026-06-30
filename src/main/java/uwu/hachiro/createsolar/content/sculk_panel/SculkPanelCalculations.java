package uwu.hachiro.createsolar.content.sculk_panel;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import uwu.hachiro.createsolar.SolarConfig;

public class SculkPanelCalculations {
    private static final int MOON_RISE = 13000;
    private static final int MOON_SET = 23000;
    private static final int MIDNIGHT = 18000;
    private static final int RISE_DURATION = MIDNIGHT - MOON_RISE;
    private static final int SET_DURATION = MOON_SET - MIDNIGHT;

    public static double getMoonFactor(Level level) {
        int time = (int)(level.getDayTime() % Level.TICKS_PER_DAY);

        if (time < MOON_RISE || time >= MOON_SET) return 0;

        int moonTime = time - MOON_RISE;

        if (moonTime <= RISE_DURATION) {
            return (double)moonTime / RISE_DURATION;
        } else {
            double timeAfterMidnight = moonTime - RISE_DURATION;
            return 1.0 - (timeAfterMidnight / SET_DURATION);
        }
    }

    public static double getPhaseFactor(Level level) {
        return level.getMoonBrightness();
    }

    public static double getTemperatureFactor(Level level, BlockPos pos) {
        if (!SolarConfig.SOLAR_PANEL_TEMP_DEPENDENCE_ENABLED.get()) return 1.0;

        double minTemp = SolarConfig.SOLAR_PANEL_TEMP_MIN_VALUE.get();
        double maxTemp = SolarConfig.SOLAR_PANEL_TEMP_MAX_VALUE.get();
        double minFactor = SolarConfig.SOLAR_PANEL_TEMP_MIN_FACTOR.get();
        double maxFactor = SolarConfig.SOLAR_PANEL_TEMP_MAX_FACTOR.get();

        float temp = level.getBiome(pos).value().getBaseTemperature();
        if (minTemp >= maxTemp) return minFactor;
        if (temp <= minTemp) return minFactor;
        if (temp >= maxTemp) return maxFactor;
        double t = (temp - minTemp) / (maxTemp - minTemp);
        return minFactor + t * (maxFactor - minFactor);
    }
}
