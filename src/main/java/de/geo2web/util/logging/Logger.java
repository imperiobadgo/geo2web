package de.geo2web.util.logging;

public class Logger {

    public static void log(Level level, Class callerClass, String callerFunction, String message){
        System.out.println(message);
    }
}
