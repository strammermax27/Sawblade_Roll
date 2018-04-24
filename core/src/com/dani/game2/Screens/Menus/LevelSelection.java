package com.dani.game2.Screens.Menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.dani.game2.ChainsawRun;
import com.dani.game2.Screens.PlayScreen;


import java.io.File;

/**
 * Created by root on 12.09.16.
 */
public class LevelSelection extends Menu{

    //private Button LevelP;
    private Array<Button> levels;
    private FileHandle[] lvlDirectory;

    private String loadLevel;


    public LevelSelection (ChainsawRun game){
        super(game);

    }

    protected void make_stage(){
        super.make_stage();
        lvlDirectory = Gdx.files.internal("levels/levels").list();
        levels = new Array<Button>();

        Label tempText;
        Button tempButton;

        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        Button.ButtonStyle bstyle = new Button.ButtonStyle();

        //Label tempText = new Label("LevelP", font);
        //LevelP = new Button(tempText, bstyle);
        //table.add(LevelP);


        for (FileHandle level : lvlDirectory){

            if (level.file().getName().endsWith("tmx")) {
                ////System.out.print(level.file().getName() + "\n");
                tempText = new Label((level.file().getName()), font);
                tempButton = new Button(tempText, bstyle);
                tempButton.setName(level.file().getName());
                levels.add(tempButton);

                table.add(tempButton);
                table.row();
            }
        }

        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);

    }

    @Override
    protected void handle_input() {

        if (loading && stage != null) {
            game.setScreen(new PlayScreen(game, ("levels/levels/" + loadLevel), "campain"));
            dispose();
        }


        if (stage != null) {
            for (Button level : levels) {
                if (level.isPressed()) {
                    loading = true;
                    loadLevel = level.getName();

                }
            }
        }


    }

    @Override
    public void dispose(){
        loading = true;
        stage.dispose();
        stage = null;
        game.render();


    }

    @Override
    public void show() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

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
}
