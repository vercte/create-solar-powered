package uwu.hachiro.createsolar.util.datagen.assets;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.ParticleDescriptionProvider;
import uwu.hachiro.createsolar.CreateSolarPowered;
import uwu.hachiro.createsolar.SolarParticles;

public class SolarParticleDescriptionGen extends ParticleDescriptionProvider {
    public SolarParticleDescriptionGen(PackOutput output, ExistingFileHelper fileHelper) {
        super(output, fileHelper);
    }

    @Override
    protected void addDescriptions() {
        spriteSet(SolarParticles.SPARKLE.get(), CreateSolarPowered.at("sparkle"));
    }
}
