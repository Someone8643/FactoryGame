package com.placeholder.factory_game.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.math.MathUtils;
import com.placeholder.factory_game.asset.SoundAsset;

public class Interact implements Component {
    public static final ComponentMapper<Interact> MAPPER = ComponentMapper.getFor(Interact.class);

//    private float damage;
    private float interactDelay;
    private float interactTimer;
    private SoundAsset sfx;

    public Interact(float interactDelay, SoundAsset sfx) {
        this.interactDelay = interactDelay;
        this.sfx = sfx;
        this.interactTimer = 0f;
    }

    public boolean canInteract() {
        return this.interactTimer == 0f;
    }

    public boolean isInteracting() {
        return this.interactTimer > 0f;
    }

    public boolean hasInteractStarted() {
        return MathUtils.isEqual(this.interactTimer, this.interactDelay, 0.0001f);
    }

    public void startInteract() {
        this.interactTimer = this.interactDelay;
    }

    public void decInteractTimer(float deltaTime) {
        interactTimer = Math.max(0f, interactTimer - deltaTime);
    }

    public SoundAsset getSfx() {
        return sfx;
    }
}
