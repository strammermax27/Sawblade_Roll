package com.dani.game2.Sprites.Enemys;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.dani.game2.Animation.SpriteManager;
import com.dani.game2.ChainsawRun;
import com.dani.game2.Screens.PlayScreen;

/**
 * Created by root on 21.07.16.
 */
public abstract class Enemy extends Sprite {
    protected World world;
    protected PlayScreen screen;
    public Body b2dbody;
    public SpriteManager.BodyPack bodyPack;
    protected Array<TextureRegion> walkFrames;
    protected Array<TextureRegion>deadFrames;
    public Vector2 velocity;
    protected float reverseVelocityCount;
    public boolean touchedGround;
    public boolean setToDestroy;
    protected SpriteManager spmanager;
    protected boolean alive;

    public Array<Fixture> obstaclesInWay;

    protected int health;
    protected float dieAnimTime;
    protected boolean animRemoved;

    public Enemy(PlayScreen screen, float x, float y){
        this.world = screen.getWorld();
        this.screen = screen;
        spmanager = screen.getSpriteManager();
        walkFrames = new Array<TextureRegion>();
        deadFrames = new Array<TextureRegion>();
        setPosition(x,y);
        defineEnemy();
        reverseVelocityCount = 0;
        touchedGround = false;
        obstaclesInWay = new Array<Fixture>();
        alive = true;
        animRemoved = false;


        velocity = new Vector2(1, 0);
        b2dbody.setActive(false);
    }


    protected abstract void defineEnemy();
    protected abstract void die(float dt);
    protected abstract void move(float dt);
    protected abstract void hunt(float dt);
    public abstract void hitMe();

    public abstract void update(float dt);
    public abstract void dispose();
    public abstract void player_detected();

    public void reverseVelocity(boolean x, boolean y) {
        ////System.out.print("reverseVelocity\n");


        if(x)
            velocity.x = -velocity.x;
        if(y)
            velocity.y = -velocity.y;


    }

    protected void intitialize_dead(){

        b2dbody.setFixedRotation(false);
        b2dbody.applyAngularImpulse(30, true);


    }

    public void kill_me(){
        health = 0;
    }
}
