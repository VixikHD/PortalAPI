package vixikhd.portal.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Utils {

    public static String getBackwardsAddress(String targetAddress) throws UnknownHostException {
        if(targetAddress.equals("127.0.0.1")) {
            return "127.0.0.1"; // local
        }

        return InetAddress.getLocalHost().getHostAddress();
    }
}
