package vixikhd.portal.network.packets;

import vixikhd.portal.network.*;

import java.util.UUID;

@PacketId(TransferResponsePacket.NETWORK_ID)
@PacketDirection(PacketDirection.DIRECTION_PROXY_CLIENT)
public class TransferResponsePacket extends Packet {

    public static final byte NETWORK_ID = ProtocolInfo.TRANSFER_RESPONSE_PACKET;

    public static final int RESPONSE_SUCCESS = 0;
    public static final int RESPONSE_GROUP_NOT_FOUND = 1;
    public static final int RESPONSE_SERVER_NOT_FOUND = 2;
    public static final int RESPONSE_ALREADY_ON_SERVER = 3;
    public static final int RESPONSE_PLAYER_NOT_FOUND = 4;
    public static final int RESPONSE_ERROR = 5;

    public UUID uuid;
    public int status;
    public String reason;

    @Override
    public void encodePayload(PacketBuffer buffer) {
        buffer.writeUUID(this.uuid);
        buffer.writeByte((byte)this.status);
        if(this.status == TransferResponsePacket.RESPONSE_ERROR) {
            buffer.writeString(this.reason);
        }
    }

    @Override
    public void decodePayload(PacketBuffer buffer) {
        this.uuid = buffer.readUUID();
        this.status = buffer.readByte();
        if(this.status == TransferResponsePacket.RESPONSE_ERROR) {
            this.reason = buffer.readString();
        }
    }

    /**
     * Translates status, if successful, returns null
     */
    public static String translateStatus(int status, String reason) {
        switch (status) {
            case TransferResponsePacket.RESPONSE_SUCCESS:
                return null;
            case TransferResponsePacket.RESPONSE_GROUP_NOT_FOUND:
                return "Invalid group";
            case TransferResponsePacket.RESPONSE_SERVER_NOT_FOUND:
                return "Invalid server";
            case TransferResponsePacket.RESPONSE_ALREADY_ON_SERVER:
                return "Player is already connected to target server";
            case TransferResponsePacket.RESPONSE_PLAYER_NOT_FOUND:
                return "Player not found";
            case TransferResponsePacket.RESPONSE_ERROR:
                return reason;
        }

        throw new IllegalArgumentException("Invalid status " + status + " received in TransferResponsePacket");
    }
}
