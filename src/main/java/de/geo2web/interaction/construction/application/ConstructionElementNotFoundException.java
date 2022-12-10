package de.geo2web.interaction.construction.application;

import de.geo2web.interaction.construction.ConstructionElementId;
import de.geo2web.util.logging.Level;
import de.geo2web.util.logging.Logger;

public class ConstructionElementNotFoundException extends RuntimeException {
    public ConstructionElementNotFoundException(ConstructionElementId id) {
        super(id.toString());
        Logger.log(Level.Error, ConstructionElementNotFoundException.class, id.toString());
    }
}
