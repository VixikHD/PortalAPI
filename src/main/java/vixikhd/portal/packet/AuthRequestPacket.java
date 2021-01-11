package vixikhd.portal.packet;

import vixikhd.portal.Portal;

public class AuthRequestPacket extends Packet {

    public static final byte NETWORK_ID = ProtocolInfo.AUTH_REQUEST_PACKET;

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

    public void decodePayload() {
        this.type = this.getByte();
        this.secret = this.getString();
        this.name = this.getString();
        this.group = this.getString();
        this.address = this.getString();
    }

    public void encodePayload() {
        this.putByte((byte)this.type);
        this.putString(this.secret);
        this.putString(this.name);
        this.putString(this.group);
        this.putString(this.address);
    }

    public byte pid() {
        return AuthRequestPacket.NETWORK_ID;
    }

    public void handlePacket() {
        Portal.getInstance().getLogger().error("Received AuthRequestPacket unexpectedly");
    }
}
