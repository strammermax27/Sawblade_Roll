package com.dani.game2.Sprites.items;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.dani.game2.Sprites.Player;

/**
 * Created by root on 01.08.16.
 */
public abstract class Item extends Sprite {

    protected com.dani.game2.Screens.PlayScreen screen;
    protected World world;
    protected Vector2 velocity;
    protected boolean toDestroy;
    protected boolean destroyed;
    protected Body body;

    public Item(com.dani.game2.Screens.PlayScreen screen, float x, float y){
        this.screen = screen;
        this.world = screen.getWorld();
        setPosition(x,y);
        //System.out.print(x+ " " + y + "\n");
        setBounds(getX(), getY(), 16 / com.dani.game2.ChainsawRun.PPM, 16 / com.dani.game2.ChainsawRun.PPM);
        defineItem();
        toDestroy = false;
        destroyed = false;
    }

    public  abstract void defineItem();
    public abstract void use(Player player);

    public void update(float dt){
        if(toDestroy && !destroyed){
            world.destroyBody(body);
            destroyed = true;
        }
    }

    public void draw(Batch batch){
        if(!destroyed){
            super.draw(batch);
        }
    }

    public void destroy(){
        toDestroy = true;
    }


    public void reverseVelocity(boolean x, boolean y){
        ////System.out.print("reverseVelocity\n");
        if(x)
            velocity.x = -velocity.x;
        if(y)
            velocity.y = -velocity.y;
    }
}

