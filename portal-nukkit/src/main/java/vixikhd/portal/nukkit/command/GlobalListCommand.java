package vixikhd.portal.nukkit.command;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginIdentifiableCommand;
import cn.nukkit.plugin.Plugin;
import vixikhd.portal.PortalAPI;
import vixikhd.portal.network.packets.ServerListRequestPacket;
import vixikhd.portal.network.packets.ServerListResponsePacket;
import vixikhd.portal.nukkit.Portal;

import java.util.Arrays;

public class GlobalListCommand extends Command implements PluginIdentifiableCommand {

    public GlobalListCommand() {
        super("globallist", "Displays list of all the players connected to the proxy");

        this.setAliases(new String[]{"glist"});
        this.setPermission("portal.command.globallist");
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if(!this.testPermission(sender)) {
            return false;
        }

        PortalAPI.sendPacket(ServerListRequestPacket.create(), response -> {
            if(!(response instanceof ServerListResponsePacket)) {
                return;
            }

            int totalOnline = 0;
            for(ServerListResponsePacket.ServerEntry entry : ((ServerListResponsePacket) response).servers) {
                totalOnline += entry.playerCount;
            }

            sender.sendMessage("§aThere are " + totalOnline + " players online on " + (int) Arrays.stream(((ServerListResponsePacket) response).servers).filter(serverEntry -> serverEntry.isOnline).count() + " servers:");
            for(ServerListResponsePacket.ServerEntry entry : ((ServerListResponsePacket) response).servers) {
                if(entry.isOnline) {
                    sender.sendMessage("§7" + entry.name + ": " + entry.playerCount + " Online players");
                }
            }
        });
        return false;
    }

    @Override
    public Plugin getPlugin() {
        return Portal.getInstance();
    }
}
