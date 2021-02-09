package vixikhd.portal.gomint.config;

import io.gomint.config.YamlConfig;
import io.gomint.config.annotation.Comment;

public class PortalConfig extends YamlConfig {

    @Comment("IP and PORT of portal's TCP socket")
    public SocketConfig socket = new SocketConfig();
    @Comment("Name & Group of current server")
    public ServerConfig server = new ServerConfig();
    @Comment("Secret must be equal on all the servers")
    public String secret = "";

    public String getHost() {
        return this.socket.address;
    }

    public int getPort() {
        return this.socket.port;
    }

    public String getServerName() {
        return this.server.name;
    }

    public String getServerGroup() {
        return this.server.group;
    }

    public String getSecret() {
        return this.secret;
    }
}
