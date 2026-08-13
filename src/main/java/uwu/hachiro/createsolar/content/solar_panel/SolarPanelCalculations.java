package uwu.hachiro.createsolar.content.solar_panel;

import net.minecraft.world.level.Level;

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
}
