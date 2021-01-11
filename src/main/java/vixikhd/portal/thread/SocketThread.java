package vixikhd.portal.thread;

import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.utils.Binary;
import cn.nukkit.utils.MainLogger;
import lombok.SneakyThrows;
import org.apache.commons.lang.ArrayUtils;
import vixikhd.portal.packet.AuthRequestPacket;

import java.net.InetSocketAddress;
import java.net.Socket;
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

    public SocketThread(String host, int port, String secret, String name, String group, String address) {
        this.host = host;
        this.port = port;
        this.secret = secret;
        this.name = name;
        this.group = group;
        this.address = address;

        this.start();
    }

    @SneakyThrows
    @Override
    public void run() {
        Socket socket = this.connectToSocketServer();

        while (this.isRunning) {
            byte[] toSend;
            while ((toSend = this.sendQueue.poll()) != null) {
                socket.getOutputStream().write(ArrayUtils.addAll(Binary.writeLInt(toSend.length), toSend));
            }

            byte[] lengthInBytes;
            int length;
            int read;
            do {
                lengthInBytes = new byte[4];
                read = socket.getInputStream().read(lengthInBytes);
                if(read != 4) {
                    socket.close();
                    socket = this.connectToSocketServer();
                    continue;
                }

                length = Binary.readLInt(lengthInBytes);
                byte[] buffer = new byte[length];
                read = socket.getInputStream().read(buffer);
                if(read != length) {
                    socket.close();
                    socket = this.connectToSocketServer();
                    continue;
                }

                this.receiveBuffer.add(buffer);
            }
            while (socket.getInputStream().available() >= 4);
        }

        socket.close();
    }

    @SneakyThrows
    public Socket connectToSocketServer() {
        Socket socket;
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(this.host, this.port));
        } catch (Exception e) {
            MainLogger.getLogger().error("[Portal] Could not connect to " + this.host + ":" + this.port + ". Trying again in 10 seconds.");
            Thread.sleep(10000);
            return connectToSocketServer();
        }

        AuthRequestPacket pk = AuthRequestPacket.create(AuthRequestPacket.CLIENT_TYPE_SERVER, this.secret, this.name, this.group, this.address);
        this.addPacketToQueue(pk);

        return socket;
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
