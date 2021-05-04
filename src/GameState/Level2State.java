package GameState;

import Audio.AudioPlayer;
import Entity.*;
import Entity.Enemies.Projectile;
import Main.GamePanel;
import TileMap.TileMap;


import TileMap.Background;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Level2State extends GameState{


    private TileMap tileMap;
    private Background bg;
    private Player player;
   // private ArrayList<Enemy> enemies;
    //private ArrayList<Explosion>explosions;
    //private ArrayList<KenzoHead>kenzoHeads;
    private HUD hud;
    private AudioPlayer bgMusic;
    private AudioPlayer bossMusic;
    //private ArrayList<ForestThings>forest;
    //private boolean Switch;
    //private AudioPlayer KHAudio;
    //private boolean timeToSpawnPortal;
    //private Portal level2Portal;


    public Level2State(GameStateManager gsm)
    {
        this.gsm=gsm;
        init();
    }
    public void init()
    {
        tileMap=new TileMap(16);
        tileMap.loadTiles("/Tilesets/tileset2.png");
        tileMap.loadMap("/Maps/Level2.map");
        tileMap.setPosition(0,0);
        this.bg=new Background("/Backgrounds/LV2_BG2.png");
        player=new Player(tileMap);
        player.setPosition(100,100);
        hud=new HUD(player);
        bgMusic=new AudioPlayer("/Music/level2.mp3");
        bossMusic=new AudioPlayer("/Music/Boss2.mp3");
        bgMusic.play();

    }

    private void musicThings()
    {
        if(player.getx()==2700) {
            bgMusic.stop();
            if (!bossMusic.hasStopped()) {
                bgMusic.stop();

            }
            else
            {
                bossMusic.play();
            }

        }
        if(player.getx()<2700 && bgMusic.hasStopped() && bossMusic.hasStopped() &&!player.isDead())
        {
            bgMusic.play();
        }
        if(player.getx()>2700 && bossMusic.hasStopped() )
        {
            bgMusic.stop();
            bossMusic.play();
        }
        if(player.isDead())
        {
            bgMusic.stop();
            bossMusic.stop();
        }
    }

    public void update() {

        player.update();
        tileMap.setPosition(GamePanel.WIDTH/2-player.getx(),GamePanel.HEIGHT/2-player.gety());
        musicThings();


    }


    //Metode de draw

    public void draw(Graphics2D g) {

        bg.draw(g);
        tileMap.draw(g);
        player.draw(g);
        hud.draw(g);
        g.drawString("Score:"+ player.Score,230,12);



    }


    public void keyPressed(int k) {
        if(player.getHealth()!=0) {
            if (k == KeyEvent.VK_LEFT) player.setLeft(true);
            if (k == KeyEvent.VK_RIGHT) player.setRight(true);
            if (k == KeyEvent.VK_UP) player.setUp(true);
            if (k == KeyEvent.VK_DOWN) player.setDown(true);
            if (k == KeyEvent.VK_W) player.setJumping(true);
            if (k == KeyEvent.VK_R) player.setLongAttacking(true);
            if (k == KeyEvent.VK_F) player.setSmallAttacking(true);
        }
    }

    public void keyReleased(int k) {
        if(k == KeyEvent.VK_LEFT) player.setLeft(false);
        if(k == KeyEvent.VK_RIGHT) player.setRight(false);
        if(k == KeyEvent.VK_UP) player.setUp(false);
        if(k == KeyEvent.VK_DOWN) player.setDown(false);
        if(k == KeyEvent.VK_W) player.setJumping(false);

    }

}
