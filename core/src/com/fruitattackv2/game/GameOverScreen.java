package com.fruitattackv2.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class GameOverScreen implements Screen
{
    final FruitAttack_Game game;
    OrthographicCamera camera;
    private Texture fondoPantalla;
    private Rectangle fondo;
    private Music music;
    private Texture imgBomba;
    private Array<Rectangle> arrayBombas;
    private long lastDropTime;

    public GameOverScreen(final FruitAttack_Game ga)
    {
        this.game = ga;

        //Cargar fondo de pantalla
        fondoPantalla = new Texture(Gdx.files.internal("gameoverbackground.png"));

        imgBomba = new Texture(Gdx.files.internal("bomba.png"));
        arrayBombas = new Array<Rectangle>();
        spawnBombasdrop();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 480, 800);

        //Fondo
        fondo = new Rectangle();

        //Cargar la música
        music = Gdx.audio.newMusic(Gdx.files.internal("gameovermusic.mp3"));
        music.setLooping(true);
    }

    //Spam de bombas
    private void spawnBombasdrop()
    {
        Rectangle bomba = new Rectangle();
        bomba.x = MathUtils.random(0, 480-64);
        bomba.y = 800;
        bomba.width = 64;
        bomba.height = 64;
        arrayBombas.add(bomba);
        lastDropTime = TimeUtils.nanoTime();
    }

    @Override
    public void render(float delta)
    {
        //Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();

        //Renderizar fondo
        game.batch.draw(fondoPantalla, fondo.x, fondo.y);

        for(Rectangle bomba: arrayBombas)
        {
            game.batch.draw(imgBomba, bomba.x, bomba.y);
        }

        game.batch.end();

        //Bombas
        if(TimeUtils.nanoTime() - lastDropTime > 2100000000)
        {
            spawnBombasdrop();
        }

        //Borramos del array las bombas que llegan al fondo de la pantalla
        Iterator<Rectangle> iterBomba = arrayBombas.iterator();
        while(iterBomba.hasNext())
        {
            Rectangle bomba = iterBomba.next();
            bomba.y -= 350 * Gdx.graphics.getDeltaTime();
            if(bomba.y + 64 < 0)
                iterBomba.remove();
        }
    }

    @Override
    public void show()
    {
        music.play();
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose()
    {
        fondoPantalla.dispose();
        music.dispose();
        imgBomba.dispose();
    }
}
