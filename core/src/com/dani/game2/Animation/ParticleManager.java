package com.dani.game2.Animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IdentityMap;
import com.dani.game2.ChainsawRun;
import com.dani.game2.Screens.PlayScreen;


import java.util.HashMap;

import static java.lang.Math.random;

/**
 * Created by root on 27.11.16.
 */

public class ParticleManager {
    private PlayScreen screen;

    private Array<Body_Particle_Pack> order;

    //Textures
    //private static Array<ParticleEffectPool.PooledEffect> effects;
    private ParticleEffectPool bloodParticle1_Pool;
    private ParticleEffectPool bloodParticle2_Pool;
    private ParticleEffectPool rocket_pool;

    private static float pi = (float)Math.PI;



    public ParticleManager(PlayScreen screen) {
        this.screen = screen;


        //effects = new Array<ParticleEffectPool.PooledEffect>();
        order = new Array<Body_Particle_Pack>();
        load_particle_pools();

    }


    private void load_particle_pools(){
        TextureAtlas atlas;

        //Textures
        atlas = new TextureAtlas("animations/Texturepacks/texPack1/txtPack1.txt");

        //booldParticle1
        ParticleEffect temp_Effect = new ParticleEffect();
        temp_Effect.load(Gdx.files.internal("particles/bloodParticle1.party"), Gdx.files.internal("particles"));
        temp_Effect.setEmittersCleanUpBlendFunction(false);
        temp_Effect.scaleEffect(1/ChainsawRun.PPM);
        bloodParticle1_Pool = new ParticleEffectPool(temp_Effect, 40, 40);



        //booldParticle1
        temp_Effect = new ParticleEffect();
        temp_Effect.load(Gdx.files.internal("particles/bloodParticle2.party"), Gdx.files.internal("particles"));
        temp_Effect.setEmittersCleanUpBlendFunction(false);
        temp_Effect.scaleEffect(1/ChainsawRun.PPM);
        bloodParticle2_Pool = new ParticleEffectPool(temp_Effect, 40, 40);


        //rocket
        temp_Effect = new ParticleEffect();
        temp_Effect.load(Gdx.files.internal("particles/rocket_particle2.party"), Gdx.files.internal("particles"));
        temp_Effect.setEmittersCleanUpBlendFunction(false);
        temp_Effect.scaleEffect(.8f/ChainsawRun.PPM); //this used to be temp_Effect.scaleEffect(1/ChainsawRun.PPM);
        rocket_pool = new ParticleEffectPool(temp_Effect, 40, 40);

    }

    public void update_effects(){
        for (Body_Particle_Pack pack : order){
            Vector2 position = pack.body.getPosition();
            pack.particleEffect.setPosition(position.x, position.y);
        }
    }


    public void add_effect_to_body(Body body, String animName){
        ParticleEffectPool.PooledEffect effect = get_effect((String) animName);
        effect.setPosition(body.getPosition().x , body.getPosition().y);

        effect.start();

        order.add(new Body_Particle_Pack(body, effect));

    }


    private ParticleEffectPool.PooledEffect get_effect(String category){
        ParticleEffectPool.PooledEffect effect = null;
        float index = -2;

        if (category.equals("BLOOD1"))
            effect = bloodParticle1_Pool.obtain();
        else if (category.equals("BLOOD2"))
            effect = bloodParticle2_Pool.obtain();
        else if (category.equals("ROCKET"))
            effect = rocket_pool.obtain();

        return effect;
    }




    public void remove(Body body){
        int i = 0;
        for (Body_Particle_Pack pack : order){
            if (pack.body == body)
                order.removeIndex(i);
            i++;
        }
    }


    public void draw_effects(SpriteBatch batch, float dt){
        int render_count = 0;
        ////System.out.println(order.size);
        for (Body_Particle_Pack pack : order) {
            pack.dt += dt;
           // //System.out.println(pack.dt);
        }
        ////System.out.println();


        if (order.size >= 1) {
            ////System.out.println("oreder.size >= 1");
            batch.begin();

            for (int i = order.size; i >= 1; i--) {
                Body_Particle_Pack pack = order.get(i - 1);
                ParticleEffectPool.PooledEffect effect = pack.particleEffect;
                effect.draw(batch, pack.dt);

                pack.dt = 0;
                if (effect.isComplete()) {
                    effect.free();
                    order.removeIndex(i - 1);

                }
                render_count++;
                if (render_count == 80) {
                    //batch.end();

                    //System.out.println();
                    //System.out.println("batch restart");
                    //System.out.println("renderCalls:re " + batch.renderCalls + "  |  maxSpritesInBatch: " + batch.maxSpritesInBatch);

                    break;
                    //batch.begin();
                    //render_count = 0;
                }
            }


            batch.end();

            if (batch.maxSpritesInBatch >= 8000)
                System.out.println("WARNING batch may have been overloaded: " + batch.maxSpritesInBatch);


            ////System.out.println("renderCalls:   " + batch.renderCalls + "  |  maxSpritesInBatch: " +  batch.maxSpritesInBatch);
            // //System.out.println(" ");
        }
        /*for (Body_Particle_Pack pack : order){
            pack.particleEffect.update(dt);
        }*/
    }


    private class Body_Particle_Pack{
        private Body body;
        private ParticleEffectPool.PooledEffect particleEffect;
        private float dt;

        private Body_Particle_Pack(Body body, ParticleEffectPool.PooledEffect particleEffect){
            this.body = body;
            this.particleEffect = particleEffect;
            dt = 0;
        }

            }

}

/*public void flip(Body body, String direction){
        float[] list = order.get(body);
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

    public boolean isflip(Body body){
        float[] list = order.get(body);
        if (list[1] == 1) return true;
        else return false;
    }

    public void makevisible(Body body, boolean visible){
        if(visible)order.get(body)[1] = 1;
        else order.get(body)[1] = 0;
    }



////System.out.println("\n adding anim to body:  " + animName);
        /*float effectIndex;
        float[] value = new float[8];
        boolean hadAnim = false;

        //visible
        value[0] = 1;

        //change direction
        if(change_direction) value[1] = 1;
        else value[1] = 0;



        for(Fixture fix: body.getFixtureList()){

            effectIndex = active_new_effect(animName, body.getPosition().x, body.getPosition().y);

            if (effectIndex < 1000 && !hadAnim) {
                ////System.out.print("i have a regoin\n");
                value[2] = effectIndex;
                value[3] = -1;
            }

            if (effectIndex >= 1000){
                ////System.out.print(" i have an anim \n");
                value[2] = effectIndex - 1000;
                value[3] = 10 * (float)random();
                hadAnim = true;
            }

            value[4] = width;
            value[5] = height;
            value[6] = xplus;
            value[7] = yplus;*/



