package com.johnprikkel.virusgame.Sprites.TileObjects;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.johnprikkel.virusgame.Sprites.Lumberjack;
import com.johnprikkel.virusgame.LumberjackDef;
import com.johnprikkel.virusgame.Scenes.Hud;
import com.johnprikkel.virusgame.Screens.PlayScreen;

public class Brick extends InteractiveTileObject {
    public Brick(PlayScreen screen, MapObject object){
        super(screen, object);
        fixture.setUserData(this);
        setCategoryFilter(LumberjackDef.BRICK_BIT);
    }

    @Override
    public void onHeadHit(Lumberjack lumberjack) {
            setCategoryFilter(LumberjackDef.DESTROYED_BIT);
            getCell().setTile(null);
            Hud.addScore(200);
            LumberjackDef.manager.get("audio/sounds/breakblock.wav", Sound.class).play();
    }

    @Override
    public void hitGoal(Lumberjack lumberjack) {

    }
}
