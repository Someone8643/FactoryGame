package com.placeholder.factory_game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.placeholder.factory_game.GdxGame;
import com.placeholder.factory_game.asset.AssetService;
import com.placeholder.factory_game.asset.AtlasAsset;
import com.placeholder.factory_game.asset.SkinAsset;
import com.placeholder.factory_game.asset.SoundAsset;

public class LoadingScreen extends ScreenAdapter {

    private final GdxGame game;
    private final AssetService assetService;

    public LoadingScreen(GdxGame game) {
        this.game = game;
        this.assetService = game.getAssetService();
    }

    /**
     * Queues all required assets for loading.
     */
    @Override
    public void show() {
        for (AtlasAsset atlasAsset : AtlasAsset.values()) {
            assetService.queue(atlasAsset);
        }
        assetService.queue(SkinAsset.DEFAULT);
        for (SoundAsset soundAsset : SoundAsset.values()) {
            assetService.queue(soundAsset);
        }
    }

    /**
     * Updates asset loading progress and transitions to menu when complete.
     */
    @Override
    public void render(float delta) {
        if (assetService.update()) {
            Gdx.app.debug("LoadingScreen", "Finished loading assets");
            createScreens();
            this.game.removeScreen(this);
            this.dispose();
            this.game.setScreen(MenuScreen.class);
        }
    }

    private void createScreens() {
        this.game.addScreen(new GameScreen(this.game));
        this.game.addScreen(new MenuScreen(this.game));
    }
}
