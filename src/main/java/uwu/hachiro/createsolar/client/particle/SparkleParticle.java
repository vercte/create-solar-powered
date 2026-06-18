package uwu.hachiro.createsolar.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SparkleParticle extends GlowParticle {
    private final float initialQuadSize;
    private final float rotSpeed;

    protected SparkleParticle(ClientLevel level, double p_172137_, double p_172138_, double p_172139_, double p_172140_, double p_172141_, double p_172142_, SpriteSet p_172143_) {
        super(level, p_172137_, p_172138_, p_172139_, p_172140_, p_172141_, p_172142_, p_172143_);
        speedUpWhenYMotionIsBlocked = false;
        quadSize *= 1.5f;
        initialQuadSize = quadSize;
        rotSpeed = level.random.nextBoolean() ? -1 : 1;
    }

    @Override
    public void render(@NotNull VertexConsumer consumer, @NotNull Camera camera, float dt) {
        super.render(consumer, camera, dt);
        this.oRoll = roll;
        this.roll += (float)Math.toRadians(rotSpeed);

        double expectancy = (age + dt) / (double)lifetime;
        quadSize = initialQuadSize * (float)Math.sin(expectancy * Math.PI);
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public Provider(SpriteSet sprite) {
            this.sprite = sprite;
        }

        @Override
        public @Nullable Particle createParticle(
            @NotNull SimpleParticleType particleType, @NotNull ClientLevel level,
            double x, double y, double z,
            double xd, double yd, double zd
        ) {
            SparkleParticle sparkle = new SparkleParticle(level, x, y, z, 0.0, 0.0, 0.0, this.sprite);
            sparkle.setColor(1.0F, 0.9F, 1.0F);
            sparkle.setParticleSpeed(xd * 0.25, yd * 0.25, zd * 0.25);
            int lifetimeMin = 8;
            int lifetimeVar = 2;
            sparkle.setLifetime(level.random.nextInt(lifetimeVar) + lifetimeMin);
            return sparkle;
        }
    }
}
