package uwu.hachiro.createsolar.client;

import net.createmod.ponder.foundation.PonderIndex;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import uwu.hachiro.createsolar.CreateSolarPowered;
import uwu.hachiro.createsolar.SolarParticles;
import uwu.hachiro.createsolar.client.particle.SparkleParticle;
import uwu.hachiro.createsolar.ponder.SolarPonderIndex;

@Mod(value = CreateSolarPowered.ID, dist = Dist.CLIENT)
public class SolarClient {
    public SolarClient(IEventBus bus) {
        bus.addListener(this::clientInit);
        bus.addListener(this::registerParticleProviders);
    }

    private void registerParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(SolarParticles.SPARKLE.get(), SparkleParticle.Provider::new);
    }

    public void clientInit(final FMLClientSetupEvent event) {
        PonderIndex.addPlugin(new SolarPonderIndex());
    }
}
