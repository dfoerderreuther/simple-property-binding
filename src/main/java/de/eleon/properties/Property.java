package de.eleon.properties;

public interface Property<BO, T> {

    void set(BO b, T t);

    T get(BO b);

}
