package com.placeholder.factory_game.component;

public class Item {
    public final String name;
    public final boolean stackable;
    public final int maxStack;

    public Item(String name, boolean stackable, int maxStack) {
        this.name = name;
        this.stackable = stackable;
        this.maxStack = maxStack;
    }

    @Override
    public String toString() {
        return name;
    }
}
