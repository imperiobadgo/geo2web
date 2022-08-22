package de.geo2web;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@Value
@Builder(toBuilder = true)
public class Example {

    @NonNull
    @Builder.Default
    UUID id = UUID.randomUUID();

    @NonNull
    String name;
}
