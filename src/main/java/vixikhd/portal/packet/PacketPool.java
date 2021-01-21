package vixikhd.portal.packet;

import cn.nukkit.utils.VarInt;
import lombok.SneakyThrows;

import java.io.ByteArrayInputStream;

public class PacketPool {

    public static final int PID_MASK = 0x3ff; // 1023

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
        Packet pk = PacketPool.packetPool[((int)(VarInt.readUnsignedVarInt(stream) & PacketPool.PID_MASK))].clone();
        if (pk != null) {
            pk.setBuffer(buffer, buffer.length - stream.available());
            pk.decode();

            return pk;
        }

        return null;
    }

    public static void registerPacket(byte networkId, Packet packet) {
        PacketPool.packetPool[networkId & 255] = packet;
    }
}
