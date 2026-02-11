package com.placeholder.factory_game.screen;


import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.placeholder.factory_game.GdxGame;
import com.placeholder.factory_game.asset.MusicAsset;
import com.placeholder.factory_game.asset.SkinAsset;
import com.placeholder.factory_game.input.KeyboardController;
import com.placeholder.factory_game.input.UiControllerState;
import com.placeholder.factory_game.ui.model.MenuViewModel;
import com.placeholder.factory_game.ui.view.MenuView;

public class DeathScreen extends ScreenAdapter {

    private final GdxGame game;
    private final Stage stage;
    private final Skin skin;
    private final Viewport uiViewport;
    private final KeyboardController keyboardController;

    public DeathScreen(GdxGame game) {
        this.game = game;
        this.uiViewport = new FitViewport(800f, 450f);
        this.stage = new Stage(uiViewport, game.getBatch());
        this.skin = game.getAssetService().get(SkinAsset.DEFAULT);
        this.keyboardController = new KeyboardController(UiControllerState.class, null, stage);
    }

    @Override
    public void resize(int width, int height) {
        uiViewport.update(width, height, true);
    }

    @Override
    public void show() {
        this.game.setInputProcessors(stage, keyboardController);

        this.stage.addActor(new MenuView(stage, skin, new MenuViewModel(game)));
        this.game.getAudioService().playMusic(MusicAsset.MENU);
    }

    @Override
    public void hide() {
        this.stage.clear();
    }

    @Override
    public void render(float delta) {
        uiViewport.apply();
        stage.getBatch().setColor(Color.WHITE);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
