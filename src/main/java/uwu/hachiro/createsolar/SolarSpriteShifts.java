package uwu.hachiro.createsolar;

import com.simibubi.create.foundation.block.connected.AllCTTypes;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.CTSpriteShifter;


public class SolarSpriteShifts {
    public static final CTSpriteShiftEntry SOLAR_PANEL_TOP = getCT("solar_panel_top");
    public static final CTSpriteShiftEntry SOLAR_PANEL_BOTTOM = getCT("solar_panel_bottom");


    private static CTSpriteShiftEntry getCT(String blockTextureName) {
        return CTSpriteShifter.getCT(AllCTTypes.OMNIDIRECTIONAL, CreateSolarPowered.at("block/" + blockTextureName),
                CreateSolarPowered.at("block/" + blockTextureName + "_connected"));
    }
}
