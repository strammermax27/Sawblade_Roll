package com.dani.game2.Sprites.Enemys;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
import com.dani.game2.Animation.SpriteManager;

import static com.badlogic.gdx.math.MathUtils.random;

/**
 * Created by root on 21.07.16.
 */
public class Rabbit extends Enemy {

    private float stateTime;


    private SpriteManager spmanager;

    private boolean destroyed;

    private enum State {JUMPING, STANDING, RUNNING, GRINDING};
    private State currentState;

    private boolean runnungRight;

    private static Array<TextureAtlas.AtlasRegion> walkFrames;
    //private PlayScreen screen;

    private float radius;


    private String state;

    private Vector2 aplImpulse;

    private static float ppm = ChainsawRun.PPM;


    public Rabbit(PlayScreen screen, float x, float y) {
        super(screen, x, y);

        state = "standing";
        aplImpulse = new Vector2(0,0);
        setToDestroy = false;
        destroyed = false;

        stateTime = 1 * random();
        spmanager = screen.getSpriteManager();

        runnungRight = true;

        health = 1;

    }

    public void update(float dt) {


        if (health <= 0 && alive) {
            alive = false;

            intitialize_dead();

            Filter filter = new Filter();
            filter.categoryBits = ChainsawRun.DEAD_BIT;
            filter.maskBits = ChainsawRun.RABBIT_OBSTACLE_BIT |
                    ChainsawRun.SAWBLADE_BIT;
            for (Fixture fix : b2dbody.getFixtureList()) {
                fix.setFilterData(filter);
            }
        }

        if (!alive){
            ////System.out.println("rabbit !alive");
            die(dt);
        }else if(alive)
            hunt(dt);

        handle_flip();
    }
    public void player_detected(){}

    private void handle_flip() {
        if (b2dbody.getLinearVelocity().x < 0)
            screen.getSpriteManager().flip(bodyPack, "l");
        else if (b2dbody.getLinearVelocity().x > 0)
            screen.getSpriteManager().flip(bodyPack, "r");
    }

    @Override
    public void dispose() {
        spmanager.remove(bodyPack);
        if (world != null ) {
            world.destroyBody(b2dbody);
        }
        destroyed = true;
        stateTime = 0;
        screen.deleteEnemy(this);
    }

    @Override


    protected void die(float dt){

        stateTime += dt;
        dieAnimTime += dt;


        if (dieAnimTime >= .18f) {
            dieAnimTime = 0;
            if (animRemoved) {
                animRemoved = false;
                bodyPack = spmanager.add_animation_to_body(b2dbody, "RABBIT", true, 40/ppm, 40/ppm, 0/ppm, 17/ppm);
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
        if (velocity.x < b2dbody.getLinearVelocity().x)
            b2dbody.applyLinearImpulse(new Vector2(-0.1f, 0), b2dbody.getWorldCenter(), true);
        else if (velocity.x > b2dbody.getLinearVelocity().x)
            b2dbody.applyLinearImpulse(new Vector2(0.1f, 0), b2dbody.getWorldCenter(), true);
    }

    protected void hunt(float dt){


        Vector2 aimPos = screen.getPlayer().b2dbody.getPosition();
        Vector2 pos = b2dbody.getPosition();
        Vector2 vel = b2dbody.getLinearVelocity();
        float x = pos.x;
        float y = pos.y + 4 / ChainsawRun.PPM;
        float x_vel = vel.x;
        float y_vel = vel.y;
        // we need this because rabbists aren't allowed to run faster than 30
        float xscale = 1;
        float yscale = 0;

        float jumpImpulse = 2;

        //check if he is allowed to go faster
        if (x_vel > 3 || x_vel < -3){
            xscale = 0;
        }
        /*if (!touchedGround){
            yscale = 0;
        }*/
        if (vel.y == 0 && aimPos.y > pos.y ){
            yscale = 1;
        }
        //make an impulse to the ridht direction
        jumpImpulse = 0;
        if (x < aimPos.x && y < aimPos.y) {
            aplImpulse = new Vector2(0.07f, jumpImpulse);
        }else if (x < aimPos.x && y > aimPos.y){
            aplImpulse = new Vector2(0.07f, 0f);
        }else if (x > aimPos.x && y < aimPos.y){
            aplImpulse = new Vector2(-0.07f, jumpImpulse);
        }else if (x > aimPos.x && y > aimPos.y){
            aplImpulse = new Vector2(-0.07f, 0f);
        }
        jumpImpulse = 4f;


        aplImpulse = avoid_obstacles(aplImpulse, jumpImpulse, aimPos);
        /*if (touchedGround && aplImpulse.y <= 0){
            yscale = 0;
            //System.out.print("yscale =" + yscale + "\n");
        }*/

        ////System.out.print(aplImpulse.x + " " + aplImpulse.y + "\n\n");
        //dimish impulse if necessary
        aplImpulse.x += aplImpulse.x * random() * .4f;
        aplImpulse.scl(xscale, yscale);

        //apply impulsew
        b2dbody.applyLinearImpulse(aplImpulse, b2dbody.getWorldCenter(), true);
        ////System.out.print(touchedGround + " " + yscale + "\n");
    }

    public Vector2 avoid_obstacles(Vector2 aplImpulse, float jumpImpulse, Vector2 aimPos){
        for(Fixture obstacle : obstaclesInWay){

            Vector2 velocity = b2dbody.getLinearVelocity();
            Vector2 pos = b2dbody.getPosition();
            Body obsBody = obstacle.getBody();
            Vector2 obsPos = obsBody.getPosition();

            PolygonShape shape = (PolygonShape)obstacle.getShape();

            if (velocity.x >= 0 && obsPos.x >= pos.x && aimPos.x > obsPos.x){
                aplImpulse.y = jumpImpulse;
                ////System.out.print("jump " + pos.x + " \n");
            }else if(velocity.x <= 0 && obsPos.x <= pos.x && aimPos.x < obsPos.x){
                aplImpulse.y = jumpImpulse;
            }
        }

        return aplImpulse;
    }


    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX()/ ChainsawRun.PPM, getY()/ ChainsawRun.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2dbody = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();

        radius = 5;
        shape.setRadius(radius/ ChainsawRun.PPM);
        fdef.shape = shape;



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
        vertice[0] = new Vector2(-60, 1).scl(1/ ChainsawRun.PPM);
        vertice[1] = new Vector2( 60, 1).scl(1/ ChainsawRun.PPM);
        vertice[2] = new Vector2(0,0.1f).scl(1/ChainsawRun.PPM);
        SensoerShape.set(vertice);
        fdef.shape = SensoerShape;
        fdef.isSensor = true;
        fdef.filter.categoryBits = ChainsawRun.ENEMY_SENSOR_BIT;
        fdef.restitution = 0;

        b2dbody.createFixture(fdef).setUserData(this);
        bodyPack = screen.getSpriteManager().add_animation_to_body(b2dbody, "RABBIT", true, 40/ppm, 40/ppm, 0/ppm, 17/ppm);

    }



    public void hitMe() {
        health --;
        ////System.out.print("enemy hit on head\n");



        if (!setToDestroy){
            screen.getParticleManager().add_effect_to_body(b2dbody, "BLOOD1");
        }




    }



    private State getState(){
      return State.RUNNING;
    }


}
