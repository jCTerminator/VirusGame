package com.johnprikkel.virusgame.Sprites.Enemies;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.johnprikkel.virusgame.Screens.PlayScreen;

public abstract class Enemy extends Sprite {
    protected World world;
    protected com.johnprikkel.virusgame.Screens.PlayScreen screen;
    public Body b2dbody;
    public Vector2 velocity;

    public Enemy(PlayScreen screen, float x, float y) {
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x, y);
        defineEnemy();
        velocity = new Vector2((float) 0.2, 2);
        b2dbody.setActive(false);
    }

    protected abstract void defineEnemy();
    public abstract void update(float dt);

    public void reverseVelocity(boolean x, boolean y){
        if(x)
            velocity.x = -velocity.x;
        if(y)
            velocity.y = -velocity.y;
    }
}
