package vixikhd.portal;

import vixikhd.portal.network.Packet;
import vixikhd.portal.network.PacketDirection;
import vixikhd.portal.network.packets.AuthResponsePacket;
import vixikhd.portal.network.packets.PlayerInfoResponsePacket;
import vixikhd.portal.network.packets.TransferResponsePacket;
import vixikhd.portal.utils.Logger;

import java.util.Optional;
import java.util.function.Consumer;

public class ClientPacketHandler {

    /**
     * Handles packet (only in Proxy->Client direction), checks for errors and calls response handlers
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

        for(Consumer<Packet> handler : PortalAPI.getHandlers()) {
            handler.accept(packet);
        }

        Optional<Packet> request = PortalAPI.getResponseQueue().keySet().stream().filter(pk -> pk.getResponseId() == packet.getPacketId()).findFirst();
        if(request.isPresent()) {
            PortalAPI.getResponseQueue().remove(request.get()).accept(packet);
            return true;
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
}
