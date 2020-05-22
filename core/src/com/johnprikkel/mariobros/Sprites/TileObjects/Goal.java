package com.johnprikkel.mariobros.Sprites.TileObjects;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.johnprikkel.mariobros.MarioBros;
import com.johnprikkel.mariobros.Scenes.Hud;
import com.johnprikkel.mariobros.Screens.PlayScreen;
import com.johnprikkel.mariobros.Sprites.Mario;

public class Goal extends InteractiveTileObject {
   public Goal(PlayScreen screen, MapObject object){
       super(screen, object);
       fixture.setUserData(this);
       setCategoryFilter(com.johnprikkel.mariobros.MarioBros.GOAL_BIT);
   }

    @Override
    public void onHeadHit(Mario mario) {

    }

    @Override
    public void hitGoal(Mario mario) {
       screen.winner();
    }

}
