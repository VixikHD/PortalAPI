package vixikhd.portal.thread;

import lombok.AccessLevel;
import lombok.Getter;
import vixikhd.portal.Portal;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

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
            this.getSocket().setSoTimeout(100);
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
     *
     * @return Read bytes count, if -1, socket is closed
     */
    public int read(byte[] bytes) {
        int len;
        try {
            len = this.getSocket().getInputStream().read(bytes, 0, bytes.length);
        }
        catch (SocketTimeoutException e) {
            return 0;
        }
        catch (IOException e) {
            Portal.getInstance().getLogger().error("Error whilst reading socket: " + e.getMessage());
            return -1;
        }

        return len;
    }

    /**
     * Basically same method as Socket.write(), but doesn't throw exception
     */
    public boolean write(byte[] bytes) {
        try {
            this.getSocket().getOutputStream().write(bytes);
        } catch (IOException e) {
            Portal.getInstance().getLogger().error("Error whilst writing to socket: " + e.getMessage());
            return false;
        }
        return true;
    }
}
