package vixikhd.portal.packet;

import cn.nukkit.utils.VarInt;
import lombok.SneakyThrows;
import vixikhd.portal.Portal;

import java.io.ByteArrayInputStream;

public class PacketPool {

    private static final Packet[] packetPool = new Packet[256];

    public static void init() {
        PacketPool.registerPacket(ProtocolInfo.AUTH_REQUEST_PACKET, new AuthRequestPacket());
        PacketPool.registerPacket(ProtocolInfo.AUTH_RESPONSE_PACKET, new AuthResponsePacket());
        PacketPool.registerPacket(ProtocolInfo.TRANSFER_REQUEST_PACKET, new TransferRequestPacket());
        PacketPool.registerPacket(ProtocolInfo.TRANSFER_RESPONSE_PACKET, new TransferResponsePacket());
    }

    @SneakyThrows
    public static Packet getPacket(byte[] buffer) {
        ByteArrayInputStream stream = new ByteArrayInputStream(buffer);
        int pid = (int) VarInt.readUnsignedVarInt(stream);
        Packet pk = PacketPool.packetPool[pid];
        if (pk != null) {
            pk = pk.clone();
            pk.setBuffer(buffer, buffer.length - stream.available());
            pk.decode();

            return pk;
        }

        Portal.getInstance().getLogger().error("Unhandled packet " + pid);
        return null;
    }

    public static void registerPacket(byte networkId, Packet packet) {
        PacketPool.packetPool[networkId & 0xff] = packet;
    }
}
