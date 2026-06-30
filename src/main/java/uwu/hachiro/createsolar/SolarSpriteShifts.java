package uwu.hachiro.createsolar;

import com.simibubi.create.foundation.block.connected.AllCTTypes;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.CTSpriteShifter;
import com.simibubi.create.foundation.block.connected.CTType;

public class SolarSpriteShifts {
    public static final CTSpriteShiftEntry SOLAR_PANEL_TOP = getCT(AllCTTypes.OMNIDIRECTIONAL, "solar_panel_top");
    public static final CTSpriteShiftEntry SOLAR_PANEL_BOTTOM = getCT(AllCTTypes.OMNIDIRECTIONAL, "solar_panel_bottom");


    private static CTSpriteShiftEntry getCT(CTType type, String blockTextureName) {
        return CTSpriteShifter.getCT(type, CreateSolarPowered.at("block/" + blockTextureName),
                CreateSolarPowered.at("block/" + blockTextureName + "_connected"));
    }
}
