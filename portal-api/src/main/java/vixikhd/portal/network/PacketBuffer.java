package vixikhd.portal.network;

import lombok.Getter;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class PacketBuffer {

    private byte[] buffer = new byte[32];

    @Getter
    private int count = 0;
    @Getter
    private int offset = 0;

    public void writeByte(byte value) {
        this.ensureCapacity(this.count + 1);
        this.buffer[this.count++] = value;
    }

    public byte readByte() {
        return this.buffer[this.offset++];
    }

    public void writeBytes(byte[] bytes) {
        this.ensureCapacity(this.count + bytes.length);
        System.arraycopy(bytes, 0, this.buffer, this.count, bytes.length);
        this.count += bytes.length;
    }

    public byte[] readBytes(int len) {
        byte[] bytes = new byte[len];
        System.arraycopy(this.buffer, this.offset, bytes, 0, len);
        this.offset += len;
        return bytes;
    }

    public void writeUnsignedVarInt(int value) {
        while((value & -128) != 0) {
            this.writeByte((byte)(value & 127 | 128));
            value >>>= 7;
        }

        this.writeByte((byte)value);
    }

    public int readUnsignedVarInt() {
        int value = 0;
        int i = 0;

        do {
            byte b;
            if (((b = this.readByte()) & 128) == 0) {
                return value | b << i;
            }

            value |= (b & 127) << i;
            i += 7;
        } while(i <= 35);

        throw new RuntimeException("VarInt too big");
    }
    
    public void writeString(String string) {
        this.writeUnsignedVarInt(string.length());
        this.writeBytes(string.getBytes(StandardCharsets.UTF_8));
    }

    public String readString() {
        int len = this.readUnsignedVarInt();
        return new String(this.readBytes(len), StandardCharsets.UTF_8);
    }
    
    public void writeLInt(int value) {
        this.ensureCapacity(this.count + 4);
        for(int i = 0; i < 4; i++) {
            this.buffer[this.count++] = (byte)(value >> (i << 3) & 255);
        }
//        this.buffer[this.count++] = (byte)(value & 255);
//        this.buffer[this.count++] = (byte)(value >> 8 & 255);
//        this.buffer[this.count++] = (byte)(value >> 16 & 255);
//        this.buffer[this.count++] = (byte)(value >> 24 & 255);
    }
    
    public int readLInt() {
        return this.buffer[this.offset++] & 255 |
                (this.buffer[this.offset++] & 255) << 8 |
                (this.buffer[this.offset++] & 255) << 16 |
                (this.buffer[this.offset++] & 255) << 24;
    }

    public void writeLLong(long value) {
        this.ensureCapacity(this.count + 8);
        for(int i = 0; i < 8; i++) {
            this.buffer[this.count++] = (byte)((int)(value >>> (i << 3)));
        }
//        this.buffer[this.offset++] = (byte)((int)(value));
//        this.buffer[this.offset++] = (byte)((int)(value >>> 8));
//        this.buffer[this.offset++] = (byte)((int)(value >>> 16));
//        this.buffer[this.offset++] = (byte)((int)(value >>> 24));
//        this.buffer[this.offset++] = (byte)((int)(value >>> 32));
//        this.buffer[this.offset++] = (byte)((int)(value >>> 40));
//        this.buffer[this.offset++] = (byte)((int)(value >>> 48));
//        this.buffer[this.offset++] = (byte)((int)(value >>> 56));
    }

    public long readLLong() {
        long value = 0;
        for(int i = 0; i < 8; i++) {
            value += ((long)(this.buffer[this.offset++] & 255) << (i << 3));
        }

        return value;
//        return ((long)this.buffer[this.offset + 7] << 56) +
//                ((long)(this.buffer[this.offset + 6] & 255) << 48) +
//                ((long)(this.buffer[this.offset + 5] & 255) << 40) +
//                ((long)(this.buffer[this.offset + 4] & 255) << 32) +
//                ((long)(this.buffer[this.offset + 3] & 255) << 24) +
//                ((long)(this.buffer[this.offset + 2] & 255) << 16) +
//                ((long)(this.buffer[this.offset + 1] & 255) << 8) +
//                ((long) (this.buffer[this.offset] & 255));
    }

    public void writeUUID(UUID uuid) {
        this.writeLLong(uuid.getMostSignificantBits());
        this.writeLLong(uuid.getLeastSignificantBits());
    }

    public UUID readUUID() {
        return new UUID(this.readLLong(), this.readLLong());
    }

    public void ensureCapacity(int minimumSize) {
        if(minimumSize > this.buffer.length) {
            this.grow(minimumSize);
        }
    }

    private void grow(int size) {
        byte[] newBuffer = new byte[this.buffer.length << 1];
        System.arraycopy(this.buffer, 0, newBuffer, 0, this.buffer.length);

        this.buffer = newBuffer;
        this.ensureCapacity(size);
    }

    public void setBuffer(byte[] buffer) {
        this.buffer = buffer;
        this.count = buffer.length;
    }

    public byte[] getBuffer() {
        byte[] buffer = new byte[this.count];
        System.arraycopy(this.buffer, 0, buffer, 0, this.count);

        return buffer;
    }
}
