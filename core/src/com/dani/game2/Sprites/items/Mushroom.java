package com.dani.game2.Sprites.items;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

/**
 * Created by root on 01.08.16.
 */
public class Mushroom extends Item{
    public Mushroom(com.dani.game2.Screens.PlayScreen screen, float x, float y) {
        super(screen, x, y);
        //System.out.print(" init Mushroom ");
        setRegion(screen.getAtlas().findRegion("mushroom"), 0, 0, 16, 16);
        velocity = new Vector2(0.7f,0);
        //setPosition(90, 90);
    }




    @Override
    public void defineItem() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();

        float radius = (8);
        shape.setRadius(radius/ com.dani.game2.ChainsawRun.PPM);
        fdef.shape = shape;

        fdef.filter.categoryBits = com.dani.game2.ChainsawRun.ITEM_BIT;
        fdef.filter.maskBits = com.dani.game2.ChainsawRun.PLAYER_BIT |
                com.dani.game2.ChainsawRun.RABBIT_OBSTACLE_BIT |
                com.dani.game2.ChainsawRun.GROUND_BIT;

        body.createFixture(fdef).setUserData(this);

    }

    @Override
    public void use(com.dani.game2.Sprites.Player player) {
        //System.out.print(player);
        //player.grow();
        destroy();
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        velocity.y = body.getLinearVelocity().y;
        body.setLinearVelocity(velocity);
    }


}
