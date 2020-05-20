package com.brentaureli.mariobros.Sprites.TileObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.brentaureli.mariobros.MarioBros;
import com.brentaureli.mariobros.Scenes.Hud;
import com.brentaureli.mariobros.Screens.PlayScreen;

public class Brick extends InteractiveTileObject {
    public Brick(PlayScreen screen, MapObject object){
        super(screen, object);
        fixture.setUserData(this);
        setCategoryFilter(MarioBros.BRICK_BIT);
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("Brick", "Collosion");
        setCategoryFilter(MarioBros.DESTROYED_BIT);
        getCell().setTile(null);
        Hud.addScore(200);
        MarioBros.manager. get("audio/sounds/breakblock.wav", Sound.class).play();
    }
}
//import com.badlogic.gdx.audio.Sound;
//import com.badlogic.gdx.maps.MapObject;
//import com.badlogic.gdx.math.Rectangle;
//import com.brentaureli.mariobros.MarioBros;
//import com.brentaureli.mariobros.Scenes.Hud;
//import com.brentaureli.mariobros.Screens.PlayScreen;
//import com.brentaureli.mariobros.Sprites.Mario;
//
///**
// * Created by brentaureli on 8/28/15.
// */
//public class Brick extends InteractiveTileObject {
//    public Brick(PlayScreen screen, MapObject object){
//        super(screen, object);
//        fixture.setUserData(this);
//        setCategoryFilter(MarioBros.BRICK_BIT);
//    }
//
//    @Override
//    public void onHeadHit(Mario mario) {
//        if(mario.isBig()) {
//            setCategoryFilter(MarioBros.DESTROYED_BIT);
//            getCell().setTile(null);
//            Hud.addScore(200);
//            MarioBros.manager.get("audio/sounds/breakblock.wav", Sound.class).play();
//        }
//        MarioBros.manager.get("audio/sounds/bump.wav", Sound.class).play();
//    }
//
//}
