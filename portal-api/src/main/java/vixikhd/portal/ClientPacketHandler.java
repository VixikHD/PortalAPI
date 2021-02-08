package vixikhd.portal;

import vixikhd.portal.network.Packet;
import vixikhd.portal.network.PacketDirection;
import vixikhd.portal.network.packets.*;
import vixikhd.portal.utils.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ClientPacketHandler {

    private final static List<Consumer<Packet>> handlers = new ArrayList<>();

    /**
     * Handles packet (only in Proxy->Client direction), mostly just checks for errors
     *
     * @param packet packet
     * @return Returns if the packet was handled
     */
    public static boolean handlePacket(Packet packet) {
        int direction = packet.getClass().getAnnotation(PacketDirection.class).value();
        if(direction != PacketDirection.DIRECTION_PROXY_CLIENT && direction != PacketDirection.DIRECTION_BOTH) {
            Logger.getInstance().error("Received packet " + packet.getClass().getName() + " from wrong direction (" + direction + ")");
            return true;
        }

        for(Consumer<Packet> handler : ClientPacketHandler.handlers) {
            handler.accept(packet);
        }

        if(packet instanceof AuthResponsePacket) {
            return ClientPacketHandler.handleAuth((AuthResponsePacket) packet);
        }
        if(packet instanceof PlayerInfoResponsePacket) {
            return ClientPacketHandler.handlerPlayerInfo((PlayerInfoResponsePacket) packet);
        }
        if(packet instanceof TransferResponsePacket) {
            return ClientPacketHandler.handleTransfer((TransferResponsePacket) packet);
        }

        return false;
    }

    private static boolean handleAuth(AuthResponsePacket packet) {
        String status = AuthResponsePacket.translateStatus(packet.status);
        if(status == null) {
            return false;
        }

        Logger.getInstance().error("Error whilst connecting to the proxy: " + status);
        return true;
    }

    private static boolean handlerPlayerInfo(PlayerInfoResponsePacket packet) {
        return false; // Should be handled by plugins
    }

    private static boolean handleTransfer(TransferResponsePacket packet) {
        return false; // Should be handled by plugins
    }

    /**
     * Registers handler, consumer will be called when receiving packet from proxy.
     */
    public static void registerHandler(Consumer<Packet> handler) {
        ClientPacketHandler.handlers.add(handler);
    }
}
