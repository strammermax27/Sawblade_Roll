package com.dani.game2.Screens.Menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dani.game2.ChainsawRun;
import com.dani.game2.Screens.PlayScreen;
import com.dani.game2.Sprites.Guns.Flame_thrower;
import com.dani.game2.Sprites.Guns.Gun;
import com.dani.game2.Sprites.Guns.Normal_gun;

import static com.badlogic.gdx.scenes.scene2d.InputEvent.Type.touchDown;
//import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader;

/**
 * Created by root on 11.09.16.
 */
public class HomeScreen implements Screen{
    /*private ChainsawRun game;
    private Viewport viewport;

    private Stage stage;
    private Table table;*/

    protected ChainsawRun game;
    protected Viewport viewport;

    protected Stage stage;
    protected Table table;

    public boolean loading = false;

    OrthographicCamera cam;

    //tabels
    private Table mainTable;
    private Table equipment;
    private Table settings1;
    private Table settings2;
    private Table changeEquipment1; //choose whelchair - chose weapon at the same time!!
    private Table changeEquipment2;
    private Table changeEquipment3;
    private Table worldSelection;
    private Table involveYourIdeas;
    private Table afterGameMenu;

    private Table current_Table;
    private Array<Table> tables;

    //buttonPacks
    private ButtonPack equipmentButtons;
    private ButtonPack settings1Buttons;
    private ButtonPack settings2Buttons;
    private ButtonPack changeEquipment1Buttons;
    private ButtonPack changeEquipment2Buttons;
    private ButtonPack changeEquipment3Buttons;
    private ButtonPack chooseWeaponButtons;
    private ButtonPack worldSelectionButtons;
    private ButtonPack involveYourIdeasButtons;
    private ButtonPack afterGameMenuButtons;


    //assets
    private TextureAtlas texAtlas1;
    private TextureRegion next_reg;
    private TextureRegion settings_reg;
    private TextureRegion back_reg;
    private TextureRegion backround_reg;
    private TextureRegion chooseGun_reg;
    private TextureRegion chooseWheelchair_reg;
    private TextureRegion chair1_reg;
    private TextureRegion chair2_reg;
    private TextureRegion flameThrower_reg;
    private TextureRegion mashineGun_reg;
    private TextureRegion normalGun_reg;
    private TextureRegion shotGun_reg;
    private TextureRegion sniperRiffle_reg;
    private TextureRegion normalWorld_reg;
    private TextureRegion northKoreaWorld_reg;
    private TextureRegion beginnerWorld_reg;



    //camera movement
    private final float elapseTime = .6f;
    private float elapsed;
    private Interpolation.ElasticOut movment = Interpolation.elasticOut;
    private Vector2 startPos = new Vector2(0,0);
    private Vector2 endPos;
    private float distance;
    private float constant;
    private boolean move_to_X;
    private boolean move_to_Y;
    private boolean movment_going_on = false;




    private Button campaign;
    private Button endless;
    private Button upgrades;
    private Button ideas;
    private Button options;

    private String screenName;
    private String worldToLoad;
    public String gunToLoad;

    private float delta;

    public HomeScreen (ChainsawRun game){
        this.game = game;

        viewport = new FitViewport(ChainsawRun.V_Width, ChainsawRun.V_Height, new OrthographicCamera());
        cam = new OrthographicCamera();
        viewport.setCamera(cam);
        game.batch.setProjectionMatrix(cam.combined);
        ////System.out.println("initialise startpos");
        startPos = new Vector2(cam.position.x, cam.position.y);

        if (game.prefs.contains("lastGunUsed"))
            gunToLoad = game.prefs.getString("lastGunUsed");
        else
            gunToLoad = "normal_gun";

        //System.out.println("HOMESCREEEN gunToLoad: " + gunToLoad);

        load_stuff();
        make_stage();
        focus_on_table(equipment);

        //cam.rotate(20);
        cam.update();

        delta = 0;


    }

    private void load_stuff(){

        texAtlas1 = game.asset_loader.menu_atlases.first();

        backround_reg = texAtlas1.findRegion("backround");
        back_reg = texAtlas1.findRegion("back_button");
        next_reg = texAtlas1.findRegion("next_button");
        settings_reg = texAtlas1.findRegion("settings_button");

        chooseGun_reg = texAtlas1.findRegion("choose_gun_button");
        chooseWheelchair_reg = texAtlas1.findRegion("choose_wheelchair_button");

        chair1_reg = texAtlas1.findRegion("chair");
        chair2_reg = texAtlas1.findRegion("chair");

        flameThrower_reg = texAtlas1.findRegion("flame_thrower");
        mashineGun_reg = texAtlas1.findRegion("mashine_gun");
        normalGun_reg = texAtlas1.findRegion("normal_gun");
        shotGun_reg = texAtlas1.findRegion("shot_gun");
        sniperRiffle_reg = texAtlas1.findRegion("sniper_rifle");

        normalWorld_reg = texAtlas1.findRegion("world1");
        northKoreaWorld_reg = texAtlas1.findRegion("world2");
        beginnerWorld_reg = texAtlas1.findRegion("world1");

    }



    protected void make_stage(){
        stage = new Stage(viewport, game.batch);
        tables = new Array<Table>();


        float v_width = viewport.getScreenWidth();// * cam.zoom;
        float v_height = viewport.getScreenHeight();// * cam.zoom;


        equipment = new Table();
        String[] buttons_to_add = {"next", "back", "settings", "choose_gun", "choose_wheelchair"};
        equipmentButtons = new ButtonPack(buttons_to_add, this);
        configure_buttons(equipment);
        equipment.add();//.width(stage.getWidth()/3 - equipmentButtons.next.getWidth());
        equipment.add(equipmentButtons.settings).expandX().top().right();
        equipment.row();
        equipment.add(equipmentButtons.chooseGun).expand();
        equipment.add(equipmentButtons.chooseWheelchair).expand();
        equipment.add();
        equipment.row();
        equipment.add(equipmentButtons.back).left().bottom().padBottom(7);
        equipment.add(equipmentButtons.next).right().bottom().padBottom(7);
        //equipment.bottom().left();
        equipment.setSize(stage.getWidth(), stage.getHeight());
        tables.add(equipment);



        worldSelection = new Table();
        buttons_to_add = new String[]{"back", "settings", "scroll_worlds"};
        worldSelectionButtons = new ButtonPack(buttons_to_add, this);
        configure_buttons(worldSelection);
        worldSelection.add();//.width(stage.getWidth()/3 - worldSelectionButtons.next.getWidth());;
        worldSelection.add(worldSelectionButtons.settings).expandX().top().right();
        worldSelection.row();
        //worldSelection.add();
        worldSelection.add(worldSelectionButtons.scroll_worlds).expand().left().minWidth(stage.getWidth());
        //worldSelection.add();
        worldSelection.row();
        worldSelection.add(worldSelectionButtons.back).expand().left().padLeft(3).bottom().padBottom(7);
        worldSelection.add(worldSelectionButtons.next).right().padRight(3).bottom().padBottom(7);
        //worldSelection.right();
        tables.add(worldSelection);


        involveYourIdeas = new Table();
        buttons_to_add = new String[]{"next", "back", "settings"};
        involveYourIdeasButtons = new ButtonPack(buttons_to_add, this);
        configure_buttons(involveYourIdeas);
        involveYourIdeas.add();//.width(stage.getWidth()/3 - involveYourIdeasButtons.next.getWidth());;
        involveYourIdeas.add(involveYourIdeasButtons.settings).expandX().top().right();
        involveYourIdeas.row();
        involveYourIdeas.add(involveYourIdeasButtons.back).expand().left().padLeft(3).bottom().padBottom(7);
        involveYourIdeas.add(involveYourIdeasButtons.next).right().padRight(3).bottom().padBottom(7);
        //involveYourIdeas();
        tables.add(involveYourIdeas);

        afterGameMenu = new Table();
        buttons_to_add = new String[]{"next", "back", "settings"};
        afterGameMenuButtons = new ButtonPack(buttons_to_add, this);
        configure_buttons(afterGameMenu);
        afterGameMenu.add();//.width(stage.getWidth()/3 - afterGameMenuButtons.next.getWidth());;
        afterGameMenu.add(afterGameMenuButtons.settings).expandX().top().right();
        afterGameMenu.row();
        afterGameMenu.add(afterGameMenuButtons.back).expand().left().padLeft(3).bottom().padBottom(7);
        afterGameMenu.add(afterGameMenuButtons.next).right().padRight(3).bottom().padBottom(7);
        tables.add(afterGameMenu);


        settings1 = new Table();
        buttons_to_add = new String[]{"next", "back", "settings"};
        settings1Buttons = new ButtonPack(buttons_to_add, this);
        configure_buttons(settings1);
        settings1.add();//.width(stage.getWidth()/3 - settings1Buttons.next.getWidth());;
        settings1.add(settings1Buttons.settings).expandX().top().right();
        settings1.row();
        settings1.add(settings1Buttons.back).expand().left().padLeft(3).bottom().padBottom(7);
        settings1.add(settings1Buttons.next).right().padRight(3).bottom().padBottom(7);
        tables.add(settings1);


        settings2 = new Table();
        buttons_to_add = new String[]{"next", "back", "settings"};
        settings2Buttons = new ButtonPack(buttons_to_add, this);
        configure_buttons(settings2);
        settings2.add();//.width(stage.getWidth()/3 - settings2Buttons.next.getWidth());;
        settings2.add(settings2Buttons.settings).expandX().top().right();
        settings2.row();
        settings2.add(settings2Buttons.back).expand().left().padLeft(3).bottom().padBottom(7);
        settings2.add(settings2Buttons.next).right().padRight(3).bottom().padBottom(7);
        tables.add(settings2);

        changeEquipment1 = new Table();
        buttons_to_add = new String[]{"next", "back", "settings", "scroll_guns", "scroll_chairs"};
        changeEquipment1Buttons = new ButtonPack(buttons_to_add, this);
        configure_buttons(changeEquipment1);
        changeEquipment1.add();//.width(stage.getWidth()/3 - changeEquipment1Buttons.next.getWidth());;
        changeEquipment1.add(changeEquipment1Buttons.settings).expandX().top().right();
        changeEquipment1.row();
        changeEquipment1.add(changeEquipment1Buttons.back).expand().left().padLeft(3).bottom().padBottom(7);
        changeEquipment1.add(changeEquipment1Buttons.next).right().padRight(3).bottom().padBottom(7);
        tables.add(changeEquipment1);

        changeEquipment2 = new Table();
        buttons_to_add = new String[]{"next", "back", "settings", "scroll_guns", "scroll_chairs"};
        changeEquipment2Buttons = new ButtonPack(buttons_to_add, this);
        configure_buttons(changeEquipment2);
        changeEquipment2.add();//.width(stage.getWidth()/3 - changeEquipment2Buttons.next.getWidth());;
        changeEquipment2.add(changeEquipment2Buttons.settings).expandX().top().right();
        changeEquipment2.row();
        changeEquipment2.add(changeEquipment2Buttons.back).expand().left().padLeft(3).bottom().padBottom(7);
        changeEquipment2.add(changeEquipment2Buttons.next).right().padRight(3).bottom().padBottom(7);
        tables.add(changeEquipment2);

        changeEquipment3 = new Table();
        buttons_to_add = new String[]{"next", "back", "settings", "scroll_guns", "scroll_chairs"};
        changeEquipment3Buttons = new ButtonPack(buttons_to_add, this);
        configure_buttons(changeEquipment3);
        changeEquipment3.add();
        changeEquipment3.add(changeEquipment3Buttons.settings).expandX().top().right();
        changeEquipment3.row();
        changeEquipment3.add(changeEquipment3Buttons.back).expand().left().padLeft(3).bottom().padBottom(7);
        changeEquipment3.add(changeEquipment3Buttons.next).right().padRight(3).bottom().padBottom(7);
        tables.add(changeEquipment3);

        for (Table table : tables){
            table.setFillParent(true);

        }


        mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.center();

        mainTable.add();
        mainTable.add(settings1);
        mainTable.add(settings2);
        mainTable.add();

        mainTable.row();

        mainTable.add(involveYourIdeas);
        mainTable.add(equipment);
        mainTable.add(worldSelection);
        mainTable.add(afterGameMenu);

        mainTable.row();

        mainTable.add();
        mainTable.add(changeEquipment1);
        mainTable.add(changeEquipment2);
        mainTable.add(changeEquipment3);



        for (Cell cell : mainTable.getCells()){
            cell.minWidth(stage.getWidth());
            cell.minHeight(stage.getHeight());
        }
        TextureRegionDrawable backround_drawa = new TextureRegionDrawable(backround_reg);
        backround_drawa.setMinWidth(stage.getWidth()*4);
        backround_drawa.setMinHeight(stage.getHeight()*3);

        //mainTable.background(backround_drawa);


        stage.addActor(mainTable);

        //stage.setDebugAll(true);
        Gdx.input.setInputProcessor(stage);

        //debugging
        stage.addListener(new InputListener(){
            @Override
            public boolean scrolled(InputEvent event, float x, float y,
                                    int amount) {
                cam.zoom += amount * 1/3f;
                cam.update();
                return true;
            }
        });

        stage.addListener(new InputListener(){
                @Override
                public boolean keyDown(InputEvent event, int key_code){
                    int w_code = 51;
                    int a_code = 29;
                    int s_code = 47;
                    int d_code = 32;

                    int k_code = 39;
                    int l_code = 40;


                    if (key_code == w_code)
                        cam.position.y += 10;
                    else if (key_code == s_code)
                        cam.position.y -= 10;
                    else if (key_code == a_code)
                        cam.position.x -= 10;
                    else if (key_code == d_code)
                        cam.position.x += 10;
                    //for testing
                    else if (key_code == k_code)
                        focus_on_table(equipment);
                    else if (key_code == l_code)
                        focus_on_table(worldSelection);

                    cam.update();
                   // //System.out.println("key_pressed,  keycode: " + key_code);

                    return  true;
                }

            });

        //stage.addListener((InputListener) TouchEvent()
    }

    private void configure_buttons(Table table){


        if (table == equipment){
            equipmentButtons.next.addListener(new InputListener(){
               @Override
               public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                   focus_on_table(worldSelection);
                   return true;
               }
            });
            equipmentButtons.settings.addListener(new InputListener(){
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                    focus_on_table(settings1);
                    return true;
                }
            });
            equipmentButtons.chooseGun.addListener(new InputListener(){
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                    setup_table_as(changeEquipment1, changeEquipment1Buttons, "gun_selection");
                    focus_on_table(changeEquipment1);
                    return  true;
                }
            });
            equipmentButtons.chooseWheelchair.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    setup_table_as(changeEquipment1, changeEquipment1Buttons, "chair_selection");
                    focus_on_table(changeEquipment1);
                    return true;
                }
            });

        }else if (table == worldSelection){

            worldSelectionButtons.back.addListener(new InputListener(){
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                    focus_on_table(equipment);
                    return true;
                }
            });
            worldSelectionButtons.settings.addListener(new InputListener(){
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                    focus_on_table(settings2);
                    return true;
                }
            });
            worldSelectionButtons.world1.addListener(new InputListener(){
                private Vector2 startTouchPos;

                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                    startTouchPos = new Vector2(x,y);
                    return true;
                }
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    boolean posNear = false;

                    if (Math.abs(startTouchPos.x - x) < 30 && Math.abs(startTouchPos.y - y) < 30)
                        posNear = true;
                    if (!worldSelectionButtons.scroll_worlds.isDragging() && posNear){
                        loading = true;
                        worldToLoad = "normal";
                    }
                }
            });
            worldSelectionButtons.world2.addListener(new InputListener(){
                private Vector2 startTouchPos;

                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                    startTouchPos = new Vector2(x,y);
                    return true;
                }
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                    boolean posNear = false;
                    if (Math.abs(startTouchPos.x - x) < 30 && Math.abs(startTouchPos.y - y) < 30) {
                        posNear = true;
                    }
                    if (!worldSelectionButtons.scroll_worlds.isDragging() && posNear) {
                        worldToLoad = "north_korea";
                        loading = true;
                    }
                }
            });
            worldSelectionButtons.world3.addListener(new InputListener(){
                private Vector2 startTouchPos;

                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                    startTouchPos = new Vector2(x,y);
                    return true;
                }
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    boolean posNear = false;

                    if (Math.abs(startTouchPos.x - x) < 30 && Math.abs(startTouchPos.y - y) < 30)
                        posNear = true;
                    if (!worldSelectionButtons.scroll_worlds.isDragging() && posNear){
                        loading = true;
                        worldToLoad = "normal";
                    }
                }
            });


        }else if (table == settings1){

            settings1Buttons.back.addListener(new InputListener(){
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                    focus_on_table(equipment);
                    return true;
                }
            });


        }else if (table == settings2){

            settings2Buttons.back.addListener(new InputListener(){
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                    focus_on_table(worldSelection);
                    return true;
                }
            });

        }else if (table == changeEquipment1 || table == changeEquipment2 || table == changeEquipment3){
            ButtonPack temp_Buttons = null;
            if (table == changeEquipment1) {
                temp_Buttons = changeEquipment1Buttons;
                temp_Buttons.back.addListener(new InputListener(){
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                        focus_on_table(equipment);
                        return true;
                    }
                });

                temp_Buttons.next.addListener(new InputListener(){
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                        focus_on_table(worldSelection);
                        return true;
                    }
                });



            }else if (table == changeEquipment2)
                temp_Buttons = changeEquipment2Buttons;


            else if (table == changeEquipment3) {
                temp_Buttons = changeEquipment3Buttons;
                temp_Buttons.back.addListener(new InputListener(){
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                        focus_on_table(afterGameMenu);
                        return true;
                    }
                });

            }

            temp_Buttons.settings.addListener(new InputListener(){
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                    focus_on_table(settings1);
                    return true;
                }
            });



            temp_Buttons.normal_gun.addListener(new InputListener(){
                private Vector2 startTouchPos;

                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                    startTouchPos = new Vector2(x,y);
                    return true;
                }
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                    boolean posNear = false;
                    if (Math.abs(startTouchPos.x - x) < 30 && Math.abs(startTouchPos.y - y) < 30) {
                        posNear = true;
                    }
                    if (!worldSelectionButtons.scroll_worlds.isDragging() && posNear) {
                        //do whatever it tkes to select that gun
                    }
                }
            });
            temp_Buttons.mashine_gun.addListener(new InputListener(){
                private Vector2 startTouchPos;

                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                    startTouchPos = new Vector2(x,y);
                    return true;
                }
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                    boolean posNear = false;
                    if (Math.abs(startTouchPos.x - x) < 30 && Math.abs(startTouchPos.y - y) < 30) {
                        posNear = true;
                    }
                    if (!worldSelectionButtons.scroll_worlds.isDragging() && posNear) {
                        //do whatever it tkes to select that gun
                    }
                }
            });

            temp_Buttons.chair1.addListener(new InputListener(){
                private Vector2 startTouchPos;

                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                    startTouchPos = new Vector2(x,y);
                    return true;
                }
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                    boolean posNear = false;
                    if (Math.abs(startTouchPos.x - x) < 30 && Math.abs(startTouchPos.y - y) < 30) {
                        posNear = true;
                    }
                    if (!worldSelectionButtons.scroll_worlds.isDragging() && posNear) {
                        //do whatever it tkes to select that gun
                    }
                }
            });
            temp_Buttons.chair2.addListener(new InputListener(){
                private Vector2 startTouchPos;

                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                    startTouchPos = new Vector2(x,y);
                    return true;
                }
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                    boolean posNear = false;
                    if (Math.abs(startTouchPos.x - x) < 30 && Math.abs(startTouchPos.y - y) < 30) {
                        posNear = true;
                    }
                    if (!worldSelectionButtons.scroll_worlds.isDragging() && posNear) {
                        //do whatever it tkes to select that gun
                    }
                }
            });




        }










    }


    private void setup_table_as(Table table, ButtonPack buttons, String mode){
        table.clear();

        if (mode.equals("gun_selection")){
            table.add();//.width(stage.getWidth()/3 - changeEquipment1Buttons.next.getWidth());;
            table.add(buttons.settings).expandX().top().right();
            table.row();
            table.add(buttons.scroll_guns).minWidth(stage.getWidth()).left();
            table.row();
            table.add(buttons.back).expand().left().padLeft(3).bottom().padBottom(7);
            table.add(buttons.next).right().padRight(3).bottom().padBottom(7);

        }else if (mode.equals("chair_selection")){
            table.add();//.width(stage.getWidth()/3 - changeEquipment1Buttons.next.getWidth());;
            table.add(buttons.settings).expandX().top().right();
            table.row();
            //table.add(buttons.scroll_chairs).minWidth(stage.getWidth()).left();
            table.row();
            table.add(buttons.back).expand().left().padLeft(3).bottom().padBottom(7);
            table.add(buttons.next).right().padRight(3).bottom().padBottom(7);


        }else
            System.out.println("WARNING: invalid table mode");


    }


    protected void  handle_input(){
        if (loading && stage != null){

            game.prefs.flush();
            game.setScreen(new PlayScreen(game, worldToLoad, "endless"));

            dispose();

        }




        /*if (stage != null) {

            if (campaign.isPressed()) {
                ////System.out.print("lvlselpressed\n");
                loading = true;
                screenName = "campaign";

            } else if (endless.isPressed()) {
                loading = true;
                screenName = "endless";

            }

        }*/

    }

    /*protected void update(){
        handle_input();
    }*/

    /*public void render(float delta) {
        update();
        Gdx.gl.glClearColor(0, 0.4f, 0.5f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw_polygons();
    }*/

    public void pause() {

    }


    public void resume() {

    }

   public void resize(int width, int height) {
        //float aspectRatio = (float) width / (float) height;
        //Camera camera = new OrthographicCamera(640, 360);
        //camera.translate(320,180);
        //camera.update();

        //stage.setViewport(new FillViewport(640, 360, camera));
        //stage.getViewport().setCamera(camera);
       viewport.update(width, height);
       cam.update();
       make_stage();

       render(0);
       focus_on_table(equipment);
       //render(0);

       //System.out.println("resize");
//       table.setFillParent(true);

       //stage.getViewport().update(width, height);

        //table.set
   }

   public void dispose() {
        stage.dispose();
        stage = null;
        //System.out.println("dispose: " + stage);
        //game.render();

   }

   private void focus_on_table(Table toFocustable) {

       move_cam(elapseTime);

       movment_going_on = true;
       move_to_X = false;
       move_to_Y = false;

       elapsed = 0;

       endPos = new Vector2(toFocustable.localToStageCoordinates(new Vector2(0, 0)));
       //endPos.x += cam.viewportWidth / 2;
       //endPos.y += cam.viewportHeight / 2;


       if (startPos == null){
           ////System.out.println("startPos == null");
           startPos = new Vector2(0,0);
       }

       float x_distance = Math.abs(startPos.x - endPos.x);
       float y_distance = Math.abs(startPos.y - endPos.y);

       if (x_distance > y_distance) {
           move_to_X = true;
           cam.position.y = endPos.y;
       }else if (y_distance > x_distance){
           move_to_Y = true;
           cam.position.x = endPos.x;
       }if(move_to_X && move_to_Y) {
           System.out.println("WARNING movment in two directions claimed, startPos:" + startPos + "   | endPos: " + endPos + "   cam.height:" + cam.viewportHeight);
       }


       if(move_to_X){
           distance = endPos.x - startPos.x;
       }else{
           distance = endPos.y - startPos.y;
       }
       ////System.out.println("distance: " + distance);


       constant = distance/ movment.apply(1);
       




       ////System.out.println("focus on Table, cam.position:  " + cam.position + "  | cam.zoom: " + cam.zoom);

   }

   private void move_cam(float dt){

       elapsed += dt;
       float var = Math.min(1f, elapsed/elapseTime);

       if (constant > 0) {
           if (move_to_X)
               cam.position.x = startPos.x + (movment.apply(var) * constant) + cam.viewportWidth / 2;
           if (move_to_Y)
               cam.position.y = startPos.y + (movment.apply(var) * constant)  + cam.viewportHeight/2;
       }else {
           if (move_to_X)
               cam.position.x = startPos.x + (movment.apply(var) * constant)  + cam.viewportWidth/2;
           if (move_to_Y)
               cam.position.y = startPos.y + (movment.apply(var) * constant)  + cam.viewportHeight/2;
       }


       if (move_to_X)
            cam.position.y = endPos.y + cam.viewportHeight/2;
       if (move_to_Y)
            cam.position.x = endPos.x + cam.viewportWidth/2;
           ////System.out.println(movment.apply(var) * constant + "    " + distance);

       cam.update();


       if (var >= 1) {
           movment_going_on = false;
           startPos = endPos;
       }

   }



    public void show() {

    }

    public void hide() {

    }


    protected void update(float dt){

        if (loading && stage != null){
            ////System.out.println("loading  " + stage);
           // //System.out.println(worldToLoad);
            game.prefs.flush();
            game.asset_loader.load_backround_textures(worldToLoad);
            game.playScreen = new PlayScreen(game, worldToLoad, "endless");
            game.setScreen(game.playScreen);

            game.prefs.flush();
            //dispose();
        }

        if (movment_going_on) {
            move_cam(dt);
        }

        worldSelectionButtons.scroll_worlds.updateVisualScroll();
    }

    public void render(float delta) {
        this.delta = delta;
        update(delta);
        Gdx.gl.glClearColor(0, 0f, 0f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        if (stage != null){
            float stage_w = stage.getWidth();
            float stage_h = stage.getHeight();

            game.batch.begin();
            game.batch.draw(backround_reg, -1.5f* stage_w - 100, -stage_h - 100, 4*stage_w + 200, 3*stage_h + 200);
            game.batch.end();

            stage.act(delta);
            stage.draw();
        }

        if (loading)
            draw_loading_screen();
    }
    protected void draw_loading_screen(){
        Gdx.gl.glClearColor(0, 0.4f, 0.5f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        game.batch.begin();
        //System.out.println("draw loading texture");
        game.batch.draw(ChainsawRun.LoadingTexture, 0, 0, viewport.getScreenWidth(), viewport.getScreenHeight());


        game.batch.end();

   }

   private class ButtonPack{
       private HomeScreen homeScreen;

       private Button next;
       private Button back;
       private Button settings;
       private Button choose;
       private Button chooseGun;
       private Button chooseWheelchair;

       private ScrollPane scroll_worlds;
       private Table worlds;
       private Button world1;
       private Button world2;
       private Button world3;

       private ScrollPane scroll_guns;
       private Table guns;
       private Button flame_thrower;
       private Button mashine_gun;
       private Button normal_gun;
       private Button shot_gun;
       private Button sniper_riffle;


       private ScrollPane scroll_chairs;
       private Table chairs;
       private Button chair1;
       private Button chair2;

       private Array<Button> buttons;

       private Vector2 satrtPos_guns = new Vector2();

       private ButtonPack(String[] buttons_to_add, HomeScreen homeScreen){
           this.homeScreen = homeScreen;
           buttons = new Array<Button>();
           make_buttons(buttons_to_add);
       }

       private void make_buttons(String[] buttons_to_add){
           TextureRegionDrawable temp_drawa;
           for (String button_name : buttons_to_add) {
               if (button_name.equals("next")) {
                   next = new ImageButton(new TextureRegionDrawable(homeScreen.next_reg));
                   next.setSize(homeScreen.next_reg.getRegionWidth()/3, homeScreen.next_reg.getRegionHeight()/3);
                   buttons.add(next);
               } else if (button_name.equals("back")) {
                   back = new ImageButton(new TextureRegionDrawable(back_reg));
                   back.setSize(homeScreen.back_reg.getRegionWidth()/3, homeScreen.back_reg.getRegionHeight()/3);
                   buttons.add(back);
               } else if (button_name.equals("settings")){
                   settings = new ImageButton(new TextureRegionDrawable(settings_reg));
                   settings.setSize(homeScreen.settings_reg.getRegionWidth()/3, homeScreen.settings_reg.getRegionHeight()/3);
                   buttons.add(settings);
               }else if (button_name.equals("choose_gun")){
                   temp_drawa = new TextureRegionDrawable(chooseGun_reg);
                   temp_drawa.setMinHeight(10000);
                   chooseGun = new Button(new TextureRegionDrawable(temp_drawa));
                    buttons.add(chooseGun);
               }else if (button_name.equals("choose_wheelchair")){
                   chooseWheelchair = new ImageButton(new TextureRegionDrawable(chooseWheelchair_reg));
                   buttons.add(chooseWheelchair);
               }else if (button_name.equals("scroll_worlds")){
                   world1 = new Button(new TextureRegionDrawable(normalWorld_reg));
                   world2 = new Button(new TextureRegionDrawable(northKoreaWorld_reg));
                   world3 = new Button(new TextureRegionDrawable(beginnerWorld_reg));
                   worlds = new Table();
                   float width = world1.getMinWidth();
                   worlds.add().minWidth(width * 3/4);
                   worlds.add(world1);
                   worlds.add().minWidth(width/2);
                   worlds.add(world2);
                   worlds.add().minWidth(width/2);
                   worlds.add(world3);
                   worlds.add().minWidth(width * 3/4);
                   scroll_worlds = new ScrollPane(worlds);
               }else if (button_name.equals("scroll_guns")){
                   flame_thrower = new Button(new TextureRegionDrawable(flameThrower_reg));
                   mashine_gun = new Button(new TextureRegionDrawable(mashineGun_reg));
                   normal_gun = new Button(new TextureRegionDrawable(normalGun_reg));
                   shot_gun = new Button(new TextureRegionDrawable(shotGun_reg));
                   sniper_riffle = new Button(new TextureRegionDrawable(sniperRiffle_reg));

                   flame_thrower.addListener(new InputListener(){
                   @Override
                   public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                       satrtPos_guns = new Vector2(x,y);
                       return true;
                   }
                   @Override
                   public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                       if (Math.abs(x - satrtPos_guns.x) < 30 && Math.abs(y - satrtPos_guns.y) < 30 && !scroll_guns.isDragging()) {
                           gunToLoad = "flame_thrower";
                           focus_on_table(worldSelection);
                           game.prefs.putString("lastGunUsed", gunToLoad);

                      }
                   }
                   });
                   mashine_gun.addListener(new InputListener(){
                       @Override
                       public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                           satrtPos_guns = new Vector2(x, y);
                           return true;
                       }
                       @Override
                       public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                           if (Math.abs(x - satrtPos_guns.x) < 30 && Math.abs(y - satrtPos_guns.y) < 30 && !scroll_guns.isDragging()) {
                               gunToLoad = "mashine_gun";
                               focus_on_table(worldSelection);
                               game.prefs.putString("lastGunUsed", gunToLoad);
                          }
                       }
                   });
                   normal_gun.addListener(new InputListener(){
                       @Override
                       public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                           satrtPos_guns = new Vector2(x, y);
                           return true;
                       }
                       @Override
                       public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                           if (Math.abs(x - satrtPos_guns.x) < 30 && Math.abs(y - satrtPos_guns.y) < 30 && !scroll_guns.isDragging()) {
                               gunToLoad = "normal_gun";
                               focus_on_table(worldSelection);
                               game.prefs.putString("lastGunUsed", gunToLoad);
                          }
                       }
                   });
                   shot_gun.addListener(new InputListener(){
                       @Override
                       public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                           satrtPos_guns = new Vector2(x, y);
                           return true;
                       }
                       @Override
                       public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                           if (Math.abs(x - satrtPos_guns.x) < 30 && Math.abs(y - satrtPos_guns.y) < 30 && !scroll_guns.isDragging()) {
                               gunToLoad = "shot_gun";
                               focus_on_table(worldSelection);
                               game.prefs.putString("lastGunUsed", gunToLoad);
                           }
                       }
                   });
                   sniper_riffle.addListener(new InputListener(){
                       @Override
                       public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                           satrtPos_guns = new Vector2(x, y);
                           return true;
                       }
                       @Override
                       public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                           if (Math.abs(x - satrtPos_guns.x) < 30 && Math.abs(y - satrtPos_guns.y) < 30 && !scroll_guns.isDragging()) {
                               gunToLoad = "sniper_riffle";
                               focus_on_table(worldSelection);
                               game.prefs.putString("lastGunUsed", gunToLoad);
                          }
                       }
                   });


                   guns = new Table();
                   float width = normal_gun.getMinWidth();
                   guns.add().minWidth(width * 3/4);
                   guns.add(flame_thrower);
                   guns.add().minWidth(width/2);
                   guns.add(mashine_gun);
                   guns.add().minWidth(width * 3/4);
                   guns.add(normal_gun);
                   guns.add().minWidth(width * 3/4);
                   guns.add(shot_gun);
                   guns.add().minWidth(width * 3/4);
                   guns.add(sniper_riffle);
                   guns.add().minWidth(width * 3/4);
                   scroll_guns = new ScrollPane(guns);
               }else if (button_name.equals("scroll_chairs")){
                   chair1 = new Button(new TextureRegionDrawable(chair1_reg));
                   chair2 = new Button(new TextureRegionDrawable(chair2_reg));
                   chairs = new Table();
                   float width = chair1.getMinWidth();
                   chairs.add().minWidth(width * 3/4);
                   chairs.add(chair1);
                   chairs.add().minWidth(width/2);
                   chairs.add(chair2);
                   chairs.add().minWidth(width * 3/4);
                   scroll_chairs = new ScrollPane(chairs);
               }else
                   System.out.println("WARNING: Invalid button name");
           }



       }

   }



}



/*
package com.dani.game2.Screens.Menus;

        import com.badlogic.gdx.Gdx;
        import com.badlogic.gdx.Screen;
        import com.badlogic.gdx.graphics.Camera;
        import com.badlogic.gdx.graphics.Color;
        import com.badlogic.gdx.graphics.GL20;
        import com.badlogic.gdx.graphics.OrthographicCamera;
        import com.badlogic.gdx.graphics.g2d.BitmapFont;
        import com.badlogic.gdx.math.Vector3;
        import com.badlogic.gdx.scenes.scene2d.InputEvent;
        import com.badlogic.gdx.scenes.scene2d.Stage;
        import com.badlogic.gdx.scenes.scene2d.ui.Button;
        import com.badlogic.gdx.scenes.scene2d.ui.Label;
        import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
        import com.badlogic.gdx.scenes.scene2d.ui.Table;
        import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
        import com.badlogic.gdx.utils.viewport.FillViewport;
        import com.badlogic.gdx.utils.viewport.Viewport;
        import com.dani.game2.ChainsawRun;
        import com.dani.game2.Screens.PlayScreen;

public class HomeScreen extends Menu{
    private ChainsawRun game;
    private Viewport viewport;

    private Stage stage;
    private Table table;
    OrthographicCamera cam;


    private Button campaign;
    private Button endless;
    private Button upgrades;
    private Button ideas;
    private Button options;

    private String screenName;


    public HomeScreen (ChainsawRun game){
        super(game);
        this.game = game;
        viewport = new FillViewport(ChainsawRun.V_Width, ChainsawRun.V_Height, new OrthographicCamera());
        make_stage();
        cam = new OrthographicCamera();
        viewport.setCamera(cam);
        game.batch.setProjectionMatrix(cam.combined);
        cam.viewportWidth  = 350;
        cam.viewportHeight = 200;
        cam.position.set(175, 100, 0);


        //cam.rotate(20);
        cam.update();


    }

    protected void make_stage(){
        super.make_stage();
        stage = new Stage(viewport, game.batch);
        table = new Table();

        table.center();
        table.setFillParent(true);

        LabelStyle font = new LabelStyle(new BitmapFont(), Color.WHITE);
        Button.ButtonStyle bstyle = new Button.ButtonStyle();


        Label tempText = new Label("Campaign", font);
        campaign = new Button(tempText, bstyle);




        tempText = new Label("Endless", font);
        endless = new Button(tempText, bstyle);

        tempText = new Label("Upgrades", font);
        upgrades = new Button(tempText, bstyle);

        tempText = new Label("Involve Your Ideas", font);
        ideas = new Button(tempText, bstyle);

        tempText = new Label("Options",font);
        options = new Button(tempText, bstyle);

        campaign.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                //campaign.setText("Starting new game");
                Gdx.app.log("MenuScreen", "clicked button");

            }
        });


        table.add(campaign);
        table.row();
        table.add(endless);
        table.row();
        table.add(upgrades);
        table.row();
        table.add(ideas);
        table.row();
        table.add(options);
        table.row();

        stage.addActor(table);
        //stage.setDebugAll(true);
        Gdx.input.setInputProcessor(stage);
    }

    protected void  handle_input(){
        ////System.out.print(campaign.isPressed() + "\n");
        if (loading && stage != null){
            //System.out.println("loading  " + stage);
            if (screenName.equals("campaign"))
                game.setScreen(new LevelSelection(game));

            if (screenName.equals("endless"))
                game.setScreen(new PlayScreen(game, "f", "endless"));

            dispose();

        }




        if (stage != null) {

            if (campaign.isPressed()) {
                ////System.out.print("lvlselpressed\n");
                loading = true;
                screenName = "campaign";

            } else if (endless.isPressed()) {
                loading = true;
                screenName = "endless";

            }

        }

    }

    protected void update(){
        handle_input();
    }

    public void render(float delta) {
        update();
        Gdx.gl.glClearColor(0, 0.4f, 0.5f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw_polygons();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void resize(int width, int height) {
        //float aspectRatio = (float) width / (float) height;
        //Camera camera = new OrthographicCamera(640, 360);
        //camera.translate(320,180);
        //camera.update();

        //stage.setViewport(new FillViewport(640, 360, camera));
        //stage.getViewport().setCamera(camera);
        viewport.update(width, height);
        //stage.getViewport().update(width, height);

        //table.set
    }

    @Override
    public void dispose() {
        stage.dispose();
        stage = null;
        //System.out.println("dispose: " + stage);
        //game.render();


    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }
}
*/