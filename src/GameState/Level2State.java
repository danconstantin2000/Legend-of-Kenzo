package GameState;

import Audio.AudioPlayer;
import Entity.*;
import Entity.Enemies.*;
import Main.GamePanel;
import TileMap.TileMap;


import TileMap.Background;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Math.abs;

public class Level2State extends GameState{


    private TileMap tileMap;
    private Background bg;
    private Player player;
    private ArrayList<Enemy> enemies;
    private ArrayList<Explosion>explosions;
    private ArrayList<KenzoHead>kenzoHeads;
    private HUD hud;
    private boolean BossIsDead;
    private boolean Switch;
    private AudioPlayer KHAudio;
    private HashMap<String, AudioPlayer> MusicBg;
    private Ryzen ryzen;
    private long count;
    private HashMap<String,Enemy>BossEnemies;
    private boolean exit;
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
        BossEnemies=new HashMap<String,Enemy>();
        this.bg=new Background("/Backgrounds/LV2_BG2.png");
        MusicBg=new HashMap<String,AudioPlayer>();

        player=new Player(tileMap);
        player.setPosition(2500,100);
        hud=new HUD(player);
        MusicBg.put("Boss",new AudioPlayer("/Music/Boss2.mp3"));
        MusicBg.put("bg",new AudioPlayer("/Music/level2.mp3"));
        MusicBg.get("bg").play();
        populateEnemies();
        explosions=new ArrayList<Explosion>();
       Projectile.projectiles=new ArrayList<Projectile>();
        BossIsDead=false;
        Switch=false;
        kenzoHeads=new ArrayList<KenzoHead>();
        kenzoHeads.add(new KenzoHead(tileMap));
        kenzoHeads.get(0).setPosition(2700,10);
        KHAudio=new AudioPlayer("/SFX/Pickup 4.wav");
        ryzen=new Ryzen(tileMap);
        ryzen.setPosition(3150,80);
        count=0;
        exit=false;

    }

    private void populateEnemies() {
        enemies = new ArrayList<Enemy>();
        Point[] myPointArray = new Point[]{

              new Point(700, 10),
                new Point(1200, 10),
                new Point(2150, 10),

        };
        Point[] myPointArray2 = new Point[]{
                new Point(1400, 30),
                new Point(2000, 30),
                new Point(2630, 10),


        };
        Point[] myPointArray3 = new Point[]{

                new Point(500, 30),
                new Point(940, 30),
                new Point(1650, 10),
                new Point(2400, 10),
        };
        Point[] myPointArray4 = new Point[]{

                new Point(298,80),
                new Point(1070,80),
                new Point(2280,100),
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
        for(int i=0;i<myPointArray3.length;i++)
        {
            Skeleton s=new Skeleton(tileMap,player);
            s.setPosition(myPointArray3[i].x,myPointArray3[i].y);
            enemies.add(s);
        }
        for(int i=0;i<myPointArray4.length;i++)
        {
            FlyingEye f=new FlyingEye(tileMap,player);
            f.setPosition(myPointArray4[i].x,myPointArray4[i].y);
            enemies.add(f);
        }
        FireMagician FM=new FireMagician(tileMap,player);
        FM.setPosition(3000,20);
        enemies.add(FM);
        BossEnemies.put("Boss",FM);


    }
    private void updateAllEnemies()
    {

        for(int i=0;i<enemies.size();i++)
        {
            Enemy e=enemies.get(i);
            e.update();
            if(e.isDead())
            {
                if(e instanceof FireMagician) {
                    if (((FireMagician) e).hasPlayed()) {

                        player.Score = player.Score + e.getScore();
                        enemies.remove(i);
                        i--;
                        explosions.add(new Explosion(e.getx(), e.gety()));
                        BossIsDead = true;
                        if(MusicBg.containsKey("Boss"))
                            MusicBg.get("Boss").stop();
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
            if(MusicBg.containsKey("bg") && MusicBg.containsKey("Boss")) {
                MusicBg.get("bg").stop();
                MusicBg.get("Boss").stop();
                MusicBg.remove("bg");
                MusicBg.remove("Boss");
            }
                if (player.getSwitchState() || player.getVoid())
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
    private void musicThings()
    {
        if(MusicBg.containsKey("bg") && MusicBg.containsKey("Boss")) {
            if (player.getx() == 2700) {
                MusicBg.get("bg").stop();
                if (!MusicBg.get("Boss").hasStopped()) {
                    MusicBg.get("bg").stop();
                } else {
                    MusicBg.get("Boss").play();
                }

            }
            if (player.getx() < 2700 && MusicBg.get("bg").hasStopped() && MusicBg.get("Boss").hasStopped() && !player.isDead()) {
                MusicBg.get("bg").play();
            }
            if (player.getx() > 2700 && MusicBg.get("Boss").hasStopped() && !BossIsDead) {
                MusicBg.get("bg").stop();
                MusicBg.get("Boss").play();
            }
        }

    }

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

        if(!kenzoHeads.isEmpty())
            if(player.intersects(kenzoHeads.get(0)))
            {
                KHAudio.play();
                player.setHealth(5);
                kenzoHeads.get(0).setRemove(true);
            }

        for(int i=0;i<kenzoHeads.size();i++)
        {
            kenzoHeads.get(i).update();
            if(kenzoHeads.get(i).shouldRemove())
            {
                kenzoHeads.remove(i);
                i--;
            }
        }
        ryzen.update();
       if(BossIsDead==true)
       {
            player.setFacingRight(true);

           for(int i=0;i<8;i++)
           {
               tileMap.setTiles(i,198,0);
               tileMap.setTiles(i,193,0);
           }
           for(int j=198;j>193;j--)
           {
               tileMap.setTiles(1,j,0);
           }

           if(abs(player.getx()-ryzen.getx())>50 )
           {
                 ryzen.setLeft(true);

           }
           else
           {
               ryzen.setLeft(false);

           }

       }



    }


    //Metode de draw
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
        player.draw(g);
        drawEnemies(g);
        hud.draw(g);
        g.drawString("Score:"+ player.Score,230,12);
        drawProjectiles(g);

        for(int i=0;i<kenzoHeads.size();i++)
        {
            kenzoHeads.get(i).draw(g);


        }

        ryzen.draw(g);
        if(abs(player.getx()-BossEnemies.get("Boss").getx())<110 && exit==false)
        {
            g.setColor(Color.black);
            g.fillRect(0,170,320,100);
            g.fillRect(0,0,320,50);
            g.setColor(Color.white);
            g.setFont(new Font("Courier New",Font.PLAIN,10));
            GamePanel.inGameFocus=false;
            player.setLeft(false);
            player.setRight(false);
            count++;
            if (count < 200) {
                g.drawString("Kenzo:Dark Magician is Dead!", 30, 200);
                g.drawString("You are next!", 30, 220);

            } else if (count > 200 && count < 600) {
                g.drawString("Fire Magician:I will kill your brother", 30, 200);
                g.drawString("after I kill you.", 30, 220);

            } else if (count > 600 && count < 1000) {
                g.drawString("Kenzo:Your time is over. ", 30, 180);
                g.drawString("Good always defeats evil.", 30, 200);
                g.drawString("The evil will disappear from this land!", 30, 220);

            } else if (count > 1000 && count < 1400) {
                g.drawString("Fire Magician:Let's see!", 30, 200);

            }
            else if(count>1400)
            {
                GamePanel.inGameFocus=true;
                exit=true;
                count=0;
            }
        }


        if(BossIsDead==true)
        {
            g.setColor(Color.black);
            g.fillRect(0,170,320,100);
            g.fillRect(0,0,320,50);
            GamePanel.inGameFocus=false;
            player.setRight(false);
            player.setLeft(false);
            g.setColor(Color.white);
            g.setFont(new Font("Courier New",Font.PLAIN,10));
            if(abs(player.getx()-ryzen.getx())<110 ) {
                count++;
                if (count < 200) {
                    g.drawString("Kenzo:I'm so happy to see you again!", 30, 200);
                } else if (count > 200 && count < 600) {
                    g.drawString("Ryzen:Me too brother!", 30, 180);
                    g.drawString("Finnaly we defeated the two evil wizards.", 30, 200);
                    g.drawString("Now the land of Aokigahara is safe!", 30, 220);
                } else if (count > 600 && count < 1000) {
                    g.drawString("Kenzo:That's right!", 30, 180);
                    g.drawString("Let's get out of here and teach the world", 30, 200);
                    g.drawString("how to defend themselves from the evil.", 30, 220);
                } else if (count > 1000 && count < 1400) {
                    g.drawString("Ryzen:Let's go!", 30, 200);

                }
                else if(count>1400)
                {

                }
            }

        }


    }


    public void keyPressed(int k) {
        if(player.getHealth()!=0 ) {
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
