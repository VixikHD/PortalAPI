package vixikhd.portal.thread;

import vixikhd.portal.network.Packet;
import vixikhd.portal.network.PacketBuffer;
import vixikhd.portal.network.packets.AuthRequestPacket;
import vixikhd.portal.utils.Logger;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SocketThread extends Thread {

    private final String host;
    private final int port;

    private final String secret;
    private final String name;
    private final String group;
    private final String address;

    private final ConcurrentLinkedQueue<PacketBuffer> sendQueue = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<PacketBuffer> receiveQueue = new ConcurrentLinkedQueue<>();

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

        long start = System.currentTimeMillis();

        loop:
        while (this.isRunning) {
            // Sending everything from sendQueue
            PacketBuffer toSend;
            while ((toSend = this.sendQueue.poll()) != null) {
                PacketBuffer buffer = new PacketBuffer();
                buffer.writeLInt(toSend.getBuffer().length);
                buffer.writeBytes(toSend.getBuffer());

                if(!this.socket.write(buffer.getBuffer())) {
                    this.socket.close();
                    this.reconnectToSocketServer();
                    continue loop;
                }
            }

            // Reading
            byte[] encodedLength = new byte[4];
            int len = this.socket.read(encodedLength);

            // Check for length
            if(!this.validatePacketLength(len)) {
                continue;
            }

            PacketBuffer header = new PacketBuffer();
            header.setBuffer(encodedLength);
            int length = header.readLInt();

            byte[] buffer = new byte[length];
            len = this.socket.read(buffer);
            if(!this.validatePacketLength(len)) {
                continue;
            }

            PacketBuffer receivedBuffer = new PacketBuffer();
            receivedBuffer.setBuffer(buffer);
            this.receiveQueue.add(receivedBuffer);

            long time = System.currentTimeMillis() - start;
            if(time < 200) {
                try {
                    Thread.sleep(200 - time);
                } catch (InterruptedException e) {
                    this.socket.close();
                    return;
                }
            }
            start = System.currentTimeMillis();
        }

        this.socket.close();
    }

    /**
     * Return if it is possible to read packet from length
     *
     * @param length Packet length
     * @return Returns if it is possible to read packet
     */
    private boolean validatePacketLength(int length) {
        if(length == -1) { // Connection closed
            this.socket.close();
            this.reconnectToSocketServer();
            return false;
        }
        return length != 0; // Any packets to receive
    }

    private void connectToSocketServer() {
        this.socket = new PortalSocket(this.host, this.port);
        this.reconnectToSocketServer();
    }

    private void reconnectToSocketServer() {
        try {
            this.socket.connect();
        } catch (IOException e) {
            Logger.getInstance().error("Could not connect to " + this.host + ":" + this.port + ": " + e.getMessage() + ". Trying again in 10 seconds.");
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ignore) { }

            this.socket.close();
            reconnectToSocketServer();
            return;
        }

        Logger.getInstance().info("Successfully connected to remote socket at " + this.host + ":" + this.address + "!");

        AuthRequestPacket pk = AuthRequestPacket.create(AuthRequestPacket.CLIENT_TYPE_SERVER, this.secret, this.name, this.group, this.address);
        this.addPacketToQueue(pk);
    }

    public void addPacketToQueue(Packet packet) {
        packet.encode();
        this.sendQueue.add(packet.getBuffer());
    }

    public PacketBuffer getBuffer() {
        return this.receiveQueue.poll();
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
