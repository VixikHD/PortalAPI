package vixikhd.portal.packet;

import vixikhd.portal.Portal;

public class TransferResponsePacket extends Packet {

    public static final byte NETWORK_ID = ProtocolInfo.TRANSFER_RESPONSE_PACKET;

    public static final int RESPONSE_SUCCESS = 0;
    public static final int RESPONSE_GROUP_NOT_FOUND = 1;
    public static final int RESPONSE_SERVER_NOT_FOUND = 2;
    public static final int RESPONSE_ALREADY_ON_SERVER = 3;
    public static final int RESPONSE_PLAYER_NOT_FOUND = 4;
    public static final int RESPONSE_ERROR = 5;

    public long entityRuntimeId;
    public int status;
    public String reason;

    @Override
    public void encodePayload() {
        this.putEntityRuntimeId(this.entityRuntimeId);
        this.putByte((byte)this.status);
        this.putString(this.reason);
    }

    @Override
    public void decodePayload() {
        this.entityRuntimeId = this.getEntityRuntimeId();
        this.status = this.getByte();
        this.reason = this.getString();
    }

    @Override
    public byte pid() {
        return TransferResponsePacket.NETWORK_ID;
    }

    @Override
    public boolean handlePacket() {
        if(this.status != TransferResponsePacket.RESPONSE_SUCCESS) {
            Portal.getInstance().getLogger().error("Error (" + this.status + ") whilst transferring player: " + this.reason);
            return true;
        }

        Portal.getInstance().getLogger().info(this.reason);
        return true;
    }
}
