package uwu.hachiro.createsolar.network.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import uwu.hachiro.createsolar.CreateSolarPowered;
import uwu.hachiro.createsolar.SolarConfig;
import uwu.hachiro.createsolar.content.panel.SolarPanelBlockEntity;
import uwu.hachiro.createsolar.content.panel.storage.SolarPanelSharedEnergyStorage;

public record RequestEnergyPacketC2S(BlockPos pos) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<RequestEnergyPacketC2S> TYPE = new CustomPacketPayload.Type<>(CreateSolarPowered.at("energy_request"));
    public static final StreamCodec<ByteBuf, RequestEnergyPacketC2S> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, RequestEnergyPacketC2S::pos, RequestEnergyPacketC2S::new
    );
    private static BlockPos lastPos = null;
    private static long lastRequest = 0;

    @Override
    @NotNull
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void send(BlockPos pos) {
        boolean newPos = lastPos == null || !lastPos.equals(pos);
        if(newPos || Util.getMillis() - lastRequest > 200) {
            lastPos = pos;
            lastRequest = Util.getMillis();
            PacketDistributor.sendToServer(new RequestEnergyPacketC2S(pos));
        }
    }

    public static void handle(RequestEnergyPacketC2S packet, ServerPlayer player) {
        Level level = player.level();
        BlockEntity be = level.getBlockEntity(packet.pos());
        if(!(be instanceof SolarPanelBlockEntity panel)) return;

        SolarPanelSharedEnergyStorage shared = panel.getSharedEnergyStorage();
        int energy = shared != null ? shared.getEnergyStored() : panel.getEnergyStored();
        int capacity = shared != null ? shared.getMaxEnergyStored() : SolarConfig.SOLAR_PANEL_MAX_ENERGY_STORED.getAsInt();

        PacketDistributor.sendToPlayer(player, new EnergyResultPacketS2C(panel.isOutputtingBelow(), panel.getLastOutput(), energy, capacity));
    }
}
