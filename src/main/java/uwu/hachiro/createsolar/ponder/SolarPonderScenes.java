package uwu.hachiro.createsolar.ponder;

import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import uwu.hachiro.createsolar.SolarBlocks;

public class SolarPonderScenes {

    private static Block cca(String path) {
        Block block = BuiltInRegistries.BLOCK.get(ResourceLocation.parse("createaddition:" + path));
        return block == Blocks.AIR ? null : block;
    }

    public static void solarPanel(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("solar_panel", "Generating Energy from Sunlight");
        scene.configureBasePlate(0, 0, 5);
        scene.idle(5);

        scene.world().showSection(util.select().layer(0), Direction.UP);
        scene.idle(10);

        scene.world().setBlock(util.grid().at(2, 1, 2),
                SolarBlocks.SOLAR_PANEL.get().defaultBlockState(), false);
        scene.idle(15);

        scene.overlay().showText(80)
                .text("text_1")
                .pointAt(util.vector().topOf(2, 1, 2))
                .placeNearTarget();
        scene.idle(90);

        scene.addKeyframe();

        scene.overlay().showText(70)
                .text("text_2")
                .pointAt(util.vector().topOf(2, 1, 2))
                .placeNearTarget();
        scene.idle(80);

        scene.addKeyframe();

        Block connector = cca("connector");
        Block accumulator = cca("modular_accumulator");
        if (connector != null) {
            scene.world().setBlock(util.grid().at(3, 1, 2),
                    connector.defaultBlockState()
                            .setValue(BlockStateProperties.FACING, Direction.WEST), false);
        }
        if (accumulator != null) {
            scene.world().setBlock(util.grid().at(4, 1, 2),
                    accumulator.defaultBlockState(), false);
        }
        scene.idle(10);

        scene.overlay().showOutline(PonderPalette.GREEN, new Object(),
                util.select().fromTo(3, 1, 2, 4, 1, 2), 50);
        scene.overlay().showText(80)
                .text("text_3")
                .pointAt(util.vector().topOf(4, 1, 2))
                .placeNearTarget();
        scene.idle(90);

        scene.addKeyframe();

        scene.overlay().showOutline(PonderPalette.WHITE, new Object(),
                util.select().position(2, 1, 2), 60);
        scene.overlay().showText(70)
                .text("text_4")
                .pointAt(util.vector().topOf(2, 1, 2))
                .placeNearTarget();
        scene.idle(80);

        scene.addKeyframe();

        scene.world().setBlock(util.grid().at(0, 1, 2),
                SolarBlocks.SOLAR_PANEL.get().defaultBlockState(), false);
        scene.world().setBlock(util.grid().at(1, 1, 2),
                SolarBlocks.SOLAR_PANEL.get().defaultBlockState(), false);
        scene.idle(10);

        scene.overlay().showOutline(PonderPalette.BLUE, new Object(),
                util.select().fromTo(2, 1, 2, 0, 1, 2), 50);
        scene.overlay().showText(80)
                .text("text_5")
                .pointAt(util.vector().topOf(1, 1, 2))
                .placeNearTarget();
        scene.idle(90);

        scene.markAsFinished();
    }

    public static void sculkPanel(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("sculk_panel", "Generating Experience from Moonlight");
        scene.configureBasePlate(0, 0, 5);
        scene.idle(5);

        scene.world().showSection(util.select().layer(0), Direction.UP);
        scene.idle(10);

        scene.world().setBlock(util.grid().at(2, 1, 2),
                SolarBlocks.SCULK_PANEL.get().defaultBlockState(), false);
        scene.idle(15);

        scene.overlay().showText(80)
                .text("text_1")
                .pointAt(util.vector().topOf(2, 1, 2))
                .placeNearTarget();
        scene.idle(90);

        scene.addKeyframe();

        scene.overlay().showText(70)
                .text("text_2")
                .pointAt(util.vector().topOf(2, 1, 2))
                .placeNearTarget();
        scene.idle(80);

        scene.addKeyframe();

        scene.overlay().showOutline(PonderPalette.WHITE, new Object(),
                util.select().position(2, 1, 2), 60);
        scene.overlay().showText(70)
                .text("text_3")
                .pointAt(util.vector().topOf(2, 1, 2))
                .placeNearTarget();
        scene.idle(80);

        scene.addKeyframe();

        scene.overlay().showControls(util.vector().topOf(2, 1, 2), Pointing.DOWN, 40).rightClick();
        scene.idle(10);
        scene.overlay().showText(80)
                .text("text_4")
                .pointAt(util.vector().topOf(2, 1, 2))
                .placeNearTarget();
        scene.idle(90);

        scene.addKeyframe();

        scene.world().setBlock(util.grid().at(0, 1, 2),
                SolarBlocks.SCULK_PANEL.get().defaultBlockState(), false);
        scene.world().setBlock(util.grid().at(1, 1, 2),
                SolarBlocks.SCULK_PANEL.get().defaultBlockState(), false);
        scene.idle(10);

        scene.overlay().showOutline(PonderPalette.BLUE, new Object(),
                util.select().fromTo(2, 1, 2, 0, 1, 2), 50);
        scene.overlay().showText(80)
                .text("text_5")
                .pointAt(util.vector().topOf(1, 1, 2))
                .placeNearTarget();
        scene.idle(90);

        scene.markAsFinished();
    }
}
