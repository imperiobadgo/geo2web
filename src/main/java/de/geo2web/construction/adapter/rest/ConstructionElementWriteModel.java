package de.geo2web.construction.adapter.rest;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.UUID;

@Data
@NoArgsConstructor
public class ConstructionElementWriteModel {

    @NonNull
    UUID id;

    String input;

}
