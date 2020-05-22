package com.johnprikkel.mariobros.Sprites;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.johnprikkel.mariobros.Screens.PlayScreen;
import com.johnprikkel.mariobros.MarioBros;

public class Mario extends Sprite {

    public enum State { FALLING, JUMPING, STANDING, RUNNING, DEAD, WON };
    public State currentState;
    public State previousState;
    public World world;
    public Body b2dbody;
    private TextureRegion marioStand;
    private TextureRegion marioDead;
    private TextureRegion marioWon;
    private Animation marioRun;
    private TextureRegion marioJump;
    private float stateTimer;
    private boolean runningRight;
    private boolean marioIsDead;
    private boolean marioHasWon;


    public Mario(PlayScreen screen){
        //super(screen.getAtlas().findRegion("little_mario"));
        this.world = screen.getWorld();
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;

        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 1; i < 4; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("little_mario"), i * 16, 0 , 16, 16));
        marioRun = new Animation(0.1f, frames);
        frames.clear();

        marioJump = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 80, 0, 16, 16);

        marioWon = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 80, 0, 16, 16);

        marioDead = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 96, 0, 16, 16);

        defineMario();
        marioStand = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 0, 0, 16, 16);
        setBounds(0,0, 16 / com.johnprikkel.mariobros.MarioBros.PPM, 16/ com.johnprikkel.mariobros.MarioBros.PPM);
        setRegion(marioStand);
    }

    public void update(float dt){
        setPosition(b2dbody.getPosition().x - getWidth() / 2, b2dbody.getPosition().y - getHeight() / 2);
        setRegion(getFrame(dt));
    }

    public TextureRegion getFrame(float dt){
        currentState = getState();

        TextureRegion region;
        switch(currentState) {
            case DEAD:
                region = marioDead;
                break;
            case WON:
                region = marioWon;
                break;
            case JUMPING:
                region = marioJump;
                break;
            case RUNNING:
                region =  (TextureRegion) marioRun.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
            case STANDING:
            default:
                region = marioStand;
                break;
        }

        if((b2dbody.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()){
            region.flip(true, false);
            runningRight = false;
        }
        else if((b2dbody.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
            region.flip(true, false);
            runningRight = true;
        }

        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;

    }

    public State getState(){
        if(marioIsDead)
            return State.DEAD;
        else if(marioHasWon)
            return State.WON;
        else if(b2dbody.getLinearVelocity().y > 0 || (b2dbody.getLinearVelocity().y < 0 && previousState == State.JUMPING))
            return State.JUMPING;
        else if(b2dbody.getLinearVelocity().y < 0)
            return State.FALLING;
        else if(b2dbody.getLinearVelocity().x != 0)
            return State.RUNNING;
        else
            return State.STANDING;
    }

    public boolean isDead(){
        return marioIsDead;
    }

    public boolean hasWon(){
        return marioHasWon;
    }

    public float getStateTimer(){
        return stateTimer;
    }

    public void hit(){
            com.johnprikkel.mariobros.MarioBros.manager.get("audio/music/doom.ogg", Music.class).stop();
            com.johnprikkel.mariobros.MarioBros.manager.get("audio/sounds/mariodie.wav", Sound.class).play();
            marioIsDead = true;
            Filter filter = new Filter();
            filter.maskBits = com.johnprikkel.mariobros.MarioBros.NOTHING_BIT;
            for(Fixture fixture: b2dbody.getFixtureList())
                fixture.setFilterData(filter);
            b2dbody.applyLinearImpulse(new Vector2(0, 4f), b2dbody.getWorldCenter(), true);
    }

    public void hitGoal(){
        com.johnprikkel.mariobros.MarioBros.manager.get("audio/music/doom.ogg", Music.class).stop();
        com.johnprikkel.mariobros.MarioBros.manager.get("audio/music/levelcomplete.ogg", Music.class).play();
        marioHasWon = true;
    }

    public void defineMario() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(32 / com.johnprikkel.mariobros.MarioBros.PPM, 32 / com.johnprikkel.mariobros.MarioBros.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2dbody = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / com.johnprikkel.mariobros.MarioBros.PPM);
        fdef.filter.categoryBits = com.johnprikkel.mariobros.MarioBros.MARIO_BIT;
        fdef.filter.maskBits = com.johnprikkel.mariobros.MarioBros.GROUND_BIT | com.johnprikkel.mariobros.MarioBros.COIN_BIT | com.johnprikkel.mariobros.MarioBros.BRICK_BIT | com.johnprikkel.mariobros.MarioBros.ENEMY_BIT | com.johnprikkel.mariobros.MarioBros.OBJECT_BIT |com.johnprikkel.mariobros.MarioBros.GOAL_BIT | com.johnprikkel.mariobros.MarioBros.ITEM_BIT;

        fdef.shape = shape;
        b2dbody.createFixture(fdef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2/ com.johnprikkel.mariobros.MarioBros.PPM, 6 / com.johnprikkel.mariobros.MarioBros.PPM), new Vector2(2 / com.johnprikkel.mariobros.MarioBros.PPM, 6/ com.johnprikkel.mariobros.MarioBros.PPM));
        fdef.filter.categoryBits = MarioBros.MARIO_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;

        b2dbody.createFixture(fdef).setUserData(this);

    }

}
