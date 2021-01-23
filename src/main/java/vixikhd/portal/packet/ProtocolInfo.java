package vixikhd.portal.packet;

public interface ProtocolInfo {
    byte AUTH_REQUEST_PACKET = (byte)0xd0;
    byte AUTH_RESPONSE_PACKET = (byte)0xd1;
    byte TRANSFER_REQUEST_PACKET = (byte)0xd2;
    byte TRANSFER_RESPONSE_PACKET = (byte)0xd3;
    byte PLAYER_INFO_REQUEST_PACKET = (byte)0xd4;
    byte PLAYER_INFO_RESPONSE_PACKET = (byte)0xd5;
}
