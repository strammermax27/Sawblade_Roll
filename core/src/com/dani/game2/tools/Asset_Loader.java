package com.dani.game2.tools;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.dani.game2.ChainsawRun;

/**
 * Created by root on 18.04.17.
 */

public class Asset_Loader {
    private ChainsawRun game;
    private static AssetManager manager;

    public static Array<TextureAtlas> menu_atlases;
    public static Array<TextureAtlas> sprite_atlases;
    public static Array<Texture> backround_textures;
    public static Array<Sprite> ground_sprites;


    private String file_name_menu;
    private Array<String> file_names_sprite;
    private Array<String> file_names_backround;
    private Array<String> file_names_ground;

    private String current_world;

    public Asset_Loader(ChainsawRun game){
        this.game = game;
        this.manager = this.game.manager;

        current_world = "none";

    }

    public void load_menu(){
        if (menu_atlases != null)
            dispose_menu();
        file_name_menu = "animations/menus/menu_texture_pack.txt";
        manager.load(file_name_menu, TextureAtlas.class);
        manager.finishLoading();

        menu_atlases = new Array<TextureAtlas>();
        menu_atlases.add(manager.get(file_name_menu ,TextureAtlas.class));

    }


    public void load_all_sprite_textures(){

        if (sprite_atlases != null)
            dispose_all_sprites();

        file_names_sprite = new Array<String>();
        file_names_sprite.add("animations/Texturepacks/texPack1/txtPack1.txt");
        file_names_sprite.add("animations/Texturepacks/texPack2/txtPack2.txt");
        file_names_sprite.add("animations/Texturepacks/texPack3/txtPack3.txt");
        file_names_sprite.add("animations/runningGoodRabbit/runningGoodRabbit.txt");
        for (String name : file_names_sprite){
            manager.load(name, TextureAtlas.class);
        }
        manager.finishLoading();

        sprite_atlases = new Array<TextureAtlas>();
        for (String name : file_names_sprite){
            sprite_atlases.add(manager.get(name, TextureAtlas.class));
        }

        file_names_ground = new Array<String>();
        file_names_ground.add("animations/ground/upGround.png");
        file_names_ground.add("animations/ground/downGround1.png");
        file_names_ground.add("animations/ground/downGround2.png");
        file_names_ground.add("animations/ground/downGround3.png");

        for (String fileName: file_names_ground) {
            manager.load(fileName, Texture.class);
        }
        manager.finishLoading();

        ground_sprites = new Array<Sprite>();
        Texture tempTexture;
        Sprite tempSprite;
        for (String fileName : file_names_ground){
            tempTexture = manager.get(fileName, Texture.class);
            tempTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
            tempSprite = new Sprite(tempTexture);
            tempSprite.setFlip(false, true);
            ground_sprites.add(new Sprite(tempSprite));
        }


        /*Texture upGround = new Texture("animations/ground/upGround.png");  // gorund bitmaps have to be loaded with asset_loader!!! not directly in this class for better performance
        upGround.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        Texture downGround1 = new Texture("animations/ground/downGround1.png");
        downGround1.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        Texture downGround2 = new Texture("animations/ground/downGround2.png");
        downGround2.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        Texture downGround3 = new Texture("animations/ground/downGround3.png");
        downGround3.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        ground_sprites = new Array<Sprite>();

        ground_sprites.add(new Sprite(upGround));
        ground_sprites.add(new Sprite(downGround1));
        ground_sprites.add(new Sprite(downGround2));
        ground_sprites.add(new Sprite(downGround3));

        for (Sprite sprite : ground_sprites){
            sprite.flip(false, true);
        }*/

    }


    public void load_backround_textures(String world){

        boolean need_to_load_world = true;

        if (world.equals(current_world)){
            need_to_load_world = false;
        }

        if (backround_textures != null && need_to_load_world)
            dispose_backround_textures();

        if (world.equals("normal") && need_to_load_world) {
            file_names_backround = new Array<String>();
            file_names_backround.add("animations/backrounds/normal/clouds.png");
            file_names_backround.add("animations/backrounds/normal/hills_b.png");
            file_names_backround.add("animations/backrounds/normal/skyImage.png");
            file_names_backround.add("animations/backrounds/normal/hills_f.png");

            for (String name : file_names_backround) {
                manager.load(name, Texture.class);
            }
            manager.finishLoading();

            /*backround_textures = new Array<Texture>();
            for (String name : file_names_backround) {
                backround_textures.add(manager.get(name, Texture.class));
            }*/
        }else if (world.equals("north_korea") && need_to_load_world) {
            file_names_backround = new Array<String>();
            file_names_backround.add("animations/backrounds/north_korea/city_B.png");
            file_names_backround.add("animations/backrounds/north_korea/city_F.png");
            file_names_backround.add("animations/backrounds/north_korea/skyImage.png");

            for (String name : file_names_backround) {
                manager.load(name, Texture.class);
            }
            manager.finishLoading();

            /*backround_textures = new Array<Texture>();
            for (String name : file_names_backround) {
                backround_textures.add(manager.get(name, Texture.class));
            }*/


        }

        current_world = world;


    }

    public void dispose_menu(){
        menu_atlases.clear();
        manager.unload("animations/menus/menu_texture_pack.txt");
    }

    public void dispose_all_sprites(){
        sprite_atlases.clear();

        for (String name : file_names_sprite){
            manager.unload(name);
        }


    }

    public void dispose_backround_textures(){
        backround_textures.clear();

        for (String name : file_names_backround){
            manager.unload(name);
        }



    }

}
