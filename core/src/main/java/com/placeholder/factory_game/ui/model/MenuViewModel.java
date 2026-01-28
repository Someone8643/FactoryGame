package com.placeholder.factory_game.ui.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.TimeUtils;
import com.placeholder.factory_game.GdxGame;
import com.placeholder.factory_game.asset.SoundAsset;
import com.placeholder.factory_game.audio.AudioService;
import com.placeholder.factory_game.screen.GameScreen;

public class MenuViewModel extends ViewModel {

    private final AudioService audioService;
    private long lastSndPlayTime;

    public MenuViewModel(GdxGame game) {
        super(game);
        this.audioService = game.getAudioService();
        this.lastSndPlayTime = 0L;
    }

    public float getMusicVolume() {
        return audioService.getMusicVolume();
    }

    public void setMusicVolume(float volume) {
        this.audioService.setMusicVolume(volume);
    }

    public float getSoundVolume() {
        return audioService.getSoundVolume();
    }

    public void setSoundVolume(float soundVolume) {
        this.audioService.setSoundVolume(soundVolume);
        if (TimeUtils.timeSinceMillis(lastSndPlayTime) > 500L) {
            this.lastSndPlayTime = TimeUtils.millis();
            this.audioService.playSound(SoundAsset.SWORD_HIT);
        }
    }

    public void startGame() {
        game.setScreen(GameScreen.class);
    }

    public void quitGame() {
        Gdx.app.exit();
    }
}
