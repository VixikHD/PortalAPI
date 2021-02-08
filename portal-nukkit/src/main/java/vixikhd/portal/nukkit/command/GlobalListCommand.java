package vixikhd.portal.nukkit.command;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginIdentifiableCommand;
import cn.nukkit.plugin.Plugin;
import vixikhd.portal.nukkit.Portal;

public class GlobalListCommand extends Command implements PluginIdentifiableCommand {

    public GlobalListCommand() {
        super("globallist", "Displays list of all the players connected to the proxy");

        this.setAliases(new String[]{"glist"});
        this.setPermission("portal.command.glist");
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        return false;
    }

    @Override
    public Plugin getPlugin() {
        return Portal.getInstance();
    }
}
