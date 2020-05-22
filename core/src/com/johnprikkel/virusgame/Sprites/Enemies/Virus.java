package com.johnprikkel.virusgame.Sprites.Enemies;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.johnprikkel.virusgame.LumberjackDef;
import com.johnprikkel.virusgame.Screens.PlayScreen;

public class Virus extends Enemy {
    private float stateTime;
    private Array<TextureRegion> frames;
    private Animation<TextureRegion> virusAnimation;
    private boolean setToDestroy;
    private boolean destroyed;

    public Virus(PlayScreen screen, float x, float y) {
            super(screen, x, y);
            frames = new Array<TextureRegion>();
            for(int i = 0; i < 7; i ++)
                frames.add(new TextureRegion(screen.getAtlas().findRegion("virus"), i * 64, 32, 64, 64));
            virusAnimation = new Animation(0.5f, frames);
            stateTime = 0;
            setBounds(getX(), getY(), 64 / LumberjackDef.PPM , 64 / LumberjackDef.PPM);
            setToDestroy = false;
            destroyed = false;
    }

    public void update(float dt){
        stateTime += dt;
        if(setToDestroy && !destroyed){
            world.destroyBody(b2dbody);
            destroyed = true;
            setRegion(new TextureRegion(screen.getAtlas().findRegion("virus"), 32, 448, 64, 64));
            stateTime = 0;
        }
        else if(!destroyed) {
            b2dbody.setLinearVelocity(velocity);
            setPosition(b2dbody.getPosition().x - getWidth() / 2, b2dbody.getPosition().y - getHeight() / 2);
            setRegion(virusAnimation.getKeyFrame(stateTime, true));
        }
    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(this.getX(), this.getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2dbody = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / LumberjackDef.PPM);
        fdef.filter.categoryBits = LumberjackDef.ENEMY_BIT;
        fdef.filter.maskBits = LumberjackDef.GROUND_BIT | LumberjackDef.COIN_BIT | LumberjackDef.BRICK_BIT | LumberjackDef.ENEMY_BIT | LumberjackDef.OBJECT_BIT | LumberjackDef.LUMBERJACK_BIT;

        fdef.shape = shape;
        b2dbody.createFixture(fdef).setUserData(this);

    }

    public void draw(Batch batch){
        if(!destroyed || stateTime < 1){
            super.draw(batch);
        }
    }

}


