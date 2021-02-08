package vixikhd.portal.network.packets;

import vixikhd.portal.network.*;

import java.util.UUID;

@PacketId(ProtocolInfo.PLAYER_INFO_REQUEST_PACKET)
@PacketDirection(PacketDirection.DIRECTION_CLIENT_PROXY)
public class PlayerInfoRequestPacket extends Packet {

    public UUID uuid;

    public static PlayerInfoRequestPacket create(UUID uuid) {
        PlayerInfoRequestPacket pk = new PlayerInfoRequestPacket();
        pk.uuid = uuid;

        return pk;
    }

    @Override
    public void encodePayload(PacketBuffer buffer) {
        buffer.writeUUID(this.uuid);
    }

    @Override
    public void decodePayload(PacketBuffer buffer) {
        this.uuid = buffer.readUUID();
    }
}
