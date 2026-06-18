package uwu.hachiro.createsolar.util;

import net.createmod.catnip.lang.LangBuilder;
import uwu.hachiro.createsolar.CreateSolarPowered;

public class SolarLang {
    public static String formatEnergy(int n) { // Thank you Create Addition
        if (n >= 1000000000) {
            double var2 = (double)Math.round((double)n / (double)1.0E8F);
            return var2 / (double)10.0F + "G";
        } else if (n >= 1000000) {
            double var1 = (double)Math.round((double)n / (double)100000.0F);
            return var1 / (double)10.0F + "M";
        } else if (n >= 1000) {
            double var10000 = (double)Math.round((double)n / (double)100.0F);
            return var10000 / (double)10.0F + "K";
        } else {
            return "" + n;
        }
    }

    public static LangBuilder builder() {
        return new LangBuilder(CreateSolarPowered.ID);
    }
}
