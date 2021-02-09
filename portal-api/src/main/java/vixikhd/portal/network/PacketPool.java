package vixikhd.portal.network;

import vixikhd.portal.network.packets.*;
import vixikhd.portal.utils.Logger;

public class PacketPool {

    private static final Packet[] packetPool = new Packet[256];

    public static void init() {
        PacketPool.registerPacket(ProtocolInfo.AUTH_REQUEST_PACKET, new AuthRequestPacket());
        PacketPool.registerPacket(ProtocolInfo.AUTH_RESPONSE_PACKET, new AuthResponsePacket());
        PacketPool.registerPacket(ProtocolInfo.TRANSFER_REQUEST_PACKET, new TransferRequestPacket());
        PacketPool.registerPacket(ProtocolInfo.TRANSFER_RESPONSE_PACKET, new TransferResponsePacket());
        PacketPool.registerPacket(ProtocolInfo.PLAYER_INFO_REQUEST_PACKET, new PlayerInfoRequestPacket());
        PacketPool.registerPacket(ProtocolInfo.PLAYER_INFO_RESPONSE_PACKET, new PlayerInfoResponsePacket());
    }

    public static Packet getPacket(byte[] buffer) {
        try {
            int pid = PacketPool.decodePacketId(buffer);
            Packet packet = PacketPool.packetPool[pid];
            if(packet == null) {
                Logger.getInstance().error("Received unknown packet (pid=" + pid + ")");
                return null;
            }

            packet = packet.clone();
            packet.getBuffer().setBuffer(buffer);
            packet.decode();

            return packet;
        }
        catch (Exception e) {
            Logger.getInstance().error("Unable to handle packet (" + e.getClass().getName() + ": " + e.getMessage() + ")");
            e.printStackTrace();
            return null;
        }
    }

    public static Packet getPacketById(int id) {
        return PacketPool.packetPool[id] != null ? PacketPool.packetPool[id].clone() : null;
    }

    private static int decodePacketId(byte[] buffer) {
        byte b1 = buffer[0];
        byte b2 = buffer[1];

        return b1 | b2 << 8;
    }

    public static void registerPacket(byte networkId, Packet packet) {
        PacketPool.packetPool[networkId] = packet;
    }
}
