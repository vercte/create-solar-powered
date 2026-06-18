package uwu.hachiro.createsolar.network.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;
import uwu.hachiro.createsolar.CreateSolarPowered;

public record EnergyResultPacketS2C(boolean flowingDown, int output, int energy, int capacity) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<EnergyResultPacketS2C> TYPE = new CustomPacketPayload.Type<>(CreateSolarPowered.at("energy_result"));
    public static final StreamCodec<ByteBuf, EnergyResultPacketS2C> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, EnergyResultPacketS2C::flowingDown,
            ByteBufCodecs.INT, EnergyResultPacketS2C::output,
            ByteBufCodecs.INT, EnergyResultPacketS2C::energy,
            ByteBufCodecs.INT, EnergyResultPacketS2C::capacity,
            EnergyResultPacketS2C::new
    );

    private static boolean lastFlow = false;
    private static int lastOutput = 0;
    private static int lastEnergy = 0;
    private static int lastCapacity = 0;

    @Override
    @NotNull
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(EnergyResultPacketS2C packet) {
        lastFlow = packet.flowingDown;
        lastOutput = packet.output;
        lastEnergy = packet.energy;
        lastCapacity = packet.capacity;
    }

    public static boolean isOutputtingBelow() {
        return lastFlow;
    }

    public static int getLastOutput() {
        return lastOutput;
    }

    public static int getLastEnergy() {
        return lastEnergy;
    }

    public static int getLastCapacity() {
        return lastCapacity;
    }
}
