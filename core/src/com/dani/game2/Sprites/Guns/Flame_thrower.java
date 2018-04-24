package com.dani.game2.Sprites.Guns;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.dani.game2.ChainsawRun;
import com.dani.game2.Screens.PlayScreen;

/**
 * Created by root on 04.04.17.
 */

public class Flame_thrower extends Gun{


    protected boolean automatic;
    protected float firingRate;
    protected float prSize;
    protected float prMass; //for kickback
    protected float prSpeed;
    protected float percision;
    protected float prsPerShot;

    public Flame_thrower(Body body, PlayScreen screen) {
        super(body, screen, ChainsawRun.NORMALG_BIT);
    }


    @Override
    protected void make_values(Gun gun) {


        gun.automatic = true;
        gun.firingRate = 20;
        gun.prSize = 10;
        gun.prMass = .1f;
        gun.prSpeed = 10;
        gun.prsPerShot = 10;
        gun.Unpercision = 0.4f;
        gun.flytime = .4f;

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
