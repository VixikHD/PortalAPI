package vixikhd.portal.network;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface PacketDirection {
    int DIRECTION_UNKNOWN = -1;
    int DIRECTION_CLIENT_PROXY = 0;
    int DIRECTION_PROXY_CLIENT = 1;
    int DIRECTION_BOTH = 2;

    int value();
}
