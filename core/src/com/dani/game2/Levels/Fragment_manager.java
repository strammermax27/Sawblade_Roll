package com.dani.game2.Levels;

import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.dani.game2.Screens.PlayScreen;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by root on 15.02.17.
 */
//good luck
public class Fragment_manager {

    private PlayScreen screen;

    private com.dani.game2.Levels.B2worldcreator creator;
    private Body player;

    private Level_loader level_loader;

    private Fragment_Pack rolling_climbing;
    private Fragment_Pack climbing_rolling;
    private Fragment_Pack rolling_speeding;
    private Fragment_Pack speeding_rolling;

    private Array<Fragment_Pack> maps;
    private Array<Fragment_Pack> rolling_maps;
    private Array<Fragment_Pack> speeding_maps;
    private Array<Fragment_Pack> climbing_maps;
    private HashMap<State, Array<Fragment_Pack>> state_tiledMap;
    private TmxMapLoader mapLoader;


    private Current_state_pack csp;
    private boolean new_fragment;
    public float endXofCurrentFragment;

    private Random randor;

    protected enum State {rolling, speeding, climbing};
    private State current_state;
    private State last_state;
    private State[] states;
    private float stateCount;

    private float addor;

    private String worldName;

    public Fragment_manager(PlayScreen screen, TmxMapLoader tmxMapLoader, String worldName) {
        this.screen = screen;
        this.worldName = worldName;
        creator = screen.getCreator();
        player = screen.getPlayer().b2dbody;
        maps = new Array<Fragment_Pack>();
        current_state = State.rolling;
        mapLoader = tmxMapLoader;
        stateCount = 0;
        level_loader = new Level_loader(mapLoader);
        load_maps();

        csp = new Current_state_pack();

        randor = new Random();
        states = State.values();

        endXofCurrentFragment = 0;


        //creator.new_fragment(state_tiledMap.get(State.rolling).get(1), stateCount);
        creator.first_fragment(state_tiledMap.get(State.rolling).get(1));


    }

    private void load_maps() {
        state_tiledMap = new HashMap<State, Array<Fragment_Pack>>();

        speeding_rolling = level_loader.speeding_rolling;
        rolling_speeding = level_loader.rolling_speeding;
        climbing_rolling = level_loader.climbing_rolling;
        rolling_climbing = level_loader.rolling_climbing;


        if (worldName == "normal") {
            rolling_maps = level_loader.normal_worldPack.rollingMaps;
            speeding_maps = level_loader.normal_worldPack.speedingMaps;
            climbing_maps = level_loader.normal_worldPack.climbingMaps;

        }else if(worldName == "north_korea"){
            rolling_maps = level_loader.northKorea_worldPack.rollingMaps;
            speeding_maps = level_loader.northKorea_worldPack.speedingMaps;
            climbing_maps = level_loader.northKorea_worldPack.climbingMaps;

        }else if(worldName == "beginner"){
            rolling_maps = level_loader.beginnerWorldPack.rollingMaps;
            speeding_maps = level_loader.beginnerWorldPack.speedingMaps;
            climbing_maps = level_loader.beginnerWorldPack.climbingMaps;
        }





        state_tiledMap.put(State.rolling, rolling_maps);
        state_tiledMap.put(State.speeding, speeding_maps);
        state_tiledMap.put(State.climbing, climbing_maps);

        /*Fragment_Pack currentFragment;

        //rolling
        rolling_maps = new Array<Fragment_Pack>();
        for (int i = 1; i <= 7; i++) {
            currentFragment = new Fragment_Pack(mapLoader.load("levels/levels/fragments/fragment_set_2/rolling/rolling" + i + ".tmx"), State.rolling);
            rolling_maps.add(currentFragment);
        }
        state_tiledMap.put(State.rolling, rolling_maps);


        //speeding
        speeding_maps = new Array<Fragment_Pack>();
        for (int i = 1; i <= 5; i++) {
            speeding_maps.add(new Fragment_Pack(mapLoader.load("levels/levels/fragments/fragment_set_1/speeding/speeding" + i + ".tmx"), State.speeding));
        }
        state_tiledMap.put(State.speeding, speeding_maps);
        speeding_rolling = new Fragment_Pack(mapLoader.load("levels/levels/fragments/fragment_set_1/zz_speeding_rolling.tmx"), State.rolling);
        rolling_speeding = new Fragment_Pack(mapLoader.load("levels/levels/fragments/fragment_set_1/zz_rolling_speeding.tmx"), State.speeding);

        //climbing
        climbing_maps = new Array<Fragment_Pack>();
        for (int i = 1; i <= 5; i++) {
            climbing_maps.add(new Fragment_Pack(mapLoader.load("levels/levels/fragments/fragment_set_1/climbing/climbing" + i + ".tmx"), State.climbing));
        }
        state_tiledMap.put(State.climbing, climbing_maps);
        climbing_rolling = new Fragment_Pack(mapLoader.load("levels/levels/fragments/fragment_set_1/zz_climbing_rolling.tmx"), State.rolling);
        rolling_climbing = new Fragment_Pack(mapLoader.load("levels/levels/fragments/fragment_set_1/zz_rolling_climbing.tmx"), State.climbing);
        */

    }

    public void update() {
        check_for_new_fragment();

    }

    private void check_for_new_fragment() {

        if (endXofCurrentFragment - player.getPosition().x <= 140)
            new_fragment = true;


        if (new_fragment) {
            ////System.out.println("\nnew Fragment");
            // //System.out.println("endXofCurrentFragment: " + endXofCurrentFragment + "player.getPosition(): " + player.getPosition());
            // //System.out.println("age: " + csp.age);
            csp.age += 1;
            new_fragment = false;

            if (csp.age + 2 >= csp.size) {
                // //System.out.println("new State");
                set_new_State();
                next_fragment();//remove later
                //inbetween_fragment();
            } else {
                next_fragment();
                ////System.out.println("normal_fragment");
            }
        }
    }


    private void set_new_State() {
        stateCount++;
        last_state = current_state;

        if (current_state != State.rolling) {
            current_state = State.rolling;
        } else {
            while (current_state == State.rolling)
                current_state = states[randor.nextInt(states.length)];
        }

        csp.new_State();

    }


    private void next_fragment() {

        creator.new_fragment(state_tiledMap.get(State.rolling).get(csp.fragments[csp.age]), stateCount);
        //creator.new_fragment(state_tiledMap.get(current_state).get(csp.fragments[csp.age]), stateCount);
        //creator.new_fragment(state_tiledMap.get(State.rolling).get(0), stateCount);

    }


    private void inbetween_fragment() {

        if (last_state == State.climbing && current_state == State.rolling) {
            ////System.out.println("climbing_rolling");
            creator.new_fragment(climbing_rolling, stateCount);


        } else if (last_state == State.rolling && current_state == State.climbing) {
            ////System.out.println("rolling_climbing");
            creator.new_fragment(rolling_climbing, stateCount);


        } else if (last_state == State.speeding && current_state == State.rolling) {
            ////System.out.println("speeding_rolling");
            creator.new_fragment(speeding_rolling, stateCount);


        } else if (last_state == State.rolling && current_state == State.speeding) {
            ////System.out.println("rolling_speeding");
            creator.new_fragment(rolling_speeding, stateCount);


        }

    }


    private class Current_state_pack {

        private int size;
        private int age;
        private State state;
        private Random rand;
        private int[] fragments;

        private Current_state_pack() {
            rand = new Random();
            size = rand.nextInt(5) + 2;
            age = 0;
            state = State.rolling;

            fragments = new int[size];
            for (int i = 0; i < size; i++) {
                fragments[i] = rand.nextInt(4) + 0;
            }

        }


        private void new_State() {
            age = 0;
            size = rand.nextInt(9) + 5;
            state = current_state;

            fragments = new int[size];
            for (int i = 0; i < size; i++) {
                fragments[i] = rand.nextInt(state_tiledMap.get(state).size) + 0;
            }

        }


    }

}
