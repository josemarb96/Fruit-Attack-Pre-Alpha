package com.fruitattackv2.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class MainMenuScreen implements Screen
{
    final FruitAttack_Game game;
    OrthographicCamera camera;
    private Texture fondoPantalla;
    private Rectangle fondo;
    private Music music;
    private long lastDropTime;
    private Array<Rectangle> arrayPeras;
    private Array<Rectangle> arrayNaranjas;
    private Array<Rectangle> arrayPinhas;
    private Texture imgPera;
    private Texture imgNaranja;
    private Texture imgPinha;

    public MainMenuScreen(final FruitAttack_Game game)
    {
        this.game = game;

        //Cargar fondo de pantalla
        fondoPantalla = new Texture(Gdx.files.internal("mainmenubackground.png"));

        // Cargar las imágenes de "Assets", de 64x64
        imgPera = new Texture(Gdx.files.internal("pera.png"));
        imgNaranja = new Texture(Gdx.files.internal("naranja.png"));
        imgPinha = new Texture(Gdx.files.internal("pinha.png"));

        //Array de peras
        arrayPeras = new Array<Rectangle>();
        spawnPerasdrop();

        //Array de naranjas
        arrayNaranjas = new Array<Rectangle>();
        spawnNaranjasdrop();

        //Array de pinhas
        arrayPinhas = new Array<Rectangle>();
        spawnPinhasdrop();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 480, 800);

        //Fondo
        fondo = new Rectangle();

        //Cargar la música
        music = Gdx.audio.newMusic(Gdx.files.internal("mozart.mp3"));
        music.setLooping(true);

        //Array de peras
        arrayPeras = new Array<Rectangle>();
        spawnPerasdrop();

        //Array de naranjas
        arrayNaranjas = new Array<Rectangle>();
        spawnNaranjasdrop();

        //Array de pinhas
        arrayPinhas = new Array<Rectangle>();
        spawnPinhasdrop();
    }

    //Spam de peras
    private void spawnPerasdrop()
    {
		/*Crear un rectangle con las peras y meterlo en el array de peras.
		 *El random hace que salgan aleatoriamente  en el ancho de la pantalla.*/
        Rectangle pera = new Rectangle();
        pera.x = MathUtils.random(0, 480-64);
        pera.y = 800;
        pera.width = 64;
        pera.height = 64;
        arrayPeras.add(pera);
        lastDropTime = TimeUtils.nanoTime();
    }

    //Spam de naranjas
    private void spawnNaranjasdrop()
    {
        Rectangle naranja = new Rectangle();
        naranja.x = MathUtils.random(0, 480-64);
        naranja.y = 800;
        naranja.width = 64;
        naranja.height = 64;
        arrayNaranjas.add(naranja);
        lastDropTime = TimeUtils.nanoTime();
    }

    //Spam de pinhas
    private void spawnPinhasdrop()
    {
        Rectangle pinha = new Rectangle();
        pinha.x = MathUtils.random(0, 480-64);
        pinha.y = 800;
        pinha.width = 64;
        pinha.height = 64;
        arrayPinhas.add(pinha);
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

        //Renderizar peras
        for(Rectangle pera: arrayPeras)
        {
            game.batch.draw(imgPera, pera.x, pera.y);
        }

        //Renderizar naranjas
        for(Rectangle naranja: arrayNaranjas)
        {
            game.batch.draw(imgNaranja, naranja.x, naranja.y);
        }

        //Renderizar pinhas
        for(Rectangle pinha: arrayPinhas)
        {
            game.batch.draw(imgPinha, pinha.x, pinha.y);
        }

        game.batch.end();

        if (Gdx.input.isTouched())
        {
            game.setScreen(new GameScreen(game));
            dispose();
        }

        //Spam de frutas y bombas
        if(TimeUtils.nanoTime() - lastDropTime > 2100000000)
        {
            spawnPerasdrop();
            spawnNaranjasdrop();
            spawnPinhasdrop();
        }

        //Borramos del array las peras que llegan al fondo de la pantalla
        Iterator<Rectangle> iterPera = arrayPeras.iterator();
        while(iterPera.hasNext())
        {
            Rectangle pera = iterPera.next();
            pera.y -= 100 * Gdx.graphics.getDeltaTime();
            if(pera.y + 64 < 0)
                iterPera.remove();
        }

        //Borramos del array las naranjas que llegan al fondo de la pantalla
        Iterator<Rectangle> iterNaranja = arrayNaranjas.iterator();
        while(iterNaranja.hasNext())
        {
            Rectangle naranja = iterNaranja.next();
            naranja.y -= 200 * Gdx.graphics.getDeltaTime();
            if(naranja.y + 64 < 0)
                iterNaranja.remove();
        }

        //Borramos del array las pinhas que llegan al fondo de la pantalla
        Iterator<Rectangle> iterPinha = arrayPinhas.iterator();
        while(iterPinha.hasNext())
        {
            Rectangle pinha = iterPinha.next();
            pinha.y -= 300 * Gdx.graphics.getDeltaTime();
            if(pinha.y + 64 < 0)
                iterPinha.remove();
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
        imgPera.dispose();
        imgPinha.dispose();
        imgNaranja.dispose();
    }
}
