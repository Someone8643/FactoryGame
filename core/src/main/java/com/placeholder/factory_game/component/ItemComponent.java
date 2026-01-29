package com.placeholder.factory_game.component;

import com.badlogic.ashley.core.Component;
import com.placeholder.factory_game.component.Item;

public class ItemComponent implements Component {
    public Item item;
    public int amount;

    public ItemComponent(Item item, int amount) {
        this.item = item;
        this.amount = amount;
    }
}
