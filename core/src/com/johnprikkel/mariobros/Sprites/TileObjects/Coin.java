package com.johnprikkel.mariobros.Sprites.TileObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Vector2;
import com.johnprikkel.mariobros.MarioBros;
import com.johnprikkel.mariobros.Scenes.Hud;
import com.johnprikkel.mariobros.Screens.PlayScreen;
import com.johnprikkel.mariobros.Sprites.Items.ItemDef;
import com.johnprikkel.mariobros.Sprites.Items.Mushroom;
import com.johnprikkel.mariobros.Sprites.Mario;

public class Coin extends InteractiveTileObject {
    private static TiledMapTileSet tileSet;
    private final int BLANK_COIN = 28;
    public Coin(PlayScreen screen, MapObject object){
        super(screen, object);
        tileSet = map.getTileSets().getTileSet("tileset_gutter");
        fixture.setUserData(this);
        setCategoryFilter(com.johnprikkel.mariobros.MarioBros.COIN_BIT);
    }

    @Override
    public void onHeadHit(Mario mario) {
        Gdx.app.log("Coin", "Collision");
        if(getCell().getTile().getId() == BLANK_COIN)
            com.johnprikkel.mariobros.MarioBros.manager.get("audio/sounds/bump.wav", Sound.class).play();
        else {
            if(object.getProperties().containsKey("mushroom")) {
                screen.spawnItem(new ItemDef(new Vector2(body.getPosition().x, body.getPosition().y + 16 / com.johnprikkel.mariobros.MarioBros.PPM), Mushroom.class)); //check if errors
                com.johnprikkel.mariobros.MarioBros.manager.load("audio/sounds/powerup_spawn.wav", Sound.class);
            }
            else
                MarioBros.manager.get("audio/sounds/coin.wav", Sound.class).play();
        }
        getCell().setTile(tileSet.getTile(BLANK_COIN));
        Hud.addScore(500);
    }
}