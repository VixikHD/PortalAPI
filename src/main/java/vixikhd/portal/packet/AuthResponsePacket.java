package vixikhd.portal.packet;

import vixikhd.portal.Portal;

public class AuthResponsePacket extends Packet {

    public static final byte NETWORK_ID = ProtocolInfo.AUTH_RESPONSE_PACKET;

    public static final int RESPONSE_SUCCESS = 0;
    public static final int RESPONSE_INCORRECT_SECRET = 1;
    public static final int RESPONSE_UNKNOWN_TYPE = 2;
    public static final int RESPONSE_INVALID_DATA = 3;

    public int status;

    public void encodePayload() {
        this.putByte((byte)this.status);
    }

    public void decodePayload() {
        this.status = this.getByte();
    }

    public byte pid() {
        return AuthResponsePacket.NETWORK_ID;
    }

    public boolean handlePacket() {
        if(this.status == AuthResponsePacket.RESPONSE_SUCCESS) {
            Portal.getInstance().getLogger().info("Authentication with Portal was successful!");
            return true;
        }

        String reason = "";
        switch (this.status) {
            case AuthResponsePacket.RESPONSE_INCORRECT_SECRET:
                reason = "Incorrect secret";
                break;
            case AuthResponsePacket.RESPONSE_UNKNOWN_TYPE:
                reason = "Invalid authentication type";
                break;
            case AuthResponsePacket.RESPONSE_INVALID_DATA:
                reason = "Invalid data sent (tried to authenticate with invalid group or current server is already authenticated)";
        }
        Portal.getInstance().getLogger().error("Error whilst connecting to the proxy: " + reason);
        return true;
    }
}
