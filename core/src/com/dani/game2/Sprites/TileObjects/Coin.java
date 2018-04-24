package com.dani.game2.Sprites.TileObjects;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Vector2;
import com.dani.game2.Screens.PlayScreen;
import com.dani.game2.Sprites.items.ItemDef;
import com.dani.game2.Sprites.items.Mushroom;

/**
 * Created by root on 17.07.16.
 */
public class Coin extends InteractiveTileObject {
    private static TiledMapTileSet tileSet;
    private final  int BLANK_COIN = 28;
    public  Coin(PlayScreen screen, MapObject object, com.dani.game2.ChainsawRun chainsawRun) {
        super(screen, object, chainsawRun);
        tileSet = map.getTileSets().getTileSet("mapstuff");
        //setFilter(com.dani.game2.ChainsawRun.COIN_BIT);
    }



    public void onHeadHit() {
        //System.out.print("Coin: Collision \n");

        //setFilter(ChainsawRun.DESTROYED_BIT);

        if (getCell() != null) {

            if (getCell().getTile().getId() == BLANK_COIN) {
                chainsawRun.manager.get("sounds/effects/bump.wav", Sound.class).play();
            } else {
                getCell().setTile(tileSet.getTile(BLANK_COIN));
                com.dani.game2.Scenes.Hud.add_Score(100);

                if (object.getProperties().containsKey("mushroom")) {
                    chainsawRun.manager.get("sounds/effects/powerup_spawn.wav", Sound.class).play();
                    screen.spwanItem(new ItemDef(new Vector2(body.getPosition().x, body.getPosition().y + 16 / com.dani.game2.ChainsawRun.PPM),
                            Mushroom.class));
                }
                else {
                    chainsawRun.manager.get("sounds/effects/coin.wav", Sound.class).play();

                }

            }


        }
    }



}
