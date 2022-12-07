package de.geo2web.interaction.construction.adapter.rest;

import lombok.Value;

import java.util.UUID;

@Value
public class ConstructionElementReadModel {

    UUID id;

    int constructionIndex;

    String name;

    String input;

    String output;

    float[] transform;
}
