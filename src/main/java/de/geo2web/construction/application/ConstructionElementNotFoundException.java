package de.geo2web.construction.application;

import de.geo2web.util.logging.Level;
import de.geo2web.util.logging.Logger;

public class ConstructionElementNotFoundException extends RuntimeException {
    public ConstructionElementNotFoundException(String message) {
        super(message);
        Logger.log(Level.Error, ConstructionElementNotFoundException.class, message);
    }
}
