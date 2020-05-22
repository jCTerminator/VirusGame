package com.johnprikkel.mariobros.Sprites.TileObjects;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.johnprikkel.mariobros.MarioBros;
import com.johnprikkel.mariobros.Scenes.Hud;
import com.johnprikkel.mariobros.Screens.PlayScreen;
import com.johnprikkel.mariobros.Sprites.Mario;

public class Brick extends InteractiveTileObject {
    public Brick(PlayScreen screen, MapObject object){
        super(screen, object);
        fixture.setUserData(this);
        setCategoryFilter(com.johnprikkel.mariobros.MarioBros.BRICK_BIT);
    }

    @Override
    public void onHeadHit(Mario mario) {
            setCategoryFilter(com.johnprikkel.mariobros.MarioBros.DESTROYED_BIT);
            getCell().setTile(null);
            Hud.addScore(200);
            com.johnprikkel.mariobros.MarioBros.manager.get("audio/sounds/breakblock.wav", Sound.class).play();
    }

    @Override
    public void hitGoal(Mario mario) {

    }
}
