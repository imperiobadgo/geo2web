package de.geo2web.util.logging;

public class Logger {

    public static void log(Level level, Class callerClass, String message){
        StackTraceElement[] stackTraceElement = Thread.currentThread().getStackTrace();
        String functionName = "";
        if (stackTraceElement.length > 1){
            functionName = stackTraceElement[1].getMethodName();
        }
        System.out.println(message);
    }
}
