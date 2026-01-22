package com.placeholder.factory_game.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.placeholder.factory_game.Resources;

public class Player extends Actor {

    private final Sprite sprite;
    private final Body physicalBody;

    private Vector2 currentVelocity = new Vector2(0, 0);
    private PlayerState currentState = PlayerState.STANDING_S;

    /** Velocidad en unidades del mundo por segundo */
    public float speed = 5f * Resources.TILE_SIZE;

    public Player(Vector2 startPosition, Body physicalBody, Sprite sprite) {
        this.sprite = sprite;
        this.physicalBody = physicalBody;

        // Conectar Actor ↔ Box2D
        this.physicalBody.setUserData(this);

        // Posición y tamaño del Actor (Scene2D)
        setBounds(
            startPosition.x,
            startPosition.y,
            sprite.getWidth(),
            sprite.getHeight()
        );

        // Listener de movimiento libre
        addListener(new FreeRoamingMovementListener(this));
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        trackMovement(delta);
    }

    private void trackMovement(float delta) {
        // Sincronizar Actor con Box2D (no escalar velocidad aquí)
        setPosition(
            physicalBody.getPosition().x - Resources.WORLD_TILE_SIZE / 2f,
            physicalBody.getPosition().y - Resources.WORLD_TILE_SIZE / 2f
        );
    }

    public void setStateAndVelocity(PlayerState newState, Vector2 newVelocity) {
        this.currentState = newState;
        this.currentVelocity = newVelocity;

        // Aplicar velocidad al cuerpo físico correctamente
        physicalBody.setLinearVelocity(newVelocity.cpy().scl(speed));
    }

    public PlayerState getCurrentState() {
        return currentState;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        sprite.setPosition(getX(), getY());
        sprite.draw(batch);
    }
}
