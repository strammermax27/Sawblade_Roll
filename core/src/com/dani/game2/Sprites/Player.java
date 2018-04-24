package com.dani.game2.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
//import com.badlogic.gdx.physics.box2d.joints.
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.WheelJoint;
import com.badlogic.gdx.physics.box2d.joints.WheelJointDef;
import com.badlogic.gdx.utils.Array;
import com.dani.game2.Animation.SpriteManager;
import com.dani.game2.ChainsawRun;
import com.dani.game2.Scenes.Controller;
import com.dani.game2.Screens.Menus.HomeScreen;
import com.dani.game2.Screens.PlayScreen;
import com.dani.game2.Sprites.Enemys.Enemy;
import com.dani.game2.Sprites.Guns.Flame_thrower;
import com.dani.game2.Sprites.Guns.Gun;
import com.dani.game2.Sprites.Guns.Mashine_Gun;
import com.dani.game2.Sprites.Guns.Normal_gun;
import com.dani.game2.Sprites.Guns.Shot_Gun;
import com.dani.game2.Sprites.Guns.Sniper_Riffle;


/**
 * Created by root on 16.07.16.
 */
public class Player extends Sprite{
    public enum State {FALLING, JUMPING, STANDING, RUNNING, GROWING, DYING};

    public State currentState;

    public State previoudState;
    public World world;
    private PlayScreen screen;

    private float radius;

    public boolean touchedGround = false;

    public Body b2dbody;
    private Body bWheelBody;
    private Body fWheelBody;
    private Body stableBody;
    private SpriteManager.BodyPack bodyPack;

    public WheelJoint bWheelJoint;
    private WheelJoint fWheelJoint;
    private RevoluteJoint stableJoint;

    private Array<TextureRegion> frames;

    private BodyDef bWheelDef;
    private BodyDef fWheelDef;
    private FixtureDef bWheelfDef;
    private FixtureDef fWheelfDef;
    private SpriteManager.BodyPack bWheelPack;
    private SpriteManager.BodyPack fWheelPack;


    private PolygonShape mainShape;
    private CircleShape bWheel;
    private CircleShape fWheel;
    private CircleShape stableShape;

    private TextureRegion playerStand;
    private TextureRegion playerJump;
    private Animation playerRun;

    private TextureRegion bigPlayerStand;
    private TextureRegion bigPlayerJump;
    private Animation bigPlayerRun;
    private Animation playerGrow;

    public boolean runnungRight;
    private boolean playerIsBig;
    private boolean runGrowANimation;
    private boolean timeToDefineBigMario;
    private boolean has_to_brake;
    public boolean touching_ground;

    public boolean punshbuttonperssed;
    public Array<Enemy> enemys_in_right_zone;
    public Array<Enemy> enemys_in_left_zone;

    private float dtime_last_rend;
    private float old_last_rend;
    private float hammertime;

    public boolean isDead;
    private float deadtime;

    private boolean bleeding;


    private float pi;

    private static float ppm = ChainsawRun.PPM;

    private Gun gun;

    private float wishSpeed;

    private float t_last_click;

    private boolean speedApplied;

    public Vector2 touchPos;


    public Player(PlayScreen screen){
        //super(screen.getAtlas().findRegion("little_mario"));

        this.screen = screen;
        this.world = screen.getWorld();

        radius = 10;

        isDead = false;
        bleeding = false;
        deadtime = 0;

        currentState = State.STANDING;
        previoudState = State.STANDING;
        dtime_last_rend = 0;
        old_last_rend = 0;

        runnungRight = true;
        punshbuttonperssed = false;

        enemys_in_left_zone = new Array<Enemy>();
        enemys_in_right_zone = new Array<Enemy>();

        load_wheelchair_animations();

        mainShape = new PolygonShape();
        define_disabled();
        ////System.out.print(b2dbody.getPosition()+ "\n");



        pi = (float) Math.PI ;

        load_gun();

        wishSpeed = 0;
        speedApplied = false;

        touchPos = new Vector2();
    }

    private void load_gun(){
        String gunName  = screen.game.homeScreen.gunToLoad;


        if (gunName.contains("flame_thrower")){
            gun = new Flame_thrower(b2dbody, screen);
        }else if (gunName.contains("mashine_gun")){
            gun = new Mashine_Gun(b2dbody, screen);
        }else if (gunName.contains("normal_gun")){
            gun = new Normal_gun(b2dbody, screen);
        }else if (gunName.contains("shot_gun")){
            gun = new Shot_Gun(b2dbody, screen);
        }else if (gunName.contains("sniper_riffle")){
            gun = new Sniper_Riffle(b2dbody, screen);
        }else {
            System.out.println("WARNING: invalid gun name: " + gunName);
        }


    }


    public void update(float dt){
        speedApplied = false;
        ////System.out.print(b2dbody.getPosition().x + "\n");

        dtime_last_rend += dt;
        hammertime += dt;
        setPosition(b2dbody.getPosition().x, b2dbody.getPosition().y);
        //setRotation(b2dbody.getAngle() * (180/ pi));
        ////System.out.print(b2dbody.getAngle() +"\n");
        //setRegion(getFrame(dt));

        bWheelJoint.enableMotor(false);

        if (punshbuttonperssed && hammertime > dt){
            punshbuttonperssed = false;
            hammertime = 0;
        }

        if (!isDead){
            handleInput();
            move(dt);
            brake_automatic(dt);
        }
        else {
            die(dt);
        }






        gun.update(dt);

        if(b2dbody.getLinearVelocity().y < -50){
            //System.out.println("WARNING: dead caused because his down vel is to fast");
            die(dt);
        }
       /* if (b2dbody.getPosition().y < 1000){
            die(dt);
        }*/
        ////System.out.print("left: " + screen.controller.leftPressed + "        shoot: " + screen.controller.get_justAttackPressed() + "\n");
    }

    private void move(float dt){
        float crSpeed = bWheelJoint.getJointSpeed();
        ////System.out.print(1 + "\n");
        if (t_last_click <= 0.3) {
            ////System.out.print(2 + "\n");
            if (crSpeed < wishSpeed) {
                ////System.out.print(3 + "\n");
                bWheelJoint.enableMotor(true);
                bWheelJoint.setMotorSpeed(crSpeed + 20f * dt);
                speedApplied = true;
            } else if (crSpeed > wishSpeed) {
               // //System.out.print(3 + "\n");
                bWheelJoint.enableMotor(true);
                bWheelJoint.setMotorSpeed(crSpeed - 20f * dt);
                speedApplied = false;
            }
        }

        if (t_last_click >= 0.3){
            wishSpeed = 0;
        }

        t_last_click += dt;

    }

    public void handleInput(){
        Controller controller = screen.controller;


        if(Gdx.input.isKeyJustPressed(Input.Keys.W) || controller.get_upPressed()) {
            b2dbody.applyLinearImpulse(new Vector2(0, 5000/ppm), b2dbody.getWorldCenter(), true);
            //b2dbody.setTransform(10, 10,0);
            ////System.out.println("w_pressed");
        }

        if((controller.rightPressed || Gdx.input.isKeyPressed(Input.Keys.D))){
            //has_to_brake = false;
            //b2dbody.applyLinearImpulse(new Vector2(0.7f, 0), b2dbody.getWorldCenter(), true);//remove later
            //*bWheelJoint.enableMotor(true);

            //if (bWheelJoint.getJointSpeed() >= -300)
                //bWheelJoint.setMotorSpeed(bWheelJoint.getJointSpeed() - 100);
                wishSpeed =  - 300;
                //if(bWheelJoint.getJointSpeed() < -300) bWheelJoint.setMotorSpeed(-300);
            t_last_click = 0;
            //if (touching_ground)
             //   has_to_brake = false;

        }else {
            if (!touching_ground && !has_to_brake) {
                has_to_brake = true;
                screen.getParticleManager().add_effect_to_body(b2dbody, "ROCKET");
            }

        }

        if ((controller.leftPressed || Gdx.input.isKeyPressed(Input.Keys.A))){
            //b2dbody.applyLinearImpulse(new Vector2(-0.7f, 0), b2dbody.getWorldCenter(), true);//remove later
            //bWheelJoint.enableMotor(true);
            //if (bWheelJoint.getJointSpeed() <= 300)
                wishSpeed =  + 300;
                //bWheelJoint.setMotorSpeed(bWheelJoint.getJointSpeed() + 100);
                //if(bWheelJoint.getJointSpeed() > 300) bWheelJoint.setMotorSpeed(300);*/
            t_last_click = 0;
        }

        /*if (Gdx.input.isKeyPressed(Input.Keys.SPACE)|| controller.AttackPressed){
            punsh();
            ////System.out.print("bodies in world:" + world.getBodyCount() + "\n");
            gun.aimPoint = new Vector2(10, 0);
            gun.shoot();

        }*/

        if (Gdx.input.isTouched(0)) {
            touchPos.x = Gdx.input.getX(0);
            touchPos.y = Gdx.input.getY(0);


           // //System.out.print("touch pos: "+ screen.getViewport(). +"\n");
            if(touchPos.y < screen.getViewport().getScreenHeight() - controller.stageHeight || touchPos.x > screen.getViewport().getScreenHeight()/3) {
                touchPos = screen.getViewport().unproject(touchPos);

            ////System.out.print(touchPos + "\n");


                gun.aimPoint = touchPos;
                gun.shoot();
            }
            if (Gdx.input.justTouched()){
                if(Gdx.input.getY() < screen.getViewport().getWorldHeight() - controller.stageHeight/2 || touchPos.x > screen.getViewport().getWorldWidth()/2)
                    gun.loked = false;


                touchPos.x = Gdx.input.getX();
                touchPos.y = Gdx.input.getY();
                touchPos = screen.getViewport().unproject(touchPos);
                //System.out.print(touchPos + "\n");
            }
        }

        if (Gdx.input.isTouched(1)) {
            touchPos.x = Gdx.input.getX(1);
            touchPos.y = Gdx.input.getY(1);


            // //System.out.print("touch pos: "+ screen.getViewport(). +"\n");
            if(touchPos.y < screen.getViewport().getWorldHeight() - controller.stageHeight || touchPos.x > screen.getViewport().getWorldWidth()/3) {
                touchPos = screen.getViewport().unproject(touchPos);

                ////System.out.print(touchPos + "\n");


                gun.aimPoint = touchPos;
                gun.shoot();
            }
            if (Gdx.input.justTouched()){
                if(Gdx.input.getY() < screen.getViewport().getWorldHeight() - controller.stageHeight/2 || touchPos.x > screen.getViewport().getWorldWidth()/2)
                    gun.loked = false;


                touchPos.x = Gdx.input.getX();
                touchPos.y = Gdx.input.getY();
                touchPos = screen.getViewport().unproject(touchPos);
                //System.out.print(touchPos + "\n");
            }
        }
        if (Gdx.input.isTouched(2)) {
            touchPos.x = Gdx.input.getX(2);
            touchPos.y = Gdx.input.getY(2);


            // //System.out.print("touch pos: "+ screen.getViewport(). +"\n");
            if(touchPos.y < screen.getViewport().getWorldHeight() - controller.stageHeight || touchPos.x > screen.getViewport().getWorldWidth()/3) {
                touchPos = screen.getViewport().unproject(touchPos);

                ////System.out.print(touchPos + "\n");


                gun.aimPoint = touchPos;
                gun.shoot();
            }
            if (Gdx.input.justTouched()){
                if(Gdx.input.getY() < screen.getViewport().getWorldHeight() - controller.stageHeight/2 || touchPos.x > screen.getViewport().getWorldWidth()/2)
                    gun.loked = false;


                touchPos.x = Gdx.input.getX();
                touchPos.y = Gdx.input.getY();
                touchPos = screen.getViewport().unproject(touchPos);
                ////System.out.print(touchPos + "\n");
            }
        }





    }


    public void punsh(){
        punshbuttonperssed = true;
        /*if (runnungRight && !right_hammer_is_active){
            right_hammer_is_active = true;
        }else if(!runnungRight && !left_hammer_is_active){
            left_hammer_is_active = true;
        }*/

    }

    private void brake_automatic(float dt){

        Vector2 vel = b2dbody.getLinearVelocity();
        if (has_to_brake && vel.x >= 3){




            Vector2 brakeImpulse = new Vector2(0,0);
            //brakeImpulse.x = b2dbody.getLinearVelocity().x * 20f * dt * -1;
            float bodyImpulseX = b2dbody.getMass() * b2dbody.getLinearVelocity().x;


            if (vel.y > -35 ) {
                brakeImpulse.y =  -300f * dt;
            }

            // brakeImpulse.x = -170 * dt;


            if (bodyImpulseX > -brakeImpulse.x) {
                b2dbody.applyLinearImpulse(brakeImpulse.x, brakeImpulse.y, b2dbody.getWorldCenter().x - .2f, b2dbody.getWorldCenter().y, false);
            }
            else {
                b2dbody.applyLinearImpulse(-bodyImpulseX,  brakeImpulse.y, b2dbody.getWorldCenter().x - .2f, b2dbody.getWorldCenter().y, false);
            }
        }

        if (touching_ground) {
            has_to_brake = false;
            screen.getParticleManager().remove(b2dbody);
        }

        ////System.out.println("touching_ground: " + touching_ground);



    }

    /*public TextureRegion getFrame(float dt) {
        currentState = getState(dt);

        TextureRegion region;
        switch (currentState) {
            case GROWING:
                region = playerGrow.getKeyFrame(dtime_last_rend);
                if(playerGrow.isAnimationFinished(dtime_last_rend))
                    runGrowANimation = false;
                break;
            case JUMPING:
                region = playerIsBig ? bigPlayerJump : playerJump;
                break;
            case RUNNING:
                region = playerIsBig ? bigPlayerRun.getKeyFrame(dtime_last_rend, true) : playerRun.getKeyFrame(dtime_last_rend, true);
                break;
            case FALLING:
            case STANDING:
            default:
                region = playerIsBig ? bigPlayerStand : playerStand;
                break;

        }

        /*if((b2dbody.getLinearVelocity().x <0 || !runnungRight) && !region.isFlipX()) {
            region.flip(true, false);
            runnungRight = false;
        }

        else  if((b2dbody.getLinearVelocity().x > 0 || runnungRight) && region.isFlipX()) {
            region.flip(true, false);
            runnungRight = true;
        }
        dtime_last_rend = currentState == previoudState ? dtime_last_rend + dt : 0;
        old_last_rend = currentState != State.STANDING ?  dtime_last_rend : old_last_rend;
        previoudState = currentState;
        return region;

    }*/

    /*public State getState(float dt){
        if(b2dbody.getLinearVelocity().y > 0)
            return State.JUMPING;
        else if(b2dbody.getLinearVelocity().y < 0 )
                return  State.FALLING;
        else if(b2dbody.getLinearVelocity().x != 0)
                    return State.RUNNING;
        else if(runGrowANimation)
            return State.GROWING;
        else
                return State.STANDING;


    }*/

    /*private void load_animations(){

        Array<TextureRegion> frames = new Array<TextureRegion>();

        for (int i=1; i <= 3; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("little_mario"), i * 16, 0, 16, 16));
        }
        playerRun = new Animation(0.17f, frames);
        frames.clear();

        for (int i = 1; i<= 3; i++){
            frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), i*16, 0, 16, 32));
        }
        bigPlayerRun = new Animation(0.17f, frames);
        frames.clear();

        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 240, 0, 16,32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"),   0, 0, 16,32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 240, 0, 16,32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"),   0, 0, 16,32));
        playerGrow = new Animation(0.2f, frames);

        frames.clear();

        playerStand = new TextureRegion(screen.getAtlas().findRegion("little_mario"),0,0,16,16);
        bigPlayerStand = new TextureRegion(screen.getAtlas().findRegion("big_mario"),0,0,16, 32);

        bigPlayerJump = new TextureRegion(screen.getAtlas().findRegion("big_mario"), 80,0, 16, 32);
        playerJump = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 80, 0, 16, 16);

        setRegion(playerStand);
        setBounds(0,0,16/ ppm, 16/ ppm);
    }*/
    private void load_wheelchair_animations(){
        Array<TextureRegion> frames = new Array<TextureRegion>();
        Texture picture = new Texture("animations/rollingMikael/rollingMikael.png");


        for (int i=1; i <= 3; i++) {
            frames.add(new TextureRegion(picture));
        }
        playerRun = new Animation(0.17f, frames);
        frames.clear();

        for (int i = 1; i<= 3; i++){
            frames.add(new TextureRegion(picture));
        }
        bigPlayerRun = new Animation(0.17f, frames);
        frames.clear();

        frames.add(new TextureRegion(picture));


        playerGrow = new Animation(0.2f, frames);

        frames.clear();

        playerStand = new TextureRegion(new TextureRegion(picture));
        bigPlayerStand = new TextureRegion(new TextureRegion(picture));

        bigPlayerJump = new TextureRegion(new TextureRegion(picture));
        playerJump = new TextureRegion(new TextureRegion(picture));

        //setRegion(playerJump);
        //setBounds(0, 0, 50/ ppm, 50/ ppm);
    }

    /*public State getCurrentState () {
        return currentState;
    }*/




    private void define_disabled(){
        //System.out.print("start Define_Disabled" + "\n");


        //System.out.print("whelchairshapes");
        make_wheelchair_shapes();

        BodyDef bdef = new BodyDef();
        bdef.position.set(100/ ppm,200/ ppm);
        bdef.type = BodyDef.BodyType.DynamicBody;


        FixtureDef hammerfdef = new FixtureDef();
        FixtureDef mainFdef = new FixtureDef();


        mainFdef.shape = mainShape;

        bdef.fixedRotation = false;
        bdef.linearDamping = 0.1f;
        bdef.angularDamping = .7f;

        mainFdef.density = 10;
        mainFdef.friction = 1;

        //fdef.friction = 2;
        mainFdef.filter.categoryBits = ChainsawRun.PLAYER_BIT;
        mainFdef.filter.maskBits = ChainsawRun.GROUND_BIT
                | ChainsawRun.RABBIT_OBSTACLE_BIT
                | ChainsawRun.ENEMY_BIT
                | ChainsawRun.ITEM_BIT
                | ChainsawRun.SAWBLADE_BIT
                | ChainsawRun.ENEMY_SENSOR_BIT;
        //System.out.print("b2body to world  ");
        b2dbody = world.createBody(bdef);
        //System.out.print("b2tw end");
        b2dbody.createFixture(mainFdef).setUserData(this);
        //System.out.print(" add shape end ");
        b2dbody.setFixedRotation(false);
        bodyPack = screen.getSpriteManager().add_animation_to_body(b2dbody, "PLAYER", false, 50/ppm, 65/ppm, 0, 0);


        PolygonShape polygon = new PolygonShape();
        Vector2[] points = new Vector2[3];
        points[0] = new Vector2( 0, 0).scl(1/ ChainsawRun.PPM);
        points[1] = new Vector2( 20, -60).scl(1/ ChainsawRun.PPM);
        points[2] = new Vector2(-20,-60f).scl(1/ChainsawRun.PPM);
        polygon.set(points);
        mainFdef.shape = polygon;
        mainFdef.isSensor = true;
        mainFdef.density = 0;
        mainFdef.filter.categoryBits = ChainsawRun.PLAYER_SENSOR_BIT;
        mainFdef.filter.maskBits = ChainsawRun.GROUND_BIT;
        b2dbody.createFixture(mainFdef).setUserData(this);
        ////System.out.print("body fixed rotation:" + b2dbody.isFixedRotation() + "\n");
        // b2dbody.

        /*mainFdef.filter.maskBits = ChainsawRun.ENEMY_BIT;

        hammerfdef.filter.categoryBits = ChainsawRun.PLAYER_BIT;
        hammerfdef.filter.maskBits = ChainsawRun.ENEMY_BIT;

        CircleShape hammer = new CircleShape();
        hammer.setRadius(2*radius /ppm);
        hammerfdef.isSensor = true;

        hammer.setPosition(new Vector2(b2dbody.getPosition().x/ppm +  radius/v , b2dbody.getPosition().y/ppm));
        hammerfdef.shape = hammer;
        hammerfdef.filter.categoryBits = ChainsawRun.PLAYER_RIGHT_HAMMER_BIT;
        b2dbody.createFixture(hammerfdef).setUserData(this);

        hammer.setPosition(new Vector2(b2dbody.getPosition().x/ppm - radius/ppm, b2dbody.getPosition().y/ppm ));
        hammerfdef.shape = hammer;
        hammerfdef.filter.categoryBits = ChainsawRun.PLAYER_LEFT_HAMMER_BIT;
        b2dbody.createFixture(hammerfdef).setUserData(this);*/

        //System.out.print("wheel bodies");
        make_wheel_bodies();

        //System.out.print("completing");
        complete_him();

        //b2dbody.getFixtureList().set(1,1);*/
        //System.out.print("end Define_Disabled" + "\n");
    }

    private void complete_him(){
        WheelJointDef wheelJointDef= new WheelJointDef();
        RevoluteJointDef rDef = new RevoluteJointDef();
//        wheelJointDef FwheelJointDef = new wheelJointDef();


        /*wheelJointDef.bodyA = b2dbody;
        wheelJointDef.bodyB = bWheelBody;
        wheelJointDef.collideConnected = false;*/
        wheelJointDef.enableMotor = true;
        wheelJointDef.maxMotorTorque = 1000;
        wheelJointDef.motorSpeed = 0f;
        wheelJointDef.dampingRatio = .8f;
        wheelJointDef.frequencyHz = 7f;



        wheelJointDef.initialize(b2dbody, bWheelBody, bWheelBody.getPosition(), new Vector2(1,1));

       bWheelJoint = (WheelJoint) world.createJoint(wheelJointDef);
       //bwheelJoint.setMotorSpeed(10000f);

        wheelJointDef = new WheelJointDef();
        wheelJointDef.enableMotor = false;
        wheelJointDef.dampingRatio = .8f;
        wheelJointDef.frequencyHz = 15f;

        wheelJointDef.initialize(b2dbody, fWheelBody, fWheelBody.getPosition(), new Vector2(0,1));
        fWheelJoint = (WheelJoint) world.createJoint(wheelJointDef);


        //System.out.print("start making joint\n");
        stableBody.setTransform(b2dbody.getWorldCenter(), 0);
        rDef.enableLimit = true;
        rDef.localAnchorA.set(stableBody.getLocalCenter());
        rDef.localAnchorB.set(b2dbody.getLocalCenter());
        rDef.upperAngle = 1;
        rDef.lowerAngle = -0.6f;

        rDef.initialize(stableBody, b2dbody, b2dbody.getWorldCenter());
        stableJoint = (RevoluteJoint) world.createJoint(rDef);
        //System.out.print("end making joint\n");



    }

    private void make_wheel_bodies(){
        //System.out.print("start making wheel bodies\n");

        bWheelDef = new BodyDef();
        fWheelDef = new BodyDef();
        BodyDef stableDef = new BodyDef();
        bWheelfDef = new FixtureDef();
        fWheelfDef = new FixtureDef();
        FixtureDef stablefDef = new FixtureDef();

        bWheelfDef.shape = bWheel;
        bWheelfDef.density = 1f;
        bWheelfDef.friction = 20;
        bWheelfDef.filter.categoryBits = ChainsawRun.SAWBLADE_BIT;
        bWheelfDef.filter.maskBits = ChainsawRun.GROUND_BIT | ChainsawRun.RABBIT_OBSTACLE_BIT | ChainsawRun.ENEMY_BIT;


        fWheelfDef.shape = fWheel;
        fWheelfDef.density = 1f;
        fWheelfDef.friction = 20;
        fWheelfDef.filter.categoryBits = ChainsawRun.F_WHEEL_BIT;
        fWheelfDef.filter.maskBits = ChainsawRun.GROUND_BIT | ChainsawRun.RABBIT_OBSTACLE_BIT;

        stablefDef.shape = stableShape;
        stablefDef.density = 10;
        stablefDef.friction = 2;
        stablefDef.filter.categoryBits = ChainsawRun.NULL_BIT;


        bWheelDef.fixedRotation = false;
        bWheelDef.angularDamping = 2;
        bWheelDef.type = BodyDef.BodyType.DynamicBody;
        bWheelDef.position.set(new Vector2(b2dbody.getPosition().x - 5/ppm , b2dbody.getPosition().y + 10/ppm));

        fWheelDef.fixedRotation = false;
        fWheelDef.angularDamping = 0.01f;
        fWheelDef.type = BodyDef.BodyType.DynamicBody;
        fWheelDef.position.set(new Vector2(b2dbody.getPosition().x + 40/ppm, b2dbody.getPosition().y + 1/ ppm));

        stableDef.fixedRotation = true;//important
        stableDef.type = BodyDef.BodyType.DynamicBody;
        stableDef.position.set(b2dbody.getWorldCenter());

        bWheelBody = world.createBody(bWheelDef);
        fWheelBody = world.createBody(fWheelDef);
        stableBody = world.createBody(stableDef);


        bWheelBody.createFixture(bWheelfDef).setUserData(this);
        fWheelBody.createFixture(fWheelfDef).setUserData(this);
        stableBody.createFixture(stablefDef).setUserData(this);

        //bWheelBody.createFixture(bWheelfDef);
        //fWheelBody.createFixture(fWheelfDef);


        bWheelPack = screen.getSpriteManager().add_animation_to_body(bWheelBody, "SAWBLADE", false, bWheel.getRadius()*2, bWheel.getRadius()*2, -bWheel.getRadius(),-bWheel.getRadius());
        fWheelPack = screen.getSpriteManager().add_animation_to_body(fWheelBody, "F_WHEEL", false, fWheel.getRadius()*2, fWheel.getRadius()*2, -fWheel.getRadius(), -fWheel.getRadius());

        //System.out.print("end making wheel bodies\n");
    }



    private void make_wheelchair_shapes(){
        //System.out.print("making wheelchair shapes \n");
        //mainShape = new PolygonShape();
        bWheel = new CircleShape();
        fWheel = new CircleShape();
        stableShape = new CircleShape();


        Vector2[] mainShape_Points;
        Vector2 bWheel_Center;
        float bWheel_Radius;
        Vector2 fWheel_Center;
        float fWheel_Radius;


        mainShape_Points = new Vector2[8];

        mainShape_Points[0] = new Vector2(0,-5).scl(1/ppm);
        mainShape_Points[1] = new Vector2(11,-5).scl(1/ppm);
        mainShape_Points[2] = new Vector2(11,36).scl(1/ppm);
        mainShape_Points[3] = new Vector2(-5,36).scl(1/ppm);
        mainShape_Points[4] = new Vector2(-5,6).scl(1/ppm);
        mainShape_Points[5] = new Vector2(-5,6).scl(1/ppm);
        mainShape_Points[6] = new Vector2(34,17).scl(1/ppm);
        mainShape_Points[7] = new Vector2(33,5).scl(1/ppm);

        for (Vector2 point : mainShape_Points){
            point.y += 10 / ppm;
            point.x += 10 /ppm;
        }


        mainShape.set(mainShape_Points);



        //bWheel_Center = new Vector2( 0, -0.3f/ppm);
        bWheel_Center = new Vector2(0,0);
        bWheel_Radius =     30 / ppm;

        //fWheel_Center = new Vector2(-1/ppm, 0.5f/ppm);
        fWheel_Center = new Vector2(0,0);
        fWheel_Radius = 10f/ppm;

        Vector2 stableShape_Center = new Vector2(0,0);
        float stable_radius = 5f/ppm;
        //mainShape.set(mainShape_Points);
        //mainShape.setAsBox(5,5);


        bWheel.setPosition(bWheel_Center);
        bWheel.setRadius(bWheel_Radius);

        fWheel.setPosition(fWheel_Center);
        fWheel.setRadius(fWheel_Radius);

        stableShape.setPosition(stableShape_Center);
        stableShape.setRadius(stable_radius);


    }


    public void define_walker(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(30/ ppm,90/ ppm);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2dbody = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();


        shape.setRadius(radius/ ppm);



        //fdef.friction = 2;
        fdef.filter.categoryBits = ChainsawRun.PLAYER_BIT;
        fdef.filter.maskBits = ChainsawRun.GROUND_BIT
                | ChainsawRun.RABBIT_OBSTACLE_BIT
                | ChainsawRun.ENEMY_BIT
                | ChainsawRun.ITEM_BIT
                | ChainsawRun.SAWBLADE_BIT
                | ChainsawRun.ENEMY_SENSOR_BIT;
        ////System.out.print("in mario start: \n RABBIT_OBSTACLE_BIT:" + com.dani.game2.ChainsawRun.RABBIT_OBSTACLE_BIT + "\n ENEMY_BIT:" + com.dani.game2.ChainsawRun.ENEMY_BIT + "\n  DESTROYED_BIT" + com.dani.game2.ChainsawRun.DESTROYED_BIT + "\n mario end \n");

        fdef.shape = shape;
        b2dbody.createFixture(fdef).setUserData(this);

        CircleShape hammer = new CircleShape();
        hammer.setRadius(2*radius /ppm);
        fdef.isSensor = true;

        hammer.setPosition(new Vector2(b2dbody.getPosition().x/ppm +  radius/ppm , b2dbody.getPosition().y/ppm));
        fdef.shape = hammer;
        fdef.filter.categoryBits = ChainsawRun.PLAYER_RIGHT_HAMMER_BIT;
        b2dbody.createFixture(fdef).setUserData(this);

        hammer.setPosition(new Vector2(b2dbody.getPosition().x/ppm - radius/ppm, b2dbody.getPosition().y/ppm ));
        fdef.shape = hammer;
        fdef.filter.categoryBits = ChainsawRun.PLAYER_LEFT_HAMMER_BIT;
        b2dbody.createFixture(fdef).setUserData(this);


        PolygonShape polygon = new PolygonShape();
        Vector2[] points = new Vector2[3];
        points[0] = new Vector2(0,0);
        points[1] = new Vector2(-.1f, -.5f);
        points[2] = new Vector2(.1f, .5f);
        polygon.set(points);
        fdef.shape = polygon;
        fdef.filter.categoryBits = ChainsawRun.PLAYER_SENSOR_BIT;
        fdef.filter.maskBits = ChainsawRun.GROUND_BIT;
        b2dbody.createFixture(fdef);
        //b2dbody.getFixtureList().set(1,1);
    }

    private void die(float dt){
        if (deadtime == 0){
            b2dbody.applyLinearImpulse(new Vector2(0,180), b2dbody.getWorldCenter(), true);
            stableBody.setFixedRotation(false);
            b2dbody.applyAngularImpulse(10, false);
            /*bloodpe = new ParticleEffect();
            bloodpe.load(Gdx.files.internal("particles/testparticles4.party"), Gdx.files.internal("particles"));
            bloodpe.scaleEffect(1/ppm);*/
        }
        else if (deadtime >= 3){
            screen.game.setScreen(screen.game.homeScreen);
        }
        deadtime += dt;
    }

    public void enemyContact(){
        isDead = true;
        bleeding = true;
        screen.getParticleManager().add_effect_to_body(b2dbody, "BLOOD1");
    }


    public void dispose(){
        if (world != null){
            world.destroyBody(b2dbody);
            world.destroyBody(bWheelBody);
            world.destroyBody(fWheelBody);
        }
        screen.getSpriteManager().remove(bodyPack);
        screen.getSpriteManager().remove(fWheelPack);
        screen.getSpriteManager().remove(bWheelPack);
        screen.getParticleManager().remove(b2dbody);
    }

}




 /*public void grow(){
        com.dani.game2.ChainsawRun.manager.get("sounds/effects/powerup.wav", Sound.class).play();
        runGrowANimation = true;
        marioIsBig = true;
        timeToDefineBigMario = true;
        setBounds(getX(), getY(), getWidth(), 32/ com.dani.game2.ppm);
    }

    public void defineBigMario(){
        Vector2 currentPosition = b2dbody.getPosition();
        world.destroyBody(b2dbody);

        BodyDef bdef = new BodyDef();
        bdef.position.set(currentPosition.add(0, 10 / com.dani.game2.ppm));
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2dbody = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();

        float radius = (playerStand.getRegionHeight()/2);
        shape.setRadius(radius/ com.dani.game2.ppm);



        //fdef.friction = 2;
        fdef.filter.categoryBits = com.dani.game2.ChainsawRun.PLAYER_BIT;
        fdef.filter.maskBits = com.dani.game2.ChainsawRun.GROUND_BIT
                | com.dani.game2.ChainsawRun.BRICK_BIT
                | com.dani.game2.ChainsawRun.COIN_BIT
                | com.dani.game2.ChainsawRun.RABBIT_OBSTACLE_BIT
                | com.dani.game2.ChainsawRun.ENEMY_BIT
                | com.dani.game2.ChainsawRun.ENEMY_HEAD_BIT
                | com.dani.game2.ChainsawRun.ITEM_BIT;
        //System.out.print("in mario start: \n RABBIT_OBSTACLE_BIT:" + com.dani.game2.ChainsawRun.RABBIT_OBSTACLE_BIT + "\n ENEMY_BIT:" + com.dani.game2.ChainsawRun.ENEMY_BIT + "\n  DESTROYED_BIT" + com.dani.game2.ChainsawRun.DESTROYED_BIT + "\n mario end \n");

        fdef.shape = shape;
        b2dbody.createFixture(fdef).setUserData(this);
        shape.setPosition(new Vector2(0, -14 / com.dani.game2.ppm));
        fdef.shape = shape;
        b2dbody.createFixture(fdef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-0.2f/ com.dani.game2.ppm, (float) (1.1*radius/ com.dani.game2.ppm)), new Vector2(0.2f/ com.dani.game2.ppm, (float) ((1.1*radius)/ com.dani.game2.ppm)));
        fdef.filter.categoryBits = com.dani.game2.ChainsawRun.PLAYER_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;


        b2dbody.createFixture(fdef).setUserData(this);
        timeToDefineBigMario = false;

    }*/