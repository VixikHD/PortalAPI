package vixikhd.portal;

import vixikhd.portal.network.Packet;

public interface ClientInterface {

    /**
     * Function which sends packet to the server
     * @param packet packet
     */
    void sendPacket(Packet packet);

    /**
     * Function which receives packet from the server
     * @return packet packet
     */
    Packet receivePacket();

    /**
     * Stops client process
     */
    void stop();
}
