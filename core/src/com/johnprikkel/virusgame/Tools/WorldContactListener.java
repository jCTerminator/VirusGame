package com.johnprikkel.virusgame.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.johnprikkel.virusgame.Sprites.Enemies.Enemy;
import com.johnprikkel.virusgame.Sprites.Lumberjack;
import com.johnprikkel.virusgame.Sprites.TileObjects.InteractiveTileObject;
import com.johnprikkel.virusgame.LumberjackDef;

public class WorldContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch(cDef){
            case LumberjackDef.MARIO_HEAD_BIT | LumberjackDef.BRICK_BIT:
            case LumberjackDef.MARIO_HEAD_BIT | LumberjackDef.COIN_BIT:
                if(fixA.getFilterData().categoryBits == LumberjackDef.MARIO_HEAD_BIT)
                    ((InteractiveTileObject) fixB.getUserData()).onHeadHit((Lumberjack) fixA.getUserData());
                else
                    ((InteractiveTileObject) fixA.getUserData()).onHeadHit((Lumberjack) fixB.getUserData());
                break;
            case LumberjackDef.ENEMY_BIT | LumberjackDef.OBJECT_BIT:
                if(fixA.getFilterData().categoryBits == LumberjackDef.ENEMY_BIT)
                    ((Enemy)fixA.getUserData()).reverseVelocity(true,true);
                else
                    ((Enemy)fixB.getUserData()).reverseVelocity(true, true);
                break;
            case LumberjackDef.LUMBERJACK_BIT | LumberjackDef.ENEMY_BIT:
                if(fixA.getFilterData().categoryBits == LumberjackDef.LUMBERJACK_BIT)
                    ((Lumberjack) fixA.getUserData()).hit();
                else
                    ((Lumberjack) fixB.getUserData()).hit();
                break;
            case LumberjackDef.ENEMY_BIT | LumberjackDef.ENEMY_BIT:
                ((Enemy)fixA.getUserData()).reverseVelocity(true,true);
                ((Enemy)fixB.getUserData()).reverseVelocity(true,true);
                break;
            case LumberjackDef.ENEMY_BIT | LumberjackDef.GROUND_BIT:
                if(fixA.getFilterData().categoryBits == LumberjackDef.ENEMY_BIT)
                    ((Enemy)fixA.getUserData()).reverseVelocity(true,true);
                else
                    ((Enemy)fixB.getUserData()).reverseVelocity(true, true);
                break;
            case LumberjackDef.LUMBERJACK_BIT | LumberjackDef.GOAL_BIT:
                if(fixA.getFilterData().categoryBits == LumberjackDef.LUMBERJACK_BIT)
                    ((Lumberjack) fixA.getUserData()).hitGoal();
                else
                    ((Lumberjack) fixB.getUserData()).hitGoal();
                break;
        }

    }

    @Override
    public void endContact(Contact contact) {
        Gdx.app.log("End Contact", "");
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
