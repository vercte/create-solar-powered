package uwu.hachiro.createsolar;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class SolarParticles {
    private static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, CreateSolarPowered.ID);

    public static final Supplier<SimpleParticleType> SPARKLE = PARTICLE_TYPES.register("sparkle", () -> new SimpleParticleType(false));

    public static void loadAndRegister(IEventBus bus) {
        PARTICLE_TYPES.register(bus);
    }
}
