package vixikhd.portal;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import vixikhd.portal.network.Packet;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PortalAPI {

    @Getter(AccessLevel.PACKAGE)
    private static final List<Consumer<Packet>> handlers = new ArrayList<>();
    @Setter
    private static ClientInterface client = null;

    public static void registerHandler(Consumer<Packet> handler) {
        if(PortalAPI.handlers.contains(handler)) {
            throw new IllegalArgumentException("Handler " + handler.getClass().getName() + " is already registered");
        }
        PortalAPI.handlers.add(handler);
    }

    public static void sendPacket(Packet packet) {
        PortalAPI.getClient().sendPacket(packet);
    }

    public static Packet receivePacket() {
        return PortalAPI.getClient().receivePacket();
    }

    private static ClientInterface getClient() {
        if(PortalAPI.client == null) {
            throw new IllegalStateException("Client is not registered");
        }

        return PortalAPI.client;
    }
}
