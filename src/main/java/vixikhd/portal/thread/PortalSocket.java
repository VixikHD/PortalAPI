package vixikhd.portal.thread;

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
    private Socket socket = null;

    /**
     * Creates socket
     *
     * @param host IP of target server (proxy's tcp socket)
     * @param port PORT of target server (proxy's tcp socket)
     */
    public PortalSocket(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * Connects to address which was given in constructor
     * @throws IOException if it isn't possible to connect to target address
     */
    public void connect() throws IOException {
        if(this.getSocket() == null) {
            this.socket = new Socket();
        }
        this.getSocket().connect(new InetSocketAddress(this.getHost(), this.getPort()));
    }

    /**
     * Closes socket
     */
    public void close() {
        try {
            this.getSocket().close();
            this.socket = null;
        } catch (IOException ignore) { }
    }

    /**
     * Basically same method as Socket.read(), but doesn't throw exception
     */
    public boolean read(byte[] bytes) {
        try {
            int len = this.getSocket().getInputStream().read(bytes, 0, bytes.length);
            if(len == -1) {
                throw new IOException("Read: Cannot read packet length (expected 4 bytes, got " + len + ")");
            }
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * Basically same method as Socket.write(), but doesn't throw exception
     */
    public boolean write(byte[] bytes) {
        try {
            this.getSocket().getOutputStream().write(bytes);
        } catch (IOException e) {
            return false;
        }
        return true;
    }
}
