package com.johnprikkel.virusgame.Sprites.TileObjects;

import com.badlogic.gdx.maps.MapObject;
import com.johnprikkel.virusgame.LumberjackDef;
import com.johnprikkel.virusgame.Screens.PlayScreen;
import com.johnprikkel.virusgame.Sprites.Lumberjack;

public class Goal extends InteractiveTileObject {
   public Goal(PlayScreen screen, MapObject object){
       super(screen, object);
       fixture.setUserData(this);
       setCategoryFilter(LumberjackDef.GOAL_BIT);
   }

    @Override
    public void onHeadHit(Lumberjack lumberjack) {

    }

    @Override
    public void hitGoal(Lumberjack lumberjack) {
       screen.winner();
    }

}
