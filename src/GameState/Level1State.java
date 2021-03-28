package GameState;

import Audio.AudioPlayer;
import Entity.*;
import Entity.Enemies.Mushroom;
import Main.GamePanel;
import TileMap.TileMap;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import Audio.AudioPlayer.*;

import TileMap.Background;
public class Level1State extends GameState{

    private TileMap tileMap;
    private Background bg;
    private Player player;
    private ArrayList<Enemy> enemies;
    private ArrayList<Explosion>explosions;
    private HUD hud;
    private Tree tree;
    private AudioPlayer bgMusic;
    public Level1State(GameStateManager gsm)
    {
        this.gsm=gsm;
        init();

    }


    public void init() {
        tileMap=new TileMap(16);
        tileMap.loadTiles("/Tilesets/Tileset3.png");
        tileMap.loadMap("/Maps/Level1.map");////switch
        tileMap.setPosition(0,0);
        tileMap.setTween(1);
       this.bg=new Background("/Backgrounds/BG5.png",0.1);
       player=new Player(tileMap);
       player.setPosition(100,30);
        populateEnemies();
       hud=new HUD(player);
       explosions=new ArrayList<Explosion>();
        bgMusic=new AudioPlayer("/Music/08-China-Great-Wall.mp3");
        bgMusic.play();





    }

    private void populateEnemies()
    {
        enemies=new ArrayList<Enemy>();
        Point[] myPointArray=new Point[]{
                new Point(300,10),
                new Point(840, 10),
                new Point(1560, 10),
                new Point(1670, 10),
                new Point(1800, 10)

        };
        for(int i=0;i<myPointArray.length;i++)
        {
            Mushroom m=new Mushroom(tileMap);
            m.setPosition(myPointArray[i].x,myPointArray[i].y);
            enemies.add(m);
        }
    }


    public void update() {
    //update palyer
        player.update();
       tileMap.setPosition(GamePanel.WIDTH/2-player.getx(),GamePanel.HEIGHT/2-player.gety());

       player.checkAttack(enemies);
       //update all enemies;
        for(int i=0;i<enemies.size();i++)
        {
            Enemy e=enemies.get(i);
            e.update();
            if(e.isDead())
            {
                enemies.remove(i);
                i--;
                explosions.add(new Explosion(e.getx(),e.gety()));
            }
        }
        for(int i=0;i<explosions.size();i++)
        {
            explosions.get(i).update();
            if(explosions.get(i).shouldRemove())
            {
                explosions.remove(i);
                i--;
            }
        }


    }


    public void draw(Graphics2D g) {
        //clear screen;
        bg.draw(g);
        tileMap.draw(g);

        player.draw(g);
        //draw enemies;
        for(int i=0;i<enemies.size();i++)
        {
            enemies.get(i).draw(g);
        }
        //draw explotions
        for(int i=0;i<explosions.size();i++)
        {
            explosions.get(i).setMapPosition(
                    (int)tileMap.getx(), (int)tileMap.gety());
            explosions.get(i).draw(g);
        }
        hud.draw(g);

    }


    public void keyPressed(int k) {
        if(k == KeyEvent.VK_LEFT) player.setLeft(true);
        if(k == KeyEvent.VK_RIGHT) player.setRight(true);
        if(k == KeyEvent.VK_UP) player.setUp(true);
        if(k == KeyEvent.VK_DOWN) player.setDown(true);
        if(k == KeyEvent.VK_W) player.setJumping(true);
        if(k == KeyEvent.VK_R) player.setLongAttacking(true);
        if(k == KeyEvent.VK_F) player.setSmallAttacking(true);
    }

    public void keyReleased(int k) {
        if(k == KeyEvent.VK_LEFT) player.setLeft(false);
        if(k == KeyEvent.VK_RIGHT) player.setRight(false);
        if(k == KeyEvent.VK_UP) player.setUp(false);
        if(k == KeyEvent.VK_DOWN) player.setDown(false);
        if(k == KeyEvent.VK_W) player.setJumping(false);

    }
}
