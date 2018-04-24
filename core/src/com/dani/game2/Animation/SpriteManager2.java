package com.dani.game2.Animation;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IdentityMap;
import com.dani.game2.ChainsawRun;
import com.dani.game2.Screens.PlayScreen;

import static java.lang.Math.random;

/**
 * Created by root on 29.10.16.
 */
/*public final class SpriteManager2 {

    private PlayScreen screen;

    private IdentityMap<Body, Sprite> order;

    //Textures
    private static Array<Sprite> Sprites;

    private static Sprite sawblade;
    private static Sprite frozenMikael;
    private static Sprite bWheel;
    private static Sprite fWheel;
    private static Sprite gun;

    //Animations
    private static Array<Animation> animations;

    private static Animation rebWalkAnim;

    private static float pi = (float)Math.PI;

    private TextureRegion testTexture;

    public SpriteManager2(PlayScreen screen) {
        this.screen = screen;

        TextureAtlas tempAtlas = new TextureAtlas("animations/Texturepacks/texPack1/txtPack1.txt");
        testTexture = tempAtlas.findRegion("guy");

        Sprites = new Array<Sprite>();
        animations = new Array<Animation>();
        //order = new IdentityMap<Body, float[]>();
        load_sprites();



    }

    private void load_sprites(){

        make_region_arrays();

        make_anim_arrays();
    }

    private void make_region_arrays(){
        TextureAtlas tempAtlas;

        //Textures
        tempAtlas = new TextureAtlas("animations/Texturepacks/texPack1/txtPack1.txt");

        bWheel          = new Sprite(tempAtlas.findRegion("bWheel", -1));
        fWheel          = new Sprite(tempAtlas.findRegion("fWheel"));
        frozenMikael    = new Sprite(tempAtlas.findRegion("guy"));
        gun             = new Sprite(tempAtlas.findRegion("Gun.jpeg"));
        sawblade        = new Sprite(tempAtlas.findRegion("SpinningSawblade.0001"));

        Sprites.add(bWheel);
        Sprites.add(fWheel);
        Sprites.add(frozenMikael);
        Sprites.add(sawblade);
        Sprites.add(gun);

        tempAtlas.dispose();
    }

    private void make_anim_arrays(){
        TextureAtlas tempAtlas;
        Array<TextureRegion> tempFrames = new Array<TextureRegion>();
        String regionName;

        //rabbit
        tempAtlas = new TextureAtlas("animations/runningGoodRabbit/runningGoodRabbit.txt");

        for (int i = 13; i <= 56; i++) {
            regionName = "runnungGoodRabbit.00" + i;
            TextureRegion textureRegion = tempAtlas.findRegion(regionName);
            tempFrames.add(textureRegion);
        }

        rebWalkAnim = new Animation(0.4f, tempFrames);

        animations.add(rebWalkAnim);

    }

    public void flip(Body body){
        float[] list = order.get(body);
        if (list[1] ==  0){
            //System.out.print("flipping back\n");
            list[1] = 1;}
        else{
            list[1] = 0;
            //System.out.print("flipping forward\n");}

    }

    public boolean isflip(Body body){
        float[] list = order.get(body);
        if (list[1] == 1) return true;
        else return false;
    }

    public void add_animation_to_body(Body body, boolean change_direction, float width, float height, float xplus, float yplus){
        ////System.out.print("adding anim to body\n");
        float animIndex;
        float[] value = new float[8];
        boolean hadAnim = false;

        //visible
        value[0] = 1;

        //change direction
        if(change_direction) value[1] = 1;
        else value[1] = 0;



        for(Fixture fix: body.getFixtureList()){

            animIndex = get_Anim_index(fix.getFilterData().categoryBits);

            if (animIndex < 1000 && !hadAnim) {
                //System.out.print("i have a regoin\n");
                value[2] = animIndex;
                value[3] = -1;
            }

            if (animIndex >= 1000){
                //System.out.print(" i have an anim \n");
                value[2] = animIndex - 1000;
                value[3] = 10 * (float)random();
                hadAnim = true;
            }

            value[4] = width;
            value[5] = height;
            value[6] = xplus;
            value[7] = yplus;


            order.put(body, value);
            ////System.out.print("   hihi " +order + "\n");

        }



    }

    private float get_Anim_index(Short category){
        float index = -2;


        int cDef = category;

            if (cDef == ChainsawRun.GROUND_BIT)
                index = -2;
            else if (cDef == ChainsawRun.PLAYER_BIT){
                index =  Sprites.indexOf(frozenMikael, false);
                //System.out.print("cdef = player\n");}
            else if (cDef == ChainsawRun.SAWBLADE_BIT){
                index = Sprites.lastIndexOf(sawblade, false);
                //System.out.print("cdef = sawblade\n");}
            else if (cDef == ChainsawRun.ENEMY_SENSOR_BIT)
                index = -2;
            else if (cDef == ChainsawRun.RABBIT_OBSTACLE_BIT)
                index = -2;
            else if (cDef == ChainsawRun.ENEMY_BIT){
                index = animations.indexOf(rebWalkAnim, false) + 1000;
                //System.out.print("cdef = rabbit\n");}
            else if (cDef == ChainsawRun.DESTROYED_BIT){
                index = -2;}
            else if (cDef == ChainsawRun.ITEM_BIT){
                index = -2;}
            else if (cDef == ChainsawRun.B_WHEEL_BIT){
                //System.out.print("cdef = bwheel\n");
                index = Sprites.indexOf(bWheel, false);}
            else if (cDef == ChainsawRun.F_WHEEL_BIT){
                //System.out.print("cdef = fWheel\n");
                index = Sprites.indexOf(fWheel, false);}
            else if (cDef == ChainsawRun.NORMALG_BIT){
                //System.out.print("cDef = gun \n");
                index = Sprites.indexOf(gun, false);
            }
            else if (cDef == ChainsawRun.PR_BIT){
                index = Sprites.indexOf(gun, false);}



        ////System.out.print("index in get anim index: " + index +"\n");
        return index;
    }

    private TextureRegion getRegion(float index){

        TextureRegion region = Sprites.get((int)index);
        return region;
    }

    private Animation get_Anim(float index){

        Animation anim = animations.get((int) index);

        return anim;
    }




    public void makevisible(Body body, boolean visible){
        if(visible)order.get(body)[1] = 1;
        else order.get(body)[1] = 0;
    }

    public void remove(Body body){
        order.remove(body);
    }

    public void update_anims(float dt){
        float[] list;

        ////System.out.print("update anims:\n");

        for (IdentityMap.Entry ent : order){
            list = order.get((Body) ent.key);
            ////System.out.print("   "+ list[3] + "\n");
            if (list[3] >= 0){
                ////System.out.print("updating anim time \n");
                list[3] += 1;
            }
        }
    }

    public void draw_effects(SpriteBatch batch){
        //update_anims(world, dt);
        ////System.out.print("drawing sprites\n");

        float[] list;
        Body body;
        float rotation;
        float posx;
        float posy;
        float width;
        float height;

        TextureRegion tempregion;
        Animation tempAnim;

        //batch.draw_polygons(testTexture,0,0,10,10);

        for (IdentityMap.Entry ent: order){
            list = order.get((Body) ent.key);
            if (list[0] == 1) {
                body = (Body)ent.key;

                rotation = body.getTransform().getRotation() * (180/ pi);
                posx = body.getPosition().x + list[6];
                posy = body.getPosition().y + list[7];
                width = list[4];
                height = list[5];

                if (list[2] == -2 ){//System.out.print("waring: invalid order member\n");}

                else if (list[3] < 0) {
                    ////System.out.print(list[3] + "drawing texture\n");

                    tempregion = getRegion(list[2]);

                    if(list[2] == 0){
                        tempregion.flip(true, false);
                        //System.out.print("flipping \n");
                    }

                    batch.draw_polygons(tempregion, posx, posy, -list[6] , -list[7], width, height, 1, 1, rotation);
                    //batch.draw_polygons(tempregion, width,height, (Affine2) body.getTransform());



                } else if (list[3] >= 0) {
                   // //System.out.print("drawing ainamtion\n");
                    tempAnim = get_Anim(list[2]);


                    tempregion = tempAnim.getKeyFrame(list[3], true);

                    if (list[2] == 1)
                        tempregion.flip(true, false);
                    //                               origin dk           scale dk
                    batch.draw_polygons(tempregion, posx - width/2, posy - height/2, width/2,height/2, width, height, 1, 1, rotation);

                }
            }
        }



    }


}

*/