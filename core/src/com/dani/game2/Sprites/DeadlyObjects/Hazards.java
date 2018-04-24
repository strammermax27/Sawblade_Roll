package com.dani.game2.Sprites.DeadlyObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.dani.game2.Screens.PlayScreen;

/**
 * Created by root on 04.08.16.
 */
public abstract class Hazards extends Sprite{
    protected PlayScreen screen;
    protected Shape shape;
    protected World world;
    protected Vector2 position;
    protected MapObject object;




    public Hazards(PlayScreen screen, Shape shape, Vector2 position, MapObject object){
        this.screen = screen;
        this.world = screen.getWorld();
        this.shape = shape;
        this.position = position;
        this.object = object;

        define();
    }


    protected abstract void define();
    public abstract void dispose();
}
