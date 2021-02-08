package vixikhd.portal.network;

import lombok.Getter;

import java.lang.reflect.InvocationTargetException;

@PacketId(-1)
@PacketDirection(PacketDirection.DIRECTION_UNKNOWN)
abstract public class Packet {

    @Getter
    private final PacketBuffer buffer = new PacketBuffer();

    public final void encode() {
        this.encodeHeader(this.buffer);
        this.encodePayload(this.buffer);
    }

    public final void decode() {
        this.decodeHeader(this.buffer);
        this.decodePayload(this.buffer);
    }

    protected void encodeHeader(PacketBuffer buffer) {
        byte pid = this.getClass().getAnnotation(PacketId.class).value();

        buffer.writeByte(pid);
        buffer.writeByte((byte)(pid >> 8));
    }

    protected void decodeHeader(PacketBuffer buffer) {
        buffer.readByte();
        buffer.readByte();
    }

    public abstract void encodePayload(PacketBuffer buffer);

    public abstract void decodePayload(PacketBuffer buffer);

    public Packet clone() {
        try {
            return (Packet) super.clone();
        } catch (CloneNotSupportedException ignore) {}

        try {
            return this.getClass().getConstructor().newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ignore) {
            return null;
        }
    }
}
