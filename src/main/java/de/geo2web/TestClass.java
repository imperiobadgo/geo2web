package de.geo2web;

import de.geo2web.pack.Another;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class TestClass {

    @NonNull
    String name;


    public Another apply(final Another.AnotherBuilder builder) {
        return builder
                .name(name)
                .build();
    }

}
