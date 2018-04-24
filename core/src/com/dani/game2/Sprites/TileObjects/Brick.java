package com.dani.game2.Sprites.TileObjects;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.dani.game2.ChainsawRun;

/**
 * Created by root on 17.07.16.
 */
public class Brick extends InteractiveTileObject {
    public Brick(com.dani.game2.Screens.PlayScreen screen, MapObject object, com.dani.game2.ChainsawRun chainsawRun) {
        super(screen, object, chainsawRun);
        //setFilter(ChainsawRun.COIN_BIT);
    }

    public void onHeadHit(){
 //       //System.out.print("Start Brick: Collision: \n");// + body.getFixtureList().get(0).getFilterData().categoryBits + " \n");

//        //System.out.print(body.getFixtureList().get(1).getFilterData().categoryBits + "\n");
        setFilter(ChainsawRun.NULL_BIT);
//        //System.out.print(body.getFixtureList().get(1).getFilterData().categoryBits + "\n");
        ////System.out.print("End   Brick: Collision: " + body.getFixtureList().get(0).getFilterData().categoryBits + " \n");
        getCell().setTile(null);
        com.dani.game2.Scenes.Hud.add_Score(200);
        chainsawRun.manager.get("sounds/effects/breakblock.wav", Sound.class).play();


    }
}
