package de.geo2web.construction.adapter.rest;

import lombok.Value;

import java.util.UUID;

@Value
public class ConstructionElementReadModel {

    UUID id;

    int constructionIndex;

    String input;

}