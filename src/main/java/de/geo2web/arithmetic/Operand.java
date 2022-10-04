package de.geo2web.arithmetic;

public interface Operand {

    Operand deepClone();

    String toReadableString();
}
