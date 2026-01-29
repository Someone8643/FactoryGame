package com.placeholder.factory_game.component;

public class ItemStack {
    public Item item = null;
    public int amount = 0;

    public boolean isEmpty() {
        return item == null || amount <= 0;
    }
}
