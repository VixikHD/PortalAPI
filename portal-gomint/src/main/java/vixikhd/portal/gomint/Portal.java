package vixikhd.portal.gomint;

import io.gomint.config.InvalidConfigurationException;
import io.gomint.plugin.*;
import lombok.Getter;
import lombok.SneakyThrows;
import vixikhd.portal.PortalAPI;
import vixikhd.portal.PortalClient;
import vixikhd.portal.gomint.config.PortalConfig;
import vixikhd.portal.network.PacketPool;
import vixikhd.portal.utils.Logger;
import vixikhd.portal.utils.Utils;

import java.io.File;

@PluginName("Portal")
@Version(major = 1, minor = 0)
@Startup(StartupPriority.LOAD)
public class Portal extends Plugin {

    @Getter
    private PortalConfig config;

    @SneakyThrows
    @Override
    public void onStartup() {
        Logger.setInstance(new GoMintLogger(this.logger()));
        PacketPool.init();

        this.loadConfig();

        String backwardAddress = Utils.getBackwardsAddress(this.getConfig().getHost()) + ":" + this.server().port();
        this.logger().info("Connecting to " + this.getConfig().getHost() + ":" + this.getConfig().getPort() + " [ServerAddress='" + backwardAddress + "']");

        PortalAPI.registerClient(new PortalClient(
                this.getConfig().getHost(),
                this.getConfig().getPort(),
                this.getConfig().getSecret(),
                this.getConfig().getServerName(),
                this.getConfig().getServerGroup(),
                backwardAddress
        ));
    }

    @Override
    public void onUninstall() {
        PortalAPI.unregisterClient();
    }

    private void loadConfig() throws InvalidConfigurationException {
        this.config = new PortalConfig();
        this.config.init(new File(this.dataFolder() + "/config.yml"));
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
