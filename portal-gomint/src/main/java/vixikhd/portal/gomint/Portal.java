package vixikhd.portal.gomint;

import io.gomint.plugin.*;
import lombok.SneakyThrows;
import vixikhd.portal.network.PacketPool;
import vixikhd.portal.utils.Logger;

@PluginName("Portal")
@Version(major = 1, minor = 0)
@Startup(StartupPriority.STARTUP)
public class Portal extends Plugin {

    @SneakyThrows
    @Override
    public void onStartup() {
        Logger.setInstance(new GoMintLogger(this.logger()));
        PacketPool.init();

//        this.saveDefaultConfig();
//
//        String host = this.getConfig().getString("proxy-address", "127.0.0.1");
//        int port = this.getConfig().getInt("socket.port");
//        String secret = this.getConfig().getString("socket.secret");
//        String group = this.getConfig().getString("server.group");
//        String name = this.getConfig().getString("server.name");
//
//        PortalAPI.setClient(new PortalClient(host, port, secret, name, group, Utils.getBackwardsAddress(host) + ":" + this.server().port()));
    }

    public static class GoMintLogger extends Logger {
        private final org.slf4j.Logger logger;

        public GoMintLogger(org.slf4j.Logger logger) {
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
