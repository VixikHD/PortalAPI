package vixikhd.portal.packet;

import java.util.UUID;

public class TransferRequestPacket extends Packet {

    public static final byte NETWORK_ID = ProtocolInfo.TRANSFER_REQUEST_PACKET;

    public UUID uuid;
    public String group;
    public String name;

    public static TransferRequestPacket create(UUID uuid, String group, String name) {
        TransferRequestPacket pk = new TransferRequestPacket();
        pk.uuid = uuid;
        pk.group = group;
        pk.name = name;

        return pk;
    }

    @Override
    public void encodePayload() {
        this.putUUID(this.uuid);
        this.putString(this.group);
        this.putString(this.name);
    }

    @Override
    public void decodePayload() {
        this.uuid = this.getUUID();
        this.group = this.getString();
        this.name = this.getString();
    }

    @Override
    public byte pid() {
        return TransferRequestPacket.NETWORK_ID;
    }

    @Override
    public boolean handlePacket() {
        return false;
    }
}
