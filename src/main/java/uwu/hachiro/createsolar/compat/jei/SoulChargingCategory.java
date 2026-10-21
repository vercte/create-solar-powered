package uwu.hachiro.createsolar.compat.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.createmod.catnip.gui.element.GuiGameElement;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import uwu.hachiro.createsolar.SolarBlocks;
import uwu.hachiro.createsolar.content.sculk_panel.SoulChargingRecipe;
import uwu.hachiro.createsolar.content.solar_panel.SolarPanelBlock;

import java.util.List;
import java.util.Optional;

public class SoulChargingCategory extends CreateRecipeCategory<SoulChargingRecipe> {
    public SoulChargingCategory(Info<SoulChargingRecipe> info) {
        super(info);
    }

    @Override
    protected void setRecipe(IRecipeLayoutBuilder builder, SoulChargingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 27, 38)
                .setBackground(getRenderedSlot(), -1, -1)
                .addIngredients(recipe.getProcessedItem());

        List<ProcessingOutput> results = recipe.getRollableResults();
        boolean single = results.size() == 1;
        for (int i = 0; i < results.size(); i++) {
            ProcessingOutput output = results.get(i);
            int xOffset = i % 2 == 0 ? 0 : 19;
            int yOffset = (i / 2) * -19;
            builder.addSlot(RecipeIngredientRole.OUTPUT, single ? 132 : 132 + xOffset, 38 + yOffset)
                    .setBackground(getRenderedSlot(output), -1, -1)
                    .addItemStack(output.getStack())
                    .addRichTooltipCallback(addStochasticTooltip(output));
        }
    }

    @Override
    protected void draw(@NotNull SoulChargingRecipe recipe, IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics graphics, double mouseX, double mouseY) {
        AllGuiTextures.JEI_SHADOW.render(graphics, 62, 47);

        Optional<ItemStack> displayedIngredient = recipeSlotsView.getSlotViews()
                .getFirst()
                .getDisplayedIngredient(VanillaTypes.ITEM_STACK);

        if (displayedIngredient.isEmpty())
            return;

        Item item = displayedIngredient.get()
                .getItem();
        if (!(item instanceof BlockItem blockItem))
            return;

        BlockState state = blockItem.getBlock()
                .defaultBlockState();

        BlockState panel = SolarBlocks.SCULK_PANEL.get()
                .defaultBlockState()
                .setValue(SolarPanelBlock.ACTIVE, true);

        PoseStack matrixStack = graphics.pose();
        matrixStack.pushPose();
        matrixStack.translate(74, 51, 100);
        matrixStack.mulPose(Axis.XP.rotationDegrees(-15.5f));
        matrixStack.mulPose(Axis.YP.rotationDegrees(22.5f));
        int scale = 20;

        GuiGameElement.of(state)
                .lighting(AnimatedKinetics.DEFAULT_LIGHTING)
                .scale(scale)
                .render(graphics);

        matrixStack.pushPose();
        matrixStack.translate(0, -20, 0);

        GuiGameElement.of(panel)
                .lighting(AnimatedKinetics.DEFAULT_LIGHTING)
                .scale(scale)
                .render(graphics);

        matrixStack.popPose();
        matrixStack.popPose();
    }
}
