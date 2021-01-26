package vixikhd.portal.packet;

import vixikhd.portal.Portal;

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
            byte b1 = buffer[0];
            byte b2 = buffer[1];

            int pid = b1 | b2 << 8;
            Packet pk = PacketPool.packetPool[pid];
            if (pk != null) {
                pk = pk.clone();
                pk.setBuffer(buffer, 2);
                pk.decode();

                return pk;
            }

            Portal.getInstance().getLogger().error("Unhandled packet " + pid);
            return null;
        }
        catch (Exception e) {
            Portal.getInstance().getLogger().error("Unable to handle packet (" + e.getMessage() + ")");
            return null;
        }
    }

    public static void registerPacket(byte networkId, Packet packet) {
        PacketPool.packetPool[networkId] = packet;
    }
}
