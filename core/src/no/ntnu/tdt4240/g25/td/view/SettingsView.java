package no.ntnu.tdt4240.g25.td.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;

import java.util.Locale;

import no.ntnu.tdt4240.g25.td.asset.Assets;
import no.ntnu.tdt4240.g25.td.asset.Audio;
import no.ntnu.tdt4240.g25.td.asset.GameMusic;
import no.ntnu.tdt4240.g25.td.controller.SettingsController;

public class SettingsView extends AbstractView {

    private final Skin skin = Assets.getInstance().getSkin();
    private final CheckBox musicToggle = new CheckBox("Music", skin, "switch");
    private final CheckBox SFXToggle = new CheckBox("Sound", skin, "switch");
    private final Button volumeUp = new Button(skin, "plus");
    private final Button volumeDown = new Button(skin, "minus");
    private final TextButton backButton = new TextButton("Back to Menu", skin, "big");
    private final Label volumeValue = new Label("volume", skin, "default"); //spinner
    private final Table table = new Table();

    private final SettingsController.ViewCallbackHandler viewCallback;

    public SettingsView(SpriteBatch batch, SettingsController.ViewCallbackHandler viewCallback) {
        super(viewport, batch);
        Gdx.input.setInputProcessor(this);
        this.viewCallback = viewCallback;
        buildTable();
        attatchListeners();
    }

    private void buildTable() {
        Label title = new Label("Settings", skin, "title");
        title.setFontScale(1.8f);
        table.setFillParent(true);
        table.add(title).padBottom(50).colspan(3).row();

        musicToggle.getImage().setScaling(Scaling.fill);
        musicToggle.getImageCell().size(50);
        musicToggle.getLabelCell().padLeft(60);
        musicToggle.getLabel().setFontScale(2);
        musicToggle.padBottom(20);
        table.add(musicToggle).width(100).colspan(3).row();

        SFXToggle.getImage().setScaling(Scaling.fill);
        SFXToggle.getImageCell().size(50);
        SFXToggle.getLabelCell().padLeft(60);
        SFXToggle.getLabel().setFontScale(2);
        SFXToggle.padBottom(20);
        table.add(SFXToggle).width(100).colspan(3).row();

        table.add(volumeDown).size(60, 50).padBottom(10).padTop(10);
        volumeValue.setFontScale(1.5f);
        table.add(volumeValue).width(30).pad(10);
        table.add(volumeUp).size(60, 50).padBottom(10).padTop(10).row();

        backButton.getLabel().setFontScale(2);
        table.add(backButton).size(350, 90).pad(10).colspan(3).row();

        getRoot().addActor(table);
    }

    public void updateVolume(float volume) {
    	this.volumeValue.setText(String.format(Locale.ENGLISH, "%.0f", volume * 100) + "%");
    }

    public void setMusic(boolean music) {
        this.musicToggle.setChecked(music);
    }

    public void setSFX(boolean sfx) {
    	this.SFXToggle.setChecked(sfx);
    }

    private void attatchListeners() {
        musicToggle.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                viewCallback.toggleMusic();
                viewCallback.getMusic();
            }
        });
        SFXToggle.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                viewCallback.toggleSFX();
                viewCallback.getSFX();
            }
        });
        volumeUp.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                viewCallback.increaseVolume();
                viewCallback.getVolume();
            }
        });
        volumeDown.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                viewCallback.decreaseVolume();
                viewCallback.getVolume();
            }
        });
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                viewCallback.clickOk();
            }
        });

    }

    @Override
    public void show() {
        Audio.playMusic(GameMusic.SETTINGS);
        super.show();
        viewCallback.getVolume();
        viewCallback.getMusic();
        viewCallback.getSFX();
    }

    @Override
    public void hide() {
        Audio.stopMusic();
        super.hide();
    }
}
