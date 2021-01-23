package vixikhd.portal.packet;

import java.util.UUID;

public class PlayerInfoResponsePacket extends Packet {

    public static final int NETWORK_ID = ProtocolInfo.PLAYER_INFO_RESPONSE_PACKET;

    public static final int RESPONSE_SUCCESS = 0;
    public static final int RESPONSE_PLAYER_NOT_FOUND = 1;

    public UUID uuid;
    public int status;
    public String xuid;
    public String address;

    @Override
    public void encodePayload() {
        this.putUUID(this.uuid);
        this.putByte((byte)this.status);
        this.putString(this.xuid);
        this.putString(this.address);
    }

    @Override
    public void decodePayload() {
        this.uuid = this.getUUID();
        this.status = this.getByte();
        this.xuid = this.getString();
        this.address = this.getString();
    }

    @Override
    public boolean handlePacket() {
        return true;
    }

    @Override
    public byte pid() {
        return PlayerInfoResponsePacket.NETWORK_ID;
    }
}
