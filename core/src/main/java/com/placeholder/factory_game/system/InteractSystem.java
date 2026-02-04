package com.placeholder.factory_game.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape.Type;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.placeholder.factory_game.audio.AudioService;
import com.placeholder.factory_game.component.*;
import com.placeholder.factory_game.component.Facing.FacingDirection;

import java.util.Objects;

public class InteractSystem extends IteratingSystem {
    public static final Rectangle interactAABB = new Rectangle();

    private final AudioService audioService;
    private final World world;
    private final Vector2 tmpVertex;
    private Body interacterBody;
//    private float attackDamage;

    public InteractSystem(World world, AudioService audioService) {
        super(Family.all(Attack.class, Facing.class, Physic.class).get());
        this.audioService = audioService;
        this.world = world;
        this.tmpVertex = new Vector2();
        this.interacterBody = null;
//        this.attackDamage = 0f;
    }

    /**
     * Processes attack logic including sound effects and damage detection.
     */
    @Override
    protected void processEntity(Entity entity, float deltaTime) {


        try {

//        Gdx.app.debug("TEST", "testsfasfassdsd");
            Interact interact = Interact.MAPPER.get(entity);
            if (interact == null) return;
            // can attack = true means that the attack was not started yet
            if (interact.canInteract()) return;



            if (interact.hasInteractStarted() && interact.getSfx() != null) {
                audioService.playSound(interact.getSfx());
                Move move = Move.MAPPER.get(entity);
                if (move != null) {
                    move.setRooted(true);
                }
            }

            interact.decInteractTimer(deltaTime);
            if (interact.canInteract()) {
                FacingDirection facingDirection = Facing.MAPPER.get(entity).getDirection();
                interacterBody = Physic.MAPPER.get(entity).getBody();
                PolygonShape attackPolygonShape = getAttackFixture(interacterBody, facingDirection);
                updateInteractAABB(interacterBody.getPosition(), attackPolygonShape);

//                Gdx.app.debug("TEST", "entra en interact");

                world.QueryAABB(this::interactCallback, interactAABB.x, interactAABB.y, interactAABB.width, interactAABB.height);

                Move move = Move.MAPPER.get(entity);
                if (move != null) {
                    move.setRooted(false);
                }
            }
        } catch (Exception ex) {

            Gdx.app.debug("Exception in interact", "Ex: " + ex );
        }
    }

    private boolean interactCallback(Fixture fixture) {
        Body body = fixture.getBody();
        if (body.equals(interacterBody)) return true;
        if (!(body.getUserData() instanceof Entity entity)) return true;

//        Gdx.app.debug("Interact", "INTERACT CALLBACK AAAAA");

        Tiled tile = Tiled.MAPPER.get(entity);
        if (tile == null) {
            return true;
        }

        if (Objects.equals(tile.getMapObjectRef().getName(), "Ore")) { // Mine the ore by 1

            MapProperties tileProperties = tile.getMapObjectRef().getProperties();
            Gdx.app.debug("Minar amb interact", "Minar ore, ara té: " + tileProperties.get("oreAmount"));

            if ((int)tileProperties.get("oreAmount") > 0) {

                tileProperties.put("oreAmount", (int)tileProperties.get("oreAmount") - 1);
                Gdx.app.debug("Minar amb interact", "Queda: " + tile.getMapObjectRef().getProperties().get("oreAmount"));
            }
        }



        return true;
    }

    private void updateInteractAABB(Vector2 bodyPosition, PolygonShape attackPolygonShape) {
        attackPolygonShape.getVertex(0, tmpVertex);
        tmpVertex.add(bodyPosition);
        interactAABB.setPosition(tmpVertex.x, tmpVertex.y);

        attackPolygonShape.getVertex(2, tmpVertex);
        tmpVertex.add(bodyPosition);
        interactAABB.setSize(tmpVertex.x, tmpVertex.y);
    }

    private PolygonShape getAttackFixture(Body body, FacingDirection direction) {
        Array<Fixture> fixtureList = body.getFixtureList();
        String fixtureName = "attack_sensor_" + direction.getAtlasKey();
        for (Fixture fixture : fixtureList) {
            if (fixtureName.equals(fixture.getUserData()) && Type.Polygon.equals(fixture.getShape().getType())) {
                return (PolygonShape) fixture.getShape();
            }
        }

        throw new GdxRuntimeException("Entity has no polygon attack sensor with userData '" + fixtureName + "'");
    }
}
