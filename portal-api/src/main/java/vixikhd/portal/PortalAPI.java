package vixikhd.portal;

import lombok.AccessLevel;
import lombok.Getter;
import vixikhd.portal.network.Packet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class PortalAPI {

    @Getter(AccessLevel.PACKAGE)
    private static final List<Consumer<Packet>> handlers = new ArrayList<>();
    @Getter(AccessLevel.PACKAGE)
    private static final Map<Packet, Consumer<Packet>> responseQueue = new HashMap<>();

    private static ClientInterface client = null;

    /**
     * Registers handler, consumer will be called when receiving packet from proxy.
     */
    public static void registerHandler(Consumer<Packet> handler) {
        if(PortalAPI.handlers.contains(handler)) {
            throw new IllegalArgumentException("Handler " + handler.getClass().getName() + " is already registered");
        }

        PortalAPI.handlers.add(handler);
    }

    /**
     * Sends packet to proxy
     */
    public static void sendPacket(Packet request) {
        PortalAPI.getClient().sendPacket(request);
    }

    /**
     * Sends packet to proxy and calls callback when the response is received
     * @param request Packet
     * @param callback Consumer for handling response
     */
    public static void sendPacket(Packet request, Consumer<Packet> callback) {
        PortalAPI.responseQueue.put(request, callback);

        PortalAPI.sendPacket(request);
    }

    /**
     * Returns Packet or null
     */
    public static Packet receivePacket() {
        return PortalAPI.getClient().receivePacket();
    }

    public static void registerClient(ClientInterface client) {
        PortalAPI.client = client;
    }

    public static void unregisterClient() {
        if(PortalAPI.client == null) {
            return;
        }

        PortalAPI.client.stop();
        PortalAPI.client = null;
    }

    private static ClientInterface getClient() {
        if(PortalAPI.client == null) {
            throw new IllegalStateException("Client is not registered");
        }

        return PortalAPI.client;
    }
}
