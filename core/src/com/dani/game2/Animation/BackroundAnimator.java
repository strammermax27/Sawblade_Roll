package com.dani.game2.Animation;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.dani.game2.Screens.PlayScreen;

/**
 * Created by root on 21.12.16.
 */

public class BackroundAnimator {

    PlayScreen screen;

    private java.lang.String world;


    private Texture old_layer;
    private float old_RelDistance;
    private float[] old_iterateList;

    //private LinkedHashMap<Texture, float[]> layers;
    private Array<Texture_Values_Pack> layers;
    private float[] iterateList;
    private TextureAtlas atlas;

    private Texture SkyLayer;

    private float camX;
    private float camY;


    public BackroundAnimator(PlayScreen playScreen, java.lang.String world){
        ////System.out.println("start: INIT BackroundAnimator");
        this.screen = playScreen;
        this.world = world;

        //layers = new LinkedHashMap<Texture, float[]>();
        layers = new Array<Texture_Values_Pack>();
        load_layers();
        resize();
        camX = screen.getGamecam().position.x;
        camY = screen.getGamecam().position.y;
       // //System.out.println("end: INIT BackroundAnimator");



        old_RelDistance = 9999;

    }

    private void load_layers(){
        ////System.out.println("    sart: load_layers");
        Texture tempTexture;
        float temp_partImageWidth  = screen.getGamecam().viewportWidth;
        float temp_partImageHeight = screen.getGamecam().viewportHeight;
        AssetManager manager = screen.game.manager;

        if (world =="normal") {
            ////System.out.println("        world = normal");



            //Sky
            SkyLayer =  manager.get("animations/backrounds/normal/skyImage.png");

            //clouds
            tempTexture = manager.get("animations/backrounds/normal/clouds.png");
            Vector2 temp_multi = new Vector2(3, .7f);
            float temp_timesDrawn = 4;
            float temp_RelDistance = .9f;
            float y_down_quotient = 1;
            addLayer(tempTexture, temp_partImageWidth, temp_partImageHeight, temp_multi, temp_timesDrawn, temp_RelDistance, y_down_quotient);

            //hills_b
            tempTexture = manager.get("animations/backrounds/normal/hills_b.png");//new Texture("animations/backrounds/normal/hills_b.png");
            tempTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
            temp_multi = new Vector2(8, 5);
            temp_timesDrawn = 4;
            temp_RelDistance = .8f;
            y_down_quotient = 6;
            addLayer(tempTexture, temp_partImageWidth, temp_partImageHeight, temp_multi, temp_timesDrawn, temp_RelDistance, y_down_quotient);

            //hills_f
            tempTexture = manager.get("animations/backrounds/normal/hills_f.png");
            temp_multi = new Vector2(8, 5);
            temp_timesDrawn = 4;
            temp_RelDistance = .7f;
            y_down_quotient = 10;
            addLayer(tempTexture, temp_partImageWidth, temp_partImageHeight, temp_multi, temp_timesDrawn, temp_RelDistance, y_down_quotient);
        }
            ////System.out.println("        imageWidth: " + temp_partImageWidth + "  imageHeight: " + temp_partImageHeight);
        else if (world =="north_korea"){
            ////System.out.println("        world = north_korea");


            //Sky
            SkyLayer = manager.get("animations/backrounds/north_korea/skyImage.png");

            //clouds
            /*tempTexture = new Texture("animations/backrounds/normal/clouds.png");
            Vector2 temp_multi = new Vector2(3, .7f);
            float temp_timesDrawn = 4;
            float temp_RelDistance = .9f;
            float y_down_quotient = 1;
            addLayer(tempTexture, temp_partImageWidth, temp_partImageHeight, temp_multi, temp_timesDrawn, temp_RelDistance, y_down_quotient);
            */

            //city_b
            tempTexture = manager.get("animations/backrounds/north_korea/city_B.png");
            Vector2 temp_multi = new Vector2(4, 4);
            float temp_timesDrawn = 4;
            float temp_RelDistance = .8f;
            float y_down_quotient = 1.2f;
            addLayer(tempTexture, temp_partImageWidth, temp_partImageHeight, temp_multi, temp_timesDrawn, temp_RelDistance, y_down_quotient);

            //city_f
            tempTexture = manager.get("animations/backrounds/north_korea/city_F.png");
            temp_multi = new Vector2(8, 5);
            temp_timesDrawn = 4;
            temp_RelDistance = .7f;
            y_down_quotient = 2.1f;
            addLayer(tempTexture, temp_partImageWidth, temp_partImageHeight, temp_multi, temp_timesDrawn, temp_RelDistance, y_down_quotient);

        }

        else
            System.out.println("                     WARNING: Invalid world name");
        ////System.out.println("    end: load_layers");
    }

    private void addLayer(Texture texture, float partImageWidth, float partImageHeight, Vector2 multiplier, float timesDrawn, float relDistance, float y_down_quotient){
        ////System.out.println("        start: addLayer");
        float[] list = new float[9];

        //float timesDrawn = 5;//screen.getGamecam().viewportWidth/imageWidth * 5;
        //float steps = 1;
        //float rightMove = 0;
        float temp_uRight = partImageWidth;
        float temp_vTop = partImageHeight;
        float temp_imageWidth = partImageWidth *  multiplier.x * timesDrawn;
        float temp_RelDistance = 0.05f;
        float temp_xPos = 0 - partImageWidth;

        Texture_Values_Pack tv_pack = new Texture_Values_Pack(texture);
        tv_pack.partImageHeight = partImageHeight;
        tv_pack.partImageHeight = partImageHeight;
        tv_pack.uRight = temp_uRight;
        tv_pack.vTop = temp_vTop;
        tv_pack.multi_x = multiplier.x;
        tv_pack.multi_y = multiplier.y;
        tv_pack.xPos = temp_xPos;
        tv_pack.timesDrawn = timesDrawn;
        tv_pack.imageWidth = temp_imageWidth;
        tv_pack.RelDistance = relDistance;
        tv_pack.y_down_quotient = y_down_quotient;

        layers.add(tv_pack);

        ////System.out.println("            texture: " + texture + "    stepWidth: " + stepWidth);
        ////System.out.println("        end: addLayer");
    }

    private void check_for_steps(){
        ////System.out.println("    start: check_for_steps");


        for (Texture_Values_Pack tv_pack : layers){
            // //System.out.println("        start: iterate");
          //  //System.out.println("           beginn: iterateList: " + iterateList.toString());


            if(camX - (camX* tv_pack.RelDistance) >=  tv_pack.xPos + (tv_pack.timesDrawn - 2) * tv_pack.partImageWidth){
                tv_pack.xPos += (tv_pack.timesDrawn - 3) * tv_pack.partImageWidth;
            }
            else if(camX - (camX*tv_pack.RelDistance) < tv_pack.xPos + (tv_pack.timesDrawn - 3) * tv_pack.partImageWidth){
                tv_pack.xPos -= (tv_pack.timesDrawn - 3) * tv_pack.partImageWidth;
            }


        }
        ////System.out.println("    end: check_for_steps");


    }

    public void draw(SpriteBatch sb, float Cx, float Cy, float Vw, float Vh){
        ////System.out.println("start: drawing");
        camX = screen.getGamecam().position.x;
        camY = screen.getGamecam().position.y;

        check_for_steps();


        sb.draw(SkyLayer, Cx - Vw/2, Cy - Vh/2, Vw , Vh );

        for (Texture_Values_Pack tv_pack : layers){


            ////System.out.println("    start: Iterating");
            ////System.out.println("        atStart: layer.getValue: " + layer.getValue());
            //Debug Version




            /*//System.out.println("        atEnd: ");

            //System.out.println("            temp_layer: " + tv_pack.layer);
            //System.out.println("            temp_xPos: " + tv_pack.xPos + camX * tv_pack.RelDistance);
            //System.out.println("            temp_imageWidth: " + tv_pack.imageWidth);
            //System.out.println("            temp_partImageHeight: " + tv_pack.partImageHeight);
            //System.out.println("            temp_partImageWidth: " + tv_pack.partImageWidth);
            //System.out.println("            temp_timesDrawn: " + tv_pack.timesDrawn);
            //System.out.println("            temp_uRight: " + tv_pack.uRight);
            //System.out.println("            temp_vTop: " + tv_pack.vTop);



            //System.out.println("    end: Iterating");*/
            sb.draw(tv_pack.layer, tv_pack.xPos + camX * tv_pack.RelDistance, camY - (tv_pack.partImageHeight - (tv_pack.partImageHeight / tv_pack.y_down_quotient)), tv_pack.imageWidth, tv_pack.partImageHeight, tv_pack.timesDrawn, 1, tv_pack.uRight, tv_pack.vTop);
            //sb.draw(tv_pack.layer, tv_pack.xPos + camX * tv_pack.RelDistance, camY - (tv_pack.partImageHeight - (tv_pack.partImageHeight / tv_pack.y_down_quotient)), tv_pack.imageWidth, tv_pack.partImageHeight);



            if (old_RelDistance < tv_pack.RelDistance && old_layer != null){
                layers.add(tv_pack);
                ////System.out.println("WARING: puttingputtingputtingputtingputtingputtingputtingputtingputtingputtingputtingputtingputtingputtingputtingputtingputtingputtingputtingputtingputtingputtingputting");
            }
            /*old_RelDistance = temp_RelDistance;
            old_iterateList = iterateList;
            old_layer = temp_layer;*/
        }


        ////System.out.println("end: drawing");


        ////System.out.println("end: drawing");
    }

    public void resize(){

        for (Texture_Values_Pack tv_pack : layers) {
  /*          temp_partImageWidth  = iterateList[0];
            temp_partImageHeight = iterateList[1];
            temp_multi           = iterateList[4];
            temp_timesDrawn      = iterateList[6];
            temp_imageWidth      = iterateList[7];
*/

            tv_pack.partImageWidth  = screen.getGamecam().viewportWidth * tv_pack.multi_x;// (int)ChainsawRun.PPM / 2;
            tv_pack.partImageHeight = screen.getGamecam().viewportHeight * tv_pack.multi_y;// (int)ChainsawRun.PPM / 2;
            tv_pack.imageWidth = tv_pack.partImageWidth * tv_pack.timesDrawn;

            //tv_pack.uRight = tv_pack.partImageWidth;
            //tv_pack.vTop = tv_pack.partImageHeight;

            /*iterateList[0] = temp_partImageWidth;
            iterateList[1] = temp_partImageHeight;
            iterateList[4] = temp_multi;
            iterateList[6] = temp_timesDrawn;
            iterateList[7] = temp_imageWidth;
*/
        }

    }

    public void dispose(){
        for (Texture_Values_Pack pack : layers){
            pack.dispose();
        }

    }


    private class Texture_Values_Pack{
        private Texture layer;
        private float partImageWidth;
        private float partImageHeight;
        private float uRight;
        private float vTop;
        private float multi_x;
        private float multi_y;
        private float xPos;
        private float timesDrawn;
        private float imageWidth;
        private float RelDistance;
        private float y_down_quotient;

        private Texture_Values_Pack(Texture texture){
            layer = texture;
            layer.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
            layer.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }

        private void dispose(){

        }

    }





}



















/*
    private Texture layer1;
    private int imageWidth;
    private int imageHeight;
    private float uRight;
    private float vTop;
    private float timesDrawn;
    private float rightMove;
    private float steps;
    private float stepWidth;
*/




/**

BackroundAnimator
14.01.2017


package com.dani.game2.Animation;

        import com.badlogic.gdx.graphics.Texture;
        import com.badlogic.gdx.graphics.TextureData;
        import com.badlogic.gdx.graphics.g2d.SpriteBatch;
        import com.badlogic.gdx.utils.IdentityMap;
        import com.dani.game2.ChainsawRun;
        import com.dani.game2.Screens.PlayScreen;
        import com.sun.org.apache.xpath.internal.operations.String;

        import java.util.HashMap;
        import java.util.Map;

**
 * Created by root on 21.12.16.
 *

public class BackroundAnimator {

    PlayScreen screen;

    private java.lang.String world;

    private Texture temp_layer;
    private float temp_imageWidth;
    private float temp_imageHeight;
    private float temp_uRight;
    private float temp_vTop;
    private float temp_timesDrawn;
    private float temp_rightMove;
    private float temp_steps;
    private float temp_stepWidth;
    private float temp_RelDistance;
    private float temp_width_multiplier;


    private HashMap<Texture, float[]> layers;
    private float[] iterateList;

    private Texture SkyLayer;

    private float camX;


    public BackroundAnimator(PlayScreen playScreen, java.lang.String world){
        //System.out.println("start: INIT BackroundAnimator");
        this.screen = playScreen;
        this.world = world;

        layers = new HashMap<Texture, float[]>();
        load_layers();
        resize();
        camX = screen.getGamecam().position.x;
        //System.out.println("end: INIT BackroundAnimator");

        /*timesDrawn = 5;
        steps = 1;
        rightMove = 0;
        layer1 = new Texture("animations/trees1.png");
        layer1.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        uRight = imageWidth;// * 1 / layer1.getWidth();
        vTop= imageHeight;// * 1/ bg.getHeight();
        **
    }

    private void load_layers(){
        //System.out.println("    sart: load_layers");
        Texture tempTexture;
        float imageWidth;
        float imageHeight;
        imageWidth = screen.getGamecam().viewportWidth;
        imageHeight= screen.getGamecam().viewportHeight;


        if (world =="normal"){
            //System.out.println("        world = normal");
            //Sky
            SkyLayer = new Texture("animations/backrounds/normal/skyImage.png");

            //hills
            tempTexture = new Texture("animations/backrounds/normal/hills.png");
            tempTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
            temp_width_multiplier = 2;
            addLayer(tempTexture, imageWidth, imageHeight, temp_width_multiplier);**

            //Trees
            tempTexture = new Texture("animations/backrounds/normal/trees1.png");
            tempTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
            temp_width_multiplier = 1;

            addLayer(tempTexture, imageWidth, imageHeight, temp_width_multiplier);
            //System.out.println("        imageWidth: " + imageWidth + "  imageHeight: " + imageHeight);

        }

        else //System.out.println("                     WARNING: Invalid world name");
        //System.out.println("    end: load_layers");
    }

    private void addLayer(Texture texture, float imageWidth, float imageHeight, float multiplier){
        //System.out.println("        start: addLayer");
        float[] list = new float[10];

        float timesDrawn = 5;//screen.getGamecam().viewportWidth/imageWidth * 5;
        float steps = 1;
        float rightMove = 0;
        float uRight = imageWidth;
        float vTop = imageHeight;
        float stepWidth = imageWidth * timesDrawn * multiplier;
        float distance = 0.05f;

        list[0] = imageWidth;
        list[1] = imageHeight;
        list[2] = uRight;
        list[3] = vTop;
        list[4] = timesDrawn;
        list[5] = rightMove;
        list[6] = steps;
        list[7] = stepWidth;
        list[8] = distance;
        list[9] = multiplier;

        layers.put(texture, list);
        ////System.out.println("            texture: " + texture + "    stepWidth: " + stepWidth);
        ////System.out.println("        end: addLayer");
    }

    private void check_for_steps(){
        ////System.out.println("    start: check_for_steps");
        /*if (camX - (camX*0.2f) > stepWidth * steps) {
            steps += 1;
            rightMove = (steps -1) * stepWidth - imageWidth;
            //System.out.println("steps + 1      " + steps + "\n");
        }
        else if (camX - (camX*0.2f) < (steps-1) * stepWidth) {
            steps -= 1;
            rightMove = (steps -1) * stepWidth - imageWidth;
            //System.out.println("steps - 1:     " + steps );
            ////System.out.println(rightMove);
        }**


        for (float[] iterateList : layers.values()){
            // //System.out.println("        start: iterate");
            //  //System.out.println("           beginn: iterateList: " + iterateList.toString());
            //Debug Version

            temp_imageWidth     = iterateList[0];
            temp_imageHeight    = iterateList[1];
            temp_uRight         = iterateList[2];
            temp_vTop           = iterateList[3];
            temp_timesDrawn     = iterateList[4];
            temp_rightMove      = iterateList[5];
            temp_steps          = iterateList[6];
            temp_stepWidth      = iterateList[7];
            temp_RelDistance       = iterateList[8];



            if (camX - (camX*temp_RelDistance) > temp_stepWidth * temp_steps + temp_stepWidth) {
                temp_steps += 1;
                temp_rightMove = (temp_steps) * temp_stepWidth - (temp_imageWidth * (temp_width_multiplier ));
                ////System.out.println("steps + 1      " +   temp_steps + "\n");            }
                else if (camX - (camX*temp_RelDistance) < (temp_steps-1) * temp_stepWidth) {
                    temp_steps -= 1;
                    temp_rightMove = (temp_steps -1) * temp_stepWidth - (temp_imageWidth * (temp_width_multiplier ));
                    // //System.out.println("steps - 1:     " + temp_steps);
                    ////System.out.println(rightMove);
                }

                iterateList[0] = temp_imageWidth;
                iterateList[1] = temp_imageHeight  ;
                iterateList[2] = temp_uRight;
                iterateList[3] = temp_vTop;
                iterateList[4] = temp_timesDrawn;
                iterateList[5] = temp_rightMove;
                iterateList[6] = temp_steps;
                iterateList[7] = temp_stepWidth;

                //layer.setValue(iterateList);
                ////System.out.println("            atEnd: iterateList: " + iterateList);
                ////System.out.println("            test;; has iterateList effect on layers: layers.containsValue()(has to be TRUE): " + layers.containsValue(iterateList));
                ////System.out.println("      end: Iterate");
                //Test if this is more more efficient
            /*list  = (float[]) layer.getValue();

            if (camX - (camX*0.2f) > list[7] * list[6]) {
                list[6] += 1;
                list[5] = (list[6] -1) * list[7] - list[0];
                //System.out.println("steps + 1      " + list[6] + "\n");
            }1
            else if (camX - (camX*0.2f) < (list[6]-1) * list[0]) {
                list[6] -= 1;
                list[5] = (list[6] -1) * list[7] - list[0];
                //System.out.println("steps - 1:     " + list[6]);
                ////System.out.println(rightMove);
            }**

            }
            ////System.out.println("    end: check_for_steps");


        }

    public void draw_polygons(SpriteBatch sb, float Cx, float Cy, float Vw, float Vh){
        ////System.out.println("start: drawing");
        camX = screen.getGamecam().position.x;

        check_for_steps();


        sb.draw_polygons(SkyLayer, Cx - Vw/2, Cy - Vh/2, Vw , Vh );

        for (HashMap.Entry<Texture, float[]> layer : layers.entrySet()){
            ////System.out.println("    start: Iterating");
            ////System.out.println("        atStart: layer.getValue: " + layer.getValue());
            //Debug Version
            iterateList  = layer.getValue();


            temp_layer          = layer.getKey();
            temp_imageWidth     = iterateList[0];
            temp_imageHeight    = iterateList[1];
            temp_uRight         = iterateList[2];
            temp_vTop           = iterateList[3];
            temp_timesDrawn     = iterateList[4];
            temp_rightMove      = iterateList[5];
            temp_steps          = iterateList[6];
            temp_stepWidth      = iterateList[7];
            temp_RelDistance    = iterateList[8];
            temp_width_multiplier = iterateList[9];
//            sb.draw_polygons(temp_layer, temp_rightMove + camX * 0.2f, 0, temp_stepWidth + 2* temp_imageWidth, temp_imageHeight, temp_timesDrawn + 2, 1, temp_uRight, temp_vTop);

            sb.draw_polygons(temp_layer, temp_rightMove + camX * temp_RelDistance, 0, temp_stepWidth + (temp_width_multiplier * temp_imageWidth), temp_imageHeight, temp_timesDrawn + 2, 1, temp_uRight, temp_vTop);


            /*System.out.println("        atEnd: ");

            //System.out.println("            temp_layer: " + temp_layer);
            //System.out.println("            temp_rightMove + camX * 0.2f (xPos): " + temp_rightMove + camX * 0.2f);
            //System.out.println("            temp_stepWidth + 2* temp_imageWidth (actual width): " + temp_stepWidth + 2* temp_imageWidth);
            //System.out.println("            temp_imageHeight: " + temp_imageHeight);
            //System.out.println("            temp_imageWidth: " + temp_imageWidth);
            //System.out.println("            temp_timesDrawn + 2: " + temp_timesDrawn + 2);
            //System.out.println("            temp_uRight: " + temp_uRight);
            //System.out.println("            temp_vTop: " + temp_vTop);
            //System.out.println("            temp_stepWidth: " + temp_stepWidth);


            //System.out.println("    end: Iterating");
        //System.out.println("end: drawing");*
        }


        ////System.out.println("end: drawing");
    }

    public void resize(){

        for (float[] iterateList : layers.values()) {
            temp_imageWidth  = iterateList[0];
            temp_imageHeight = iterateList[1];
            temp_stepWidth   = iterateList[7];
            temp_timesDrawn  = iterateList[4];
            temp_width_multiplier = iterateList[9];

            temp_imageWidth  = screen.getGamecam().viewportWidth;// (int)ChainsawRun.PPM / 2;
            temp_imageHeight = screen.getGamecam().viewportHeight;// (int)ChainsawRun.PPM / 2;
            temp_stepWidth = temp_imageWidth * temp_timesDrawn * temp_width_multiplier;

            iterateList[0] = temp_imageWidth;
            iterateList[1] = temp_imageHeight;
            iterateList[7] = temp_stepWidth;
            iterateList[4] = temp_timesDrawn;
            iterateList[9] = temp_width_multiplier;

        }

    }



 //hills1
 tempTexture = new Texture("animations/backrounds/normal/hills.png");
 tempTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
 temp_multi = 3;
 temp_timesDrawn = 4;
 temp_RelDistance = .7f;
 addLayer(tempTexture, temp_partImageWidth, temp_partImageHeight, temp_multi, temp_timesDrawn, temp_RelDistance);

 //Trees
 tempTexture = new Texture("animations/backrounds/normal/trees1.png");
 tempTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
 temp_multi = 1;
 temp_timesDrawn = 7;
 temp_RelDistance = .2f;
 addLayer(tempTexture, temp_partImageWidth, temp_partImageHeight, temp_multi, temp_timesDrawn, temp_RelDistance);




}*/