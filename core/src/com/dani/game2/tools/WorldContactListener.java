package com.dani.game2.tools;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.dani.game2.ChainsawRun;
import com.dani.game2.Screens.PlayScreen;
import com.dani.game2.Sprites.Enemys.Enemy;
import com.dani.game2.Sprites.Player;
import com.dani.game2.Sprites.items.Item;

/**
 * Created by root on 20.07.16.
 */
public class WorldContactListener implements ContactListener {

    private PlayScreen screen;

    public WorldContactListener(PlayScreen screen){
        this.screen = screen;
    }

    @Override
    public void beginContact(Contact contact) {
        ////System.out.print("Contact beginn start \n");
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        ////System.out.print("2 \n");

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef) {

            case ChainsawRun.ENEMY_BIT | ChainsawRun.RABBIT_OBSTACLE_BIT:
                ////System.out.print("Enemy-Object collision\n");
                if (fixA.getFilterData().categoryBits == ChainsawRun.ENEMY_BIT) {
                    ((Enemy) fixA.getUserData()).reverseVelocity(true, false);
                } else {
                    ((Enemy) fixB.getUserData()).reverseVelocity(true, false);
                }
                break;


            case ChainsawRun.ITEM_BIT | ChainsawRun.RABBIT_OBSTACLE_BIT:
                ////System.out.print("Enemy-Object collision\n");
                if (fixA.getFilterData().categoryBits == ChainsawRun.ITEM_BIT) {
                    ((Item) fixA.getUserData()).reverseVelocity(true, false);
                } else {
                    ((Item) fixB.getUserData()).reverseVelocity(true, false);
                }
                break;

            case ChainsawRun.ITEM_BIT | ChainsawRun.PLAYER_BIT:
                if (fixB.getUserData() != null) {
                    if (fixA.getFilterData().categoryBits == ChainsawRun.ITEM_BIT) {
                        ////System.out.print(fixB.getUserData());
                        ((Item) fixA.getUserData()).use((Player) fixB.getUserData());
                    } else {
                        ////System.out.print(fixA.getUserData());
                        ((Item) fixB.getUserData()).use((Player) fixA.getUserData());
                    }
                }
                break;

            case ChainsawRun.PLAYER_LEFT_HAMMER_BIT | ChainsawRun.ENEMY_BIT:
                ////System.out.print("colision left hammer | enemy\n");
                if (fixA.getFilterData().categoryBits == ChainsawRun.PLAYER_LEFT_HAMMER_BIT){
                    ((Player)fixA.getUserData()).enemys_in_left_zone.add((Enemy)fixB.getUserData());
                }else
                    ((Player)fixA.getUserData()).enemys_in_left_zone.add((Enemy)fixB.getUserData());

                break;

            case ChainsawRun.PLAYER_RIGHT_HAMMER_BIT | ChainsawRun.ENEMY_BIT:
                ////System.out.print("colision right hammer | enemy\n");
                if (fixA.getFilterData().categoryBits == ChainsawRun.PLAYER_RIGHT_HAMMER_BIT){
                    ((Player)fixA.getUserData()).enemys_in_right_zone.add((Enemy)fixB.getUserData());
                }else
                    ((Player)fixB.getUserData()).enemys_in_right_zone.add((Enemy)fixA.getUserData());
                break;

            case ChainsawRun.SAWBLADE_BIT | ChainsawRun.ENEMY_BIT:
                if (fixA.getFilterData().categoryBits == ChainsawRun.ENEMY_BIT){
                    ((Enemy)fixA.getUserData()).hitMe();
                }else{
                    ((Enemy)fixB.getUserData()).hitMe();
                }
                break;


            case ChainsawRun.ENEMY_BIT | ChainsawRun.GROUND_BIT:
                if (fixA.getFilterData().categoryBits == ChainsawRun.ENEMY_BIT){
                    ((Enemy)fixA.getUserData()).touchedGround = true;
                }else{
                    ((Enemy)fixB.getUserData()).touchedGround = true;
                }
                break;

            case ChainsawRun.ENEMY_SENSOR_BIT | ChainsawRun.PLAYER_BIT:
                ////System.out.println("case ChainsawRun.ENEMY_SENSOR_BIT | ChainsawRun.PLAYER_BIT");
                if (fixA.getFilterData().categoryBits == ChainsawRun.ENEMY_SENSOR_BIT){
                    ((Enemy)fixA.getUserData()).player_detected();
                }else {
                    ((Enemy)fixB.getUserData()).player_detected();
                }


            case ChainsawRun.ENEMY_SENSOR_BIT | ChainsawRun.RABBIT_OBSTACLE_BIT:
                ////System.out.print("Sensor touched obstacle:\n");
                if (fixA.getFilterData().categoryBits == ChainsawRun.ENEMY_BIT){
                    ((Enemy)fixA.getUserData()).obstaclesInWay.add(fixB);
                    //System.out.print(((Enemy)fixA.getUserData()).obstaclesInWay.size);
                }else{
                    ((Enemy)fixB.getUserData()).obstaclesInWay.add(fixA);
                    //System.out.print(((Enemy)fixB.getUserData()).obstaclesInWay.size);
                }
               // //System.out.print("sesorend\n");
                break;

            case ChainsawRun.ENEMY_BIT | ChainsawRun.PLAYER_BIT:
                //System.out.print("dead mario");
                if (fixA.getFilterData().categoryBits == ChainsawRun.ENEMY_BIT){
                    if (((Enemy)fixA.getUserData()).setToDestroy == false){

                        ((Player)fixB.getUserData()).enemyContact();
                        }
                }else{
                    if (((Enemy)fixB.getUserData()).setToDestroy == false){
                        ((Player)fixA.getUserData()).enemyContact();
                    }
                }
                break;

            case ChainsawRun.SAWBLADE_BIT | ChainsawRun.PLAYER_BIT:
                //System.out.print("dead mario");
                if (fixA.getFilterData().categoryBits == ChainsawRun.SAWBLADE_BIT){
                        ((Player)fixB.getUserData()).enemyContact();
                }else{
                        ((Player)fixA.getUserData()).enemyContact();
                }
                break;

            case ChainsawRun.PR_BIT | ChainsawRun.ENEMY_BIT:
                if (fixA.getFilterData().categoryBits == ChainsawRun.ENEMY_BIT){
                    ((Enemy)fixA.getUserData()).hitMe();
                }
                else {
                    ((Enemy)fixB.getUserData()).hitMe();
                }
                break;

            case ChainsawRun.GROUND_BIT | ChainsawRun.PLAYER_SENSOR_BIT:
                if (fixA.getFilterData().categoryBits == ChainsawRun.PLAYER_BIT) {
                    ((Player) fixA.getUserData()).touching_ground = true;
                    screen.lastGroundPos = new Vector2(fixA.getBody().getPosition());
                }else {
                    ((Player) fixB.getUserData()).touching_ground = true;
                    screen.lastGroundPos = new Vector2(fixB.getBody().getPosition());
                }
                break;


        }

    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef) {
            case ChainsawRun.PLAYER_LEFT_HAMMER_BIT | ChainsawRun.ENEMY_BIT:
                ////System.out.print("remove enemy\n");
                if (fixA.getFilterData().categoryBits == ChainsawRun.PLAYER_LEFT_HAMMER_BIT) {
                    ((Player) fixA.getUserData()).enemys_in_left_zone.removeValue((Enemy) fixB.getUserData(), true);
                } else {
                    ((Player) fixA.getUserData()).enemys_in_left_zone.removeValue((Enemy) fixB.getUserData(), true);
                }

                break;

            case ChainsawRun.PLAYER_RIGHT_HAMMER_BIT | ChainsawRun.ENEMY_BIT:
                ////System.out.print("remove enemy\n");
                if (fixA.getFilterData().categoryBits == ChainsawRun.PLAYER_RIGHT_HAMMER_BIT) {
                    ((Player) fixA.getUserData()).enemys_in_right_zone.removeValue((Enemy) fixB.getUserData(), true);
                } else {
                    ((Player) fixB.getUserData()).enemys_in_right_zone.removeValue((Enemy) fixA.getUserData(), true);
                }
                break;

            case ChainsawRun.ENEMY_BIT | ChainsawRun.GROUND_BIT:
                if (fixA.getFilterData().categoryBits == ChainsawRun.ENEMY_BIT){
                    ((Enemy)fixA.getUserData()).touchedGround = false;
                }else{
                    ((Enemy)fixB.getUserData()).touchedGround = false;
                }
                break;

            case ChainsawRun.ENEMY_SENSOR_BIT | ChainsawRun.RABBIT_OBSTACLE_BIT:
                ////System.out.print("end Snsor contact\n");
                if (fixA.getFilterData().categoryBits == ChainsawRun.ENEMY_SENSOR_BIT){
                    ((Enemy)fixA.getUserData()).obstaclesInWay.removeIndex(0);
                    ////System.out.print(((Enemy)fixA.getUserData()).obstaclesInWay.size);
                }else{
                    ((Enemy)fixB.getUserData()).obstaclesInWay.removeIndex(0);
                   // //System.out.print(((Enemy)fixB.getUserData()).obstaclesInWay.size);
                }
                ////System.out.print("end\n");
                break;

            case ChainsawRun.GROUND_BIT | ChainsawRun.PLAYER_SENSOR_BIT:
                if (fixA.getFilterData().categoryBits == ChainsawRun.PLAYER_BIT) {
                    ((Player) fixA.getUserData()).touching_ground = false;
                }else {
                    ((Player) fixB.getUserData()).touching_ground = false;
                }
                break;


        }

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
