package GameState;

import Audio.AudioPlayer;
import Entity.*;
import Entity.Enemies.DarkMagician;
import Entity.Enemies.Goblin;
import Entity.Enemies.Mushroom;
import Entity.Enemies.Projectile;
import Forest.Bush;
import Forest.ForestThings;
import Forest.Ruin;
import Forest.Tree;
import Main.GamePanel;
import TileMap.TileMap;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

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
    private boolean Switch;

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
        bgMusic.play();
        Projectile.projectiles=new ArrayList<Projectile>();
        Switch=false;

    }

    //Metoda ce populeaza harta cu ruine,copaci,tufisuri..
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
    //Metoda ce populeaza harta cu inamici precum ciuperca,gobin etc
    private void populateEnemies()
    {
        enemies=new ArrayList<Enemy>();
        Point[] myPointArray=new Point[]{
                new Point(450,100),
                new Point(840, 10),
                new Point(1200, 10),
                new Point(1430, 10),
                new Point(1560, 10),
                new Point(2200,10),
                new Point(2400,10)
        };
        Point[] myPointArray2=new Point[]{
                new Point(600,30),
                new Point(1050,30),
                new Point(1750, 10),
                new Point(1900,10),
                new Point(2640,10),

        };
        for(int i=0;i<myPointArray.length;i++)
        {
            Mushroom m=new Mushroom(tileMap,player);
            m.setPosition(myPointArray[i].x,myPointArray[i].y);
            enemies.add(m);
        }
        for(int i=0;i<myPointArray2.length;i++)
        {
            Goblin g=new Goblin(tileMap,player);
            g.setPosition(myPointArray2[i].x,myPointArray2[i].y);
            enemies.add(g);
        }
        DarkMagician DM=new DarkMagician(tileMap,player);
        DM.setPosition(3000,20);
        enemies.add(DM);
    }

    //Metode de update
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
        if(player.getx()>2700 && bossMusic.hasStopped())
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
    private void updateAllEnemies()
    {

        for(int i=0;i<enemies.size();i++)
        {
            Enemy e=enemies.get(i);
            e.update();
            if(e.isDead())
            {
                if(e instanceof DarkMagician)
                {
                    if(((DarkMagician) e).getPlayed())
                    {

                        player.Score = player.Score + e.getScore();
                        enemies.remove(i);
                        i--;
                        explosions.add(new Explosion(e.getx(), e.gety()));
                    }
                }else {

                    player.Score = player.Score + e.getScore();
                    enemies.remove(i);
                    i--;
                    explosions.add(new Explosion(e.getx(), e.gety()));
                }
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
    private void GameOver()
    {
        if(player.isDead())
        {
            bgMusic.stop();
            bossMusic.stop();
            if(player.getSwitchState() ||player.getVoid())
                gsm.setState(GameStateManager.GAMEOVERSTATE);


        }
    }
    private void updateAllProjectiles(){
        for(int i=0;i<Projectile.projectiles.size();i++)
        {
            Projectile.projectiles.get(i).update();
            if(Projectile.projectiles.get(i).shouldRemove())
            {
                Projectile.projectiles.remove(i);
            }
        }
    }
    /*Metoda de update care:
        -actualizeaza starea player-ului
        -actualizeaza harta
        -verifica atacurile dintre player-inamic,player-proiectila
        -actualizeaza inamicii
        -actualizeaza proiectilele
        -actualizeaza sunetele

     */
    public void update() {

        player.update();
        tileMap.setPosition(GamePanel.WIDTH/2-player.getx(),GamePanel.HEIGHT/2-player.gety());
        player.checkAttack(enemies);
        player.checkAttack2(Projectile.projectiles);
        updateAllEnemies();
        musicThings();
        GameOver();
        updateAllProjectiles();
        if(player.getx()>2800)
        {
          Switch=true;
          player.setJumpStart(-3.5);
        }
        if(Switch==true)
        {
            for(int i=0;i<7;i++)
            {
                tileMap.setTiles(i,155,11);
            }
        }

    }


    //Metode de draw
    private void drawForest(Graphics2D g)
    {
        for(int i=0;i<forest.size();i++)
        {
            forest.get(i).setMapPosition((int)tileMap.getx(),(int)tileMap.gety());
            forest.get(i).draw(g);
        }
    }
    private void drawEnemies(Graphics2D g)
    {
        for(int i=0;i<enemies.size();i++)
        {
            enemies.get(i).draw(g);
        }
        //draw explotions
        for(int i=0;i<explosions.size();i++)
        {
            explosions.get(i).setMapPosition((int)tileMap.getx(), (int)tileMap.gety());
            explosions.get(i).draw(g);
        }
    }
    private void drawProjectiles(Graphics2D g)
    {
        for(int i=0;i<Projectile.projectiles.size();i++)
        {
            Projectile.projectiles.get(i).draw(g);
        }

    }
    public void draw(Graphics2D g) {

        bg.draw(g);
        tileMap.draw(g);
        drawForest(g);
        player.draw(g);
        drawEnemies(g);
        hud.draw(g);
        g.drawString("Score:"+ player.Score,230,12);
        drawProjectiles(g);
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
