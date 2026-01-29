package com.placeholder.factory_game.system;
import com.badlogic.gdx.utils.Array;

public class InventorySystem {
}




    private final Array<ItemStack> slots;

    public Inventory(int size) {
        slots = new Array<>(size);
        for (int i = 0; i < size; i++) {
            slots.add(new ItemStack(null, 0));
        }
    }

    public boolean addItem(Item item, int amount) {

        // 1️⃣ Intentar apilar
        if (item.stackable) {
            for (ItemStack stack : slots) {
                if (!stack.isEmpty() && stack.item == item && !stack.isFull()) {
                    stack.add(amount);
                    return true;
                }
            }
        }

        // 2️⃣ Buscar hueco libre
        for (ItemStack stack : slots) {
            if (stack.isEmpty()) {
                stack.item = item;
                stack.amount = Math.min(amount, item.maxStack);
                return true;
            }
        }

        return false; // inventario lleno
    }

    public void removeItem(Item item, int amount) {
        for (ItemStack stack : slots) {
            if (!stack.isEmpty() && stack.item == item) {
                stack.remove(amount);
                return;
            }
        }
    }

    public Array<ItemStack> getSlots() {
        return slots;
    }
}
