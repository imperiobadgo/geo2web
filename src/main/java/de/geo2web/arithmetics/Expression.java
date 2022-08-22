package de.geo2web.arithmetics;

public interface Expression {

    Expression evaluate();

    Expression deepClone();

}
