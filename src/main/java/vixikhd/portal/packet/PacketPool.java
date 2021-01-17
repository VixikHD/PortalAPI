package vixikhd.portal.packet;

import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.utils.VarInt;
import lombok.SneakyThrows;
import vixikhd.portal.Portal;

import java.io.ByteArrayInputStream;

public class PacketPool {

    public static final int PID_MASK = 0x3ff; // 1023

    private static final Class<? extends Packet>[] packetPool = new Class[256];

    public static void init() {
        PacketPool.registerPacket(ProtocolInfo.AUTH_REQUEST_PACKET, AuthRequestPacket.class);
        PacketPool.registerPacket(ProtocolInfo.AUTH_RESPONSE_PACKET, AuthResponsePacket.class);
        PacketPool.registerPacket(ProtocolInfo.TRANSFER_REQUEST_PACKET, TransferRequestPacket.class);
        PacketPool.registerPacket(ProtocolInfo.TRANSFER_RESPONSE_PACKET, TransferResponsePacket.class);
    }

    @SneakyThrows
    public static Packet getPacket(byte[] buffer) {
        ByteArrayInputStream stream = new ByteArrayInputStream(buffer);
        DataPacket pk = Portal.getInstance().getServer().getNetwork().getPacket((int)(VarInt.readUnsignedVarInt(stream) & PacketPool.PID_MASK));
        if (pk instanceof Packet) {
            pk.setBuffer(buffer, buffer.length - stream.available());
            pk.decode();

            return (Packet) pk;
        }

        return null;
    }

    public static void registerPacket(byte networkId, Class<? extends Packet> packet) {
        PacketPool.packetPool[networkId & 255] = packet;
    }
}
