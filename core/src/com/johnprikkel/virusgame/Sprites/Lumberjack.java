package com.johnprikkel.virusgame.Sprites;

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
import com.johnprikkel.virusgame.Screens.PlayScreen;
import com.johnprikkel.virusgame.LumberjackDef;

public class Lumberjack extends Sprite {

    public enum State { FALLING, JUMPING, STANDING, RUNNING, DEAD, WON };
    public State currentState;
    public State previousState;
    public World world;
    public Body b2dbody;
    private TextureRegion lumberjackStand;
    private TextureRegion lumberjackDead;
    private TextureRegion lumberjackWon;
    private Animation lumberjackRun;
    private TextureRegion lumberjackJump;
    private float stateTimer;
    private boolean runningRight;
    private boolean lumberjackIsDead;
    private boolean lumberjackHasWon;


    public Lumberjack(PlayScreen screen){
        //super(screen.getAtlas().findRegion("little_mario"));
        this.world = screen.getWorld();
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;

        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 1; i < 4; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("lumberjack"), i * 16, 0 , 16, 32));
        lumberjackRun = new Animation(0.1f, frames);
        frames.clear();

        lumberjackJump = new TextureRegion(screen.getAtlas().findRegion("lumberjack"), 80, 0, 16, 32);

        lumberjackWon = new TextureRegion(screen.getAtlas().findRegion("lumberjack"), 80, 0, 16, 16);

        lumberjackDead = new TextureRegion(screen.getAtlas().findRegion("lumberjack"), 96, 0, 16, 32);

        defineLumberjack();
        lumberjackStand = new TextureRegion(screen.getAtlas().findRegion("lumberjack"), 0, 0, 16, 32);
        setBounds(0,0, 16 / LumberjackDef.PPM, 32/ LumberjackDef.PPM);
        setRegion(lumberjackStand);
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
                region = lumberjackDead;
                break;
            case WON:
                region = lumberjackWon;
                break;
            case JUMPING:
                region = lumberjackJump;
                break;
            case RUNNING:
                region =  (TextureRegion) lumberjackRun.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
            case STANDING:
            default:
                region = lumberjackStand;
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
        if(lumberjackIsDead)
            return State.DEAD;
        else if(lumberjackHasWon)
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
        return lumberjackIsDead;
    }

    public boolean hasWon(){
        return lumberjackHasWon;
    }

    public float getStateTimer(){
        return stateTimer;
    }

    public void hit(){
            LumberjackDef.manager.get("audio/music/doom.ogg", Music.class).stop();
            LumberjackDef.manager.get("audio/sounds/mariodie.wav", Sound.class).play();
            lumberjackIsDead = true;
            Filter filter = new Filter();
            filter.maskBits = LumberjackDef.NOTHING_BIT;
            for(Fixture fixture: b2dbody.getFixtureList())
                fixture.setFilterData(filter);
            b2dbody.applyLinearImpulse(new Vector2(0, 4f), b2dbody.getWorldCenter(), true);
    }

    public void hitGoal(){
        LumberjackDef.manager.get("audio/music/doom.ogg", Music.class).stop();
        LumberjackDef.manager.get("audio/music/levelcomplete.ogg", Music.class).play();
        lumberjackHasWon = true;
    }

    public void defineLumberjack() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(32 / LumberjackDef.PPM, 32 / LumberjackDef.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2dbody = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / LumberjackDef.PPM);
        fdef.filter.categoryBits = LumberjackDef.LUMBERJACK_BIT;
        fdef.filter.maskBits = LumberjackDef.GROUND_BIT | LumberjackDef.COIN_BIT | LumberjackDef.BRICK_BIT | LumberjackDef.ENEMY_BIT | LumberjackDef.OBJECT_BIT | LumberjackDef.GOAL_BIT ;

        fdef.shape = shape;
        b2dbody.createFixture(fdef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2/ LumberjackDef.PPM, 6 / LumberjackDef.PPM), new Vector2(2 / LumberjackDef.PPM, 6/ LumberjackDef.PPM));
        fdef.filter.categoryBits = LumberjackDef.MARIO_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;

        b2dbody.createFixture(fdef).setUserData(this);

    }

}
