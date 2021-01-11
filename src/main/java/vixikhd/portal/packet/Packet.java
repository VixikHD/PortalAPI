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

    public abstract void handlePacket();
}
