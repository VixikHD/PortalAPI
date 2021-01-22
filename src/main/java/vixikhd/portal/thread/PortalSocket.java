package vixikhd.portal.thread;

import cn.nukkit.utils.MainLogger;
import lombok.AccessLevel;
import lombok.Getter;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class PortalSocket  {

    @Getter
    private final String host;
    @Getter
    private final int port;

    @Getter(AccessLevel.PRIVATE)
    private final Socket socket;

    /**
     * Creates socket
     *
     * @param host IP of target server (proxy's tcp socket)
     * @param port PORT of target server (proxy's tcp socket)
     */
    public PortalSocket(String host, int port) {
        this.host = host;
        this.port = port;

        this.socket = new Socket();
    }

    /**
     * Connects to address which was given in constructor
     * @throws IOException if it isn't possible to connect to target address
     */
    public void connect() throws IOException {
        this.socket.connect(new InetSocketAddress(this.getHost(), this.getPort()));
    }

    /**
     * We need that if proxy comes down
     */
    private void reconnect() {
        try {
            this.connect();
        } catch (IOException e) {
            MainLogger.getLogger().error("Could not connect to " + this.getHost() + ":" + this.getPort() + ": " + e.getMessage() + "; Trying again in 5 seconds.");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ignore) {
                return;
            }
            this.reconnect();
        }
    }

    private void reconnect(IOException exception) {
        MainLogger.getLogger().info("Socket was unexpectedly closed (error: "+exception.getMessage()+"). Reconnecting...");

        this.reconnect();
    }

    /**
     * Closes socket
     */
    public void close() {
        try {
            this.getSocket().close();
        } catch (IOException ignore) { }
    }

    /**
     * @return If it is possible read from the socket
     */
    public boolean canRead() {
        int available = 0;
        try {
            available = this.getSocket().getInputStream().available();
        } catch (IOException e) {
            this.reconnect(e);
        }

        return available > 4; // 4 bytes to get len of packet
    }

    /**
     * Basically same method as Socket.read(), but doesn't throw exception
     */
    public void read(byte[] bytes) {
        try {
            this.getSocket().getInputStream().read(bytes, 0, bytes.length);
        } catch (IOException e) {
            this.reconnect(e);
        }
    }

    /**
     * Basically same method as Socket.write(), but doesn't throw exception
     */
    public void write(byte[] bytes) {
        try {
            this.getSocket().getOutputStream().write(bytes);
        } catch (IOException e) {
            this.reconnect(e);
        }
    }
}
