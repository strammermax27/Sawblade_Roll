package com.dani.game2.Sprites.DeadlyObjects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.utils.Array;
import com.dani.game2.ChainsawRun;
import com.dani.game2.Screens.PlayScreen;

/**
 * Created by root on 04.08.16.
 */
public class SawBlade extends Hazards {



    public Body body;

    private float angularVelocity;

    //private static TextureAtlas textureAtlas;
    public static Animation sawBladeAnim;
    private float renderTime;
    //private Array<TextureRegion> frames;

    public SawBlade(PlayScreen screen, Shape shape, Vector2 position, MapObject object) {
        super(screen, shape, position, object);

        /*frames = new Array<TextureRegion>();
        for(int i = 0; i< 2; i ++){
            frames.add(new TextureRegion(screen.getAtlas().findRegion("goomba"), i*16, 0, 16, 16));
        }
        spinAnimation = new Animation(0.4f, frames);
        renderTime = 0;*/
        set_angularVelocity();
        setBounds(getX() + getX(), getY(), body.getFixtureList().get(0).getShape().getRadius()*2, body.getFixtureList().get(0).getShape().getRadius()*2);
        //textureAtlas = new TextureAtlas("spinningBlade.txt");
        //loadAnimation();
        renderTime = 0;
    }

    @Override


    protected void define() {

        set_angularVelocity();


        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();


        bdef.angularVelocity = angularVelocity;
        bdef.position.set(position);

        bdef.type = BodyDef.BodyType.KinematicBody;
        //bdef.linearVelocity = new Vector2(0,0);
        ;
        //bdef.position.set(0,0);

        body = world.createBody(bdef);

        fdef.shape = shape;
        fdef.friction = 2;
        fdef.filter.categoryBits = ChainsawRun.SAWBLADE_BIT;
        //fdef.isSensor = true;

        body.createFixture(fdef);
        body.setLinearVelocity(0,0);
        body.setActive(false);

        float radius = shape.getRadius();

        screen.getSpriteManager().add_animation_to_body(body, "SAWBLADE", false, radius*2, radius*2, -radius, -radius);
        //body.setAngularVelocity(1, bdef.position);
        // ///System.out.print(body.getPosition() + "\n");
    }

    public void update(float dt){

        renderTime += dt;
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);

//        setRegion(sawBladeAnim.getKeyFrame(renderTime, true));
    }


    /*private void loadAnimation(){
        Array<TextureRegion> frames = new Array<TextureRegion>();
        String region = "";

        for (int i = 0; i <= 8; i++) {

            region = "SpinningSawblade.000" + i;


            TextureRegion textureRegion = screen.getAtlas().findRegion(region);
            if (object.getProperties().containsKey("left_spin")) {
                textureRegion.flip(true, false);
            }


            frames.add(textureRegion);
            //frames.add(screen.getAtlas().findRegion(("goomba"), 16, 0, 16, 16))
            //frames.add(new TextureRegion(screen.getAtlas().findRegion("goomba"), 16, 0, 16, 16));;

        }
        spinAnimation = new Animation(0.01f, frames);
        frames.clear();
    }*/

    private void set_angularVelocity(){

        if (object.getProperties().containsKey("right_spin"))
            angularVelocity = -10.1f;//-39;
        else
            angularVelocity = 10.1f;//39;

    }

    public void draw(Batch batch){
        ////System.out.print("drawing");
        super.draw(batch);
    }


    @Override
    public void dispose() {
        if (world != null){
            world.destroyBody(body);}
    }


    public Body getBody() {return body;}

}
