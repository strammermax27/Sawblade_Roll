package com.dani.game2.Screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dani.game2.Animation.BackroundAnimator;
import com.dani.game2.Animation.DynamicGround;
import com.dani.game2.Animation.ParticleManager;
import com.dani.game2.ChainsawRun;
import com.dani.game2.Scenes.Controller;
import com.dani.game2.Scenes.Hud;
import com.dani.game2.Sprites.DeadlyObjects.SawBlade;
import com.dani.game2.Sprites.Enemys.Enemy;
import com.dani.game2.Sprites.Player;
import com.dani.game2.Sprites.items.Item;
import com.dani.game2.Sprites.items.Mushroom;
import com.dani.game2.Levels.B2worldcreator;
import com.dani.game2.Animation.SpriteManager;
import com.dani.game2.Levels.Fragment_manager;
import com.dani.game2.tools.WorldContactListener;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

//import com.badlogic.gdx.tools.texturepacker.TexturePacker;

/**
 * Created by root on 14.07.16.
 */
public class PlayScreen implements Screen{

    public com.dani.game2.ChainsawRun game;
    private TextureAtlas atlas;

    private InputProcessor inputProcessor;

    private OrthographicCamera gamecam;

    private Viewport viewport;

    private Hud hud;

    private TmxMapLoader mapLoader;
    private TiledMap map;

    private OrthogonalTiledMapRenderer maprenderer;
    private World world;
    private String mapLocation;
    private String mode;
    private SpriteManager spriteManager;



    private ParticleManager particleManager;

    private TextureRegion testTexture;

    private Box2DDebugRenderer b2dr;

    private B2worldcreator creator;
    private Player player;

    public Vector2 lastGroundPos;
    private Vector2 playerVel;
    private Music music;

    private Array<Item> items;

    //private DebugWriter dbWriter;
    private boolean onDesktop;

    private LinkedBlockingQueue<com.dani.game2.Sprites.items.ItemDef> itemsToSpwan;

    public Controller controller;
    public FPSLogger fpsLogger;
    public Array<PolylineMapObject> groundShapes; //before set to x=0,y=0!!!
    public DynamicGround dynamicGround;

    private BackroundAnimator bKAnimator;

    private LinkedHashMap<Float, Vector2> last_velocitys;
    private float slow_down_factor;

    private Array<Enemy> enemies;

    private Fragment_manager fragment_manager;

    private Preferences prefs;




    public PlayScreen(final ChainsawRun game, String worldName, String mode) {
        //System.out.println("start init playscreen");
        //load_texture_regions();
        this.game = game;
        this.mapLocation = worldName;
        this.mode = mode;

        game.homeScreen.loading = false;

        gamecam = new OrthographicCamera();
        viewport = new StretchViewport(ChainsawRun.V_Width / ChainsawRun.PPM, ChainsawRun.V_Height / ChainsawRun.PPM, gamecam);
        groundShapes = new Array<PolylineMapObject>();
        dynamicGround = new DynamicGround(this);


        maprenderer = new OrthogonalTiledMapRenderer(map, 0.5f / ChainsawRun.PPM);

        gamecam.position.set(viewport.getWorldWidth(), viewport.getWorldHeight() / 2, 0);

        prefs = game.prefs;

        //System.out.println("init Sprite Manager");
        spriteManager = new SpriteManager(this);
        //System.out.println("init ParticleManager");
        particleManager = new ParticleManager(this);

        world = new World(new Vector2(0, -10), true);
        b2dr = new Box2DDebugRenderer();







        //System.out.print("start creating player\n");
        player = new Player(this);
        //System.out.print("end creating player\n");
        playerVel = player.b2dbody.getLinearVelocity();
        lastGroundPos = new Vector2(player.b2dbody.getPosition());


        hud = new Hud(this, game.batch);
        // TextureRegion hans = this.getAtlas().findRegion("little_mario");
        ////System.out.print(hans);

        world.setContactListener(new WorldContactListener(this));

       // music = this.game.manager.get("sounds/music/mario_music.ogg", Music.class);
       // music.setLooping(true);
       // music.play();

        items = new Array<Item>();
        itemsToSpwan = new LinkedBlockingQueue<com.dani.game2.Sprites.items.ItemDef>();

        //System.out.println("init controller");
        controller = new Controller(this);

        fpsLogger = new FPSLogger();
        //world.setGravity(new Vector2(0,0));

        //System.out.println("init Backroundanimator");
        bKAnimator = new BackroundAnimator(this, worldName);

        last_velocitys = new LinkedHashMap<Float, Vector2>();
        //gamecam.position.x =  player.b2dbody.getPosition().x + gamecam.viewportWidth/3 - player.b2dbody.getLinearVelocity().x/8;

       // gamecam.position.y = player.b2dbody.getPosition().y; // - player.b2dbody.getLinearVelocity().y/7


        enemies = new Array<Enemy>();

        mapLoader = new TmxMapLoader();
        if (mode == "campain"){
            map = mapLoader.load(worldName);
            creator = new B2worldcreator(this, game);
            creator.load_map();
            enemies = creator.getEnemys();

        }else if (mode == "endless"){
            creator = new B2worldcreator(this, game);
            fragment_manager = new Fragment_manager(this, mapLoader, worldName);
            enemies = creator.getEnemys();

        }


        //debug stuff
        if(Gdx.app.getType() == Application.ApplicationType.Desktop) onDesktop = true;
        else onDesktop = false;
        if(onDesktop) {
            //dbWriter = new DebugWriter(this);
            //dbWriter.add_listener("Sb_Time");
        }




        //just for debugging
        slow_down_factor = 0;
        this.controller.stage.addListener(new InputListener(){
            @Override
            public boolean scrolled(InputEvent event, float x, float y,
                                    int amount) {
                gamecam.zoom += amount;
                gamecam.update();
                return true;
            }
        });




        //System.out.println("step world");
        for (int i = 0; i <= 10; i++){
            world.step(0.0f, 60, 60);
        }
    }


    public void spwanItem(com.dani.game2.Sprites.items.ItemDef idef){
        itemsToSpwan.add(idef);
    }



    public void handleSpawningItems(){
        ////System.out.print("handleSpawningItems: 1 ");
        if (!itemsToSpwan.isEmpty()){
            ////System.out.print("2 ");
            com.dani.game2.Sprites.items.ItemDef idef = itemsToSpwan.poll();
            if(idef.type == Mushroom.class){
                ////System.out.print("3 ");
                items.add(new Mushroom(this, idef.position.x, idef.position.y));
            }
        }
        ////System.out.print("\n handleSpawnigItemsEnd \n");
    }

    public  TextureAtlas getAtlas(){
        return  atlas;
    }

    @Override
    public void show() {

    }

    public TiledMap getMap(){
            return (map);}

    public World getWorld(){
        return (world);
    }

    public void deleteEnemy(Enemy enemy){
        //mekae private enemy array for playscreen!!!!!!!!!
        enemies.removeValue(enemy, false);
        ////System.out.println("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBEEREMOVEENEMY");
    }

    public void handleinput(float dt){
    }

    private void set_gamecam_position(float dt){
        Vector2 body_pos = player.b2dbody.getPosition();
        Vector3 cam_pos = gamecam.position;

        gamecam.position.x = body_pos.x + gamecam.viewportWidth/3 - player.b2dbody.getLinearVelocity().x/8;

        gamecam.position.y = body_pos.y; // - player.b2dbody.getLinearVelocity().y/7;



        int i = last_velocitys.size();
        float allXvels = 0;
        float allYvels = 0;
        float avarangeXvel = 0;
        float averangeYvel = 0;
        float btime = 0;
        float iterations = 0;

        float camAddX = 0;
        float camAddY = 0;

        Vector2 temp_vel;
        float temp_dt;

        List<Float> keyList = new ArrayList<Float>(last_velocitys.keySet());

        while (btime <= .3f && i > 0){
            i --;
            iterations ++;
            temp_dt = keyList.get(i);
            temp_vel = last_velocitys.get(temp_dt);


            btime += temp_dt;
            allXvels += temp_vel.x;
            allYvels += temp_vel.y;
        }

        avarangeXvel = allXvels / iterations;
        averangeYvel = allYvels / iterations;

        camAddX = avarangeXvel;
        camAddY = averangeYvel;

        gamecam.position.x -= camAddX / 10;
        gamecam.position.y -= camAddY / 10;

        ////System.out.println("gameCamPos: " + gamecam.position + " | camAddX: " + camAddX);
        //gamecam.viewportHeight = 0.6f;
    }

    public void update(float dt) {

        //for debugging
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            slow_down_factor = .1f;
            dt *= slow_down_factor;

        }

        world.step(dt, 8, 8);


        if (mode == "endless") {
            fragment_manager.update();
        }


        ////System.out.println(game.batch.maxSpritesInBatch);
        //fpsLogger.log();
        last_velocitys.put(dt, new Vector2(player.b2dbody.getLinearVelocity()));
        handleinput(dt);
        handleSpawningItems();
        ////System.out.print(player.b2dbody.getPosition() + "\n");

        check_for_MW();

        set_gamecam_position(dt);
        zoom_camera(dt);

        gamecam.update();
        maprenderer.setView(gamecam);

        spriteManager.update_anims(dt);
        particleManager.update_effects();

        player.update(dt);
        for(Enemy enemy : enemies) {
            if(enemy.b2dbody != null){
                if (enemy.b2dbody.getPosition().x < player.b2dbody.getPosition().x + gamecam.viewportWidth * gamecam.zoom && !enemy.b2dbody.isActive()) {
                    enemy.b2dbody.setActive(true);
                }
                if (enemy.b2dbody.isActive()){
                    enemy.update(dt);
                }
            }
        }


        for (Item item : items){
            item.update(dt);
        }
        for (SawBlade sawBlade : creator.getSawblades()){
            if (sawBlade.getX() < player.getX() + ChainsawRun.V_Width/ (ChainsawRun.PPM*1.7)
             && sawBlade.getX() > player.getX() - ChainsawRun.V_Width/ ChainsawRun.PPM * 0.8 ){
                sawBlade.body.setActive(true);
                sawBlade.update(dt);
            }
            else {
                sawBlade.body.setActive(false);
            }
        }





        kill_enemys();
        ////System.out.print("end step world \n");

        hud.update(dt);


        //if(onDesktop) dbWriter.write_something();
    }

    public void render(float delta) {
        //delta *= 0.1;
        if (delta > .5)
            delta = .5f;
        update(delta);

        Gdx.gl.glClearColor(0.3f,0.3f,0.3f,1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT
                | GL20.GL_DEPTH_BUFFER_BIT
                | (Gdx.graphics.getBufferFormat().coverageSampling?GL20.GL_COVERAGE_BUFFER_BIT_NV:0));



        //maprenderer.render();


        game.batch.setProjectionMatrix(gamecam.combined);
        game.polygonBatch.setProjectionMatrix(gamecam.combined);



        // if(onDesktop)dbWriter.update_Value((int)sbtime, "Sb_Time");

        game.batch.begin();


        bKAnimator.draw(game.batch, gamecam.position.x, gamecam.position.y, viewport.getWorldWidth() * gamecam.zoom, viewport.getWorldHeight() * gamecam.zoom);
        spriteManager.draw_spites(game.batch);
        //locate "SDK Readme.txt"
        game.batch.end();
        ////System.out.println(game.batch.renderCalls);
        game.polygonBatch.begin();
        dynamicGround.draw_polygons(game.polygonBatch);
        game.polygonBatch.end();
        dynamicGround.draw_polylines();

        particleManager.draw_effects(game.batch, delta);
        ////System.out.println(game.batch.renderCalls);

        game.batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        controller.draw();
        //b2dr.render(world, gamecam.combined);




        hud.draw();

    }



    private void zoom_camera(float dt){

        float xVel = player.b2dbody.getLinearVelocity().x;//playerVel.x;
        float yVel = player.b2dbody.getLinearVelocity().y;//playerVel.y;

        if(xVel < 0) xVel *= -1;
        if(yVel < 0) yVel *= -1;
        float Vel = (xVel + yVel)/30 + 1;

       // //System.out.println("lastVel.size: " + last_velocitys.size());
        int i = last_velocitys.size();
        float allVels = 0;
        float avarangeVel = 0;
        float btime = 0;
        float iterations = 0;

        float temp_vel;
        float temp_dt;

        List<Float> keyList = new ArrayList<Float>(last_velocitys.keySet());

        while (btime <= 1 && i > 0){
            i --;
            iterations ++;
            temp_dt = keyList.get(i);
            temp_vel = Math.abs(last_velocitys.get(temp_dt).x + last_velocitys.get(temp_dt).y)/30 + 1;
            ////System.out.println("        temp_vel: " + temp_vel + " | " + "temp_dt: "+ temp_dt);

            btime += temp_dt;
            allVels += temp_vel;
        }
        ////System.out.println("iterations:     " + iterations);

        avarangeVel = allVels / iterations;

        gamecam.zoom = avarangeVel * 3.0f;



        float height = player.b2dbody.getPosition().y - lastGroundPos.y;
        float dividend = 3.f;

        if (height/dividend > 3 && height/dividend < 10)
            gamecam.zoom = (height/dividend)  * avarangeVel;
        else if (height/dividend < 3)
            gamecam.zoom = 3 * avarangeVel;
        else if (height/dividend > 10)
            gamecam.zoom = 10 * avarangeVel;
       //gamecam.zoom =60;

    }


    private void check_for_MW(){
        //if (inputProcessor.scrolled())
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        bKAnimator.resize();
        //dynamicGround. shapeRenderer.setProjectionMatrix(gamecam.combined);
        hud.resize(width, height);
        ////System.out.print("resized\n");
        controller.resize(width, height);
    }

    private void kill_enemys() {
        if (player.punshbuttonperssed) {
            ////System.out.print(player.punshbuttonperssed + "\n");
            if (player.runnungRight){
                for (Enemy enemy : player.enemys_in_right_zone) {
                    enemy.hitMe();
                }
            }
            if (!player.runnungRight)
                for (Enemy enemy : player.enemys_in_left_zone) {
                    enemy.hitMe();
                }
        }
    }

    public void pause() {

    }

    @Override
    public void resume() {

    }


    @Override
    public void hide() {

    }


    @Override
    public void dispose() {
        map.dispose();
        maprenderer.dispose();
        world.dispose();
        b2dr.dispose();
        dynamicGround.dispose();
        bKAnimator.dispose();

        player.dispose();

        for (Enemy enemy : enemies){
            enemy.dispose();
        }

        for (SawBlade sawBlade : creator.getSawblades()){
                sawBlade.dispose();
        }

    }

    public Player getPlayer() {
        return player;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public SpriteManager getSpriteManager() {return spriteManager;}

    public OrthographicCamera getGamecam() {
        return gamecam;
    }

    public ParticleManager getParticleManager() { return particleManager; }

    public B2worldcreator getCreator() {return creator;}

    public Fragment_manager getFragment_manager() {return fragment_manager;}

    public Array<Enemy> getEnemies() {return this.enemies;}
    public void setEnemies(Array<Enemy> enemies) {this.enemies = enemies;}
}

