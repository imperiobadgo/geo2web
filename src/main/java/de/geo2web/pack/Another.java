package de.geo2web.pack;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import javax.validation.constraints.Size;
import java.util.UUID;

@Value
@Builder(toBuilder = true)
public class Another {

    @NonNull
    @Builder.Default
    UUID id = UUID.randomUUID();

    @NonNull
    @Size(min = 1, max = 20)
    String name;
}
