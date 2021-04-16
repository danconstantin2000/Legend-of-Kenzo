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
    private AudioPlayer bgMusic;
    private AudioPlayer bossMusic;
    private ArrayList<ForestThings>forest;

    public Level1State(GameStateManager gsm)
    {
        this.gsm=gsm;
        init();

    }


    public void init() {
        tileMap=new TileMap(16);
        tileMap.loadTiles("/Tilesets/tileset.png");
        tileMap.loadMap("/Maps/Level1.map");
        tileMap.setPosition(0,0);

       this.bg=new Background("/Backgrounds/BG9.png");
       player=new Player(tileMap);
       player.setPosition(100,100);
        populatetrees();
        populateEnemies();
       hud=new HUD(player);
       explosions=new ArrayList<Explosion>();
        bgMusic=new AudioPlayer("/Music/08-China-Great-Wall.mp3");
        bossMusic=new AudioPlayer("/Music/Boss.mp3");
      //  bgMusic.play();








    }

    private void populatetrees()
    {
        forest=new ArrayList<ForestThings>();

        Point[] myPointArrayTrees=new Point[]{
                new Point(100,100),
                new Point(30,100),
                new Point(170,100),
                new Point(430,68),
               // new Point(100,100),
                //new Point(100,100),
                new Point(530, 51),
                new Point(600, 51),
                new Point(670, 51),
                new Point(740, 51),
                new Point(810, 51),
                new Point(950, 67),
                new Point(1020, 67),
                new Point(1090, 67),
                new Point(1160, 67),
                new Point(1300, 67),
                new Point(1450, 52),
                new Point(1560, 67),
                new Point(1670, 51),
                new Point(1740, 51),
                new Point(1810, 51),
                new Point(1880, 51),
                new Point(1950, 51),
                new Point(2020, 51),
                new Point(2150, 35),
                new Point(2220, 35),
                new Point(2290, 35),
                new Point(2360, 35),
                new Point(2430, 35),
                new Point(2520,67),
                new Point(2590, 67),
                new Point(2660, 67),
                new Point(2730, 67),
                new Point(2800, 67),
                new Point(2870, 67),
                new Point(2940, 67),





        };
        for(int i=0;i<myPointArrayTrees.length;i++)
        {
            Tree t=new Tree(myPointArrayTrees[i].x,myPointArrayTrees[i].y);
            forest.add(t);
        }
        Point[] myPointArrayBush=new Point[]
        {
            new Point(275,42),
                new Point(310,42),
                new Point(630,10),
                new Point(770,10),
                new Point(925,27),
                new Point(1050,27),
                new Point(1185,27),
                new Point(1410,10),
                new Point(1700,10),
                new Point(1920,10),
                new Point(2625,27),
                new Point(2825,27),

        };
        for(int i=0;i<myPointArrayBush.length;i++)
        {
            Bush b=new Bush(myPointArrayBush[i].x,myPointArrayBush[i].y);
            forest.add(b);
        }
        Point[] myPointArrayRuin=new Point[]{
                        new Point(985,27),
                        new Point(1830,10),
                        new Point(2550,27)
        };
        for(int i=0;i<myPointArrayRuin.length;i++)
        {
            Ruin r=new Ruin(myPointArrayRuin[i].x,myPointArrayRuin[i].y);
            forest.add(r);
        }


    }
    private void populateEnemies()
    {
        enemies=new ArrayList<Enemy>();
        Point[] myPointArray=new Point[]{
                new Point(400,100),
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
                player.score=player.score+500;
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

        if(player.getx()==2700) {
            bgMusic.stop();
            if (!bossMusic.hasStopped()) {
                //bgMusic.stop();

            }
            else
            {
                //bossMusic.play();
            }

        }
        if(player.getx()>2700 && bossMusic.hasStopped())
        {
            bgMusic.stop();
            bossMusic.play();
        }
        if(player.getHealth()==0 )
        {
            bgMusic.stop();
            if(player.switchState ||player.Gol)
                gsm.setState(GameStateManager.GAMEOVERSTATE);
        }
    }


    public void draw(Graphics2D g) {
        //clear screen;

        bg.draw(g);
        tileMap.draw(g);
        for(int i=0;i<forest.size();i++)
        {
            forest.get(i).setMapPosition((int)tileMap.getx(),(int)tileMap.gety());
            forest.get(i).draw(g);

        }

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
        g.drawString(player.getx()+"/"+player.gety()+"/"+ player.score,200,12);

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
