package com.dani.game2;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dani.game2.Screens.Menus.HomeScreen;
import com.dani.game2.Screens.PlayScreen;
import com.dani.game2.tools.Asset_Loader;


public class ChainsawRun extends Game{
	public static SpriteBatch batch;
	public static PolygonSpriteBatch polygonBatch;

	public static final int V_Width = 400;
	public static final int V_Height = 208;
	public static final float PPM = 40;

	public static final short NULL_BIT = 0;
	public static final short GROUND_BIT = 1;
	public static final short PLAYER_BIT = 2;
	public static final short SAWBLADE_BIT = 4;
	public static final short ENEMY_SENSOR_BIT = 8;
	public static final short RABBIT_OBSTACLE_BIT = 16;
	public static final short ENEMY_BIT = 32;
	public static final short DEAD_BIT = 64;
	public static final short ITEM_BIT = 128;
	public static final short PLAYER_LEFT_HAMMER_BIT = 256;
	public static final short PLAYER_RIGHT_HAMMER_BIT = 512;
	public static final short B_WHEEL_BIT = 1024;
	public static final short F_WHEEL_BIT = 2048;
	public static final short PR_BIT = 4096;
	public static final short NORMALG_BIT = 8192;
	public static final short PLAYER_SENSOR_BIT = 16384;

	public static Texture LoadingTexture;

	public AssetManager manager;
	public static Asset_Loader asset_loader;
	public Preferences prefs;

	public HomeScreen homeScreen;
	public PlayScreen playScreen;

	@Override
	public void create () {
		batch = new SpriteBatch(8191);
		polygonBatch = new PolygonSpriteBatch();
		manager = new AssetManager();
		asset_loader = new Asset_Loader(this);
		LoadingTexture = new Texture("animations/loading_debug.png");

		prefs = Gdx.app.getPreferences("game_state");

		asset_loader.load_menu();
		asset_loader.load_all_sprite_textures();

		homeScreen = new HomeScreen(this);
		//setScreen(new PlayScreen(this, "levels/levels/flat.tmx", "campain"));
		//setScreen(new PlayScreen(this, "levels/levels/testLevel.tmx", "campain"));
		//setScreen(new PlayScreen(this, "" , "endless"));
		setScreen(homeScreen);
	}


	@Override
	public void render () {
		super.render();
	}

}


		/*manager.load("sounds/music/mario_music.ogg", Music.class);
		manager.load("sounds/effects/coin.wav", Sound.class);
		manager.load("sounds/effects/bump.wav", Sound.class);
		manager.load("sounds/effects/breakblock.wav", Sound.class);
		manager.load("sounds/effects/powerup_spawn.wav", Sound.class);
		manager.load("sounds/effects/powerup.wav", Sound.class);
		manager.finishLoading();*/
