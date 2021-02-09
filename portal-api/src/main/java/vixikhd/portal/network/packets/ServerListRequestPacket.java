package vixikhd.portal.network.packets;

import vixikhd.portal.network.*;

@PacketId(ProtocolInfo.SERVER_LIST_REQUEST_PACKET)
@ResponseId(ProtocolInfo.SERVER_LIST_RESPONSE_PACKET)
@PacketDirection(PacketDirection.DIRECTION_CLIENT_PROXY)
public class ServerListRequestPacket extends Packet {

    public static ServerListRequestPacket create() {
        return new ServerListRequestPacket();
    }

    @Override
    public void encodePayload(PacketBuffer buffer) { }

    @Override
    public void decodePayload(PacketBuffer buffer) { }
}
