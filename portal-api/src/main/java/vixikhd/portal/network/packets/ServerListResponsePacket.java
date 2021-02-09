package vixikhd.portal.network.packets;

import lombok.AllArgsConstructor;
import vixikhd.portal.network.*;

@PacketId(ProtocolInfo.SERVER_LIST_RESPONSE_PACKET)
@PacketDirection(PacketDirection.DIRECTION_PROXY_CLIENT)
public class ServerListResponsePacket extends Packet {

    public ServerEntry[] servers;

    @Override
    public void encodePayload(PacketBuffer buffer) {
        buffer.writeLShort(this.servers.length);
        for(ServerEntry entry : this.servers) {
            buffer.writeString(entry.name);
            buffer.writeString(entry.group);
            buffer.writeBoolean(entry.isOnline);
            buffer.writeLShort(entry.playerCount);
        }
    }

    @Override
    public void decodePayload(PacketBuffer buffer) {
        int entryCount = buffer.readLShort();

        this.servers = new ServerEntry[entryCount];
        for(int i = 0; i < entryCount; i++) {
            this.servers[i] = new ServerEntry(buffer.readString(), buffer.readString(), buffer.readBoolean(), buffer.readLShort());
        }
    }

    @AllArgsConstructor
    public class ServerEntry {
        public String name;
        public String group;
        public boolean isOnline;
        public int playerCount;
    }
}
