package de.geo2web;

import de.geo2web.pack.Another;

public class Application {

    public static void main(String[] args)
    {
        Another a = Another.builder().name("Hallo").build();
        TestClass t = TestClass.builder().name("Change").build();

        Another b = t.apply(a.toBuilder());
    }
}
