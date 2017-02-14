package com.fruitattackv2.game;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class GameScreen implements Screen
{
    final FruitAttack_Game game;

    private Texture fondoPantalla;

    private Texture imgBomba;
    private Texture imgCubo;
    private Texture imgPera;
    private Texture imgNaranja;
    private Texture imgPinha;

    private Sound soundExplosion;
    private Sound sonidoPera;
    private Sound sonidoNaranja;
    private Sound sonidoPinha;

    private Music music;

    private OrthographicCamera camera;

    /*Cada imagen tiene un eje x/y, una representación gráfica, un peso y una altura
     *Por eso la mejor forma de manejarlas es usando un rectángulo en el que irá cada imagen. */
    private Rectangle cubo;
    private Rectangle fondo;

    private Array<Rectangle> arrayPeras;
    private Array<Rectangle> arrayNaranjas;
    private Array<Rectangle> arrayPinhas;
    private Array<Rectangle> arrayBombas;

    //Score
    private int scoreInt;
    private String scoreString;
    BitmapFont scoreBitmapFont;

    //Time
    private int timeInt;
    private String timeString;
    BitmapFont timeBitmapFont;

    //Lifes
    private int lifeInt;
    private String lifeString;
    BitmapFont lifeBitmapFont;

    //Para la frecuencia con la que las frutas spamean
    private long lastDropTime;

    public GameScreen(final FruitAttack_Game gam)
    {
        this.game = gam;

        //Cargar fondo de pantalla
        fondoPantalla = new Texture(Gdx.files.internal("gamebackground.png"));

        // Cargar las imágenes de "Assets", de 64x64
        imgBomba = new Texture(Gdx.files.internal("bomba.png"));
        imgCubo = new Texture(Gdx.files.internal("cubo.png"));
        imgPera = new Texture(Gdx.files.internal("pera.png"));
        imgNaranja = new Texture(Gdx.files.internal("naranja.png"));
        imgPinha = new Texture(Gdx.files.internal("pinha.png"));

        //Cargar los sonidos
        soundExplosion = Gdx.audio.newSound(Gdx.files.internal("explosion.mp3"));
        sonidoNaranja = Gdx.audio.newSound(Gdx.files.internal("soundnaranja.mp3"));
        sonidoPera = Gdx.audio.newSound(Gdx.files.internal("soundpera.mp3"));
        sonidoPinha = Gdx.audio.newSound(Gdx.files.internal("soundpinha.mp3"));

        //Cargar la música
        music = Gdx.audio.newMusic(Gdx.files.internal("gamemusic.mp3"));
        music.setLooping(true);

		/*La cámara siempre mostrará un área del juego de 480x800.
		 *Es como una ventana al juego.*/
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 480, 800);

        //Array de peras
        arrayPeras = new Array<Rectangle>();
        spawnPerasdrop();

        //Array de naranjas
        arrayNaranjas = new Array<Rectangle>();
        spawnNaranjasdrop();

        //Array de pinhas
        arrayPinhas = new Array<Rectangle>();
        spawnPinhasdrop();

        //Array de bombas
        arrayBombas = new Array<Rectangle>();
        spawnBombasdrop();

        //Fondo
        fondo = new Rectangle();

        //Score
        scoreString = new String();
        scoreBitmapFont = new BitmapFont();

        scoreInt = 0;
        scoreString = "SCORE: " + scoreInt;
        scoreBitmapFont = new BitmapFont();
        scoreBitmapFont.getData().setScale(2);

        //Time
        timeString = new String();
        timeBitmapFont = new BitmapFont();

        timeInt = 0;
        timeString = "TIME: " + timeInt;
        timeBitmapFont = new BitmapFont();
        timeBitmapFont.getData().setScale(2);

        //Lifes
        lifeString = new String();
        lifeBitmapFont = new BitmapFont();

        lifeInt = 3;
        lifeString = "LIFES: " + lifeInt;
        lifeBitmapFont = new BitmapFont();
        lifeBitmapFont.getData().setScale(2);

        //Cubo
        cubo = new Rectangle();
        cubo.x = 480 / 2 - 64 / 2;
        cubo.y = 20;
        cubo.width = 64;
        cubo.height = 64;
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
        /*The arguments are the red, green, blue and alpha component of that color, each within the range [0, 1].
		 *The next call instructs OpenGL to actually clear the screen.*/
        //Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // tell the camera to update its matrices.
        camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        game.batch.setProjectionMatrix(camera.combined);

        // begin a new batch and draw the bucket and all things
        game.batch.begin();

        //Renderizar fondo
        game.batch.draw(fondoPantalla, fondo.x, fondo.y);

        //Renderizar cubo
        game.batch.draw(imgCubo, cubo.x, cubo.y);

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

        //Renderizar bombas
        for(Rectangle bomba: arrayBombas)
        {
            game.batch.draw(imgBomba, bomba.x, bomba.y);
        }

        //Renderizar Score
        scoreBitmapFont.setColor(Color.WHITE);
        scoreBitmapFont.draw(game.batch, scoreString, 20, 780);

        //Renderizar Time
        timeBitmapFont.setColor(Color.WHITE);
        timeBitmapFont.draw(game.batch, timeString, 20, 740);

        //Renderizar Lifes
        lifeBitmapFont.setColor(Color.WHITE);
        lifeBitmapFont.draw(game.batch, lifeString, 20, 700);

        game.batch.end();

        //Mover cubo
        if(Gdx.input.isTouched())
        {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            cubo.x = touchPos.x - 64 / 2;
        }

        //Límites de pantalla para el cubo
        if (cubo.x < 0)
            cubo.x = 0;
        if (cubo.x > 480 - 64)
            cubo.x = 480 - 64;

        //Spam de frutas y bombas
        if(TimeUtils.nanoTime() - lastDropTime > 2100000000)
        {
            spawnPerasdrop();
            spawnNaranjasdrop();
            spawnPinhasdrop();
            spawnBombasdrop();
        }

        //Borramos del array las peras que llegan al fondo de la pantalla
        Iterator<Rectangle> iterPera = arrayPeras.iterator();
        while(iterPera.hasNext())
        {
            Rectangle pera = iterPera.next();
            pera.y -= 100 * Gdx.graphics.getDeltaTime();
            if(pera.y + 64 < 0)
                iterPera.remove();

            //Hacer desaparecer la pera (y que suene) si toca el cubo
            if(pera.overlaps(cubo))
            {
                scoreInt = scoreInt+5; //Puntos
                scoreString = "SCORE: " + scoreInt;
                sonidoPera.play();
                iterPera.remove();
            }
        }

        //Borramos del array las naranjas que llegan al fondo de la pantalla
        Iterator<Rectangle> iterNaranja = arrayNaranjas.iterator();
        while(iterNaranja.hasNext())
        {
            Rectangle naranja = iterNaranja.next();
            naranja.y -= 200 * Gdx.graphics.getDeltaTime();
            if(naranja.y + 64 < 0)
                iterNaranja.remove();

            //Hacer desaparecer la naranja (y que suene) si toca el cubo
            if(naranja.overlaps(cubo))
            {
                scoreInt = scoreInt+10; //Puntos
                scoreString = "SCORE: " + scoreInt;
                sonidoNaranja.play();
                iterNaranja.remove();
            }
        }

        //Borramos del array las pinhas que llegan al fondo de la pantalla
        Iterator<Rectangle> iterPinha = arrayPinhas.iterator();
        while(iterPinha.hasNext())
        {
            Rectangle pinha = iterPinha.next();
            pinha.y -= 300 * Gdx.graphics.getDeltaTime();
            if(pinha.y + 64 < 0)
                iterPinha.remove();

            //Hacer desaparecer la pinha (y que suene) si toca el cubo
            if(pinha.overlaps(cubo))
            {
                scoreInt = scoreInt+15; //Puntos
                scoreString = "SCORE: " + scoreInt;
                sonidoPinha.play();
                iterPinha.remove();
            }
        }

        //Borramos del array las bombas que llegan al fondo de la pantalla
        Iterator<Rectangle> iterBomba = arrayBombas.iterator();
        while(iterBomba.hasNext())
        {
            Rectangle bomba = iterBomba.next();
            bomba.y -= 350 * Gdx.graphics.getDeltaTime();
            if(bomba.y + 64 < 0)
                iterBomba.remove();

            //Hacer desaparecer la bomba (y que suene) si toca el cubo
            if(bomba.overlaps(cubo))
            {
                scoreInt = scoreInt-10; //Puntos
                scoreString = "SCORE: " + scoreInt;
                lifeInt = lifeInt-1; //Vidas
                lifeString = "LIFES: " + lifeInt;
                soundExplosion.play();
                iterBomba.remove();
            }
        }

        //Game Over
        if (lifeInt<0)
        {
            game.setScreen(new GameOverScreen(game));
            dispose();
        }
    }

    @Override
    public void show()
    {
        // start the playback of the background music
        // when the screen is shown
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
        scoreBitmapFont.dispose();
        timeBitmapFont.dispose();
        lifeBitmapFont.dispose();
        fondoPantalla.dispose();
        imgCubo.dispose();
        imgPinha.dispose();
        imgNaranja.dispose();
        imgBomba.dispose();
        imgPera.dispose();
        sonidoNaranja.dispose();
        sonidoPinha.dispose();
        sonidoPera.dispose();
        soundExplosion.dispose();
        music.dispose();
    }
}
