package uwu.hachiro.createsolar;

import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import uwu.hachiro.createsolar.content.panel.SolarPanelBlock;
import uwu.hachiro.createsolar.content.panel.SolarPanelCTBehaviour;
import uwu.hachiro.createsolar.content.growth_lamp.GrowthLampBlock;
import uwu.hachiro.createsolar.content.sculk_panel.SculkPanelBlock;

import static com.simibubi.create.foundation.data.CreateRegistrate.connectedTextures;

public class SolarBlocks {
    private static final CreateRegistrate REGISTRATE = CreateSolarPowered.registrate();

    public static final BlockEntry<SolarPanelBlock> SOLAR_PANEL = REGISTRATE.block("solar_panel", SolarPanelBlock::new)
            .properties(p -> p
                    .mapColor(MapColor.COLOR_PURPLE)
                    .strength(1.0F)
                    .sound(SoundType.NETHERITE_BLOCK)
            )
            .blockstate((c, p) -> BlockStateGen.simpleBlock(c, p, s -> {
                boolean active = s.getValue(SolarPanelBlock.ACTIVE);
                String name = c.getName() + (active ? "_active" : "");
                return p.models().getExistingFile(p.modLoc("block/" + name));
            }))
            .onRegister(connectedTextures(() -> new SolarPanelCTBehaviour(SolarSpriteShifts.SOLAR_PANEL_TOP, SolarSpriteShifts.SOLAR_PANEL_BOTTOM)))
            .simpleItem()
            .register();

    public static final BlockEntry<SculkPanelBlock> SCULK_PANEL = REGISTRATE.block("sculk_panel", SculkPanelBlock::new)
            .properties(p -> p
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(1.0F)
                    .sound(SoundType.SCULK)
            )
            .blockstate((c, p) -> BlockStateGen.simpleBlock(c, p, s -> {
                boolean active = s.getValue(SculkPanelBlock.ACTIVE);
                String name = c.getName() + (active ? "_active" : "");
                return p.models().getExistingFile(p.modLoc("block/" + name));
            }))
            .simpleItem()
            .register();

    public static final BlockEntry<GrowthLampBlock> GROWTH_LAMP = REGISTRATE.block("growth_lamp", GrowthLampBlock::new)
            .properties(p -> p
                    .mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .strength(1.0F)
                    .sound(SoundType.LANTERN)
                    .lightLevel(s -> s.getValue(GrowthLampBlock.ACTIVE) ? 15 : 0)
            )
            .blockstate((c, p) -> BlockStateGen.simpleBlock(c, p, s -> {
                boolean active = s.getValue(GrowthLampBlock.ACTIVE);
                String name = c.getName() + (active ? "_active" : "");
                return p.models().getExistingFile(p.modLoc("block/" + name));
            }))
            .simpleItem()
            .register();

    public static void loadAndRegister() {}
}
