package uwu.hachiro.createsolar.content.sunglasses;

import com.simibubi.create.content.equipment.goggles.GogglesItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.neoforged.neoforge.client.event.AddAttributeTooltipsEvent;
import net.neoforged.neoforge.client.event.GatherSkippedAttributeTooltipsEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.NotNull;
import uwu.hachiro.createsolar.CreateSolarPowered;
import uwu.hachiro.createsolar.SolarItems;
import uwu.hachiro.createsolar.util.SolarLang;

public class SunglassesItem extends Item implements Equipable {
    public static final ResourceLocation ATTRIBUTE = CreateSolarPowered.at("attribute.sunglasses.coolness");

    public SunglassesItem(Properties properties) {
        super(properties);

        NeoForge.EVENT_BUS.addListener(this::addAttributeTooltips);
        NeoForge.EVENT_BUS.addListener(this::skipAttributeTooltips);

        DispenserBlock.registerBehavior(this, ArmorItem.DISPENSE_ITEM_BEHAVIOR);
    }

    static {
        GogglesItem.addIsWearingPredicate(player -> SolarItems.SUNGLASSES.isIn(player.getItemBySlot(EquipmentSlot.HEAD)));
    }

    @NotNull
    public InteractionResultHolder<ItemStack> use(@NotNull Level world, @NotNull Player player, @NotNull InteractionHand hand) {
        return swapWithEquipmentSlot(this, world, player, hand);
    }

    @Override
    public boolean isEnderMask(@NotNull ItemStack stack, @NotNull Player player, @NotNull EnderMan enderman) {
        return true;
    }

    @Override
    @NotNull
    public ItemAttributeModifiers getDefaultAttributeModifiers(@NotNull ItemStack stack) {
        return ItemAttributeModifiers.builder()
                .add(Attributes.BURNING_TIME, new AttributeModifier(
                        ATTRIBUTE,
                        -0.15,
                        AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                ), EquipmentSlotGroup.HEAD)
                .build();
    }

    private void addAttributeTooltips(final AddAttributeTooltipsEvent event) {
        if(!event.shouldShow()) return;

        event.addTooltipLines(Component.translatable("item.modifiers.head").withStyle(ChatFormatting.GRAY));
        event.addTooltipLines(
                Component.translatable(
                                "attribute.modifier.plus.0",
                                ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(1),
                                SolarLang.builder().translate("attribute.name.generic.coolness").component()
                        )
                        .withStyle(ChatFormatting.BLUE)
        );
    }

    private void skipAttributeTooltips(final GatherSkippedAttributeTooltipsEvent event) {
        event.skipId(ATTRIBUTE);
    }

    @Override
    @NotNull
    public EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.HEAD;
    }
}
