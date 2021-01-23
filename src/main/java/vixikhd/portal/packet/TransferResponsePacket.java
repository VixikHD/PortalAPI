package vixikhd.portal.packet;

import cn.nukkit.Player;
import vixikhd.portal.Portal;

import java.util.Optional;
import java.util.UUID;

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
    public void encodePayload() {
        this.putUUID(this.uuid);
        this.putByte((byte)this.status);
        if(this.status == TransferResponsePacket.RESPONSE_ERROR) {
            this.putString(this.reason);
        }
    }

    @Override
    public void decodePayload() {
        this.uuid = this.getUUID();
        this.status = this.getByte();
        if(this.status == TransferResponsePacket.RESPONSE_ERROR) {
            this.reason = this.getString();
        }
    }

    @Override
    public byte pid() {
        return TransferResponsePacket.NETWORK_ID;
    }

    @Override
    public boolean handlePacket() {
        if(this.status == TransferResponsePacket.RESPONSE_SUCCESS) {
            return true;
        }

        String reason = this.reason;
        switch (this.status) {
            case TransferResponsePacket.RESPONSE_GROUP_NOT_FOUND:
                reason = "Invalid group";
                break;
            case TransferResponsePacket.RESPONSE_SERVER_NOT_FOUND:
                reason = "Invalid server";
                break;
            case TransferResponsePacket.RESPONSE_ALREADY_ON_SERVER:
                reason = "Player is already connected to target server";
                break;
            case TransferResponsePacket.RESPONSE_PLAYER_NOT_FOUND:
                reason = "Player not found";
                break;
        }

        Optional<Player> player = Portal.getInstance().getServer().getPlayer(this.uuid);
        String name = this.uuid.toString();
        if(player.isPresent()) {
            name = player.get().getName();
        }

        Portal.getInstance().getLogger().error("Error whilst transferring player " + name + ": " + reason);
        return true;
    }
}
