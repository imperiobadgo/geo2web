package de.geo2web.arithmetic;

public interface Expression {

    Expression evaluate();

    Expression deepClone();

}
