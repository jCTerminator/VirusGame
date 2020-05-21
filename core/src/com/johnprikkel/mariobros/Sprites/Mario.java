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

    public boolean isBig() {
        return marioIsBig;
    }

    public enum State { FALLING, JUMPING, STANDING, RUNNING, GROWING, DEAD };
    public State currentState;
    public State previousState;
    public World world;
    public Body b2dbody;
    private TextureRegion marioStand;
    private TextureRegion bigMarioStand;
    private TextureRegion bigMarioJump;
    private TextureRegion marioDead;
    private Animation bigMarioRun;
    private Animation growMario;
    private Animation marioRun;
    private TextureRegion marioJump;
    private float stateTimer;
    private boolean runningRight;
    private boolean marioIsBig;
    private boolean runGrowAnimation;
    private boolean timeToDefineBigMario;
    private boolean timeToRedefineBigMario;
    private boolean marioIsDead;


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
        for(int i = 1; i < 4; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), i * 16, 0 , 16, 32));
        bigMarioRun = new Animation(0.1f, frames);
        frames.clear();

        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 240, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 240, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32));
        growMario = new Animation(0.2f, frames);

        marioJump = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 80, 0, 16, 16);
        bigMarioJump = new TextureRegion(screen.getAtlas().findRegion("big_mario"), 80, 0, 16, 32);

        marioDead = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 96, 0, 16, 16);

        defineMario();
        marioStand = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 0, 0, 16, 16);
        bigMarioStand = new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32);
        setBounds(0,0, 16 / com.johnprikkel.mariobros.MarioBros.PPM, 16/ com.johnprikkel.mariobros.MarioBros.PPM);
        setRegion(marioStand);
    }

    public void update(float dt){
        if(marioIsBig)
            setPosition(b2dbody.getPosition().x - getWidth() / 2, b2dbody.getPosition().y - getHeight() / 2 - 6 / com.johnprikkel.mariobros.MarioBros.PPM);
        else
            setPosition(b2dbody.getPosition().x - getWidth() / 2, b2dbody.getPosition().y - getHeight() / 2);
        setRegion(getFrame(dt));
        if(timeToDefineBigMario){
            defineBigMario();
        }
        if(timeToRedefineBigMario)
            redefineMario();
    }

    public TextureRegion getFrame(float dt){
        currentState = getState();

        TextureRegion region;
        switch(currentState) {
            case DEAD:
                region = marioDead;
                break;
            case GROWING:
                region = (TextureRegion) growMario.getKeyFrame(stateTimer);
                if(growMario.isAnimationFinished(stateTimer))
                    runGrowAnimation = false;
                break;
            case JUMPING:
                region = marioIsBig ? bigMarioJump : marioJump;
                break;
            case RUNNING:
                region = marioIsBig ? (TextureRegion) bigMarioRun.getKeyFrame(stateTimer, true) : (TextureRegion) marioRun.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
            case STANDING:
            default:
                region = marioIsBig ? bigMarioStand : marioStand;
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
        else if(runGrowAnimation)
            return State.GROWING;
        else if(b2dbody.getLinearVelocity().y > 0 || (b2dbody.getLinearVelocity().y < 0 && previousState == State.JUMPING))
            return State.JUMPING;
        else if(b2dbody.getLinearVelocity().y < 0)
            return State.FALLING;
        else if(b2dbody.getLinearVelocity().x != 0)
            return State.RUNNING;
        else
            return State.STANDING;
    }

    public void grow(){
        runGrowAnimation = true;
        marioIsBig = true;
        timeToDefineBigMario = true;
        setBounds(getX(), getY(), getWidth(),getHeight() * 2);
        com.johnprikkel.mariobros.MarioBros.manager.get("audio/sounds/powerup.wav", Sound.class).play();
    }

    public boolean isDead(){
        return marioIsDead;
    }

    public float getStateTimer(){
        return stateTimer;
    }

    public void hit(){
        if(marioIsBig){
            marioIsBig = false;
            timeToRedefineBigMario = true;
            setBounds(getX(), getY(), getWidth(), getHeight() / 2);
            com.johnprikkel.mariobros.MarioBros.manager.get("audio/sounds/powerdown.wav", Sound.class).play();
        }
        else
            com.johnprikkel.mariobros.MarioBros.manager.get("audio/music/doom.ogg", Music.class).stop();
            com.johnprikkel.mariobros.MarioBros.manager.get("audio/sounds/mariodie.wav", Sound.class).play();
            marioIsDead = true;
            Filter filter = new Filter();
            filter.maskBits = com.johnprikkel.mariobros.MarioBros.NOTHING_BIT;
            for(Fixture fixture: b2dbody.getFixtureList())
                fixture.setFilterData(filter);
            b2dbody.applyLinearImpulse(new Vector2(0, 4f), b2dbody.getWorldCenter(), true);
    }

    public void redefineMario(){
        Vector2 position = b2dbody.getPosition();
        world.destroyBody(b2dbody);

        BodyDef bdef = new BodyDef();
        bdef.position.set(position);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2dbody = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / com.johnprikkel.mariobros.MarioBros.PPM);
        fdef.filter.categoryBits = com.johnprikkel.mariobros.MarioBros.MARIO_BIT;
        fdef.filter.maskBits = com.johnprikkel.mariobros.MarioBros.GROUND_BIT | com.johnprikkel.mariobros.MarioBros.COIN_BIT | com.johnprikkel.mariobros.MarioBros.BRICK_BIT | com.johnprikkel.mariobros.MarioBros.ENEMY_BIT | com.johnprikkel.mariobros.MarioBros.OBJECT_BIT | com.johnprikkel.mariobros.MarioBros.ITEM_BIT;

        fdef.shape = shape;
        b2dbody.createFixture(fdef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2/ com.johnprikkel.mariobros.MarioBros.PPM, 6 / com.johnprikkel.mariobros.MarioBros.PPM), new Vector2(2 / com.johnprikkel.mariobros.MarioBros.PPM, 6/ com.johnprikkel.mariobros.MarioBros.PPM));
        fdef.filter.categoryBits = com.johnprikkel.mariobros.MarioBros.MARIO_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;

        b2dbody.createFixture(fdef).setUserData(this);

        timeToRedefineBigMario = false;

    }

    public void defineBigMario(){
        Vector2 currentPosition = b2dbody.getPosition();
        world.destroyBody(b2dbody);

        BodyDef bdef = new BodyDef();
        bdef.position.set(currentPosition.add(0, 10 / com.johnprikkel.mariobros.MarioBros.PPM));
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2dbody = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / com.johnprikkel.mariobros.MarioBros.PPM);
        fdef.filter.categoryBits = com.johnprikkel.mariobros.MarioBros.MARIO_BIT;
        fdef.filter.maskBits = com.johnprikkel.mariobros.MarioBros.GROUND_BIT | com.johnprikkel.mariobros.MarioBros.COIN_BIT | com.johnprikkel.mariobros.MarioBros.BRICK_BIT | com.johnprikkel.mariobros.MarioBros.ENEMY_BIT | com.johnprikkel.mariobros.MarioBros.OBJECT_BIT | com.johnprikkel.mariobros.MarioBros.ITEM_BIT;

        fdef.shape = shape;
        b2dbody.createFixture(fdef).setUserData(this);
        shape.setPosition(new Vector2(0, -14 / com.johnprikkel.mariobros.MarioBros.PPM));
        b2dbody.createFixture(fdef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2/ com.johnprikkel.mariobros.MarioBros.PPM, 6 / com.johnprikkel.mariobros.MarioBros.PPM), new Vector2(2 / com.johnprikkel.mariobros.MarioBros.PPM, 6/ com.johnprikkel.mariobros.MarioBros.PPM));
        fdef.filter.categoryBits = com.johnprikkel.mariobros.MarioBros.MARIO_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;

        b2dbody.createFixture(fdef).setUserData(this);
        timeToDefineBigMario = false;
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
        fdef.filter.maskBits = com.johnprikkel.mariobros.MarioBros.GROUND_BIT | com.johnprikkel.mariobros.MarioBros.COIN_BIT | com.johnprikkel.mariobros.MarioBros.BRICK_BIT | com.johnprikkel.mariobros.MarioBros.ENEMY_BIT | com.johnprikkel.mariobros.MarioBros.OBJECT_BIT  | com.johnprikkel.mariobros.MarioBros.ITEM_BIT;

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
