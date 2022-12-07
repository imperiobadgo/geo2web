package de.geo2web.interaction.construction.adapter.rest;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ConstructionElementCreateModel {

    String name;

    String input;

    float[] transform;
}
