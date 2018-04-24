package com.dani.game2.Levels;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.dani.game2.Screens.PlayScreen;

import java.util.HashMap;

/**
 * Created by root on 01.06.17.
 */

public class Level_loader {


    protected Fragment_Pack rolling_climbing;
    protected Fragment_Pack climbing_rolling;
    protected Fragment_Pack rolling_speeding;
    protected Fragment_Pack speeding_rolling;

    protected WorldPack normal_worldPack;
    protected WorldPack northKorea_worldPack;
    protected WorldPack beginnerWorldPack;


    // normal
    private Array<Fragment_Pack> normal_maps;
    private Array<Fragment_Pack> normal_rolling_maps;
    private Array<Fragment_Pack> normal_speeding_maps;
    private Array<Fragment_Pack> normal_climbing_maps;

    // north_korea
    private Array<Fragment_Pack> northKorea_maps;
    private Array<Fragment_Pack> northKorea_rolling_maps;
    private Array<Fragment_Pack> northKorea_speeding_maps;
    private Array<Fragment_Pack> northKorea_climbing_maps;

    // beginner
    private Array<Fragment_Pack> beginner_maps;
    private Array<Fragment_Pack> beginner_rolling_maps;
    private Array<Fragment_Pack> beginner_speeding_maps;
    private Array<Fragment_Pack> beginner_climbing_maps;




    protected Level_loader(TmxMapLoader mapLoader){
        Fragment_Pack currentFragment;

        /*
        speeding_rolling = new Fragment_Pack(mapLoader.load("levels/levels/fragments/fragment_set_1/zz_speeding_rolling.tmx"), Fragment_manager.State.rolling);
        rolling_speeding = new Fragment_Pack(mapLoader.load("levels/levels/fragments/fragment_set_1/zz_rolling_speeding.tmx"), Fragment_manager.State.speeding);
        climbing_rolling = new Fragment_Pack(mapLoader.load("levels/levels/fragments/fragment_set_1/zz_climbing_rolling.tmx"), Fragment_manager.State.rolling);
        rolling_climbing = new Fragment_Pack(mapLoader.load("levels/levels/fragments/fragment_set_1/zz_rolling_climbing.tmx"), Fragment_manager.State.climbing);
        */

        //normal
        //rolling
        normal_rolling_maps = new Array<Fragment_Pack>();
        for (int i = 1; i <= 7; i++) {
            currentFragment = new Fragment_Pack(mapLoader.load("levels/levels/fragments/fragment_set_2/rolling/rolling" + i + ".tmx"), Fragment_manager.State.rolling);
            normal_rolling_maps.add(currentFragment);
        }

        //speeding
        normal_speeding_maps = new Array<Fragment_Pack>();
        for (int i = 1; i <= 5; i++) {
            normal_speeding_maps.add(new Fragment_Pack(mapLoader.load("levels/levels/fragments/fragment_set_1/speeding/speeding" + i + ".tmx"), Fragment_manager.State.speeding));
        }
        //climbing
        normal_climbing_maps = new Array<Fragment_Pack>();
        for (int i = 1; i <= 5; i++) {
            normal_climbing_maps.add(new Fragment_Pack(mapLoader.load("levels/levels/fragments/fragment_set_1/climbing/climbing" + i + ".tmx"), Fragment_manager.State.climbing));
        }

        normal_worldPack = new WorldPack(normal_rolling_maps, normal_speeding_maps, normal_climbing_maps);


        //north_korea
        //rolling
        northKorea_rolling_maps = new Array<Fragment_Pack>();
        for (int i = 1; i <= 7; i++) {
            currentFragment = new Fragment_Pack(mapLoader.load("levels/levels/fragments/fragment_set_3/rolling/rolling" + i + ".tmx"), Fragment_manager.State.rolling);
            northKorea_rolling_maps.add(currentFragment);
        }

        //speeding
        northKorea_speeding_maps = new Array<Fragment_Pack>();
        for (int i = 1; i <= 5; i++) {
            northKorea_speeding_maps.add(new Fragment_Pack(mapLoader.load("levels/levels/fragments/fragment_set_1/speeding/speeding" + i + ".tmx"), Fragment_manager.State.speeding));
        }//climbing
        northKorea_climbing_maps = new Array<Fragment_Pack>();
        for (int i = 1; i <= 5; i++) {
            northKorea_climbing_maps.add(new Fragment_Pack(mapLoader.load("levels/levels/fragments/fragment_set_1/climbing/climbing" + i + ".tmx"), Fragment_manager.State.climbing));
        }


        northKorea_worldPack = new WorldPack(northKorea_rolling_maps, normal_speeding_maps, northKorea_climbing_maps);


        //beginner
        //rolling
        beginner_rolling_maps = new Array<Fragment_Pack>();
        for (int i = 1; i <= 7; i++) {
            currentFragment = new Fragment_Pack(mapLoader.load("levels/levels/fragments/fragment_set_2/rolling/rolling" + i + ".tmx"), Fragment_manager.State.rolling);
            beginner_rolling_maps.add(currentFragment);
        }

        //speeding
        beginner_speeding_maps = new Array<Fragment_Pack>();
        for (int i = 1; i <= 5; i++) {
            beginner_speeding_maps.add(new Fragment_Pack(mapLoader.load("levels/levels/fragments/fragment_set_1/speeding/speeding" + i + ".tmx"), Fragment_manager.State.speeding));
        }
        //climbing
        beginner_climbing_maps = new Array<Fragment_Pack>();
        for (int i = 1; i <= 5; i++) {
            beginner_climbing_maps.add(new Fragment_Pack(mapLoader.load("levels/levels/fragments/fragment_set_1/climbing/climbing" + i + ".tmx"), Fragment_manager.State.climbing));
        }

        beginnerWorldPack = new WorldPack(beginner_rolling_maps, beginner_speeding_maps, beginner_climbing_maps);

    }



    protected class WorldPack{

        protected Array<Fragment_Pack> rollingMaps;
        protected Array<Fragment_Pack> speedingMaps;
        protected Array<Fragment_Pack> climbingMaps;


        protected WorldPack(Array<Fragment_Pack> rollingMaps, Array<Fragment_Pack> speedingMaps, Array<Fragment_Pack> climbingMaps){
            this.rollingMaps = rollingMaps;
            this.speedingMaps = speedingMaps;
            this.climbingMaps = climbingMaps;
        }

    }

}
