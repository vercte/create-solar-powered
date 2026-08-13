package uwu.hachiro.createsolar.content.sculk_panel;

import net.minecraft.world.level.Level;

public class SculkPanelCalculations {
    private static final int TIME_GENERATION_START = 12990;
    private static final int TIME_GENERATION_END = 23010;
    private static final int PEAK_GENERATION_START = 14000;
    private static final int PEAK_GENERATION_END = 22000;

    public static double getMoonFactor(Level level) {
        int time = Math.toIntExact(level.getDayTime() % Level.TICKS_PER_DAY);

        if(time < TIME_GENERATION_START || time > TIME_GENERATION_END) return 0;
        if(time < PEAK_GENERATION_END && time > PEAK_GENERATION_START) return 1;

        if(time < PEAK_GENERATION_START) return (double)(time - TIME_GENERATION_START) / (PEAK_GENERATION_START - TIME_GENERATION_START);
        return 1 - (double)(time - TIME_GENERATION_END) / (TIME_GENERATION_END - PEAK_GENERATION_END);
    }
}
