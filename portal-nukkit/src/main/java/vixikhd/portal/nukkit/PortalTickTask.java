package vixikhd.portal.nukkit;

import cn.nukkit.Player;
import cn.nukkit.scheduler.Task;
import vixikhd.portal.ClientPacketHandler;
import vixikhd.portal.PortalAPI;
import vixikhd.portal.network.Packet;
import vixikhd.portal.network.packets.AuthResponsePacket;
import vixikhd.portal.network.packets.TransferResponsePacket;

import java.util.Optional;

public class PortalTickTask extends Task {

    public Portal plugin;

    public PortalTickTask(Portal plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onRun(int currentTick) {
        Packet packet = PortalAPI.receivePacket();
        if(packet == null) {
            return;
        }

        if(ClientPacketHandler.handlePacket(packet)) {
            return; // Error message already sent to console
        }

        if(packet instanceof AuthResponsePacket) {
            Portal.getInstance().getLogger().info("Successfully authenticated with portal");
            return;
        }

        if(packet instanceof TransferResponsePacket) {
            String error = TransferResponsePacket.translateStatus(((TransferResponsePacket) packet).status, ((TransferResponsePacket) packet).reason);
            if(error == null) {
                return;
            }

            Optional<Player> player = Portal.getInstance().getServer().getPlayer(((TransferResponsePacket) packet).uuid);

            String name = ((TransferResponsePacket) packet).uuid.toString();
            if(player.isPresent()) {
                name = player.get().getName();
            }

            Portal.getInstance().getLogger().error("Error whilst transferring player " + name + ": " + error);
        }
    }
}
