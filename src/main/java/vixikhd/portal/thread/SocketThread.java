package vixikhd.portal.thread;

import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.utils.Binary;
import cn.nukkit.utils.MainLogger;
import vixikhd.portal.packet.AuthRequestPacket;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SocketThread extends Thread {

    private final String host;
    private final int port;

    private final String secret;
    private final String name;
    private final String group;
    private final String address;

    private final ConcurrentLinkedQueue<byte[]> sendQueue = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<byte[]> receiveBuffer = new ConcurrentLinkedQueue<>();

    private boolean isRunning;

    private PortalSocket socket;

    public SocketThread(String host, int port, String secret, String name, String group, String address) {
        this.host = host;
        this.port = port;
        this.secret = secret;
        this.name = name;
        this.group = group;
        this.address = address;

        this.start();
    }

    @Override
    public void run() {
        this.connectToSocketServer();

        while (this.isRunning) {
            byte[] toSend;
            while ((toSend = this.sendQueue.poll()) != null) {
                byte[] header = Binary.writeLInt(toSend.length);
                byte[] bytes = new byte[toSend.length + header.length];

                System.arraycopy(header, 0, bytes, 0, header.length);
                System.arraycopy(toSend, 0, bytes, header.length, toSend.length);

                this.socket.write(bytes);
            }

            byte[] encodedLength;
            int length;
            do {
                encodedLength = new byte[4];
                this.socket.read(encodedLength);

                length = Binary.readLInt(encodedLength);
                byte[] buffer = new byte[length];
                this.socket.read(buffer);

                this.receiveBuffer.add(buffer);
            }
            while (this.socket.canRead());
        }

        this.socket.close();
    }

    private void connectToSocketServer() {
        this.socket = new PortalSocket(this.host, this.port);
        try {
            this.socket.connect();
        } catch (IOException e) {
            MainLogger.getLogger().error("[Portal] Could not connect to " + this.host + ":" + this.port + ": " + e.getMessage() + ". Trying again in 10 seconds.");
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ignore) { }

            connectToSocketServer();
            return;
        }

        MainLogger.getLogger().info("[Portal] Successfully connected to " + this.host + ":" + this.address + "!");

        AuthRequestPacket pk = AuthRequestPacket.create(AuthRequestPacket.CLIENT_TYPE_SERVER, this.secret, this.name, this.group, this.address);
        this.addPacketToQueue(pk);
    }

    public void addPacketToQueue(DataPacket packet) {
        packet.encode();
        this.sendQueue.add(packet.getBuffer());
    }

    public byte[] getBuffer() {
        return this.receiveBuffer.poll();
    }

    @Override
    public synchronized void start() {
        this.isRunning = true;
        super.start();
    }

    @Override
    public void interrupt() {
        this.isRunning = false;
        super.interrupt();
    }
}
