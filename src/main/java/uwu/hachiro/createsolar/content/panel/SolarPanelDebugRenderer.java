package uwu.hachiro.createsolar.content.panel;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class SolarPanelDebugRenderer implements BlockEntityRenderer<SolarPanelBlockEntity> {
    private final BlockEntityRendererProvider.Context context;

    public SolarPanelDebugRenderer(BlockEntityRendererProvider.Context context) {
        this.context = context;
    }

    @Override
    public void render(@NotNull SolarPanelBlockEntity be, float dt, @NotNull PoseStack pose, @NotNull MultiBufferSource buffer, int light, int overlay) {
        Font font = context.getFont();
        int energy = be.getEnergyStorage().energy;
        String label = energy + "⚡";

        pose.pushPose();

        pose.mulPose(Axis.YP.rotationDegrees(180.0F));
        float scale = 0.03f;
        pose.translate(-0.5f, 1, -0.5);
        pose.scale(scale, -scale, scale);
        Vec3 cameraPosition = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();

        BlockPos pos = be.getBlockPos();
        Vec3 diff = pos.getCenter().subtract(cameraPosition);

        float yRot = (float) Mth.atan2(diff.z, -diff.x);
        pose.mulPose(Axis.YP.rotation((float) (yRot - Math.PI / 2)));

        int background = (int)(0.5 * 255.0) << 24;
        font.drawInBatch(
                label,
                -font.width(label) / 2f,
                0,
                0,
                false,
                pose.last().pose(),
                buffer,
                Font.DisplayMode.NORMAL,
                background,
                LightTexture.pack(15, 15)
        );
        font.drawInBatch(
                label,
                -font.width(label) / 2f,
                0,
                0xFFFFFFFF,
                false,
                pose.last().pose(),
                buffer,
                Font.DisplayMode.SEE_THROUGH,
                0,
                LightTexture.pack(15, 15)
        );

        pose.popPose();
    }
}
