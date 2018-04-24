package com.dani.game2.Animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.RepeatablePolygonSprite;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ShortArray;
import com.dani.game2.ChainsawRun;
import com.dani.game2.Screens.PlayScreen;
import com.dani.game2.tools.PhysicsCalculator;



/**
 * Created by root on 27.11.16.
 */

public class DynamicGround{

    public PlayScreen screen;

    public ShapeRenderer shapeRenderer;
    //public Pixmap pixmap;


    private EarClippingTriangulator triangulator;

    private Array<Vector2> startPoints;
    private Array<Vector2> connectPoints;
    private Array<Vector2> endPoints;
    private Array<Vector2> allPoints;

    private Array<float[]> rectangles; //

    private Sprite upGroundSp;
    private Sprite downGroundSp1;
    private Sprite downGroundSp2;
    private Sprite downGroundSp3;

    float tempX;
    float tempY;
    float tempWidth;
    float Height;
    float tempRotation;

    private TextureRegion testRegion;

    private Array<PolygonSprite> polyUps;
    private Array<PolygonSprite> polyDown1s;
    private Array<PolygonSprite> polyDown2s;
    private Array<PolygonSprite> polyDown3s;
    //private Array<PolygonSprite> polyDown4s;
    //private Array<PolygonSprite> polyDown5s;

    private Array<Vector2> oldPoints;

    private Vector2 pointAtVerryEnd;
    private Vector2 lastGradPos;

    private float biggestX = 0;

    public DynamicGround(PlayScreen screen){
        Height = 10;
        this.screen = screen;

        triangulator = new EarClippingTriangulator();
        shapeRenderer = new ShapeRenderer();
        rectangles = new Array<float[]>();

        startPoints = new Array<Vector2>();
        connectPoints = new Array<Vector2>();
        endPoints = new Array<Vector2>();
        allPoints = new Array<Vector2>();
        polyUps = new Array<PolygonSprite>();
        polyDown1s = new Array<PolygonSprite>();
        polyDown2s = new Array<PolygonSprite>();
        polyDown3s = new Array<PolygonSprite>();


        Array<Sprite> sprites = screen.game.asset_loader.ground_sprites;

        upGroundSp    = sprites.get(0);
        downGroundSp1 = sprites.get(1);
        downGroundSp2 = sprites.get(2);
        downGroundSp3 = sprites.get(3);


        //System.out.println("new oldPoint");
        oldPoints = new Array<Vector2>();
        oldPoints.add(new Vector2(0,0));

        pointAtVerryEnd = new Vector2(0,0);
        lastGradPos = new Vector2(0,0);

    }

    private void create_polygons(Array<Vector2> points_uneddited) {
        ////System.out.print("start: create_polygons\n");

        //test
        //Array<Vector2> testArray= new Array<Vector2>();
        //testArray.add(new Vector2(points_uneddited.get(0).x - .3f, points_uneddited.get(0).y + .0f ));
        //testArray.addAll(points_uneddited);
        //oints_uneddited = testArray;
        //test

        Array<Vector2> points = new Array<Vector2>();
        points.addAll(points_uneddited.toArray());


        float pd1;
        float pd2;
        boolean setToOldpoint = false;
        Vector2 setOldPoint = new Vector2();

        float smallestX = points.get(0).x;
        Vector2 beginningPoint = points.get(0);


        int beginnPointIndex = 0;
        int i = 0;
        for (Vector2 point: points){
            if (point.x < smallestX){
                smallestX = point.x;
                beginningPoint = point;
                beginnPointIndex = i;
                ////System.out.println("bregginging point index:  " + i);
                ////System.out.println("            smallestX: "+ smallestX + " beginningpointindex:  " + beginnPointIndex);
            }
            i ++;
        }

        //beginnPointIndex = 0;//remove later
        //beginningPoint = points.get(0); //remove later

        for (Vector2 oldPoint : oldPoints) {
            pd1 = Math.abs(oldPoint.y - beginningPoint.y);
            pd2 = Math.abs(oldPoint.x - beginningPoint.x);


            if (pd1 < 1f && pd2 < 1f) {
                ////System.out.println("      set to oldPoint    normalPoint: " + points.get(beginnPointIndex) + "   |  oldPoint: " + oldPoint);
                setToOldpoint = true;
                setOldPoint = new Vector2(oldPoint);
                points.get(beginnPointIndex).set(setOldPoint.x, setOldPoint.y);
                break;


            }

                ////System.out.println("      set not to oldPoint    " + points.get(0) + "   |  " + oldPoint);
            ////System.out.println();

        }

        float largestX = -999;
        Vector2 endPoint = new Vector2();
        for (Vector2 point: points){
            if (point.x > largestX){
               // //System.out.println("                new smallest x found");
                largestX = point.x;
                endPoint = point;
            }
        }


        oldPoints.add(endPoint);
        //oldPoints.add(beginningPoint);




        //create underneath points

        //create points2
        Array<Vector2> points2 = new Array<Vector2>();
        i = 0;
        Vector2 cp;
        cp = new Vector2(points.get(i).x - .1f, points.get(i). y - 100);
        points2.add(cp);
        points2.addAll(create_underneath_points(points, .32f, 0, false));
        i = points.size -1;
        cp = new Vector2(points.get(i).x, points.get(i). y - 100);
        points2.add(cp);

        //create points3
        Array<Vector2> points3 = new Array<Vector2>();
        i = 0;
        cp = new Vector2(points2.get(i).x - .1f, points2.get(i). y);
        points3.add(cp);
        points3.addAll(create_underneath_points(points2, 1f, 0f, true));
        i = points2.size -2;
        cp = new Vector2(points2.get(i).x, points2.get(i). y - 100);
        points3.add(cp);

        //create points4
        Array<Vector2> points4 = new Array<Vector2>();
        i = 0;
        cp = new Vector2(points.get(i).x - .1f, points3.get(i). y );
        points4.add(cp);
        points4.addAll(create_underneath_points(points3, 1.52f, .4f, true));
        i = points3.size -2;
        cp = new Vector2(points3.get(i).x, points3.get(i). y - 100);
        points4.add(cp);

        i = 0;



        //create polygonSprites




        float scalar = 35;


        //polygon ground1 sprites
        for (Vector2 point : points2){
            point.scl(scalar);
        }
        PolygonRegion poly = new PolygonRegion(new Sprite(downGroundSp1),
                                                  convert_to_floatList(points2),
                                                  triangulator.computeTriangles(convert_to_floatList(points2)).toArray());
        PolygonSprite polySprite = new PolygonSprite(poly);
        polySprite.setSize(polySprite.getWidth()/scalar, polySprite.getHeight()/scalar);
        polyDown1s.add(polySprite);
        for (Vector2 point : points2){
            point.scl(1/scalar);
        }



        //polygon ground2 sprites
        for (Vector2 point : points3){
            point.scl(scalar);
        }
        poly = new PolygonRegion(new Sprite(downGroundSp2),
                convert_to_floatList(points3),
                triangulator.computeTriangles(convert_to_floatList(points3)).toArray());
        polySprite = new PolygonSprite(poly);
        polySprite.setSize(polySprite.getWidth()/scalar, polySprite.getHeight()/scalar);
        polyDown2s.add(polySprite);
        for (Vector2 point : points3){
            point.scl(1/scalar);
        }

        //polygon ground3 sprites
        for (Vector2 point : points4){
            point.scl(scalar);
        }
        poly = new PolygonRegion(new Sprite(downGroundSp3),
                convert_to_floatList(points4),
                triangulator.computeTriangles(convert_to_floatList(points4)).toArray());
        polySprite = new PolygonSprite(poly);
        polySprite.setSize(polySprite.getWidth()/scalar, polySprite.getHeight()/scalar);
        polyDown3s.add(polySprite);
        for (Vector2 point : points4){
            point.scl(1/scalar);
        }



        //polyUp Sprites
        Array<Vector2> add_points = new Array<Vector2>(points.toArray());
        points = new Array<Vector2>();
        points.add(new Vector2(add_points.get(0).x, add_points.get(0).y - 100));
        points.addAll(add_points);
        points.add(new Vector2(add_points.get(add_points.size - 1).x, add_points.get(add_points.size -1).y - 100));

        for (Vector2 point : points){
            point.scl(scalar);
        }
        poly = new PolygonRegion(new TextureRegion(upGroundSp),
                                 convert_to_floatList(points),
                                 triangulator.computeTriangles(convert_to_floatList(points)).toArray());
        polySprite = new PolygonSprite(poly);
        polySprite.setSize(polySprite.getWidth()/scalar, polySprite.getHeight()/scalar);
        polyUps.add(polySprite);
        for (Vector2 point : points){
            point.scl(1/scalar);
        }


        add_to_polyline(points);
        add_to_polyline(points2);
    }

    private void add_to_polyline(Array<Vector2> points){
        Array<Vector2> polyPoints = new Array<Vector2>(points);
        polyPoints.removeIndex(0);
        polyPoints.removeIndex(polyPoints.size - 1);
        rectangles.add(convert_to_floatList(polyPoints));


    }


    private void make_point_packages(Array<Vector2> allPoints, java.lang.String pointKind){
    }

    public void add_Shape(PolylineMapObject object, Vector2 upsetPoint){
        java.lang.String name = object.getName();

        Array<Vector2> points = convert_to_Vector2(object);


        Vector2 point_with_smalest_x = new Vector2(-9999, 0);//can't go smaller -9999
        float last_x;

        for (Vector2 point : points){
            point.x += pointAtVerryEnd.x - (.5f*upsetPoint.x/ChainsawRun.PPM);
            point.y += pointAtVerryEnd.y - (.5f*upsetPoint.y/ChainsawRun.PPM);
        }

        create_polygons(points);



        for (Vector2 point: points){
            allPoints.add(point);

            if (name == "start") {
                startPoints.add(point);
            }
            if (name == "end") {
                //endPoints.add(point);
            }
            if (name == "n") {
                //connectPoints.add(point);
            }

        }


    }

    public void update(float dt){
    }


    public void updateEndPoint(){


        for (Vector2 point : allPoints) {
            if (pointAtVerryEnd.x < point.x) {
                pointAtVerryEnd = point;
            }
        }
    }


    public void removeAllBehindDistance(float distance){  //not all polygons are being removed yet. CHANGE THAT!! IMPORTANT!!!

        System.out.println("WARNING: removing polygonSprites: not all polygonSprites are being removed");

        int i = 0;
        float playerX = screen.getPlayer().b2dbody.getPosition().x;

        for(PolygonSprite plr: polyUps){
            if(playerX >= plr.getVertices()[0] + distance) {
                polyUps.removeIndex(i);
                i--;
            }
            i++;
        }

        i = 0;

        for(PolygonSprite plr: polyDown1s){
            if(playerX >= plr.getVertices()[0] + distance) {
                polyDown1s.removeIndex(i);
                i--;
            }

            i++;
        }

        i= 0;

        for(PolygonSprite plr: polyDown2s){
            if(playerX >= plr.getVertices()[0] + distance) {
                polyDown2s.removeIndex(i);
                i--;
            }

            i++;
        }

        i= 0;

        for(PolygonSprite plr: polyDown3s){
            if(playerX >= plr.getVertices()[0] + distance) {
                polyDown3s.removeIndex(i);
                i--;
            }

            i++;
        }

        i= 0;




        for(Vector2 point : allPoints){
            if(playerX >= point.x + distance) {
                allPoints.removeIndex(i);
                i--;
            }

            i ++;
        }

        i = 0;

        for (float[] rect : rectangles){
            if(playerX >= rect[0] + distance) {
                rectangles.removeIndex(i);
                i--;
            }

            i ++;
        }



    }

    public void draw_polygons(PolygonSpriteBatch sb){


        for (PolygonSprite plr : polyUps){
            plr.draw(sb);
        }



        //downgrounds
        for(PolygonSprite plr: polyDown1s){
            plr.draw(sb);
        }
        for(PolygonSprite plr: polyDown2s){
            plr.draw(sb);
        }
        for(PolygonSprite plr: polyDown3s){
            plr.draw(sb);
        }



        //sb.draw_polygons(upGroundSp, 1, 1);
        /*for (int i = 0; i < allPoints.size; i++){
            if(allPoints.size >= i +1 +1);
        //        shapeRenderer.line(allPoints.get(i), allPoints.get(i+1));
        }
        float[] list = convert_to_floatList(allPoints);*/

        //upGroundSp.draw_polygons(sb);
        //sb.draw_polygons(upGround, 0, 0);
        //shapeRenderer.end();


    }


    public void draw_polylines(){
        shapeRenderer.setProjectionMatrix(screen.getGamecam().combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0,0,0 ,1);

        for (float[] rectangle : rectangles){
            if (rectangle.length >= 3) {
                shapeRenderer.polyline(rectangle);
                //shapeRenderer.polygon(rectangle);
            }
        }

        shapeRenderer.end();



    }



    private Array<Vector2> create_underneath_points(Array<Vector2>  startPoints_origial, float distance, float deviation, boolean remove_first_and_last_point){

        Array<Vector2> underneath_points = new Array<Vector2>();
        Array<Vector2> startPoints = new Array<Vector2>(startPoints_origial);


        if(remove_first_and_last_point){
            startPoints.removeIndex(0);
            startPoints.removeIndex(startPoints.size - 1);
        }



        float h = distance * -1;
        float d1;
        float d2;
        float m1;
        float m2;
        float a1;
        float a2;
        Vector2 p1;
        Vector2 p2;
        Vector2 p3;
        Vector2 cp;
        float deviation_instance;
        float deviation_distance;
        float last_devitation_instance = 0;
        int i = 0;

        ////System.out.print(startPoints.get(i) + "     1|    " + "\n");

        cp = new Vector2(startPoints.get(i).x, startPoints.get(i).y + h);
        underneath_points.add(cp);
        ////System.out.print(points.get(i) + "     1|    " + cp + "\n");


        for (i = 1; i<startPoints.size - 1; i++){


            p1 = new Vector2(startPoints.get(i-1));
            p2 = new Vector2(startPoints.get(i));
            p3 = new Vector2(startPoints.get(i+1));


            float negator = -1;
            if(Math.random() > .5f)
                negator = 1;

            if(Math.abs((double)(p1.x - p2.x)) > .3f) {
                deviation_instance = (float) Math.random() * negator * deviation;
                last_devitation_instance = deviation_instance;
            }else{
                deviation_instance = last_devitation_instance;
            }

            deviation_distance = deviation_instance * distance;

            h = (-1 * distance) + deviation_distance;
            cp = new Vector2();
            m1 = (p2.y - p1.y) / (p2.x - p1.x);     //m1 = gradient of line[p1, p2]
            m2 = (p3.y - p2.y) / (p3.x - p2.x);     //m1 = gradient of line[p2, p3]
            //if (m2 >= 7 || m1 >= 7) { //so that d doesn't become to large
                //underneath_points.add(new Vector2(p2.x - h, p2.y + h));
                // //System.out.println("gradient to high");
            //}else {
                ////System.out.println("gradient low enough");
                d1 = -h * (float) Math.sqrt(1 + m1 * m1);
                d2 = -h * (float) Math.sqrt(1 + m2 * m2);
                a1 = m1 * p1.x - p1.y;
                a2 = m2 * p2.x - p2.y;
                //ORIGINAL: cp.x = (a1 + d1 - a2 - d2) / (m2 -m1);
                cp.x = -((a1 + d1 - a2 - d2) / (m2 - m1));
                cp.y = -(m1 * -cp.x + a1 + d1);

                underneath_points.add(cp);
            //}


        }
        underneath_points.add(new Vector2(startPoints.get(i).x, startPoints.get(i).y + h));


        return underneath_points;
    }





    private float[] convert_to_floatList(Array<Vector2> array){
        float[] list = new float[array.size*2];
        int count = 0;
        for (Vector2 point: array){
            list[count] = point.x;
            count++;
            list[count] = point.y;
            count++;
        }



        return list;
    }

    private short[] convert_to_shortList(ShortArray array){
        short[] list = new short[array.size];
        for (int i = 0; i < array.size; i++){
            list[i] = array.get(i);
        }
        return list;
    }

    private Array<Vector2> convert_to_Vector2(PolylineMapObject object){
        Array<Vector2> points = new Array<Vector2>();
        Vector2 currentVec2 = new Vector2();


        float[] shape_vetecies = object.getPolyline().getTransformedVertices();

        for (int index = 0; index < object.getPolyline().getTransformedVertices().length/2; index++){
            currentVec2 = new Vector2();
            currentVec2.x = PhysicsCalculator.toUnits(shape_vetecies[index * 2]);
            currentVec2.y = PhysicsCalculator.toUnits(shape_vetecies[index * 2 + 1]);

            // //System.out.print(currentVec2.x + "    " + currentVec2.y + "      ");


            points.add(currentVec2);

            /*if (index >= 1)
                //System.out.print(points.get(index-1));
            //System.out.print("\n");*/
        }

        return points;
    }

    public void remove(){

    }

    public void dispose(){
        //pixmap.dispose();
    }

    public Vector2 get_PointAtTheVerryEnd(){return pointAtVerryEnd;}


}









/*Dynamic Ground:
    Date of last change: 6.August 2017


 package com.dani.game2.Animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.RepeatablePolygonSprite;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ShortArray;
import com.dani.game2.ChainsawRun;
import com.dani.game2.Screens.PlayScreen;
import com.dani.game2.tools.PhysicsCalculator;

/**
 * Created by root on 27.11.16.
 */
/*
public class DynamicGround{

    public PlayScreen screen;

    public ShapeRenderer shapeRenderer;
    //public Pixmap pixmap;


    private EarClippingTriangulator triangulator;

    private Array<Vector2> startPoints;
    private Array<Vector2> connectPoints;
    private Array<Vector2> endPoints;
    private Array<Vector2> allPoints;

    private Array<float[]> rectangles; //

    private Texture upGround;
    private Texture downGround;
    private Sprite upGroundSp;
    private Sprite downGroundSp;
    private Sprite gradient;

    float tempX;
    float tempY;
    float tempWidth;
    float Height;
    float tempRotation;

    private TextureRegion testRegion;

    private Array<PolygonSprite> poly1s;
    //private Array<PolygonRegion> poly1s;
    private Array<PolygonSprite> poly2s;
    private Array<PolygonSprite> poly2_gradient;
    private Array<PolygonRegion> poly3s;
    private Array<PolygonRegion> poly4s;
    private Array<PolygonRegion> poly5s;

    private Array<Vector2> oldPoints;

    private Vector2 pointAtVerryEnd;
    private Vector2 lastGradPos;

    private float biggestX = 0;

    public DynamicGround(PlayScreen screen){
        Height = 10;
        this.screen = screen;

        triangulator = new EarClippingTriangulator();
        shapeRenderer = new ShapeRenderer();
        rectangles = new Array<float[]>();

        startPoints = new Array<Vector2>();
        connectPoints = new Array<Vector2>();
        endPoints = new Array<Vector2>();
        allPoints = new Array<Vector2>();
        poly1s = new Array<PolygonSprite>();
        poly2s = new Array<PolygonSprite>();
        poly2_gradient = new Array<PolygonSprite>();


        upGround = new Texture("animations/ground/upGround.png");
        upGround.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);



        downGround = new Texture("animations/ground/downGround.png");
        downGround.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.MirroredRepeat);
        downGroundSp = new Sprite(downGround);
        downGroundSp.flip(false, true);
        //downGround.dispose();

        gradient = new Sprite(new Texture(Gdx.files.internal("animations/ground/gradient.png")));


        upGroundSp = new Sprite(upGround);
        upGroundSp.scale(10000f);

        //System.out.println("new oldPoint");
        oldPoints = new Array<Vector2>();
        oldPoints.add(new Vector2(0,0));

        pointAtVerryEnd = new Vector2(0,0);
        lastGradPos = new Vector2(0,0);
        //create_polygons();
        //upGround = new Texture(create_pixmap());
//        upGroundSp = new Sprite(upGround);
        //      upGroundSp.setSize(10,10);
        //     upGroundSp.setPosition(0, 0);


        //load_polylines();
    }

    private void create_polygons(Array<Vector2> points_uneddited) {
        ////System.out.print("start: create_polygons\n");

        //test
        //Array<Vector2> testArray= new Array<Vector2>();
        //testArray.add(new Vector2(points_uneddited.get(0).x - .3f, points_uneddited.get(0).y + .0f ));
        //testArray.addAll(points_uneddited);
        //oints_uneddited = testArray;
        //test

        Array<Vector2> points = new Array<Vector2>();
        points.addAll(points_uneddited.toArray());


        float pd1;
        float pd2;
        boolean setToOldpoint = false;
        Vector2 setOldPoint = new Vector2();

        float smallestX = points.get(0).x;
        Vector2 beginningPoint = points.get(0);


        int beginnPointIndex = 0;
        int i = 0;
        for (Vector2 point: points){
            if (point.x < smallestX){
                smallestX = point.x;
                beginningPoint = point;
                beginnPointIndex = i;
                ////System.out.println("bregginging point index:  " + i);
                ////System.out.println("            smallestX: "+ smallestX + " beginningpointindex:  " + beginnPointIndex);
            }
            i ++;
        }

        //beginnPointIndex = 0;//remove later
        //beginningPoint = points.get(0); //remove later

        for (Vector2 oldPoint : oldPoints) {
            pd1 = Math.abs(oldPoint.y - beginningPoint.y);
            pd2 = Math.abs(oldPoint.x - beginningPoint.x);


            if (pd1 < 1f && pd2 < 1f) {
                ////System.out.println("      set to oldPoint    normalPoint: " + points.get(beginnPointIndex) + "   |  oldPoint: " + oldPoint);
                setToOldpoint = true;
                setOldPoint = new Vector2(oldPoint);
                points.get(beginnPointIndex).set(setOldPoint.x, setOldPoint.y);
                break;


            }

            ////System.out.println("      set not to oldPoint    " + points.get(0) + "   |  " + oldPoint);
            ////System.out.println();

        }

        float largestX = -999;
        Vector2 endPoint = new Vector2();
        for (Vector2 point: points){
            if (point.x > largestX){
                // //System.out.println("                new smallest x found");
                largestX = point.x;
                endPoint = point;
            }
        }


        oldPoints.add(endPoint);
        //oldPoints.add(beginningPoint);

        Array<Vector2> points2 = new Array<Vector2>();

        i = 0;
        final float h = -.26f;
        float d1;
        float d2;
        float m1;
        float m2;
        float a1;
        float a2;
        Vector2 p1;
        Vector2 p2;
        Vector2 p3;
        Vector2 cp;

        cp = new Vector2(points.get(i).x - .1f, points.get(i). y - 100);
        points2.add(cp);
        ////System.out.print(points.get(i) + "     1|    " + "\n");

        cp = new Vector2(points.get(i).x, points.get(i).y + h);
        points2.add(cp);
        ////System.out.print(points.get(i) + "     1|    " + cp + "\n");

        boolean gradientToHigh;
        for (i = 1; i<points.size - 1; i++){
            p1 = new Vector2(points.get(i-1));
            p2 = new Vector2(points.get(i));
            p3 = new Vector2(points.get(i+1));
            cp = new Vector2();
            m1 = (p2.y - p1.y) / (p2.x - p1.x);     //m1 = gradient of line[p1, p2]
            m2 = (p3.y - p2.y) / (p3.x - p2.x);     //m1 = gradient of line[p2, p3]
            if (m2 >= 7 || m1 >= 7) { //so that d doesn't become to large
                points2.add(new Vector2(p2.x - h, p2.y + h));
                // //System.out.println("gradient to high");
            }else {
                ////System.out.println("gradient low enough");
                d1 = -h * (float) Math.sqrt(1 + m1 * m1);
                d2 = -h * (float) Math.sqrt(1 + m2 * m2);
                a1 = m1 * p1.x - p1.y;
                a2 = m2 * p2.x - p2.y;
                //ORIGINAL: cp.x = (a1 + d1 - a2 - d2) / (m2 -m1);
                cp.x = -((a1 + d1 - a2 - d2) / (m2 - m1));
                cp.y = -(m1 * -cp.x + a1 + d1);

                points2.add(cp);
            }


            ////System.out.print(p2 + "     2|    " + cp + "\n");
        }

        //points2.add(new Vector2(-10,-10));

        i = points.size - 1;
        cp = new Vector2(points.get(i).x, points.get(i).y + h);
        points2.add(cp);
        ////System.out.print(points.get(i) + "     3|    " + cp + "\n");


        cp = new Vector2(points.get(i).x, points.get(i). y - 100);
        points2.add(cp);
        ////System.out.print(points.get(i) + "     3|    " + cp + "\n");
        i = 0;
        //points2.removeIndex(1);

        float scalar = 35;


        for (Vector2 point : points2){
            point.scl(scalar);
        }

        PolygonRegion poly = new PolygonRegion(new Sprite(downGroundSp),
                convert_to_floatList(points2),
                triangulator.computeTriangles(convert_to_floatList(points2)).toArray());
        PolygonSprite polySprite = new PolygonSprite(poly);
        polySprite.setSize(polySprite.getWidth()/scalar, polySprite.getHeight()/scalar);


        poly2s.add(polySprite);

        for (Vector2 point : points2){
            point.scl(1/scalar);
        }



        Array<Vector2> gradient_points = new Array<Vector2>();
        /*poly = new PolygonRegion(new Sprite(gradient),
                convert_to_floatList(gradient_points),
                triangulator.computeTriangles(convert_to_floatList(points2)).toArray());
        polySprite = new PolygonSprite(poly);
        polySprite.setSize(polySprite.getWidth()/scalar, polySprite.getHeight()/scalar);


        for (Vector2 grad_point : gradient_points){
            grad_point.scl(1 / scalar);
        }




        points_uneddited.removeIndex(0);

        Array<Vector2> add_points = new Array<Vector2>(points.toArray());

        points = new Array<Vector2>();

        points.add(new Vector2(add_points.get(0).x, add_points.get(0).y - 100));
        points.addAll(add_points);
        points.add(new Vector2(add_points.get(add_points.size - 1).x, add_points.get(add_points.size -1).y - 100));

        //if(setToOldpoint) points.get(1).set(new Vector2(setOldPoint.x, setOldPoint.y - 10));




        //scalar = 100;
        for (Vector2 point : points){
            point.scl(scalar);
        }
        poly = new PolygonRegion(new TextureRegion(upGround),
                convert_to_floatList(points),
                triangulator.computeTriangles(convert_to_floatList(points)).toArray());
        polySprite = new PolygonSprite(poly);
        polySprite.setSize(polySprite.getWidth()/scalar, polySprite.getHeight()/scalar);
        poly1s.add(polySprite);
        for (Vector2 point : points){
            point.scl(1/scalar);
        }

        //poly1s.add(poly);
        ////System.out.print("end: create_polygons\n");

        add_to_polyline(points);
        add_to_polyline(points2);
    }

    private void add_to_polyline(Array<Vector2> points){
        Array<Vector2> polyPoints = new Array<Vector2>(points);
        polyPoints.removeIndex(0);
        polyPoints.removeIndex(polyPoints.size - 1);
        rectangles.add(convert_to_floatList(polyPoints));


    }


    private void make_point_packages(Array<Vector2> allPoints, java.lang.String pointKind){
    }

    public void add_Shape(PolylineMapObject object, Vector2 upsetPoint){
        java.lang.String name = object.getName();


        for (Vector2 point: points){
            allPoints.add(point);

            if (name == "start") {
                startPoints.add(point);
            }
            if (name == "end") {
                //endPoints.add(point);
            }
            if (name == "n") {
                //connectPoints.add(point);
            }

        }


    }

    public void update(float dt){
    }


    public void updateEndPoint(){


        for (Vector2 point : allPoints) {
            if (pointAtVerryEnd.x < point.x) {
                pointAtVerryEnd = point;
            }
        }
    }


    public void removeAllBehindDistance(float distance){
        int i = 0;
        float playerX = screen.getPlayer().b2dbody.getPosition().x;

        for(PolygonSprite plr: poly1s){
            if(playerX >= plr.getVertices()[0] + distance) {
                poly1s.removeIndex(i);
                i--;
            }
            i++;
        }

        i = 0;

        for(PolygonSprite plr: poly2s){
            if(playerX >= plr.getVertices()[0] + distance) {
                poly2s.removeIndex(i);
                i--;
            }

            i++;
        }

        i= 0;

        for(PolygonSprite plr : poly2_gradient){
            if(playerX >= plr.getX() * 35 + distance){
                poly2_gradient.removeIndex(i);
                i --;
            }

            i ++;
        }


        i = 0;

        for(Vector2 point : allPoints){
            if(playerX >= point.x + distance) {
                allPoints.removeIndex(i);
                i--;
            }

            i ++;
        }

        i = 0;

        for (float[] rect : rectangles){
            if(playerX >= rect[0] + distance) {
                rectangles.removeIndex(i);
                i--;
            }

            i ++;
        }



    }

    public void draw_polygons(PolygonSpriteBatch sb){


        for (PolygonSprite plr : poly1s){
            plr.draw(sb);
        }




        //downgrounds
        for(PolygonSprite plr: poly2s){
            plr.draw(sb);
        }

        for (PolygonSprite plr : poly2_gradient){
            plr.draw(sb);
        }



        //sb.draw_polygons(upGroundSp, 1, 1);
        /*for (int i = 0; i < allPoints.size; i++){
            if(allPoints.size >= i +1 +1);
        //        shapeRenderer.line(allPoints.get(i), allPoints.get(i+1));
        }
        float[] list = convert_to_floatList(allPoints);*/

        //upGroundSp.draw_polygons(sb);
        //sb.draw_polygons(upGround, 0, 0);
        //shapeRenderer.end();

/*
    }


    public void draw_polylines(){
        shapeRenderer.setProjectionMatrix(screen.getGamecam().combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0,0,0 ,1);

        for (float[] rectangle : rectangles){
            if (rectangle.length >= 3) {
                shapeRenderer.polyline(rectangle);
                //shapeRenderer.polygon(rectangle);
            }
        }

        shapeRenderer.end();



    }

    private float[] convert_to_floatList(Array<Vector2> array){
        float[] list = new float[array.size*2];
        int count = 0;
        for (Vector2 point: array){
            list[count] = point.x;
            count++;
            list[count] = point.y;
            count++;
        }



        return list;
    }

    private short[] convert_to_shortList(ShortArray array){
        short[] list = new short[array.size];
        for (int i = 0; i < array.size; i++){
            list[i] = array.get(i);
        }
        return list;
    }

    private Array<Vector2> convert_to_Vector2(PolylineMapObject object){
        Array<Vector2> points = new Array<Vector2>();
        Vector2 currentVec2 = new Vector2();


        float[] shape_vetecies = object.getPolyline().getTransformedVertices();

        for (int index = 0; index < object.getPolyline().getTransformedVertices().length/2; index++){
            currentVec2 = new Vector2();
            currentVec2.x = PhysicsCalculator.toUnits(shape_vetecies[index * 2]);
            currentVec2.y = PhysicsCalculator.toUnits(shape_vetecies[index * 2 + 1]);

            // //System.out.print(currentVec2.x + "    " + currentVec2.y + "      ");


            points.add(currentVec2);

            //if (index >= 1)
                //System.out.print(points.get(index-1));
            //System.out.print("\n");
        }

        return points;
    }

    public void remove(){

    }

    public void dispose(){
        //pixmap.dispose();
    }

    public Vector2 get_PointAtTheVerryEnd(){return pointAtVerryEnd;}


}

*/






























































//Verry Old Stuff

/*Array<Vector2> points2 = new Array<Vector2>();

        int i = 0;
        final float h = .3f;
        float d1;
        float d2;
        float m1;
        float m2;
        float a1;
        float a2;
        Vector2 p1;
        Vector2 p2;
        Vector2 p3;
        Vector2 cp;

        p1 = new Vector2(points.get(i));
        p2 = new Vector2(points.get(i + 1));
        m1 = (p2.y - p1.y) / (p2.x - p1.x );
        d1 = -1 * h * (float)Math.sqrt(1 + m1);
        cp = new Vector2();
        cp.x = p1.x;
        cp.y = p1.y - d1;
        points2.add(cp);
        //System.out.print(p1 + "     |    " + cp + "\n");

        for (i = 1; i<points.size - 1; i++){
            p1 = new Vector2(points.get(i-1));
            p2 = new Vector2(points.get(i));
            p3 = new Vector2(points.get(i+1));
            cp = new Vector2();
            m1 = (p2.y - p1.y) / (p2.x - p1.x);
            m2 = (p3.y - p2.y) / (p3.x - p2.x);
            d1 = h * (float)Math.sqrt(1 + m1*m1);
            d2 = h * (float)Math.sqrt(1 + m2*m2);
            a1 = m1 * p1.x - p1.y;
            a2 = m2 * p2.x - p2.y;
            //ORIGINAL: cp.x = (a1 + d1 - a2 - d2) / (m2 -m1);
            cp.x = -((a1 + d1 - a2 - d2) / (m2 -m1));
            cp.y = -(m1 * cp.x + a1 + d1);
            points2.add(cp);
            //System.out.print(p2 + "     |    " + cp + "\n");
        }

        i = points.size - 1;
        p2 = new Vector2(points.get(i -1));
        p3 = new Vector2(points.get(i));
        cp = new Vector2();
        m2 = (p3.y - p2.y) / (p3.x - p2.x);
        d1 = -1 * h * (float)Math.sqrt(1 + m2*m2);
        cp.x = p3.x;
        cp.y = p3.y - d1;
        points2.add(cp);
        //System.out.print(p3 + "     |    " + cp + "\n");

/*float d = 7;
        float[] hans = new float[10];
        hans[0] = 0.875f/d;
        hans[1] = -7f/d;
        hans[2] = 10/d;
        hans[3] = 18f/d;
        hans[4] = 18f/d;
        hans[5] = 15f/d;
        hans[6] = 44f/d;
        hans[7] = 18f/d;
        hans[8] = 51f/d;
        hans[9] = -10/d;*/







//points2.add(new Vector2(0,0));

        /*i = points.size - 1;
        p2 = new Vector2(points.get(i -1));
        p3 = new Vector2(points.get(i));
        cp = new Vector2();
        m2 = (p3.y - p2.y) / (p3.x - p2.x);
        d1 = -1 * h * (float)Math.sqrt(1 + m2*m2);
        cp.x = p3.x;
        cp.y = p3.y - d1;
        points2.add(cp);
        //System.out.print(p3 + "     |    " + cp + "\n");
        /*p1 = new Vector2(points.get(i-1));
        p2 = new Vector2(points.get(i));
        p3 = new Vector2(points.get(i).x +1, points.get(i).y);
        cp = new Vector2();
        m1 = (p2.y - p1.y) / (p2.x - p1.x);
        m2 = (p3.y - p2.y) / (p3.x - p2.x);
        d1 = -h * (float)Math.sqrt(1 + m1*m1);
        d2 = -h * (float)Math.sqrt(1 + m2*m2);
        a1 = m1 * p1.x - p1.y;
        a2 = m2 * p2.x - p2.y;
        //ORIGINAL: cp.x = (a1 + d1 - a2 - d2) / (m2 -m1);
        cp.x = -((a1 + d1 - a2 - d2) / (m2 -m1));
        cp.y = -(m1 * -cp.x + a1 + d1);*/






        /*p1 = new Vector2(points.get(i).x - 1, points.get(i).y);
        p2 = new Vector2(points.get(i));
        p3 = new Vector2(points.get(i + 1));
        cp = new Vector2();
        m1 = (p2.y - p1.y) / (p2.x - p1.x);
        m2 = (p3.y - p2.y) / (p3.x - p2.x);
        d1 = -h * (float)Math.sqrt(1 + m1*m1);
        d2 = -h * (float)Math.sqrt(1 + m2*m2);
        a1 = m1 * p1.x - p1.y;
        a2 = m2 * p2.x - p2.y;
        //ORIGINAL: cp.x = (a1 + d1 - a2 - d2) / (m2 -m1);
        cp.x = -((a1 + d1 - a2 - d2) / (m2 -m1));
        cp.y = -(m1 * -cp.x + a1 + d1);*/









