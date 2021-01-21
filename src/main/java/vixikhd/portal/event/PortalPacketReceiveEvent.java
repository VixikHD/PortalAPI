package vixikhd.portal.event;

import cn.nukkit.event.Event;
import cn.nukkit.event.HandlerList;
import lombok.Getter;
import vixikhd.portal.packet.Packet;

public class PortalPacketReceiveEvent extends Event {

    @Getter
    private static final HandlerList handlers = new HandlerList();

    @Getter
    private final Packet packet;

    public PortalPacketReceiveEvent(Packet packet) {
        this.packet = packet;
    }
}
