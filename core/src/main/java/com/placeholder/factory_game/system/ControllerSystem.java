package com.placeholder.factory_game.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.placeholder.factory_game.GdxGame;
import com.placeholder.factory_game.component.*;
import com.placeholder.factory_game.input.Command;
import com.placeholder.factory_game.screen.InventoryUIScreen;
import com.placeholder.factory_game.screen.MenuScreen;

public class ControllerSystem extends IteratingSystem {
    private final GdxGame game;

    public ControllerSystem(GdxGame game) {
        super(Family.all(Controller.class).get());
        this.game = game;
    }

    /**
     * Processes input commands for the entity, handling movement and actions.
     */
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Controller controller = Controller.MAPPER.get(entity);
        if (controller.getPressedCommands().isEmpty() && controller.getReleasedCommands().isEmpty()) {
            return;
        }

        for (Command command : controller.getPressedCommands()) {
            switch (command) {
                case UP -> moveEntity(entity, 0f, 1f);
                case DOWN -> moveEntity(entity, 0f, -1f);
                case LEFT -> moveEntity(entity, -1f, 0f);
                case RIGHT -> moveEntity(entity, 1f, 0f);
                case SELECT -> startEntityAttack(entity);
                case INTERACT -> startEntityInteract(entity);
                case INVENTORY -> startEntityInventory(entity);
                case CANCEL -> game.setScreen(MenuScreen.class);
            }
        }
        controller.getPressedCommands().clear();

        for (Command command : controller.getReleasedCommands()) {
            switch (command) {
                case UP -> moveEntity(entity, 0f, -1f);
                case DOWN -> moveEntity(entity, 0f, 1f);
                case LEFT -> moveEntity(entity, 1f, 0f);
                case RIGHT -> moveEntity(entity, -1f, 0f);
            }
        }
        controller.getReleasedCommands().clear();
    }

    private void startEntityAttack(Entity entity) {
        Attack attack = Attack.MAPPER.get(entity);
        if (attack != null && attack.canAttack()) {
            attack.startAttack();
        }
    }

    private void startEntityInteract(Entity entity) {
        Interact interact = Interact.MAPPER.get(entity);
        if (interact != null && interact.canInteract()) {
            interact.startInteract();
        }
    }

    private void startEntityInventory(Entity entity) {
        Inventory inventory = Inventory.MAPPER.get(entity);
        Gdx.app.debug("Inventory","Crida la funció correctament");
        if (inventory != null) {
            this.game.setScreen(InventoryUIScreen.class);
        }
    }

    private void moveEntity(Entity entity, float dx, float dy) {
        Move move = Move.MAPPER.get(entity);
        if (move != null) {
            move.getDirection().x += dx;
            move.getDirection().y += dy;
        }
    }
}
