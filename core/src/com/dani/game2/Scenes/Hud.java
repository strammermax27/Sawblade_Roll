package com.dani.game2.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dani.game2.ChainsawRun;
import com.dani.game2.Screens.PlayScreen;



/**
 * Created by root on 14.07.16.
 */
public class Hud implements Disposable{
    public Stage stage;
    private Viewport viewport;

    private Body playerBody;

    private  Integer worldTimer;
    private float timecount;
    private static Integer score;
    //private Integer timerCount;


    private Label speedLabel;
    private Label distanceLabel;
    private Label worldLabel;
    private static Label scoreLabel;
    private Label countdownLabel;
    private Label marioLabel;

    private int speed;

    public  Hud(PlayScreen screen, SpriteBatch sb){

        worldTimer = 300;
        timecount = 0;
        score = 0;

        viewport = new FitViewport(ChainsawRun.V_Width, ChainsawRun.V_Height, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        playerBody = screen.getPlayer().b2dbody;

        Table table = new Table();
        table.top();
        table.setFillParent(true);



        countdownLabel = new Label(java.lang.String.format("%03d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel =     new Label(java.lang.String.format("%06d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        speedLabel = new Label(java.lang.String.format("TIME"), new Label.LabelStyle(new BitmapFont(Gdx.files.internal("animations/font_styles/testStyle.fnt")), Color.WHITE));
        distanceLabel = new Label(java.lang.String.format("1-1"), new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        worldLabel = new Label("WORLD", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        marioLabel = new Label("MARIO", new Label.LabelStyle(new BitmapFont(), Color.WHITE));


       // table.add(marioLabel).expandX().padTop(10);
        //table.add(worldLabel).expandX().padTop(10);
        table.add(speedLabel).expandX().padTop(10);
        //table.row();
        //table.add(scoreLabel).expandX();
        //table.add(distanceLabel).expandX();
        //table.add(scoreLabel);
        //table.add(countdownLabel).expandX();

        stage.addActor(table);


    }

    public void update(float dt){


        //speed = (int)(Math.abs(playerBody.getLinearVelocity().x) + Math.abs(playerBody.getLinearVelocity().y));
        speed = (int)playerBody.getPosition().x /1;
        speedLabel.setText(java.lang.String.format("%20d",speed));
        //speedLabel.setFontScale((float)speed * .05f + 1);



        timecount += dt;
        if (timecount >= 1) {
            ////System.out.print(timecount + "\n");
            worldTimer--;
            countdownLabel.setText(java.lang.String.format("%03d", worldTimer));
            timecount = 0;
            ////System.out.print(worldTimer + "timer updated\n");
            ;
        }
    }


    public static void add_Score(int value){
        score += value;
        scoreLabel.setText(java.lang.String.format("%06d", score));
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public void resize(int width, int height){
        viewport.update(width, height);
    }

    public void draw(){stage.draw();}
}
