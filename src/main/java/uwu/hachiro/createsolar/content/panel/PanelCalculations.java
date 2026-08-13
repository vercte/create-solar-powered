package uwu.hachiro.createsolar.content.panel;

import net.minecraft.world.level.Level;

public class PanelCalculations {
    public static double getWeatherFactor(Level level) {
        if (level.isThundering()) return 0.0;
        if (level.isRaining()) return 0.5;
        return 1.0;
    }
}
