package com.johnprikkel.mariobros.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.johnprikkel.mariobros.Sprites.Enemies.Enemy;
import com.johnprikkel.mariobros.Sprites.Items.Item;
import com.johnprikkel.mariobros.Sprites.Mario;
import com.johnprikkel.mariobros.Sprites.TileObjects.InteractiveTileObject;
import com.johnprikkel.mariobros.MarioBros;

public class WorldContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch(cDef){
            case com.johnprikkel.mariobros.MarioBros.MARIO_HEAD_BIT | com.johnprikkel.mariobros.MarioBros.BRICK_BIT:
            case com.johnprikkel.mariobros.MarioBros.MARIO_HEAD_BIT | com.johnprikkel.mariobros.MarioBros.COIN_BIT:
                if(fixA.getFilterData().categoryBits == com.johnprikkel.mariobros.MarioBros.MARIO_HEAD_BIT)
                    ((InteractiveTileObject) fixB.getUserData()).onHeadHit((Mario) fixA.getUserData());
                else
                    ((InteractiveTileObject) fixA.getUserData()).onHeadHit((Mario) fixB.getUserData());
                break;
            case com.johnprikkel.mariobros.MarioBros.ENEMY_BIT | com.johnprikkel.mariobros.MarioBros.OBJECT_BIT:
                if(fixA.getFilterData().categoryBits == com.johnprikkel.mariobros.MarioBros.ENEMY_BIT)
                    ((Enemy)fixA.getUserData()).reverseVelocity(true,true);
                else
                    ((Enemy)fixB.getUserData()).reverseVelocity(true, true);
                break;
            case com.johnprikkel.mariobros.MarioBros.MARIO_BIT | com.johnprikkel.mariobros.MarioBros.ENEMY_BIT:
                if(fixA.getFilterData().categoryBits == com.johnprikkel.mariobros.MarioBros.MARIO_BIT)
                    ((Mario) fixA.getUserData()).hit();
                else
                    ((Mario) fixB.getUserData()).hit();
                break;
            case com.johnprikkel.mariobros.MarioBros.ENEMY_BIT | com.johnprikkel.mariobros.MarioBros.ENEMY_BIT:
                ((Enemy)fixA.getUserData()).reverseVelocity(true,true);
                ((Enemy)fixB.getUserData()).reverseVelocity(true,true);
                break;
            case com.johnprikkel.mariobros.MarioBros.ITEM_BIT | com.johnprikkel.mariobros.MarioBros.OBJECT_BIT:
                if(fixA.getFilterData().categoryBits == com.johnprikkel.mariobros.MarioBros.ITEM_BIT)
                    ((Item)fixA.getUserData()).reverseVelocity(true,false);
                else
                    ((Item)fixB.getUserData()).reverseVelocity(true, false);
                break;
            case com.johnprikkel.mariobros.MarioBros.ITEM_BIT | com.johnprikkel.mariobros.MarioBros.MARIO_BIT:
                if(fixA.getFilterData().categoryBits == MarioBros.ITEM_BIT)
                    ((Item)fixA.getUserData()).use((Mario) fixB.getUserData());
                else
                    ((Item)fixB.getUserData()).use((Mario) fixA.getUserData());
                break;
            case com.johnprikkel.mariobros.MarioBros.ENEMY_BIT | MarioBros.GROUND_BIT:
                if(fixA.getFilterData().categoryBits == com.johnprikkel.mariobros.MarioBros.ENEMY_BIT)
                    ((Enemy)fixA.getUserData()).reverseVelocity(true,true);
                else
                    ((Enemy)fixB.getUserData()).reverseVelocity(true, true);
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
