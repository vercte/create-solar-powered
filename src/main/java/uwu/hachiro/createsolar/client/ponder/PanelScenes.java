package uwu.hachiro.createsolar.client.ponder;

import com.simibubi.create.content.redstone.nixieTube.NixieTubeBlockEntity;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.RedstoneLampBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import uwu.hachiro.createsolar.content.growth_lamp.GrowthLampBlock;
import uwu.hachiro.createsolar.content.panel.BasePanelBlock;

public class PanelScenes {
    public static void solarPanel(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("solar_panel/solar_panel", "Generating Forge Energy with Solar Panels");

        scene.configureBasePlate(0, 0, 5);
        scene.showBasePlate();
        scene.idle(10);

        BlockPos firstPanel = util.grid().at(2, 1, 2);
        scene.world().showSection(util.select().position(firstPanel), Direction.DOWN);
        scene.idle(10);

        scene.overlay().showText(60)
                .attachKeyFrame()
                .text("Solar Panels use the sun to generate Forge Energy")
                .pointAt(util.vector().blockSurface(firstPanel, Direction.WEST))
                .placeNearTarget();
        scene.idle(80);

        scene.overlay().showText(60)
                .attachKeyFrame()
                .text("To operate, they need unobstructed access to the sky")
                .pointAt(util.vector().blockSurface(firstPanel, Direction.WEST))
                .placeNearTarget();
        scene.overlay().showOutline(
                PonderPalette.GREEN,
                "skylight",
                util.select().fromTo(firstPanel, firstPanel.above(2)),
                40
        );
        scene.idle(60);

        scene.world().modifyBlock(firstPanel, PanelScenes::activatePanel, false);
        indicateTamerRedstone(scene, firstPanel);
        indicateEnergy(scene, firstPanel);

        scene.idle(20);

        BlockPos nixie = util.grid().at(2, 1, 1);
        scene.world().showSection(util.select().position(nixie), Direction.DOWN);
        scene.world().modifyBlockEntityNBT(
                util.select().position(nixie),
                NixieTubeBlockEntity.class,
                nbt -> nbt.putInt("RedstoneStrength", 15)
        );

        scene.idle(20);

        scene.overlay().showText(60)
                .attachKeyFrame()
                .text("When active, the panel will emit redstone power depending on its output")
                .pointAt(util.vector().blockSurface(nixie, Direction.WEST))
                .placeNearTarget();
        scene.idle(40);

        BlockPos redstone = util.grid().at(2, 1, 3);
        BlockPos lamp = util.grid().at(2, 1, 4);
        scene.world().showSection(util.select().fromTo(redstone, lamp), Direction.DOWN);
        scene.idle(15);

        indicateTamerRedstone(scene, redstone);
        scene.world().modifyBlock(
                redstone, s -> s.setValue(RedStoneWireBlock.POWER, 15), false
        );

        scene.idle(5);
        scene.effects().indicateRedstone(lamp);
        scene.world().modifyBlock(
                lamp, s -> s.setValue(RedstoneLampBlock.LIT, true), false
        );
    }

    public static void array(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("solar_panel/array", "Distributing Energy with Solar Panels");

        scene.configureBasePlate(0, 0, 5);
        scene.showBasePlate();
        scene.idle(10);

        BlockPos centerPanel = new BlockPos(2, 1, 2);
        Selection initialPanels = util.select().fromTo(1, 1, 2, 3, 1, 2);

        scene.world().showSection(util.select().position(centerPanel), Direction.DOWN);
        scene.idle(5);
        scene.world().showSection(util.select().position(1, 1, 2), Direction.DOWN);
        scene.world().showSection(util.select().position(3, 1, 2), Direction.DOWN);
        scene.idle(15);
        scene.effects().createRedstoneParticles(util.grid().at(1, 1, 2), 0x80FFaa, 5);
        scene.effects().createRedstoneParticles(util.grid().at(2, 1, 2), 0x80FFaa, 5);
        scene.effects().createRedstoneParticles(util.grid().at(3, 1, 2), 0x80FFaa, 5);
        scene.idle(20);

        scene.overlay().showOutlineWithText(initialPanels, 60).attachKeyFrame()
                .colored(PonderPalette.GREEN)
                .text("Connected Solar Panels will share energy with each other")
                .pointAt(util.vector().blockSurface(centerPanel, Direction.WEST))
                .placeNearTarget();
        scene.idle(70);

        scene.world().hideSection(initialPanels, Direction.DOWN);
        scene.idle(15);
        scene.world().setBlocks(initialPanels, Blocks.AIR.defaultBlockState(), false);

        scene.world().showIndependentSection(util.select().fromTo(0, 1, 2, 4, 3, 2), Direction.DOWN);
        scene.idle(15);
        for(int x = 1; x <= 3; x++) {
            BlockPos current = new BlockPos(x, 3, 2);

            scene.world().modifyBlock(current, PanelScenes::activatePanel, false);

            indicateTamerRedstone(scene, current);
            indicateEnergy(scene, current);
        }
        scene.idle(25);

        BlockPos lamp = new BlockPos(2, 2, 2);

        scene.overlay().showText(60)
                .attachKeyFrame()
                .colored(PonderPalette.FAST)
                .text("Solar Panels will push Energy into accepting blocks below them")
                .pointAt(util.vector().blockSurface(lamp, Direction.WEST))
                .placeNearTarget();

        scene.idle(40);

        BlockPos panelRight = new BlockPos(1, 3, 2);
        BlockPos panelCenter = new BlockPos(2, 3, 2);
        BlockPos panelLeft = new BlockPos(3, 3, 2);

        AABB panelRightBB = new AABB(panelRight).setMaxY(3.5);
        AABB panelCenterBB = new AABB(panelCenter).setMaxY(3.5);
        AABB panelLeftBB = new AABB(panelLeft).setMaxY(3.5);
        AABB lampBB = new AABB(lamp);

        scene.overlay().chaseBoundingBoxOutline(
                PonderPalette.FAST,
                panelRightBB,
                panelRightBB,
                5
        );
        scene.overlay().chaseBoundingBoxOutline(
                PonderPalette.FAST,
                panelLeftBB,
                panelLeftBB,
                5
        );
        scene.idle(5);

        scene.overlay().chaseBoundingBoxOutline(
                PonderPalette.FAST,
                panelRightBB,
                panelCenterBB,
                10
        );
        scene.overlay().chaseBoundingBoxOutline(
                PonderPalette.FAST,
                panelLeftBB,
                panelCenterBB,
                10
        );
        scene.idle(10);

        scene.overlay().chaseBoundingBoxOutline(
                PonderPalette.FAST,
                panelCenterBB,
                panelCenterBB,
                5
        );
        scene.idle(5);
        scene.overlay().chaseBoundingBoxOutline(
                PonderPalette.FAST,
                panelCenterBB,
                lampBB,
                15
        );
        scene.idle(10);

        scene.world().modifyBlock(lamp, s -> s.setValue(GrowthLampBlock.ACTIVE, true), false);
        indicateEnergy(scene, lamp);

        scene.idle(10);
    }

    private static BlockState activatePanel(BlockState state) {
        return state.setValue(BasePanelBlock.ACTIVE, true);
    }

    private static void indicateTamerRedstone(CreateSceneBuilder scene, BlockPos pos) {
        scene.effects().createRedstoneParticles(pos, 0xff0000, 5);
    }

    private static void indicateEnergy(CreateSceneBuilder scene, BlockPos pos) {
        scene.effects().createRedstoneParticles(pos, 0xc700ff, 10);
    }
}
