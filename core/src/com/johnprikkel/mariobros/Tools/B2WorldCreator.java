package com.johnprikkel.mariobros.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.johnprikkel.mariobros.Screens.PlayScreen;
import com.johnprikkel.mariobros.Sprites.Enemies.Goomba;
import com.johnprikkel.mariobros.Sprites.TileObjects.Brick;
import com.johnprikkel.mariobros.Sprites.TileObjects.Coin;
import com.johnprikkel.mariobros.MarioBros;

public class B2WorldCreator {
    private Array<Goomba> goombas;

    public Array<Goomba> getGoombas() {
        return goombas;
    }

    public B2WorldCreator(PlayScreen screen){
        World world = screen.getWorld();
        TiledMap map = screen.getMap();
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        for(MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth()/2)  / com.johnprikkel.mariobros.MarioBros.PPM , (rect.getY() + rect.getHeight()/2) / com.johnprikkel.mariobros.MarioBros.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth()/2 / com.johnprikkel.mariobros.MarioBros.PPM, rect.getHeight()/2 / com.johnprikkel.mariobros.MarioBros.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }

        for(MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth()/2) / com.johnprikkel.mariobros.MarioBros.PPM, (rect.getY() + rect.getHeight()/2) / com.johnprikkel.mariobros.MarioBros.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth()/2 / com.johnprikkel.mariobros.MarioBros.PPM, rect.getHeight()/2 / com.johnprikkel.mariobros.MarioBros.PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = com.johnprikkel.mariobros.MarioBros.OBJECT_BIT;
            body.createFixture(fdef);
        }

        for(MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){

            new Brick(screen, object);
        }

        for(MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){

           new Coin(screen, object);
        }

        //create all goombas
        goombas = new Array<Goomba>();
        for(MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            goombas.add(new Goomba(screen, rect.getX() / com.johnprikkel.mariobros.MarioBros.PPM, rect.getY() / MarioBros.PPM));
        }
    }

}
