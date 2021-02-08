package vixikhd.portal.nukkit.command;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginIdentifiableCommand;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.plugin.Plugin;

public class TransferCommand extends Command implements PluginIdentifiableCommand {

    public TransferCommand() {
        super("transfer", "Transfers player to another server");

        this.setAliases(new String[] {"server"});
        this.setPermission("portal.command.transfer");

        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{CommandParameter.newType("server", CommandParamType.STRING)});
        this.commandParameters.put("withPlayer", new CommandParameter[]{CommandParameter.newType("server", CommandParamType.STRING), CommandParameter.newType("player", CommandParamType.TARGET)});
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if(!this.testPermission(sender)) {
            return false;
        }
        if(args.length < 1) {
            sender.sendMessage("§cUsage: §7/transfer <server> [player]");
            return false;
        }


        return false;
    }

    @Override
    public Plugin getPlugin() {
        return null;
    }
}
