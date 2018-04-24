package com.dani.game2.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dani.game2.ChainsawRun;
import com.dani.game2.Screens.PlayScreen;

/**
 * Created by root on 15.08.16.
 */
public class Controller {
    private Viewport viewport2;
    public Stage stage;
    public boolean  leftPressed, rightPressed, AttackPressed;
    private boolean upPressed, justAttackPressed;
    private OrthographicCamera cam;
    private PlayScreen screen;
    public float stageHeight;

    public Controller(PlayScreen screen){
        this.screen = screen;
        cam = new OrthographicCamera();

        viewport2 = new StretchViewport(ChainsawRun.V_Width, ChainsawRun.V_Height, cam);

        //viewport2 = screen.getViewport();
        stage = new Stage(viewport2, ChainsawRun.batch);
        Gdx.input.setInputProcessor(stage);

        Table tableLeft = new Table();
        tableLeft.bottom();
        tableLeft.left();

        Table tableRight = new Table();
        tableRight.center();
        tableRight.padRight(100);

        Image btnImg = new Image(new Texture("controller/testbutton.png"));
        btnImg.setSize(50, 50);
        btnImg.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                upPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                upPressed = false;
            }
        });

        Image btnImg2 = new Image(new Texture("controller/testbutton.png"));
        btnImg2.setSize(50, 50);
        btnImg2.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                leftPressed= true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed = false;
            }
        });

        Image btnImg3 = new Image(new Texture("controller/testbutton.png"));
        btnImg3.setSize(50, 50);
        btnImg3.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed = false;
            }
        });

        final Image btnImg4 = new Image(new Texture("controller/testbutton.png"));
        btnImg4.setSize(50, 50);
        btnImg4.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                justAttackPressed = true;
                AttackPressed = true;
                return true;

            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                justAttackPressed = false;
                AttackPressed = false;
            }
        });

        float buttonHeight = 46;
        float buttonWidth = 80;

        //tableLeft.add(btnImg2).size(buttonWidth,buttonHeight);
        //tableLeft.add().size(20,buttonHeight);
        tableLeft.add(btnImg3).size(buttonWidth,buttonHeight);
        tableLeft.add().size(30,buttonHeight);
        /*tableLeft.add(btnImg).size(buttonWidth,buttonHeight);
        tableLeft.add().size(20, buttonHeight);
        tableLeft.add(btnImg4).size(buttonWidth,buttonHeight);
        tableLeft.padLeft(20);*/
        tableLeft.padLeft(20);

        //stage.setDebugAll(true);
        stage.addActor(tableLeft);
        //stage.addActor(tableRight);
        //System.out.print("stage width" + stage.getWidth()+ "\n");
        //stage.setDebugInvisible(false);
        stageHeight = stage.getHeight();
        //stage.setDebugAll(true);


    }

    public void draw(){
        stage.draw();
    }

    public void resize(int width, int height){
        float better_height = (float)height *1;
        float better_width = (float)width * 1f;
        viewport2.update((int) better_width, (int)better_height);
        stageHeight = stage.getHeight();

        //viewport2.setScreenPosition(-100,0);
        //viewport2 = screen.getViewport();
    }

    public boolean get_upPressed(){
        if (upPressed){
            upPressed = false;
            return true;}
        else{
            return false;
        }
    }

    public boolean get_justAttackPressed(){
        if(justAttackPressed){
            justAttackPressed = false;
            return true;}
        else {
            return false;}
    }


}
