package vixikhd.portal.network.packets;

import vixikhd.portal.network.*;

import java.util.UUID;

@PacketId(ProtocolInfo.PLAYER_INFO_RESPONSE_PACKET)
@PacketDirection(PacketDirection.DIRECTION_PROXY_CLIENT)
public class PlayerInfoResponsePacket extends Packet {

    public static final int RESPONSE_SUCCESS = 0;
    public static final int RESPONSE_PLAYER_NOT_FOUND = 1;

    public UUID uuid;
    public int status;
    public String xuid;
    public String address;

    @Override
    public void encodePayload(PacketBuffer buffer) {
        buffer.writeUUID(this.uuid);
        buffer.writeByte((byte)this.status);
        buffer.writeString(this.xuid);
        buffer.writeString(this.address);
    }

    @Override
    public void decodePayload(PacketBuffer buffer) {
        this.uuid = buffer.readUUID();
        this.status = buffer.readByte();
        this.xuid = buffer.readString();
        this.address = buffer.readString();
    }

    public static String translateStatus(int status) {
        switch (status) {
            case PlayerInfoResponsePacket.RESPONSE_SUCCESS:
                return null;
            case PlayerInfoResponsePacket.RESPONSE_PLAYER_NOT_FOUND:
                return "Player not found";
        }

        throw new IllegalArgumentException("Invalid status " + status + " received in PlayerInfoResponsePacket");
    }
}
