package vixikhd.portal.network.packets;

import vixikhd.portal.network.*;

@PacketId(ProtocolInfo.AUTH_REQUEST_PACKET)
@ResponseId(ProtocolInfo.AUTH_RESPONSE_PACKET)
@PacketDirection(PacketDirection.DIRECTION_CLIENT_PROXY)
public class AuthRequestPacket extends Packet {

    public static final int CLIENT_TYPE_SERVER = 0;

    public int type;
    public String secret;
    public String name;
    public String group; // extra data
    public String address; // extra data

    public static AuthRequestPacket create(int type, String secret, String name, String group, String address) {
        AuthRequestPacket pk = new AuthRequestPacket();
        pk.type = type;
        pk.secret = secret;
        pk.name = name;
        pk.group = group;
        pk.address = address;

        return pk;
    }

    public void encodePayload(PacketBuffer buffer) {
        buffer.writeByte((byte)this.type);
        buffer.writeString(this.secret);
        buffer.writeString(this.name);
        buffer.writeString(this.group);
        buffer.writeString(this.address);
    }

    public void decodePayload(PacketBuffer buffer) {
        this.type = buffer.readByte();
        this.secret = buffer.readString();
        this.name = buffer.readString();
        this.group = buffer.readString();
        this.address = buffer.readString();
    }
}
