package com.dani.game2.Sprites.TileObjects;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.dani.game2.ChainsawRun;

/**
 * Created by root on 17.07.16.
 */
public abstract class InteractiveTileObject {
    protected World world;


    protected TiledMap map;
    protected TiledMapTile tile;
    protected Rectangle bounds;
    protected Body body;
    protected Fixture fixture;
    protected com.dani.game2.ChainsawRun game;
    protected com.dani.game2.Screens.PlayScreen screen;
    protected MapObject object;

    protected com.dani.game2.ChainsawRun chainsawRun;

    public InteractiveTileObject(com.dani.game2.Screens.PlayScreen screen, MapObject object, com.dani.game2.ChainsawRun chainsawRun){
        this.object = object;
        this.world = screen.getWorld();
        this.map = screen.getMap();
        this.bounds = ((RectangleMapObject) object).getRectangle();
        this.screen = screen;
        this.chainsawRun = chainsawRun;

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();


        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((bounds.getX() + bounds.getWidth() / 2) / com.dani.game2.ChainsawRun.PPM, (bounds.getY() + bounds.getHeight() / 2)/ com.dani.game2.ChainsawRun.PPM);

        body = world.createBody(bdef);

        shape.setAsBox((bounds.getWidth() / 2 )/ com.dani.game2.ChainsawRun.PPM, (bounds.getHeight() / 2) / com.dani.game2.ChainsawRun.PPM);
        fdef.shape = shape;


        body.createFixture(fdef).setUserData(this);



    }

    public void setFilter(short filterBit){
        //System.out.print("new filter \n");
        Filter filter = new Filter();
        filter.categoryBits = filterBit;

        if (filterBit == ChainsawRun.NULL_BIT){filter.maskBits = 1;}
        for (Fixture fixt : body.getFixtureList()){
            fixt.setFilterData(filter);}
        //fixture.setFilterData(filter);
    }

    public abstract void onHeadHit();

    public TiledMapTileLayer.Cell getCell(){
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);
        return layer.getCell((int)(body.getPosition().x * com.dani.game2.ChainsawRun.PPM / 16),
                (int)(body.getPosition().y * com.dani.game2.ChainsawRun.PPM / 16));

    }



}
