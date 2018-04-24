package com.dani.game2.Screens.Menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dani.game2.ChainsawRun;
import com.dani.game2.Screens.PlayScreen;

import java.util.concurrent.TimeUnit;

/**
 * Created by root on 12.09.16.
 */
public abstract class Menu implements Screen {
    protected ChainsawRun game;
    protected Viewport viewport;

    protected Stage stage;
    protected Table table;

    protected boolean loading = false;

    private Button campaign;
    private Button endless;
    private Button upgrades;
    private Button ideas;
    private Button options;


    public Menu (ChainsawRun game){
        this.game = game;
        viewport = new FillViewport(ChainsawRun.V_Width, ChainsawRun.V_Height, new OrthographicCamera());
        make_stage();

    }

    protected void make_stage(){
        stage = new Stage(viewport, game.batch);
        table = new Table();

        table.center();
        table.setFillParent(true);


    }

    protected abstract void  handle_input();






    protected void update(){
        handle_input();

    }

    public void render(float delta) {
        update();
        Gdx.gl.glClearColor(0, 0.4f, 0.5f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (stage != null){
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



    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {

    }

}
