package vixikhd.portal.network.packets;

import vixikhd.portal.network.*;

import java.util.UUID;

@PacketId(ProtocolInfo.TRANSFER_REQUEST_PACKET)
@PacketDirection(PacketDirection.DIRECTION_CLIENT_PROXY)
public class TransferRequestPacket extends Packet {

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
    public void encodePayload(PacketBuffer buffer) {
        buffer.writeUUID(this.uuid);
        buffer.writeString(this.group);
        buffer.writeString(this.name);
    }

    @Override
    public void decodePayload(PacketBuffer buffer) {
        this.uuid = buffer.readUUID();
        this.group = buffer.readString();
        this.name = buffer.readString();
    }
}
