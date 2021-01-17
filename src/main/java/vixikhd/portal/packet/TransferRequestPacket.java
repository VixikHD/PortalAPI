package vixikhd.portal.packet;

public class TransferRequestPacket extends Packet {

    public static final byte NETWORK_ID = ProtocolInfo.TRANSFER_REQUEST_PACKET;

    public long entityRuntimeId;
    public String group;
    public String name;

    @Override
    public void encodePayload() {
        this.putEntityRuntimeId(entityRuntimeId);
        this.putString(this.group);
        this.putString(this.name);
    }

    @Override
    public void decodePayload() {
        this.entityRuntimeId = this.getEntityRuntimeId();
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
