package com.dani.game2.Levels;

import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by root on 01.06.17.
 */

public class Fragment_Pack {


    protected TiledMap map;
    protected Fragment_manager.State state;
    protected Vector2 startPoint;

    protected Fragment_Pack(TiledMap map, Fragment_manager.State state) {
        this.map = map;
        this.state = state;
        calculate_start_point();


    }

    protected void calculate_start_point() {
        ////System.out.println("calculate Start Point");

        //Vector2 smalestPoint = new Vector2(map.getLayers().get(0).getObjects().getByType(PolylineMapObject.class).get(0).getPolyline().getTransformedVertices()[0], map.getLayers().get(0).getObjects().getByType(PolylineMapObject.class).get(0).getPolyline().getTransformedVertices()[0]);
        Vector2 smalestPoint = new Vector2(100000, 0);


        for (int i = 0; i <= map.getLayers().getCount() - 1; i++) {
            ////System.out.print(i + "\n\n");
            if (map.getLayers().get(i).getObjects().getByType(PolylineMapObject.class).size > 0) {
                for (PolylineMapObject object : map.getLayers().get(i).getObjects().getByType(PolylineMapObject.class)) {

                    float[] vertecies = object.getPolyline().getTransformedVertices();

                    for (int i2 = 0; i2 < vertecies.length - 1; i2 += 2) {
                        if (vertecies[i2] < smalestPoint.x) {
                            smalestPoint.x = vertecies[i2];
                            smalestPoint.y = vertecies[i2 + 1];
                        }
                    }
                }
            }
        }
        startPoint = smalestPoint;
    }

    protected TiledMap getMap() {return map;}
    protected Vector2 getStartPoint() {return startPoint;}


}



