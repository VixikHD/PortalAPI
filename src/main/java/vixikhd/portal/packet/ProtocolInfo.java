package vixikhd.portal.packet;

import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.utils.VarInt;
import lombok.SneakyThrows;
import vixikhd.portal.Portal;

import java.io.ByteArrayInputStream;

public class ProtocolInfo {

    private static final int PID_MASK = 0x3ff; // 1023

    public static final byte AUTH_REQUEST_PACKET = (byte)0xd0;
    public static final byte AUTH_RESPONSE_PACKET = (byte)0xd1;

    @SneakyThrows
    public static Packet getPacket(byte[] buffer) {
        ByteArrayInputStream stream = new ByteArrayInputStream(buffer);
        DataPacket pk = Portal.getInstance().getServer().getNetwork().getPacket((int)(VarInt.readUnsignedVarInt(stream) & ProtocolInfo.PID_MASK));
        if (pk instanceof Packet) {
            pk.setBuffer(buffer, buffer.length - stream.available());
            pk.decode();

            return (Packet) pk;
        }

        return null;
    }
}
