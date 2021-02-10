package vixikhd.portal.nukkit.command;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginIdentifiableCommand;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.plugin.Plugin;
import vixikhd.portal.PortalAPI;
import vixikhd.portal.network.packets.TransferRequestPacket;

public class TransferCommand extends Command implements PluginIdentifiableCommand {

    public TransferCommand() {
        super("transfer", "Transfers player to another server");

        this.setAliases(new String[] {"server"});
        this.setPermission("portal.command.transfer");

        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{CommandParameter.newType("group", CommandParamType.STRING), CommandParameter.newType("server", CommandParamType.STRING)});
        this.commandParameters.put("withPlayer", new CommandParameter[]{CommandParameter.newType("group", CommandParamType.STRING), CommandParameter.newType("server", CommandParamType.STRING), CommandParameter.newType("player", CommandParamType.TARGET)});
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if(!this.testPermission(sender)) {
            return false;
        }

        if((args.length < 2) || (args.length < 3 && !(sender instanceof Player))) {
            if(sender instanceof Player) {
                sender.sendMessage("§cUsage: §7/transfer <group> <server> [player]");
                return false;
            }

            sender.sendMessage("§cUsage: §7/transfer <group> <server> <player>");
            return false;
        }

        Player player;
        if(args.length < 3) {
            player = (Player) sender;
        } else {
            player = Server.getInstance().getPlayer(args[2]);
        }

        if(player == null) {
            sender.sendMessage("§7[Portal] §cPlayer was not found");
            return false;
        }

        PortalAPI.sendPacket(TransferRequestPacket.create(player.getUniqueId(), args[0], args[1]));
        sender.sendMessage("§7[Portal] §aTransfer request sent to the proxy server!");
        return false;
    }

    @Override
    public Plugin getPlugin() {
        return null;
    }
}
