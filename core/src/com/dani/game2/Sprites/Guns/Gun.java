package com.dani.game2.Sprites.Guns;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.WheelJoint;
import com.badlogic.gdx.physics.box2d.joints.WheelJointDef;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IdentityMap;
import com.dani.game2.ChainsawRun;
import com.dani.game2.Screens.PlayScreen;

import static com.badlogic.gdx.math.MathUtils.cos;
import static com.badlogic.gdx.math.MathUtils.random;


/**
 * Created by root on 05.11.16.
 */
public abstract class Gun extends Sprite {

    protected Fixture ownFixture;
    public Body ownerBody;
    public Array<Body> projectile_bodys;
    public IdentityMap<Body, float[]> projectiles;

    protected WheelJoint joint;

    public Vector2 aimPoint;
    public boolean loked = false;

    protected boolean automatic;//   = false;
    protected float firingRate ;//   = 50;
    protected float prSize     ;//   = 5;
    protected float prMass     ;//   = 10; //for kickback
    protected float prSpeed    ;//   = 100;
    protected float Unpercision;//   = 1;
    protected float prsPerShot ;//   = 1;
    protected float flytime    ;//   = 1f;


    public PlayScreen screen;
    public World world;

    protected Short GunName;

    protected final float ppm = ChainsawRun.PPM;

    protected float dt_lastShoot;

    public Gun (Body body, PlayScreen screen, Short GunName){
        this.screen = screen;
        this.world = screen.getWorld();
        this.GunName = GunName;

        make_values(this);
        /*if (values[0] == 1)automatic = true;
        else  automatic = false;
        firingRate = values[1];
        prSize = values[2];
        prMass = values[3];
        prSpeed= values[4];
        Unpercision= values[5];
        prsPerShot= values[6];
        flytime = values[7];*/
       /* automatic   = false;//   is very ugly
        if(automatic) values[0] = 1;
        else values[0] = 0;
        firingRate  = 5;
        values[1] = firingRate;
        prSize      = 5;
        values[2] = prSize;
        prMass = 10;
        values[3] = prMass;
        prSpeed     = 100;
        values[4] = prSpeed;
        Unpercision   = 1;
        values[5] = Unpercision;
        prsPerShot  = 1;
        values[6] = prsPerShot;
        flytime  = 1f;
        values[7] = flytime;*/



        ////System.out.print("   pr_mass: " + prMass +"\n");

        ownerBody = body;
        projectile_bodys = new Array<Body>();
        projectiles = new IdentityMap<Body, float[]>();
        define();
        create_projectiles();

        dt_lastShoot = 0;
    }

    public void update(float dt){

        update_aim_pos();


        update_flying_prs(dt);
        put_prs_sleeping(dt);

        dt_lastShoot += dt;
    }



    protected void update_aim_pos(){

    }


    protected void put_prs_sleeping(float dt){
        for (IdentityMap.Entry<Body, float[]> entry : projectiles){
            if(entry.value[0] >= flytime){
                ////System.out.print("put pr into sleep\n");
                entry.value[0] = 0;
                entry.value[1] = 0;
                entry.key.setActive(false);
                entry.key.setTransform(ownerBody.getPosition().x - 10,ownerBody.getPosition().y - 10, 0);
            }
        }

    }

    protected void update_flying_prs(float dt){

        for (IdentityMap.Entry<Body, float[]> entry : projectiles){

            if(entry.value[1] == 1){
                entry.value[0] += dt;
                ////System.out.print("updating a fliying pr \n");
            }
        }
    }

    protected void create_projectiles(){
        Body tempBody;

        float mult = 1;
        if(!automatic)mult = 0.4f;

        for (float i = 0; i <= prsPerShot * firingRate * flytime * mult + 1; i++) { //after one second shots will be recyclet
       // for (float i = 0; i<=1; i++){
            float[] list = new float[2];
            list[0] = 0;//flighttime
            list[1] = 0; // true1 false0 isflighing

            tempBody = define_projectile();
            projectile_bodys.add(tempBody);
            float hans = list.length;
            projectiles.put(tempBody, list);


        }

    }

    protected Body define_projectile(){




        Body prBody;

        BodyDef bdef = new BodyDef();
        bdef.position.set(10,10);
        bdef.type = BodyDef.BodyType.DynamicBody;
        prBody = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();


        shape.setRadius(prSize/ ppm);



        //fdef.friction = 2;
        fdef.filter.categoryBits = ChainsawRun.PR_BIT;
        fdef.filter.maskBits = ChainsawRun.GROUND_BIT
                | ChainsawRun.ENEMY_BIT;
        ////System.out.print("in mario start: \n RABBIT_OBSTACLE_BIT:" + com.dani.game2.ChainsawRun.RABBIT_OBSTACLE_BIT + "\n ENEMY_BIT:" + com.dani.game2.ChainsawRun.ENEMY_BIT + "\n  DESTROYED_BIT" + com.dani.game2.ChainsawRun.DESTROYED_BIT + "\n mario end \n");

        fdef.density = prMass/(prSize/ppm);
        fdef.shape = shape;
        prBody.createFixture(fdef).setUserData(this);

        screen.getSpriteManager().add_animation_to_body(prBody, "PR", false, prSize*2/ppm, prSize*2/ppm, -prSize/ppm, -prSize/ppm);

        prBody.setActive(false);

        return prBody;
    }

    public void shoot(){

        if((dt_lastShoot > 1/firingRate) && !loked){
            Gdx.input.vibrate(50);
            float final_imp =  prSpeed;

            if(!automatic) {
                loked = true;
            }

            ////System.out.print("shoot\n");
            float additor = 0;
            float iterate_count = 0;

            Body tempBody;

            Vector2 pos = ownerBody.getPosition();

            //create impulse impulse: (-294736.34,294836.34)    amix:  -1.190033  aimy: 1.1904367 corrector: 247670.73

            for (int i = 0; i<= prsPerShot - 1; i ++) {
                // //System.out.print("applying impulse to body start\n");
                float aimx = aimPoint.x - pos.x ;
                float aimy = aimPoint.y - pos.y;
                boolean alphaBiggerThanPi = false;
                boolean aimxNegative = false;
                if(aimx < 0)aimxNegative = true;

                float c;
                float alpha; //radians
                float unpers = Unpercision * random();
                if (random() < 0.5)unpers *= -1;


                c = (float)Math.pow((Math.pow(aimx,2) + Math.pow(aimy,2)), 0.5);
                alpha = (float) Math.acos(aimy / c);

                ////System.out.print("\n\n" + alpha +"\n");
                alpha += 2* Math.PI * unpers;

                if(aimx < 0)alpha *= -1;
                if(alpha < 0)alpha = 2*(float)Math.PI - alpha;


                if(alpha > Math.PI && !aimxNegative)aimxNegative = true;
                if(alpha <= Math.PI && aimxNegative)aimxNegative = false;

                turn_to(alpha);
                ////System.out.print(alpha + "\n");

                aimy = cos(alpha) * c;
                aimx = (float) Math.pow(Math.pow(c,2) - Math.pow(aimy, 2)  ,0.5);

                if(aimxNegative)aimx *= -1;

                float posX = aimx;
                float posY = aimy;
                if (aimx < 0) posX *= -1;
                if (aimy < 0) posY *= -1;
                float corrector = final_imp/(posX + posY);
                Vector2 impulse = new Vector2(aimx*corrector, aimy*corrector);

                // //System.out.print("impulse: "+ impulse+ "    amix:  " +aimx+ "  aimy: "+ aimy+ "    aimpoint: " + aimPoint+  "    corrector: " + corrector + "     prSpeed: " + prSpeed +"\n");
                ////System.out.print(impulsex);





                while (projectiles.get((projectile_bodys.get(i + (int) additor)))[1] == 1 && iterate_count < projectiles.size - prsPerShot) {
                    additor += 1;
                    if (additor + i + 1 > projectiles.size) additor -= projectiles.size;
                    ////System.out.print("                          itreating through projectiles:" + additor + "\n");
                    iterate_count += 1;
                }
                //now we have found the index of the next free projectile
                if (iterate_count >= 1000) additor = projectiles.size * random();

                tempBody = projectile_bodys.get((int) (i + additor));


                projectiles.get(projectile_bodys.get(i + (int) additor))[1] = 1;

                tempBody.setActive(true);

                //   //System.out.print("   temP_body pos1" + tempBody.getPosition() + "\n");
                //set new Position, WARNING COULD CAUSE ERRORS
                tempBody.setTransform(ownerBody.getPosition(), 0);
                tempBody.setLinearVelocity(ownerBody.getLinearVelocity());

                //  //System.out.print("   temp_Body pos2:" + tempBody.getPosition() + "\n");


                tempBody.applyLinearImpulse(impulse, tempBody.getWorldCenter(), true);

                dt_lastShoot = 0;

                /*   //System.out.print("   id :" + tempBody +"\n");
                //System.out.print("   is_active: " + tempBody.isActive() + "\n");
                //System.out.print("applying impulse to body,, imp:" + impulse +"end\n");
                //System.out.print("\n");*/
                ////System.out.print("Check after shoot Start: \n");

                for (IdentityMap.Entry<Body, float[]> ent : projectiles) {
                    ////System.out.print("      id: " + ent.key + "   flightTime: " + ent.value[0] + "   isFlying: " + ent.value[1] + "\n");
                }

                ////System.out.print("Check after shoot End: \n \n \n");



            }
            float count = 0;
            for (IdentityMap.Entry<Body, float[]> ent : projectiles){
                if(ent.value[1] == 1)count += 1;
            }
            ////System.out.print(count + "\n");
        }
    }

    protected abstract void make_values(Gun gun);
    protected abstract void define();
    protected void turn_to(float alpha){


    }



}
