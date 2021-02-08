package vixikhd.portal.network.packets;

import vixikhd.portal.network.*;

@PacketId(ProtocolInfo.AUTH_RESPONSE_PACKET)
@PacketDirection(PacketDirection.DIRECTION_PROXY_CLIENT)
public class AuthResponsePacket extends Packet {

    public static final int RESPONSE_SUCCESS = 0;
    public static final int RESPONSE_INCORRECT_SECRET = 1;
    public static final int RESPONSE_UNKNOWN_TYPE = 2;
    public static final int RESPONSE_INVALID_DATA = 3;

    public int status;

    public void encodePayload(PacketBuffer buffer) {
        buffer.writeByte((byte)this.status);
    }

    public void decodePayload(PacketBuffer buffer) {
        this.status = buffer.readByte();
    }

    /**
     * Translates status, if successful, returns null
     */
    public static String translateStatus(int status) {
        switch (status) {
            case AuthResponsePacket.RESPONSE_SUCCESS:
                return null;
            case AuthResponsePacket.RESPONSE_INCORRECT_SECRET:
                return "Incorrect secret";
            case AuthResponsePacket.RESPONSE_UNKNOWN_TYPE:
                return "Invalid authentication type";
            case AuthResponsePacket.RESPONSE_INVALID_DATA:
                return "Invalid data sent (tried to authenticate with invalid group or current server is already authenticated)";
        }

        throw new IllegalArgumentException("Invalid status " + status + " received in AuthResponsePacket");
    }
}
