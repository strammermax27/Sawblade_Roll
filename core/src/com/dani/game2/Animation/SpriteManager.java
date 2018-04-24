package com.dani.game2.Animation;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Transform;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IdentityMap;
import com.dani.game2.ChainsawRun;
import com.dani.game2.Screens.PlayScreen;
import com.dani.game2.tools.Asset_Loader;

import static java.lang.Math.random;

/**
 * Created by root on 29.10.16.
 */
public final class SpriteManager {

    private PlayScreen screen;

    private Array<BodyPack> order;

    //Textures
    private static Array<TextureRegion> textureRegions;

    private static TextureRegion sawblade;
    private static TextureRegion frozenMikael;
    private static TextureRegion bWheel;
    private static TextureRegion fWheel;
    private static TextureRegion gun;

    //Animations
    private static Array<Animation> animations;

    private static Animation rebWalkAnim;
    private static Animation goatWalkAnim;
    private static Animation bDogAnim;
    private static Animation gDogAnim;
    private static Animation beeAnim;


    private static float pi = (float)Math.PI;

    private TextureRegion testTexture;

    private Asset_Loader ass_loader;

    public SpriteManager(PlayScreen screen) {
        this.screen = screen;
        ass_loader = screen.game.asset_loader;

        TextureAtlas tempAtlas = ass_loader.sprite_atlases.get(0);
        testTexture = tempAtlas.findRegion("guy");

        textureRegions = new Array<TextureRegion>();
        animations = new Array<Animation>();
        order = new Array<BodyPack>();
        load_sprites();



    }

    private void load_sprites(){

        make_region_arrays();

        make_anim_arrays();
    }

    private void make_region_arrays(){
        TextureAtlas tempAtlas;

        //Textures
        tempAtlas = ass_loader.sprite_atlases.get(0);


        bWheel          = tempAtlas.findRegion("bWheel", -1);
        fWheel          = tempAtlas.findRegion("fWheel");
        frozenMikael    = tempAtlas.findRegion("guy");
        gun             = tempAtlas.findRegion("Gun.jpeg");
        testTexture = tempAtlas.findRegion("guy");
        sawblade        = tempAtlas.findRegion("SpinningSawblade.0001");

        textureRegions.add(bWheel);
        textureRegions.add(fWheel);
        textureRegions.add(frozenMikael);
        textureRegions.add(sawblade);
        textureRegions.add(gun);


        //tempAtlas.dispose();
    }

    private void make_anim_arrays(){
        TextureAtlas tempAtlas;
        TextureAtlas tempAtlas2;
        TextureAtlas tempAtlas3;
        TextureAtlas tempAtlas4;
        TextureAtlas tempAtles5;
        Array<TextureRegion> tempFrames = new Array<TextureRegion>();
        String regionName;

        //rabbit
        tempAtlas = ass_loader.sprite_atlases.get(3);;

        for (int i = 13; i <= 56; i++) {
            regionName = "runnungGoodRabbit.00" + i;
            TextureRegion textureRegion = tempAtlas.findRegion(regionName);
            tempFrames.add(textureRegion);
        }

        rebWalkAnim = new Animation(0.4f, tempFrames);

        animations.add(rebWalkAnim);
        tempFrames.clear();

        //big_goat
        tempAtlas2 = ass_loader.sprite_atlases.get(1);

        for (int i = 0; i <= 30; i++) {
            if (i< 10)
                regionName = "walking_BigGoat_lowQuality.000" + i;
            else
                regionName = "walking_BigGoat_lowQuality.00" + i;

            TextureRegion textureRegion = tempAtlas2.findRegion(regionName);
            tempFrames.add(textureRegion);
        }

        goatWalkAnim = new Animation(1f, tempFrames);
        animations.add(goatWalkAnim);
        tempFrames.clear();

        //Bee
        for (int i = 0; i <= 18; i++) {
            if (i< 10)
                regionName = "flyingBee.000" + i;
            else
                regionName = "flyingBee.00" + i;

            TextureRegion textureRegion = tempAtlas2.findRegion(regionName);
            tempFrames.add(textureRegion);
        }

        beeAnim = new Animation(1f, tempFrames);
        animations.add(beeAnim);
        tempFrames.clear();

        //BDog
        tempAtlas3 = ass_loader.sprite_atlases.get(2);

        for (int i = 0; i <= 29; i++) {
            if (i< 10)
                regionName = "running_BDog_LowQuality.000" + i;
            else
                regionName = "running_BDog_LowQuality.00" + i;

            TextureRegion textureRegion = tempAtlas3.findRegion(regionName);
            tempFrames.add(textureRegion);
        }

        bDogAnim = new Animation(.4f, tempFrames);
        animations.add(bDogAnim);
        tempFrames.clear();


        //GDog
        for (int i = 0; i <= 19; i++) {
            if (i< 10)
                regionName = "Gdog_small_screen.000" + i;
            else
                regionName = "Gdog_small_screen.00" + i;

            TextureRegion textureRegion = tempAtlas3.findRegion(regionName);
            tempFrames.add(textureRegion);
        }

        gDogAnim = new Animation(.7f, tempFrames);
        animations.add(gDogAnim);
        tempFrames.clear();



    }

    public void flip(BodyPack pack, String direction){
        float[] list = pack.list;
        if (list != null){
            if (direction == "l"){
                ////System.out.print("flipping l\n");
                list[1] = 0;
            }
            else if (direction == "r"){
                list[1] = 1;
                ////System.out.print("flipping r\n");
                }

        }
    }

    public boolean isflip(BodyPack pack){
        float[] list = pack.list;
        if (list[1] == 1) return true;
        else return false;
    }

    public BodyPack add_animation_to_body(Body body, String animName, boolean change_direction, float width, float height, float xplus, float yplus){
        ////System.out.println("\n adding anim to body:  " + animName);
        float animIndex;
        float[] value = new float[8];
        boolean hadAnim = false;
        //BodyPack pack = new BodyPack(null, null);
        //visible
        value[0] = 1;

        //change direction
        if(change_direction) value[1] = 1;
        else value[1] = 0;



        Fixture fix = body.getFixtureList().get(0);

        animIndex = get_Anim_index(animName);

        if (animIndex < 1000 && !hadAnim) {
            ////System.out.print("i have a regoin\n");
            value[2] = animIndex;
            value[3] = -1;
        }

        if (animIndex >= 1000){
            ////System.out.print(" i have an anim \n");
            value[2] = animIndex - 1000;
            value[3] = 10 * (float)random();
            hadAnim = true;
        }

        value[4] = width;
        value[5] = height;
        value[6] = xplus;
        value[7] = yplus;

        BodyPack pack = new BodyPack(body, value);
        order.add(pack);
        ////System.out.print("   hihi " +order + "\n");
        return pack;

    }

    private float get_Anim_index(String category){
        float index = -2;


        String cDef = category;

            if (cDef == "PLAYER"){
                index =  textureRegions.indexOf(frozenMikael, false);
                //System.out.print("cdef = player\n");
                }
            else if (cDef == "SAWBLADE"){
                index = textureRegions.lastIndexOf(sawblade, false);
                //System.out.print("cdef = sawblade\n");
                }
            else if (cDef == "RABBIT"){
                index = animations.indexOf(rebWalkAnim, false) + 1000;
                ////System.out.print("cdef = rabbit\n");
                }
            else if (cDef == "B_WHEEL"){
                ////System.out.print("cdef = bwheel\n");
                index = textureRegions.indexOf(bWheel, false);}
            else if (cDef == "F_WHEEL"){
                ////System.out.print("cdef = fWheel\n");
                index = textureRegions.indexOf(fWheel, false);}
            else if (cDef == "NORMALG"){
                ////System.out.print("cDef = gun \n");
                index = textureRegions.indexOf(gun, false);}
            else if (cDef == "PR"){
                index = textureRegions.indexOf(gun, false);}
            else if (cDef == "BIG_GOAT"){
                index = animations.indexOf(goatWalkAnim, false) + 1000;}
            else if (cDef == "BDOG"){
                index = animations.indexOf(bDogAnim, false) + 1000;}
            else if (cDef == "GDOG"){
                index = animations.indexOf(gDogAnim, false) + 1000;}
            else if (cDef == "BEE"){
                index = animations.indexOf(beeAnim, false)+ 1000;}







        ////System.out.print("index in get anim index: " + index +"\n");
        return index;
    }

    private TextureRegion getRegion(float index){

        TextureRegion region = textureRegions.get((int)index);

        return region;
        //return testTexture;
    }

    private Animation get_Anim(float index){

        Animation anim = animations.get((int) index);

        return anim;
    }




    public void makevisible(BodyPack pack, boolean visible){
        if(visible)pack.list[1] = 1;
        else pack.list[1] = 0;
    }

    public void remove(BodyPack pack){
        order.removeValue(pack, false);
        ////System.out.println("value removed: " + order.removeValue(pack, false));
    }

    public void update_anims(float dt){
        float[] list;

        ////System.out.print("update anims:\n");

        for (BodyPack pack : order){
            list = pack.list;
            ////System.out.print("   "+ list[3] + "\n");
            if (list[3] >= 0){
                ////System.out.print("updating anim time \n");
                list[3] += 1;
            }
        }
    }

    public void draw_spites(SpriteBatch batch){
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

        for (BodyPack pack: order){
            list = pack.list;
            if (list[0] == 1) {
                body = pack.body;

                rotation = body.getTransform().getRotation() * (180/ pi);
                posx = body.getPosition().x + list[6];
                posy = body.getPosition().y + list[7];
                width = list[4];
                height = list[5];

                if (list[2] == -2 ){System.out.print("waring: invalid order member\n");}

                else if (list[3] < 0) {
                    tempregion = getRegion(list[2]);


                    if(list[2] == 0){
                        tempregion.flip(true, false);
                        ////System.out.print("flipping \n");
                    }

                    batch.draw(tempregion, posx, posy, -list[6] , -list[7], width, height, 1, 1, rotation);
                    //batch.draw_polygons(tempregion, width,height, (Affine2) body.getTransform());
                    ////System.out.print("tempregion:  " + tempregion + "\n");


                } else if (list[3] >= 0) {
                   // //System.out.print("drawing ainamtion\n");
                    tempAnim = get_Anim(list[2]);
                   // //System.out.println("\n\na");

                    tempregion = (TextureRegion) tempAnim.getKeyFrame(list[3], true);

                    if (list[1] == 0){
                        ////System.out.println("normal flip:     " + list[2]);
                        if(tempregion.isFlipX())
                            tempregion.flip(true, false);

                    }
                    else if (list[1] == 1){
                        ////System.out.println("flipping 2       " + list[2]);
                        if (!tempregion.isFlipX())
                            tempregion.flip(true, false);
                    }

                    //                               origin dk           scale dk
                    batch.draw(tempregion, posx - width/2, posy - height/2, width/2,height/2, width, height, 1, 1, rotation);

                    ////System.out.print("fliptempregion:  " + tempregion + "\n");
                    ////System.out.println("e");
                }

            }
        }



    }

    public class BodyPack{
        private float[] list;
        private Body body;


        private BodyPack(Body body, float[] list){
            this.body = body;
            this.list = list;
        }


    }



}

