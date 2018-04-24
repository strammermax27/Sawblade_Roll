package com.dani.game2.Levels;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.dani.game2.Animation.DynamicGround;
import com.dani.game2.ChainsawRun;
import com.dani.game2.Screens.PlayScreen;
import com.dani.game2.Sprites.DeadlyObjects.SawBlade;
import com.dani.game2.Sprites.Enemys.Bee;
import com.dani.game2.Sprites.Enemys.Big_Goat;
import com.dani.game2.Sprites.Enemys.Dog;
import com.dani.game2.Sprites.Enemys.Enemy;
import com.dani.game2.Sprites.Enemys.Rabbit;
import com.dani.game2.tools.PhysicsCalculator;

import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;


/**
 * Created by root on 17.07.16.
 */
public class B2worldcreator {
    private Array<Enemy> enemys;

    private PlayScreen screen;
    private DynamicGround dynamicGround;

    private Array<SawBlade> sawblades;

    private Array<Body> ground_bodies;

    private Array<Vector2> oldPoints;

    public Array<SawBlade> getSawblades() {
        return sawblades;
    }

    private TiledMap lastFragment;
    private TiledMap currentFragment;
    private TiledMap nextFragment;
    private Vector2 last_position;

    private World world;
    private TiledMap map;

    public B2worldcreator(PlayScreen screen, ChainsawRun chainsawRun) {
        this.screen = screen;
        this.dynamicGround = screen.dynamicGround;
        world = screen.getWorld();
        map = screen.getMap();
        enemys = new Array<Enemy>();
        sawblades = new Array<SawBlade>();
        ground_bodies = new Array<Body>();

        last_position = new Vector2(0,0);

        oldPoints = new Array<Vector2>();
    }

    public void load_map() {
        //if (Gdx.app.getType() == Application.ApplicationType.Desktop)
            //System.out.println("                mapSize: " + ObjectSizeCalculator.getObjectSize(map));

        Shape shape;
        PolygonShape polygon;
        Rectangle rect;
        ChainShape chain;

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        Body body;



        int ground = 2;
        int rabbit = 3;
        int sawblade = 4;
        int invisible = 5;
        int bigGoat = 7;
        int dog = 8;
        int bee = 9;
        int obstacle = 60;
        int experimental = 70;

        for (int i = 1; i <= map.getLayers().getCount() - 1; i++) {
            ////System.out.print(i + "\n\n");
            for (MapObject object : map.getLayers().get(i).getObjects()) {
                //rect = ((RectangleMapObject) object).getRectangle();
                shape = ShapeFactory.getShape(object, false);
                if (shape == null) {
                    continue;
                }
                ////System.out.print(i);

                if (i == ground) {
                    ////System.out.print("i == ground \n");
                    Vector2 position = new Vector2(0, 0);
                    boolean hasShape = false;
                    //PolygonShape polygon;
                    ////System.out.print(polygon);
                    if (object instanceof RectangleMapObject) {

                        rect = ((RectangleMapObject) object).getRectangle();
                        position = new Vector2((PhysicsCalculator.toUnits(rect.getX())), (PhysicsCalculator.toUnits(rect.getY())));
                        rect.setPosition(new Vector2(0, 0));
                        polygon = ShapeFactory.getRectangle(rect);
                        fdef.shape = polygon;
                        hasShape = true;


                    } else if (object instanceof PolygonMapObject) {
                        position.x = PhysicsCalculator.toUnits(((PolygonMapObject) object).getPolygon().getX());
                        position.y = PhysicsCalculator.toUnits(((PolygonMapObject) object).getPolygon().getY());
                        polygon = ShapeFactory.getPolygon((PolygonMapObject) object);
                        //((PolygonMapObject)object).getPolygon().setOrigin(0,0);
                        position.x *= (1 / ChainsawRun.PPM);
                        position.y *= 1 / ChainsawRun.PPM;
                        hasShape = true;
                        fdef.shape = polygon;
                        ////System.out.print("spast\n");
                    } else if (object instanceof PolylineMapObject) {
                        screen.groundShapes.add((PolylineMapObject) object);
                        dynamicGround.add_Shape((PolylineMapObject) object, new Vector2(0,0));
                        ////System.out.print("\n");
                        position = new Vector2((((PolylineMapObject) object).getPolyline().getOriginX()), ((PolylineMapObject) object).getPolyline().getOriginY());
                        position.scl(ChainsawRun.PPM);
                        chain = ShapeFactory.getPolyline((PolylineMapObject) object, true);
                        fdef.shape = chain;
                        hasShape = true;
                    }


                    ////System.out.print(hasShape + "\n");
                    if (hasShape) {
                        bdef.type = BodyDef.BodyType.StaticBody;
                        bdef.position.set(position);
                        //bdef.position =

                        body = world.createBody(bdef);


                        fdef.filter.categoryBits = ChainsawRun.GROUND_BIT;
                        body.createFixture(fdef);
                    }
                    ////System.out.print(body.getPosition() + "\n");
                }

                if (i == dog) {


                    ////System.out.println("i == DOG");
                    Vector2 position = ((RectangleMapObject) object).getRectangle().getPosition(new Vector2(0, 0));
                    enemys.add(new Dog(screen, position.x / 2, position.y / 2));

                }
                if (i == bee) {
                    ////System.out.println("i == BEE");

                    Vector2 position = ((RectangleMapObject) object).getRectangle().getPosition(new Vector2(0, 0));
                    enemys.add(new Bee(screen, position.x / 2, position.y / 2));

                }
                if (i == bigGoat) {
                    ////System.out.println("i == bigGoat");
                    Vector2 position = ((RectangleMapObject) object).getRectangle().getPosition(new Vector2(0, 0));
                    enemys.add(new Big_Goat(screen, position.x / 2, position.y / 2));
                }

                if (i == rabbit) {
                    ////System.out.print("i == rabbit\n");

                    Vector2 position = ((RectangleMapObject) object).getRectangle().getPosition(new Vector2(0, 0));
                    enemys.add(new Rabbit(screen, position.x / 2, position.y / 2));
                }

                if (i == sawblade) {

                    Ellipse ellipse = ((EllipseMapObject) object).getEllipse();
                    Vector2 sawbladeposition = new Vector2(PhysicsCalculator.toUnits(ellipse.x + ellipse.width / 2), PhysicsCalculator.toUnits(ellipse.y + ellipse.height / 2));

                    sawblades.add(new SawBlade(screen, shape, sawbladeposition, (MapObject) object));
                }

                if (i == invisible) {
                    ////System.out.print("i == ground \n");
                    Vector2 position = new Vector2(0, 0);
                    boolean hasShape = false;
                    //PolygonShape polygon;
                    ////System.out.print(polygon);
                    if (object instanceof RectangleMapObject) {

                        rect = ((RectangleMapObject) object).getRectangle();
                        position = new Vector2((PhysicsCalculator.toUnits(rect.getX())), (PhysicsCalculator.toUnits(rect.getY())));
                        rect.setPosition(new Vector2(0, 0));
                        polygon = ShapeFactory.getRectangle(rect);
                        fdef.shape = polygon;
                        hasShape = true;


                    } else if (object instanceof PolygonMapObject) {
                        position.x = PhysicsCalculator.toUnits(((PolygonMapObject) object).getPolygon().getX());
                        position.y = PhysicsCalculator.toUnits(((PolygonMapObject) object).getPolygon().getY());
                        polygon = ShapeFactory.getPolygon((PolygonMapObject) object);
                        //((PolygonMapObject)object).getPolygon().setOrigin(0,0);
                        position.x *= (1 / ChainsawRun.PPM);
                        position.y *= 1 / ChainsawRun.PPM;
                        hasShape = true;
                        fdef.shape = polygon;
                        ////System.out.print("spast\n");
                    } else if (object instanceof PolylineMapObject) {
                        ////System.out.print("\n");
                        position = new Vector2((((PolylineMapObject) object).getPolyline().getOriginX()), ((PolylineMapObject) object).getPolyline().getOriginY());
                        position.scl(ChainsawRun.PPM);
                        chain = ShapeFactory.getPolyline((PolylineMapObject) object, false);
                        fdef.shape = chain;
                        hasShape = true;
                    }


                    ////System.out.print(hasShape + "\n");
                    if (hasShape) {
                        bdef.type = BodyDef.BodyType.StaticBody;
                        bdef.position.set(position);
                        //bdef.position =

                        body = world.createBody(bdef);


                        fdef.filter.categoryBits = ChainsawRun.GROUND_BIT;
                        body.createFixture(fdef);
                    }
                    ////System.out.print(body.getPosition() + "\n");

                }

                if (i == obstacle) {
                    ////System.out.print("i == Obstacle \n");
                    bdef.type = BodyDef.BodyType.StaticBody;
                    rect = ((RectangleMapObject) object).getRectangle();
                    bdef.position.set(PhysicsCalculator.toUnits(rect.getX()), PhysicsCalculator.toUnits(rect.getY()));

                    body = world.createBody(bdef);



                    rect.setPosition(new Vector2(0, 0));
                    polygon = ShapeFactory.getRectangle(rect);
                    //shape.setAsBox((rect.getWidth() / 2) / ChainsawRun.PPM, (rect.getHeight() / 2) / ChainsawRun.PPM);
                    fdef.shape = polygon;
                    fdef.filter.categoryBits = ChainsawRun.RABBIT_OBSTACLE_BIT;
                    body.createFixture(fdef);
                }




                /*if (i == coin){
                    if (rect.getY() + rect.getHeight() <= 90 && rect.getY() + rect.getHeight() >= 85){rect.setY(80 - rect.getHeight());}
                    new Coin(screen, object, chainsawRun);
                }

                if (i == brick){
                    if (rect.getY() + rect.getHeight() >= 65 && rect.getY() + rect.getHeight() <= 90){rect.setY(80 - rect.getHeight());}
                    new Brick(screen,object, chainsawRun);

                }



                if (i == pipe) {
                        bdef.type = BodyDef.BodyType.StaticBody;
                        bdef.position.set((rect.getX() + rect.getWidth() / 2) / ChainsawRun.PPM, (rect.getY() + rect.getHeight() / 2) / ChainsawRun.PPM);

                        body = world.createBody(bdef);

                        shape.setAsBox((rect.getWidth() / 2) / ChainsawRun.PPM, (rect.getHeight() / 2) / ChainsawRun.PPM);
                        fdef.shape = shape;
                        fdef.filter.categoryBits = ChainsawRun.RABBIT_OBSTACLE_BIT;
                        body.createFixture(fdef);
                    }*/
            }
        }
    }



    public Array<Enemy> getEnemys() {
        return enemys;
    }







    public void new_fragment(Fragment_Pack pack, float stateCount){
        remove_lastFragment();
        lastFragment = currentFragment;
        currentFragment = nextFragment;
        nextFragment = pack.getMap();
        add_nextFragment(pack.getStartPoint(), stateCount);
        dynamicGround.updateEndPoint();
        //last_position = dynamicGround.get_PointAtTheVerryEnd();
        last_position = dynamicGround.get_PointAtTheVerryEnd();
        screen.getFragment_manager().endXofCurrentFragment = last_position.x;

        remove_lastFragment();
        remove_enemys();


    }

    public void first_fragment(Fragment_Pack pack){
        remove_lastFragment();
        lastFragment = currentFragment;
        currentFragment = nextFragment;
        nextFragment = pack.getMap();
        add_nextFragment(pack.getStartPoint(), 1);
        dynamicGround.updateEndPoint();
        last_position = dynamicGround.get_PointAtTheVerryEnd();
//        screen.getFragment_manager().endXofCurrentFragment = last_position.x;

    }

    private void remove_lastFragment(){
        float distance = 170;
        float playerX = screen.getPlayer().b2dbody.getPosition().x;

        dynamicGround.removeAllBehindDistance(distance);


        for(Body body : ground_bodies){
            if (playerX >= body.getPosition().x + distance){
                ground_bodies.removeValue(body, true);
                ////System.out.println("start removing groundBodie");
                world.destroyBody(body);
                ////System.out.println("end removing groundBodie");
            }
        }

        /*for(Enemy enemy : enemys){
            if (playerX >= enemy.b2dbody.getPosition().x + distance){
                enemy.dispose();
                //System.out.println("removing Enemy");
            }
        }

        for (SawBlade sawBlade : sawblades){
            if(playerX >= sawBlade.getBody().getPosition().x + distance) {
                sawBlade.dispose();
                //System.out.println("removing sawBlade");
            }
        }*/


    }




    private void add_nextFragment(Vector2 FragmentUpset, float stateCount){
        //last_position;
        com.badlogic.gdx.physics.box2d.Shape shape;
        PolygonShape polygon;
        Rectangle rect;
        ChainShape chain;

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        Body body;

        enemys = screen.getEnemies();
        sawblades = new Array<SawBlade>();
        int ground = 2;
        int rabbit = 3;
        int sawblade = 4;
        int invisible = 5;
        int bigGoat = 7;
        int dog = 8;
        int bee = 9;
        int obstacle = 60;
        int experimental = 70;

        Vector2 startPos = new Vector2(last_position.x - (.5f*FragmentUpset.x/ChainsawRun.PPM), last_position.y - (.5f*FragmentUpset.y/ChainsawRun.PPM));

        //System.out.println("lastPosition: " + last_position);

        Array<MapObject> objects = sort_objects(nextFragment.getLayers().get(ground).getObjects());

        int is = 0;

        for (int i = 1; i <= nextFragment.getLayers().getCount() - 1; i++) {
            ////System.out.print(i + "\n\n");
            is = 0;
            for (MapObject object : nextFragment.getLayers().get(i).getObjects()) {
                //rect = ((RectangleMapObject) object).getRectangle();
                shape = ShapeFactory.getShape(object, false);

               if (shape == null) {
                   continue;
                }
                //System.out.print(i);

                if (i == ground) {
                    //System.out.print("i == ground \n");
                    object = objects.get(is);
                    Vector2 position = new Vector2(0, 0);
                    boolean hasShape = false;


                    if (object instanceof PolylineMapObject) {
                       // //System.out.println("       StratPos: " + startPos);


                        dynamicGround.add_Shape((PolylineMapObject) object, FragmentUpset);
                        ////System.out.print("\n");
                        position = new Vector2(((PolylineMapObject) object).getPolyline().getOriginX(), ((PolylineMapObject) object).getPolyline().getOriginY());
                        position.scl(ChainsawRun.PPM);
                        position.x += startPos.x;
                        position.y += startPos.y;
                        ShapeFactory.startPoint = position;
                        chain = ShapeFactory.getPolyline((PolylineMapObject) object, true);

                        fdef.shape = chain;
                        hasShape = true;
                    }


                    ////System.out.print(hasShape + "\n");
                    if (hasShape) {
                        bdef.type = BodyDef.BodyType.StaticBody;
                        bdef.position.set(position);
                        //bdef.position =

                        body = screen.getWorld().createBody(bdef);
                        ground_bodies.add(body);


                        fdef.filter.categoryBits = ChainsawRun.GROUND_BIT;



                        body.createFixture(fdef);
                    }
                    ////System.out.print(body.getPosition() + "\n");
                }



                else if (i == dog){
                    ////System.out.println("i == DOG");

                    float firstSpawn    = Float.parseFloat((String) object.getProperties().get("firstSpawn"));
                    float multiplicator = Float.parseFloat((String) object.getProperties().get("multiplicator"));
                    float normalCount    = Float.parseFloat((String) object.getProperties().get("normalCount"));

                    float multiply_times = stateCount - firstSpawn;
                    float dogCount;

                    ////System.out.println("    first_spawn: " + firstSpawn + " | multiplicator: " + multiplicator + " | normalCount: " + normalCount + " | multiplyTimes: " + multiply_times + " | stateCount: " + stateCount);

                    if (multiply_times > 0) {


                        dogCount = multiply_times * multiplicator * normalCount;
                        //ystem.out.println("        dogCount: " + dogCount);

                        for (int iter = 1; iter < dogCount; iter++) {
                            float rand = (float)Math.random() * 1f;

                            if (object instanceof RectangleMapObject) {
                                Vector2 position = new Vector2();
                                ((RectangleMapObject) object).getRectangle().getPosition(position);
                                position.scl(1 / ChainsawRun.PPM);
                                position.scl(.5f);
                                position.x += startPos.x + rand;
                                position.y += startPos.y;
                                position.scl(ChainsawRun.PPM);
                                enemys.add(new Dog(screen, position.x, position.y));
                                ////System.out.println("            dog_added");
                            } else
                                System.out.println("WARNING: Dog not instance of rectangle map object");

                        }
                    }

                }
                /*
                    BeePos: (1872.0,4388.0)
                   BeePos: (1984.0,4388.0)
                 */
                else if (i == bee){
                    ////System.out.println("i == BEE");
                    Vector2 position = new Vector2();
                    ((RectangleMapObject) object).getRectangle().getPosition(position);
                    position.scl(1/ChainsawRun.PPM);
                    position.scl(.5f);
                    position.x += startPos.x;
                    position.y += startPos.y;
                    position.scl(ChainsawRun.PPM);
                    enemys.add(new Bee(screen, position.x, position.y));

                }
                else if (i == bigGoat) {
                    ////System.out.println("i == bigGoat");
                    Vector2 position = new Vector2();
                    ((RectangleMapObject) object).getRectangle().getPosition(position);
                    position.scl(1/ChainsawRun.PPM);
                    position.scl(.5f);
                    position.x += startPos.x;
                    position.y += startPos.y;
                    position.scl(ChainsawRun.PPM);
                    enemys.add(new Big_Goat(screen, position.x, position.y));

                }

                else if (i == rabbit) {
                    ////System.out.print("i == rabbit\n");

                    Vector2 position = new Vector2();
                    ((RectangleMapObject) object).getRectangle().getPosition(position);
                    position.scl(1/ChainsawRun.PPM);
                    position.scl(.5f);
                    position.x += startPos.x;
                    position.y += startPos.y;
                    position.scl(ChainsawRun.PPM);
                    enemys.add(new Rabbit(screen, position.x, position.y));

                }

                else if (i == sawblade) {

                    Ellipse ellipse = ((EllipseMapObject) object).getEllipse();
                    Vector2 sawbladeposition = new Vector2(PhysicsCalculator.toUnits(ellipse.x + ellipse.width / 2), PhysicsCalculator.toUnits(ellipse.y + ellipse.height / 2));
                    sawbladeposition.x += startPos.x;
                    sawbladeposition.y += startPos.y;

                    SawBlade sawBlade = new SawBlade(screen, shape, sawbladeposition, (MapObject) object);
                    Body sBody = sawBlade.body;


                    sawblades.add(sawBlade);
                } else if (i == invisible) {
                    Vector2 position = new Vector2(0, 0);
                    boolean hasShape = false;

                    if (object instanceof PolylineMapObject) {
                        ////System.out.print("\n");
                        position = new Vector2((((PolylineMapObject) object).getPolyline().getOriginX()), ((PolylineMapObject) object).getPolyline().getOriginY());
                        position.scl(ChainsawRun.PPM);
                        position.x += startPos.x;
                        position.y += startPos.y;
                        chain = ShapeFactory.getPolyline((PolylineMapObject) object, false);
                        fdef.shape = chain;
                        hasShape = true;
                    }


                    ////System.out.print(hasShape + "\n");
                    if (hasShape) {
                        bdef.type = BodyDef.BodyType.StaticBody;
                        bdef.position.set(position);
                        //bdef.position =

                        body = world.createBody(bdef);


                        fdef.filter.categoryBits = ChainsawRun.NULL_BIT;
                        body.createFixture(fdef);
                    }
                }
                is ++;

            }
        }


        screen.setEnemies(enemys);
    }

    private Array<MapObject> sort_objects(MapObjects objects){

        Array<Float> XPOSS = new Array<Float>();

        int testiterator = 0;
        for (MapObject object : objects){
            XPOSS.add(((PolylineMapObject)object).getPolyline().getX());
            //System.out.println("        polyline.X: " + ((PolylineMapObject)object).getPolyline().getX());
            testiterator ++;
            //System.out.println("            testiterator: " + testiterator);
        }

        float test_smallest_X = 9999999;
        float smallest_X = -99999999;
        int main_iterator = 0;
        int XPOSS_iterator = 0;
        int smalest_XPOSS_X_index = 0;
        Array<Integer> sortet_XPOSS_indixies = new Array<Integer>();  //contains only native indizies in the right order
        Array<Float> XPOSS_COPY = XPOSS;


        while (main_iterator <= testiterator - 1){

            for(float x : XPOSS_COPY){

                if (x < test_smallest_X && x > smallest_X){
                    test_smallest_X = x;
                    smalest_XPOSS_X_index = XPOSS_iterator;
                }
                XPOSS_iterator ++;
            }

            smallest_X = test_smallest_X;
            sortet_XPOSS_indixies.add(smalest_XPOSS_X_index);
            //XPOSS_COPY.removeIndex(smalest_XPOSS_X_index);
            smalest_XPOSS_X_index = 0;
            XPOSS_iterator = 0;
            main_iterator ++;
            //System.out.println("        smalest_X: "+ test_smallest_X + "   mainIterator: " + main_iterator);
            test_smallest_X = 9999999;
        }

        Array<MapObject> sortet_objects = new Array<MapObject>();

        for (int sortet_index : sortet_XPOSS_indixies){
            sortet_objects.add(objects.get(sortet_index));
            //System.out.println("        sortet_index:  " + sortet_index);
        }


        Array<MapObject> test_objects = new Array<MapObject>();
        for (MapObject object : objects){
            test_objects.add(object);
        }

        //System.out.println("testobjects.size: " + test_objects.size + "   sortetobjects.size: " + sortet_objects.size);

        return sortet_objects;
    }


    private void remove_enemys(){
        float x_pos = screen.getPlayer().b2dbody.getPosition().x;
        for (Enemy enemy : screen.getEnemies()){
            if (enemy.b2dbody.getPosition().x < x_pos - 60){
                enemy.kill_me();
            }

        }

    }










}
