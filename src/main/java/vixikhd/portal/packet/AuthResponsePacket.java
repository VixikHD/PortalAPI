package vixikhd.portal.packet;

import vixikhd.portal.Portal;

public class AuthResponsePacket extends Packet {

    public static final byte NETWORK_ID = ProtocolInfo.AUTH_RESPONSE_PACKET;

    public static final int RESPONSE_SUCCESS = 0;
    public static final int RESPONSE_INCORRECT_SECRET = 1;
    public static final int RESPONSE_UNKNOWN_TYPE = 2;
    public static final int RESPONSE_INVALID_DATA = 3;

    public int status;
    public String reason;

    public static AuthResponsePacket create(int status, String reason) {
        AuthResponsePacket pk = new AuthResponsePacket();
        pk.status = status;
        pk.reason = reason;

        return pk;
    }

    public void decodePayload() {
        this.status = this.getByte();
        this.reason = this.getString();
    }

    public void encodePayload() {
        this.putByte((byte)this.status);
        this.putString(this.reason);
    }

    public byte pid() {
        return AuthResponsePacket.NETWORK_ID;
    }

    public void handlePacket() {
        if(this.status != AuthResponsePacket.RESPONSE_SUCCESS) {
            Portal.getInstance().getLogger().error("Error whilst connecting to the proxy (" + this.status + "): " + this.reason);
            return;
        }

        Portal.getInstance().getLogger().info(this.reason);
    }
}
