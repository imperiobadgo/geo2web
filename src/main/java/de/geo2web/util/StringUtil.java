package de.geo2web.util;

public class StringUtil {

    public static boolean contains(final String s, final char c){
        for (int i = 0, sLength = s.length(); i <sLength; i++) {
            if (s.charAt(i) == c){
                return true;
            }
        }
        return false;
    }
}
