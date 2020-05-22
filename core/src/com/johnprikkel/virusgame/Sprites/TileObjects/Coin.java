package com.johnprikkel.virusgame.Sprites.TileObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.johnprikkel.virusgame.LumberjackDef;
import com.johnprikkel.virusgame.Sprites.Lumberjack;
import com.johnprikkel.virusgame.Scenes.Hud;
import com.johnprikkel.virusgame.Screens.PlayScreen;

public class Coin extends InteractiveTileObject {
    private static TiledMapTileSet tileSet;
    private final int BLANK_COIN = 28;
    public Coin(PlayScreen screen, MapObject object){
        super(screen, object);
        tileSet = map.getTileSets().getTileSet("tileset_gutter");
        fixture.setUserData(this);
        setCategoryFilter(LumberjackDef.COIN_BIT);
    }

    @Override
    public void onHeadHit(Lumberjack lumberjack) {
        Gdx.app.log("Coin", "Collision");
        if(getCell().getTile().getId() == BLANK_COIN)
            LumberjackDef.manager.get("audio/sounds/bump.wav", Sound.class).play();
        else {
            LumberjackDef.manager.get("audio/sounds/coin.wav", Sound.class).play();
        }
        getCell().setTile(tileSet.getTile(BLANK_COIN));
        Hud.addScore(500);
    }

    @Override
    public void hitGoal(Lumberjack lumberjack) {

    }
}
