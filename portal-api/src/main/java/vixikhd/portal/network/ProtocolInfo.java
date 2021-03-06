package vixikhd.portal.network;

public interface ProtocolInfo {
    byte AUTH_REQUEST_PACKET = (byte)0x00;
    byte AUTH_RESPONSE_PACKET = (byte)0x01;
    byte TRANSFER_REQUEST_PACKET = (byte)0x02;
    byte TRANSFER_RESPONSE_PACKET = (byte)0x03;
    byte PLAYER_INFO_REQUEST_PACKET = (byte)0x04;
    byte PLAYER_INFO_RESPONSE_PACKET = (byte)0x05;
    byte SERVER_LIST_REQUEST_PACKET = (byte)0x06;
    byte SERVER_LIST_RESPONSE_PACKET = (byte)0x07;
}
