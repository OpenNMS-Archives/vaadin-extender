package org.pax.vaadin.samples.simple.app;

public class Item {
    private final String name;

    public Item(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ":" + name;
    }
}
