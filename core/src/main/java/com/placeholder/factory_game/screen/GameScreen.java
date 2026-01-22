package com.placeholder.factory_game.screen;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.BaseTiledMapLoader;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.placeholder.factory_game.FactoryGame;
import com.placeholder.factory_game.Main;
import com.placeholder.factory_game.asset.AssetService;
import com.placeholder.factory_game.asset.MapAsset;

import java.util.function.Consumer;

public class GameScreen extends ScreenAdapter {
    private final FactoryGame game;
    private final AssetService assetService;
    private final Viewport viewport;
    private final OrthographicCamera camera;
    private final Batch batch;

    private final OrthogonalTiledMapRenderer mapRenderer;

    public GameScreen(FactoryGame game) {

        this.game = game;
        this.assetService = game.getAssetService();
        this.viewport = game.getViewport();
        this.camera = game.getCamera();
        this.batch = game.getBatch();
        this.mapRenderer = new OrthogonalTiledMapRenderer(null, FactoryGame.UNIT_SCALE, this.batch);
    }

    @Override
    public void show() {
        this.assetService.load(MapAsset.MAIN);
        this.mapRenderer.setMap(this.assetService.get(MapAsset.MAIN));
    }

    @Override
    public void render(float delta) {
        this.viewport.apply();
        this.batch.setColor(Color.WHITE);
        this.mapRenderer.setView(this.camera);
        this.mapRenderer.render();

    }

    @Override
    public void dispose() {
        this.mapRenderer.dispose();
    }
}
