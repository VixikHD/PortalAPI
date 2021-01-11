package vixikhd.portal;

import cn.nukkit.event.Listener;
import cn.nukkit.plugin.PluginBase;
import lombok.Getter;
import lombok.SneakyThrows;
import vixikhd.portal.packet.AuthRequestPacket;
import vixikhd.portal.packet.AuthResponsePacket;
import vixikhd.portal.packet.Packet;
import vixikhd.portal.packet.ProtocolInfo;
import vixikhd.portal.thread.SocketThread;

import java.net.InetAddress;
import java.util.Arrays;

public class Portal extends PluginBase implements Listener {

    @Getter
    private static Portal instance;
    @Getter
    private SocketThread thread;

    public void onLoad() {
        Portal.instance = this;
    }

    @SneakyThrows
    public void onEnable() {
        this.saveResource("/config.yml");

        String host = this.getConfig().getString("proxy-address");
        int port = this.getConfig().getInt("socket.port");

        String secret = this.getConfig().getString("socket.secret");

        String group = this.getConfig().getString("server.group");
        String name = this.getConfig().getString("server.name");

        String address = (host.equals("127.0.0.1") ? "127.0.0.1" : InetAddress.getLocalHost().getHostAddress()) + ":" + this.getServer().getPort();

        this.thread = new SocketThread(host, port, secret, name, group, address);

        this.getServer().getNetwork().registerPacket(AuthRequestPacket.NETWORK_ID, AuthRequestPacket.class);
        this.getServer().getNetwork().registerPacket(AuthResponsePacket.NETWORK_ID, AuthResponsePacket.class);

        this.getServer().getScheduler().scheduleRepeatingTask(new RefreshPortalThreadTask(this), 1);
    }

    public void tick() {
        byte[] buffer;
        Packet packet;
        while ((buffer = this.getThread().getBuffer()) != null) {
            packet = ProtocolInfo.getPacket(buffer);
            assert packet != null;
            packet.handlePacket();
        }
    }

    public void onDisable() {
        this.thread.interrupt(); // TODO - check if works
    }
}
