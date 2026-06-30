package uwu.hachiro.createsolar;

import net.createmod.ponder.foundation.PonderIndex;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import uwu.hachiro.createsolar.ponder.SolarPonderIndex;

@Mod(value = CreateSolarPowered.ID, dist = Dist.CLIENT)
public class CreateSolarPoweredClient {

    public CreateSolarPoweredClient(IEventBus bus) {
        bus.addListener(this::clientInit);
    }

    public void clientInit(final FMLClientSetupEvent event) {
        PonderIndex.addPlugin(new SolarPonderIndex());
    }
}
