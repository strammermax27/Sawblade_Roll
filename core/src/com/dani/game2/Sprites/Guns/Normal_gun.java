package com.dani.game2.Sprites.Guns;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.dani.game2.ChainsawRun;
import com.dani.game2.Screens.PlayScreen;

/**
 * Created by root on 05.11.16.
 */
public class Normal_gun extends Gun{


    protected boolean automatic;
    protected float firingRate;
    protected float prSize;
    protected float prMass; //for kickback
    protected float prSpeed;
    protected float percision;
    protected float prsPerShot;

    public Normal_gun(Body body, PlayScreen screen) {
        super(body, screen, ChainsawRun.NORMALG_BIT);
    }


    @Override
    protected void make_values(Gun gun) {


        gun.automatic = false;
        gun.firingRate = 20;
        gun.prSize = 5;
        gun.prMass = 10;
        gun.prSpeed = 100;
        gun.prsPerShot = 1;
        gun.Unpercision = 0.0f;
        gun.flytime = 2;

    }

    @Override
    protected void define() {
        BodyDef bDef = new BodyDef();
        FixtureDef fDef = new FixtureDef();

        bDef.position.set(40/ppm + ownerBody.getPosition().x, 28/ppm + ownerBody.getPosition().y);
        bDef.type = BodyDef.BodyType.DynamicBody;


        fDef.filter.categoryBits = ChainsawRun.NORMALG_BIT;
        fDef.filter.maskBits = ChainsawRun.RABBIT_OBSTACLE_BIT;
        fDef.shape = new CircleShape();
        fDef.shape.setRadius(10/ppm);
        fDef.density = 0f;
        fDef.friction = 0;

        ownFixture = ownerBody.createFixture(fDef);


        screen.getSpriteManager().add_animation_to_body(ownerBody, "NORMALG", false, 20/ppm, 20/ppm, -10/ppm, -10/ppm);
    }
}
