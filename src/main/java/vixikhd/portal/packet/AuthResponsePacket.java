package vixikhd.portal.packet;

import vixikhd.portal.Portal;

public class AuthResponsePacket extends Packet {

    public static final byte NETWORK_ID = ProtocolInfo.AUTH_RESPONSE_PACKET;

    public static final int RESPONSE_SUCCESS = 0;
    public static final int RESPONSE_INCORRECT_SECRET = 1;
    public static final int RESPONSE_UNKNOWN_TYPE = 2;
    public static final int RESPONSE_INVALID_DATA = 3;

    public int status;

    public void decodePayload() {
        this.status = this.getByte();
    }

    public void encodePayload() {
        this.putByte((byte)this.status);
    }

    public byte pid() {
        return AuthResponsePacket.NETWORK_ID;
    }

    public boolean handlePacket() {
        if(this.status != AuthResponsePacket.RESPONSE_SUCCESS) {
            Portal.getInstance().getLogger().error("Error whilst connecting to the proxy (" + this.status + ")");
            return true;
        }

        Portal.getInstance().getLogger().info("Authentication with Portal was successful!");
        return true;
    }
}
