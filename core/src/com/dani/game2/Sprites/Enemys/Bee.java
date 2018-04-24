package com.dani.game2.Sprites.Enemys;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.dani.game2.ChainsawRun;
import com.dani.game2.Screens.PlayScreen;

import static com.badlogic.gdx.math.MathUtils.random;

/**
 * Created by root on 24.01.17.
 */

public class Bee extends Enemy         {
    private float dieAnimTime;
    private float stateTime;

    private String direction;
    private Animation currentAnim;
    public static Animation RabWalkAnim;
    private Animation placeholdAnim;

    private float startHeight;
    private float playerDetectionX;
    private float waiting_time;

    private boolean destroyed;
    private boolean animRemoved;

    public enum State {hunting, waiting, spinning, attacking, dieing};
    private Bee.State currentState;

    private boolean runnungRight;

    private static Array<TextureAtlas.AtlasRegion> walkFrames;
    //private PlayScreen screen;

    private float radius;


    private Vector2 aplImpulse;

    private static float ppm = ChainsawRun.PPM;


    public Bee(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        direction = "l";
        aplImpulse = new Vector2(0,0);
        setToDestroy = false;
        destroyed = false;
        currentState = State.hunting;
        startHeight = y/ChainsawRun.PPM;
        setBounds(getX(), getY(), 32 / ChainsawRun.PPM, 32 / ChainsawRun.PPM);
        stateTime = 1 * random();


        runnungRight = true;

        health = 1;
        dieAnimTime = 0;
    }

    public void update(float dt){

        setPosition(b2dbody.getPosition().x - getWidth() / 2, b2dbody.getPosition().y - (radius/ChainsawRun.PPM));



        if (health <= 0 && currentState != State.dieing) {
            currentState = State.dieing;

            intitialize_dead();

            Filter filter = new Filter();
            filter.categoryBits = ChainsawRun.DEAD_BIT;
            filter.maskBits = ChainsawRun.GROUND_BIT|
                    ChainsawRun.RABBIT_OBSTACLE_BIT|
                    ChainsawRun.SAWBLADE_BIT;
            for (Fixture fix : b2dbody.getFixtureList()) {
                fix.setFilterData(filter);
            }
        }



        switch (currentState) {
            case dieing:
                die(dt);
                break;
            case hunting:
                hunt(dt);
                break;
            case waiting:
                wait_a_time(dt);
                break;
            case spinning:
                wait_a_time(0);
                spin();
                break;
            case attacking:
                b2dbody.applyLinearImpulse(new Vector2(0, -100), b2dbody.getWorldCenter(), true);
                waiting_time += dt;
                if (waiting_time > 2)
                    currentState = State.dieing;
                break;
        }
    }

    private void handle_flip(){
        // //System.out.println("aplimpulse.x:  " + aplImpulse.x + "\n v_vel: " + b2dbody.getLinearVelocity().x);
        if (aplImpulse.x < 0) {// || (aplImpulse.x == 0 && b2dbody.getLinearVelocity().x < 0))
            direction = "l";
            ////System.out.println("    l");
        }else if (aplImpulse.x > 0){ // || (aplImpulse.x == 0 && b2dbody.getLinearVelocity().x > 0))
            direction = "r";
            //   //System.out.println("    r");
        }

        // //System.out.println("    " + direction);
        if (bodyPack != null)
            spmanager.flip(bodyPack, direction);
    }

    @Override
    public void dispose() {
        ////System.out.println("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBDISPOSE");
        spmanager.remove(bodyPack);
        screen.getParticleManager().remove(b2dbody);
        if (world != null ){
            world.destroyBody(b2dbody);}
        b2dbody = null;
        destroyed = true;
        stateTime = 0;
        screen.deleteEnemy(this);
    }




    @Override
    public void player_detected(){
        playerDetectionX = b2dbody.getPosition().x;
        if (currentState == State.hunting)
            currentState = State.waiting;


    }


    protected void die(float dt){
        stateTime += dt;
        dieAnimTime += dt;

        if (dieAnimTime >= .18f) {
            dieAnimTime = 0;
            if (animRemoved) {
                animRemoved = false;
                bodyPack = spmanager.add_animation_to_body(b2dbody, "BEE", true, 80 / ppm, 50 / ppm, 0 / ppm, 0 / ppm);
            } else {
                spmanager.remove(bodyPack);
                animRemoved = true;
            }
        }

        if (stateTime >= 5) {
            dispose();
        }

    }

    protected void move(float dt){
      /*  if (velocity.x < b2dbody.getLinearVelocity().x)
            b2dbody.applyLinearImpulse(new Vector2(-0.1f, 0), b2dbody.getWorldCenter(), true);
        else if (velocity.x > b2dbody.getLinearVelocity().x)
            b2dbody.applyLinearImpulse(new Vector2(0.1f, 0), b2dbody.getWorldCenter(), true);
    */}

    protected void hunt(float dt){


        Vector2 aimPos = screen.getPlayer().b2dbody.getPosition();
        Vector2 pos = b2dbody.getPosition();
        Vector2 vel = b2dbody.getLinearVelocity();
        float x = pos.x;
        float x_vel = vel.x;
        float x_p = 0;
        float xscale = 1;
        float rise_distance = startHeight - pos.y;
        float y_p = 0;
        float y_vel = vel.y;

        //check if he is allowed to go faster
        if (x_vel > 4 &&  x < aimPos.x || x_vel < -4 &&  x > aimPos.x){
            xscale = 0;
        }

        if (x < aimPos.x) {
            x_p = (4 - x_vel) * b2dbody.getMass();
        }else if (x > aimPos.x){
            x_p = (-4 - x_vel) * b2dbody.getMass();
        }

        if (rise_distance > 0){
            y_p = ((float) Math.sqrt(2 * -world.getGravity().y * rise_distance) - y_vel) * b2dbody.getMass();
        }


        ////System.out.println("x_vel: " + x_vel);
        aplImpulse = new Vector2(x_p, y_p);
        aplImpulse.scl(xscale, 1);

        if (Math.abs(aimPos.x - x) > .5) {
            handle_flip();
            aplImpulse.x += random(-20, 20);
        }
        else
            aplImpulse.x = 0;


        b2dbody.applyLinearImpulse(aplImpulse, b2dbody.getWorldCenter(), true);
    }

    public Vector2 avoid_obstacles(Vector2 aplImpulse, float jumpImpulse, Vector2 aimPos){
        for(Fixture obstacle : obstaclesInWay){
            // //System.out.print("check for obstavle in way\n");
            Vector2 velocity = b2dbody.getLinearVelocity();
            Vector2 pos = b2dbody.getPosition();
            Body obsBody = obstacle.getBody();
            Vector2 obsPos = obsBody.getPosition();

            PolygonShape shape = (PolygonShape)obstacle.getShape();
            //shape.;
            /*Rectangle rect = (Rectangle)shape;

            float obsWidth = rect.width;
            float obsHeight = rect.height;*/

            if (velocity.x >= 0 && obsPos.x >= pos.x && aimPos.x > obsPos.x){
                aplImpulse.y = jumpImpulse;
                ////System.out.print("jump " + pos.x + " \n");
            }else if(velocity.x <= 0 && obsPos.x <= pos.x && aimPos.x < obsPos.x){
                aplImpulse.y = jumpImpulse;
                // //System.out.print("ljump " + pos.x + " \n");
                //aplImpulse.y =  jumpImpulse;
            }
        }

        ////System.out.print(aplImpulse.x + " " + aplImpulse.y + " \n");
        return aplImpulse;
    }

    private void spin(){

        if (b2dbody.getTransform().getRotation() <= -1.6 && direction == "l"){
            b2dbody.setFixedRotation(true);
            currentState = State.attacking;
        }else if(b2dbody.getTransform().getRotation() >= 1.6 && direction == "r"){
            b2dbody.setFixedRotation(true);
            currentState = State.attacking;
        }


    }

    private void wait_a_time(float dt){
        waiting_time += dt;
        if (waiting_time > .5 && currentState == State.waiting){
            ////System.out.println("        waiting is over:  " + currentState);
            currentState = State.spinning;
            waiting_time = 0;
            b2dbody.setFixedRotation(false);
            if (direction == "l")
                b2dbody.applyAngularImpulse(-200, true);
            else
                b2dbody.applyAngularImpulse(200, true);
        }
        Vector2 pl_vel = screen.getPlayer().b2dbody.getLinearVelocity();
        Vector2 vel = b2dbody.getLinearVelocity();
        b2dbody.setLinearVelocity(pl_vel.x * 2/3, vel.y);


        if (pl_vel.x < 0) {// || (aplImpulse.x == 0 && b2dbody.getLinearVelocity().x < 0))
            direction = "l";
            ////System.out.println("    l");
        }else if (pl_vel.x > 0){ // || (aplImpulse.x == 0 && b2dbody.getLinearVelocity().x > 0))
            direction = "r";
            //   //System.out.println("    r");
        }

        // //System.out.println("    " + direction);
        spmanager.flip(bodyPack, direction);

        Vector2 pos = b2dbody.getPosition();
        float rise_distance = startHeight - pos.y;
        float y_p = 0;
        float y_vel = vel.y;

        if (rise_distance > 0){
            y_p = ((float) Math.sqrt(2 * -world.getGravity().y * rise_distance) - y_vel) * b2dbody.getMass();
        }

        aplImpulse = new Vector2(0, y_p);

        b2dbody.applyLinearImpulse(aplImpulse, b2dbody.getWorldCenter(), true);

    }

    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX()/ ChainsawRun.PPM, getY()/ ChainsawRun.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.fixedRotation = true;
        b2dbody = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();

        radius = 30;
        shape.setRadius(radius/ ChainsawRun.PPM);
        fdef.shape = shape;
        fdef.density = 30;
        //fdef.friction = .0001f;


        //fdef.friction = 2;
        fdef.filter.categoryBits = ChainsawRun.ENEMY_BIT;
        fdef.filter.maskBits = ChainsawRun.GROUND_BIT |
                ChainsawRun.PLAYER_BIT |
                //ChainsawRun.ENEMY_BIT |
                ChainsawRun.RABBIT_OBSTACLE_BIT |
                ChainsawRun.PLAYER_LEFT_HAMMER_BIT|
                ChainsawRun.PLAYER_RIGHT_HAMMER_BIT |
                ChainsawRun.SAWBLADE_BIT|
                ChainsawRun.PR_BIT;


        b2dbody.createFixture(fdef).setUserData(this);

        PolygonShape SensoerShape = new PolygonShape();
        Vector2[] vertice = new Vector2[3];
        vertice[0] = new Vector2(-10, 1).scl(1/ ChainsawRun.PPM);
        vertice[1] = new Vector2( 100, -200).scl(1/ ChainsawRun.PPM);
        vertice[2] = new Vector2(-100,-200f).scl(1/ChainsawRun.PPM);
        SensoerShape.set(vertice);
        fdef.shape = SensoerShape;
        fdef.isSensor = true;
        fdef.density = 0;
        fdef.filter.categoryBits = ChainsawRun.ENEMY_SENSOR_BIT;
        fdef.restitution = 0;

        b2dbody.createFixture(fdef).setUserData(this);
        bodyPack = spmanager.add_animation_to_body(b2dbody, "BEE", true, 80/ppm, 50/ppm, 0/ppm, 0/ppm);


    }



    public void hitMe() {
        health --;
        ////System.out.print("enemy hit on head\n");

        screen.getParticleManager().add_effect_to_body(b2dbody, "BLOOD2");
        screen.getParticleManager().add_effect_to_body(b2dbody, "BLOOD1");
    }


}
