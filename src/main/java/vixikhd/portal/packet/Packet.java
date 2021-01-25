package vixikhd.portal.packet;

import cn.nukkit.network.protocol.DataPacket;

abstract public class Packet extends DataPacket {

    @Override
    public final void encode() {
        this.offset = 0;
        this.encodeHeader();
        this.encodePayload();
    }

    @Override
    public final void decode() {
        this.offset = 0;
        this.decodeHeader();
        this.decodePayload();
    }

    @Override
    public final Packet clone() {
        return (Packet) super.clone();
    }

    protected void encodeHeader() {
        this.putByte(this.pid());
        this.putByte((byte) (this.pid() >> 8));
    }

    protected void decodeHeader() {
        this.getByte();
        this.getByte();
    }

    public abstract void encodePayload();

    public abstract void decodePayload();

    /**
     * Handles packet
     *
     * @return Returns if packet could be handled by server.
     * Returning false will show error message in the console.
     */
    public abstract boolean handlePacket();
}
