package com.placeholder.factory_game.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.placeholder.factory_game.component.Inventory;
import com.placeholder.factory_game.component.Item;
import com.placeholder.factory_game.component.ItemStack;

public class InventorySystem extends IteratingSystem {

    public InventorySystem() {
        super(Family.all(Inventory.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Inventory inventory = Inventory.MAPPER.get(entity);

        if (inventory == null) return;

        // Limpiar stacks vacíos automáticamente
        for (ItemStack stack : inventory.getSlots()) {
            if (stack.isEmpty()) {
                stack.item = null;
                stack.amount = 0;
            }
        }

        // Aquí se podría añadir lógica para usar items automáticamente cada frame
    }

    public boolean addItem(Entity entity, Item item, int amount) {
        Inventory inventory = Inventory.MAPPER.get(entity);
        return inventory != null && inventory.addItem(item, amount);
    }

    public void removeItem(Entity entity, Item item, int amount) {
        Inventory inventory = Inventory.MAPPER.get(entity);
        if (inventory != null && inventory.getSfx() != null) {
            inventory.removeItem(item, amount);
        }
    }

    public Array<ItemStack> getSlots(Entity entity) {
        Inventory inventory = Inventory.MAPPER.get(entity);
        if (inventory != null) {
            return inventory.getSlots();
        }
        return new Array<>();
    }
}
