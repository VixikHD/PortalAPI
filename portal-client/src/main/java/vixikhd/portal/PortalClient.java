package vixikhd.portal;

import vixikhd.portal.network.Packet;
import vixikhd.portal.network.PacketBuffer;
import vixikhd.portal.network.PacketPool;
import vixikhd.portal.thread.SocketThread;

public class PortalClient implements ClientInterface {

    private final SocketThread thread;

    /**
     * PortalClient
     *
     * @param host IP to proxy tcp socket
     * @param port PORT to proxy tcp socket
     * @param secret Secret which must be equal to the proxy
     * @param name Current client name (eg. Hub-1)
     * @param group Current group name (eg. Hub)
     * @param address Address of current server
     */
    public PortalClient(String host, int port, String secret, String name, String group, String address) {
        this.thread = new SocketThread(host, port, secret, name, group, address);
    }

    /**
     * Function which sends packet to the server
     *
     * @param packet packet
     */
    @Override
    public void sendPacket(Packet packet) {
        this.thread.addPacketToQueue(packet);
    }

    /**
     * Function which receives packet from the server
     *
     * @return packet packet
     */
    @Override
    public Packet receivePacket() {
        PacketBuffer buffer = this.thread.getBuffer();
        if(buffer == null) {
            return null;
        }

        return PacketPool.getPacket(buffer.getBuffer());
    }
}
