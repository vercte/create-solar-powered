package uwu.hachiro.createsolar.network;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import uwu.hachiro.createsolar.network.packet.EnergyResultPacketS2C;
import uwu.hachiro.createsolar.network.packet.RequestEnergyPacketC2S;

public class SolarPackets {
    public static void registerPayloadHandlers(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1").executesOn(HandlerThread.MAIN);

        registrar.playToServer(
                RequestEnergyPacketC2S.TYPE,
                RequestEnergyPacketC2S.STREAM_CODEC,
                (p, cx) -> RequestEnergyPacketC2S.handle(p, (ServerPlayer) cx.player())
        );

        registrar.playToClient(
                EnergyResultPacketS2C.TYPE,
                EnergyResultPacketS2C.STREAM_CODEC,
                (p, cx) -> EnergyResultPacketS2C.handle(p)
        );
    }
}
