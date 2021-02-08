package vixikhd.portal.nukkit;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.PluginLogger;
import lombok.Getter;
import lombok.SneakyThrows;
import vixikhd.portal.PortalAPI;
import vixikhd.portal.PortalClient;
import vixikhd.portal.network.PacketPool;
import vixikhd.portal.utils.Logger;
import vixikhd.portal.utils.Utils;

public class Portal extends PluginBase {

    @Getter
    private static Portal instance;

    public void onLoad() {
        Portal.instance = this;
    }

    @SneakyThrows
    @Override
    public void onEnable() {
        Logger.setInstance(new NukkitLogger(this.getLogger()));
        PacketPool.init();

        this.saveDefaultConfig();

        String host = this.getConfig().getString("proxy-address", "127.0.0.1");
        int port = this.getConfig().getInt("socket.port");
        String secret = this.getConfig().getString("socket.secret");
        String group = this.getConfig().getString("server.group");
        String name = this.getConfig().getString("server.name");

        PortalAPI.setClient(new PortalClient(host, port, secret, name, group, Utils.getBackwardsAddress(host) + ":" + this.getServer().getPort()));
        this.getServer().getScheduler().scheduleRepeatingTask(new PortalTickTask(this), 1);
    }

    public static class NukkitLogger extends Logger {
        private final PluginLogger logger;

        public NukkitLogger(PluginLogger logger) {
            this.logger = logger;
        }

        @Override
        public void info(String message) {
            this.logger.info(message);
        }

        @Override
        public void error(String message) {
            this.logger.error(message);
        }
    }
}
