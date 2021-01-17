package vixikhd.portal.packet;

import cn.nukkit.network.protocol.DataPacket;

abstract public class Packet extends DataPacket {

    @Override
    public final void encode() {
        this.reset();
        this.encodePayload();
    }

    @Override
    public final void decode() {
        this.decodePayload();
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
