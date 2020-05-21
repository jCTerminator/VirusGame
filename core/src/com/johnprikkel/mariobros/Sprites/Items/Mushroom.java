package com.johnprikkel.mariobros.Sprites.Items;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.johnprikkel.mariobros.MarioBros;
import com.johnprikkel.mariobros.Screens.PlayScreen;
import com.johnprikkel.mariobros.Sprites.Mario;

public class Mushroom extends Item {
    public Mushroom(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        setRegion(screen.getAtlas().findRegion("mushroom"), 0, 0, 16, 16);
        velocity = new Vector2(0.7f,0);
    }

    @Override
    public void defineItem() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY()); //check if errors
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / com.johnprikkel.mariobros.MarioBros.PPM);
        fdef.filter.categoryBits = com.johnprikkel.mariobros.MarioBros.ITEM_BIT;
        fdef.filter.maskBits = com.johnprikkel.mariobros.MarioBros.MARIO_BIT | com.johnprikkel.mariobros.MarioBros.OBJECT_BIT | com.johnprikkel.mariobros.MarioBros.GROUND_BIT | com.johnprikkel.mariobros.MarioBros.COIN_BIT | MarioBros.BRICK_BIT;


        fdef.shape = shape;
        body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void use(Mario mario) {
        destroy();
        mario.grow();
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2 );
        velocity.y =body.getLinearVelocity().y;
        body.setLinearVelocity(velocity);
    }
}
