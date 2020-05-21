package com.johnprikkel.mariobros.Sprites.Enemies;//package com.brentaureli.mariobros.Sprites.Enemies;
//
//import com.badlogic.gdx.audio.Sound;
//import com.badlogic.gdx.graphics.g2d.Animation;
//import com.badlogic.gdx.graphics.g2d.Batch;
//import com.badlogic.gdx.graphics.g2d.TextureRegion;
//import com.badlogic.gdx.math.Vector2;
//import com.badlogic.gdx.physics.box2d.BodyDef;
//import com.badlogic.gdx.physics.box2d.CircleShape;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.johnprikkel.mariobros.MarioBros;
import com.johnprikkel.mariobros.Screens.PlayScreen;

public class Goomba extends Enemy {
    private float stateTime;
    private Array<TextureRegion> frames;
    private Animation<TextureRegion> walkAnimation;
    private boolean setToDestroy;
    private boolean destroyed;

    public Goomba(PlayScreen screen, float x, float y) {
            super(screen, x, y);
            frames = new Array<TextureRegion>();
            for(int i = 0; i < 2; i ++)
                frames.add(new TextureRegion(screen.getAtlas().findRegion("goomba"), i * 16, 0, 16, 16));
            walkAnimation = new Animation(0.1f, frames);
            stateTime = 0;
            setBounds(getX(), getY(), 16 / com.johnprikkel.mariobros.MarioBros.PPM , 16 / com.johnprikkel.mariobros.MarioBros.PPM);
            setToDestroy = false;
            destroyed = false;
    }

    public void update(float dt){
        stateTime += dt;
        if(setToDestroy && !destroyed){
            world.destroyBody(b2dbody);
            destroyed = true;
            setRegion(new TextureRegion(screen.getAtlas().findRegion("goomba"), 32, 0, 16, 16));
            stateTime = 0;
        }
        else if(!destroyed) {
            b2dbody.setLinearVelocity(velocity);
            setPosition(b2dbody.getPosition().x - getWidth() / 2, b2dbody.getPosition().y - getHeight() / 2);
            setRegion(walkAnimation.getKeyFrame(stateTime, true));
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
        shape.setRadius(6 / com.johnprikkel.mariobros.MarioBros.PPM);
        fdef.filter.categoryBits = com.johnprikkel.mariobros.MarioBros.ENEMY_BIT;
        fdef.filter.maskBits = com.johnprikkel.mariobros.MarioBros.GROUND_BIT | com.johnprikkel.mariobros.MarioBros.COIN_BIT | com.johnprikkel.mariobros.MarioBros.BRICK_BIT | com.johnprikkel.mariobros.MarioBros.ENEMY_BIT | com.johnprikkel.mariobros.MarioBros.OBJECT_BIT | com.johnprikkel.mariobros.MarioBros.MARIO_BIT;

        fdef.shape = shape;
        b2dbody.createFixture(fdef).setUserData(this);

    }

    public void draw(Batch batch){
        if(!destroyed || stateTime < 1){
            super.draw(batch);
        }
    }

}


