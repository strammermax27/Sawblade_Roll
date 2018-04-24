package com.dani.game2.Sprites.items;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by root on 01.08.16.
 */
public class ItemDef {
    public Vector2 position;
    public Class<?> type;

    public ItemDef(Vector2 position, Class<?> type){
        this.position = position;
        this.type = type;
    }
}
