package vixikhd.portal;

import cn.nukkit.event.Listener;
import cn.nukkit.plugin.PluginBase;
import lombok.Getter;
import lombok.SneakyThrows;
import vixikhd.portal.event.PortalPacketReceiveEvent;
import vixikhd.portal.packet.*;
import vixikhd.portal.thread.SocketThread;

import java.io.File;
import java.net.InetAddress;

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
        if(!((new File(this.getDataFolder() + "/config.yml")).exists())) {
            this.saveConfig();
        }

        PacketPool.init();

        String host = this.getConfig().getString("proxy-address");
        int port = this.getConfig().getInt("socket.port");

        String secret = this.getConfig().getString("socket.secret");

        String group = this.getConfig().getString("server.group");
        String name = this.getConfig().getString("server.name");

        String address = getBackwardsAddress(host);

        this.getLogger().debug("Starting socket with backward-address=" + address + "; proxy-address=" + host + ":" + port);
        this.thread = new SocketThread(host, port, secret, name, group, address);

        this.getServer().getScheduler().scheduleRepeatingTask(new RefreshPortalThreadTask(this), 1);
    }

    @SneakyThrows
    private String getBackwardsAddress(String targetAddress) {
        if(targetAddress.equals("127.0.0.1")) {
            return "127.0.0.1:" + this.getServer().getPort(); // local
        }

        return InetAddress.getLocalHost().getHostAddress() + ":" + this.getServer().getPort();
    }

    public void tick() {
        byte[] buffer;
        Packet packet;
        while ((buffer = this.getThread().getBuffer()) != null) {
            packet = PacketPool.getPacket(buffer);
            if(packet == null) {
                continue;
            }

            if(!packet.handlePacket()) {
                this.getLogger().error("Unexpectedly received " + packet.getClass().getName());
                continue;
            }

            this.getServer().getPluginManager().callEvent(new PortalPacketReceiveEvent(packet));
        }
    }

    public void onDisable() {
        this.thread.interrupt(); // TODO - check if works
    }
}
