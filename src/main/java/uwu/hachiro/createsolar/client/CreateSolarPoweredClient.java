package uwu.hachiro.createsolar.client;

import com.simibubi.create.AllItems;
import com.simibubi.create.CreateClient;
import com.simibubi.create.foundation.block.connected.CTModel;
import com.simibubi.create.foundation.block.connected.ConnectedTextureBehaviour;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.TooltipModifier;
import net.createmod.catnip.lang.FontHelper;
import net.createmod.catnip.registry.RegisteredObjectsHelper;
import net.createmod.ponder.foundation.PonderIndex;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import uwu.hachiro.createsolar.*;
import uwu.hachiro.createsolar.client.particle.SparkleParticle;
import uwu.hachiro.createsolar.client.ponder.SolarPonderPlugin;
import uwu.hachiro.createsolar.content.solar_panel.PanelCTBehaviour;

@Mod(value = CreateSolarPowered.ID, dist = Dist.CLIENT)
public class CreateSolarPoweredClient {
    public CreateSolarPoweredClient(IEventBus bus) {
        bus.addListener(this::onRegister);
        bus.addListener(this::registerParticleProviders);
        bus.addListener(this::clientInit);
    }

    private void onRegister(FMLCommonSetupEvent event) {
        registerCTBehaviours(SolarBlocks.SOLAR_PANEL, new PanelCTBehaviour(SolarSpriteShifts.SOLAR_PANEL_TOP, SolarSpriteShifts.SOLAR_PANEL_BOTTOM));
        registerCTBehaviours(SolarBlocks.SCULK_PANEL, new PanelCTBehaviour(SolarSpriteShifts.SCULK_PANEL_TOP));

        ItemDescription.referKey(SolarItems.SUNGLASSES, AllItems.GOGGLES);
        TooltipModifier.REGISTRY.register(SolarItems.SUNGLASSES.get(), new ItemDescription.Modifier(SolarItems.SUNGLASSES.get(), FontHelper.Palette.STANDARD_CREATE));
    }

    private void registerParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(SolarParticles.SPARKLE.get(), SparkleParticle.Provider::new);
    }

    private void clientInit(final FMLClientSetupEvent event) {
        PonderIndex.addPlugin(new SolarPonderPlugin());
    }

    private void registerCTBehaviours(DeferredBlock<?> entry, ConnectedTextureBehaviour behaviour) {
        CreateClient.MODEL_SWAPPER.getCustomBlockModels()
                .register(
                        RegisteredObjectsHelper.getKeyOrThrow(entry.get()),
                        model -> new CTModel(model, behaviour)
                );
    }
}
