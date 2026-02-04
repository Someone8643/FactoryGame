package com.placeholder.factory_game.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

/** Componente de inventario para entidades */
public class Inventory implements Component {

    // ComponentMapper para acceder rápidamente desde el sistema
    public static final ComponentMapper<Inventory> MAPPER = ComponentMapper.getFor(Inventory.class);

    private Array<ItemStack> slots = new Array<>();

    public Inventory(int size) {
        for (int i = 0; i < size; i++) {
            slots.add(new ItemStack());
        }
    }

    public Array<ItemStack> getSlots() {
        return slots;
    }

    public boolean addItem(Item item, int amount) {
        for (ItemStack stack : slots) {
            if (stack.isEmpty()) {
                stack.item = item;
                stack.amount = amount;
                return true;
            } else if (stack.item.equals(item)) {
                stack.amount += amount;
                return true;
            }
        }
        return false; // Inventario lleno
    }

    public void removeItem(Item item, int amount) {
        for (ItemStack stack : slots) {
            if (stack.item != null && stack.item.equals(item)) {
                stack.amount -= amount;
                if (stack.amount <= 0) {
                    stack.amount = 0;
                    stack.item = null;
                }
                return;
            }
        }
    }
}
