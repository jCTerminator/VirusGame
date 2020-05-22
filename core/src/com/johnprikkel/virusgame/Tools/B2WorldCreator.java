package com.johnprikkel.virusgame.Tools;

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
import com.johnprikkel.virusgame.LumberjackDef;
import com.johnprikkel.virusgame.Screens.PlayScreen;
//import com.johnprikkel.virusgame.Sprites.Enemies.Virus;
import com.johnprikkel.virusgame.Sprites.TileObjects.Brick;
import com.johnprikkel.virusgame.Sprites.TileObjects.Coin;
import com.johnprikkel.virusgame.Sprites.TileObjects.Goal;

public class B2WorldCreator {
    //private Array<Virus> goombas;

    //public Array<Virus> getGoombas() {
        //return goombas;
   // }

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
            bdef.position.set((rect.getX() + rect.getWidth()/2)  / LumberjackDef.PPM , (rect.getY() + rect.getHeight()/2) / LumberjackDef.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth()/2 / LumberjackDef.PPM, rect.getHeight()/2 / LumberjackDef.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }

        for(MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth()/2) / LumberjackDef.PPM, (rect.getY() + rect.getHeight()/2) / LumberjackDef.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth()/2 / LumberjackDef.PPM, rect.getHeight()/2 / LumberjackDef.PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = LumberjackDef.OBJECT_BIT;
            body.createFixture(fdef);
        }

        for(MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){

            new Brick(screen, object);
        }

        for(MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){

           new Coin(screen, object);
        }
        //create all goombas
        //goombas = new Array<Virus>();
        //for(MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)){
            //Rectangle rect = ((RectangleMapObject) object).getRectangle();
           //goombas.add(new Virus(screen, rect.getX() / LumberjackDef.PPM, rect.getY() / LumberjackDef.PPM));
        //}

        for(MapObject object : map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth()/2) / LumberjackDef.PPM, (rect.getY() + rect.getHeight()/2) / LumberjackDef.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth()/2 / LumberjackDef.PPM, rect.getHeight()/2 / LumberjackDef.PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = LumberjackDef.OBJECT_BIT;
            body.createFixture(fdef);

            new Goal(screen, object);
        }
    }

}

