package vixikhd.portal.packet;

import java.util.UUID;

public class PlayerInfoRequestPacket extends Packet {

    public static final byte NETWORK_ID = ProtocolInfo.PLAYER_INFO_REQUEST_PACKET;

    public UUID uuid;

    public static PlayerInfoRequestPacket create(UUID uuid) {
        PlayerInfoRequestPacket pk = new PlayerInfoRequestPacket();
        pk.uuid = uuid;

        return pk;
    }

    @Override
    public void encodePayload() {
        this.putUUID(this.uuid);
    }

    @Override
    public void decodePayload() {
        this.uuid = this.getUUID();
    }

    @Override
    public boolean handlePacket() {
        return false;
    }

    @Override
    public byte pid() {
        return PlayerInfoRequestPacket.NETWORK_ID;
    }
}
