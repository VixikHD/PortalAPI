package vixikhd.portal.utils;

import lombok.Getter;
import lombok.Setter;

public class Logger {

    @Getter @Setter
    private static Logger instance = new Logger();

    public void info(String message) {
        System.out.println("[Portal] [Info] " + message);
    }

    public void error(String message) {
        System.out.println("[Portal] [Error] " + message);
    }
}
