package GameState;

import Audio.AudioPlayer;
import Entity.*;
import Entity.Enemies.DarkMagician;
import Entity.Enemies.Goblin;
import Entity.Enemies.Mushroom;
import Entity.Enemies.Projectile;
import Forest.*;
import Main.GamePanel;
import TileMap.TileMap;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import TileMap.Background;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import static java.lang.Math.abs;
public class Level1State extends GameState{

    private TileMap tileMap;
    private Background bg;
    private Player player;
    private ArrayList<Enemy> enemies;
    private ArrayList<Explosion>explosions;
    private ArrayList<KenzoHead>kenzoHeads;
    private HUD hud;
    private ArrayList<ForestThings>forest;
    private boolean Switch;
    private AudioPlayer KHAudio;
    private boolean timeToSpawnPortal;
    private Portal level2Portal;
    private boolean stopBossMusic;
    private HashMap<String, AudioPlayer> MusicBg;
    private int count;
    private boolean exit;
    private HashMap<String,Enemy>BossEnemies;
    private boolean help;
    private boolean timeToSave;
    private boolean save;
    public Level1State(GameStateManager gsm) {
        this.gsm=gsm;
        init();
    }
    public void init() {
        tileMap=new TileMap(16);
        tileMap.loadTiles("/Tilesets/tileset.png");
        tileMap.loadMap("/Maps/Level1.map");
        tileMap.setPosition(0,0);
        this.bg=new Background("/Backgrounds/BG9.png");
        MusicBg=new HashMap<String,AudioPlayer>();
        player=new Player(tileMap);
        loadfromJsonFile();
        BossEnemies=new HashMap<String,Enemy>();
        populatetrees();
        populateEnemies();
        hud=new HUD(player);
        explosions=new ArrayList<Explosion>();
        MusicBg.put("Boss",new AudioPlayer("/Music/Boss.mp3"));
        MusicBg.put("bg",new AudioPlayer("/Music/08-China-Great-Wall.mp3"));
        MusicBg.get("bg").play();
        Projectile.projectiles=new ArrayList<Projectile>();
        Switch=false;
        kenzoHeads=new ArrayList<KenzoHead>();
        kenzoHeads.add(new KenzoHead(tileMap));
        kenzoHeads.get(0).setPosition(2700,10);
        KHAudio=new AudioPlayer("/SFX/Pickup 4.wav");
        timeToSpawnPortal=false;
        stopBossMusic=false;
        count=0;
        exit=false;
        help=false;
        timeToSave=false;
        GamePanel.LoadState=false;
    }
    private void loadfromJsonFile() {
        if(GamePanel.LoadState==true) {
            JSONParser parser =new JSONParser();
            try
            {
                Object obj=parser.parse(new FileReader("save.json"));
                JSONObject jsonObject=(JSONObject) obj;
                String healthSTR=jsonObject.get("Health").toString();
                String scoreSTR=jsonObject.get("Score").toString();
                String pxSTR=jsonObject.get("PlayerX").toString();
                String pySTR=jsonObject.get("PlayerY").toString();
                String energySTR=jsonObject.get("PlayerEnergy").toString();
                int health=Integer.parseInt(healthSTR);
                int score=Integer.parseInt(scoreSTR);
                int x=Integer.parseInt(pxSTR);
                int y=Integer.parseInt(pySTR);
                int energy=Integer.parseInt(energySTR);
                player.setPosition(x,y);
                player.setHealth(health);
                player.setSCORE(score);
                player.setEnergy(energy);
                save=true;
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        else {
            player.setPosition(100, 100);
            save=false;
        }
    }
    private void populatetrees() {
        forest=new ArrayList<ForestThings>();
        Point[] myPointArrayTrees=new Point[]{
                new Point(100,100),
                 new Point(30,100),
                new Point(170,100),
                new Point(425,68),
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
                new Point(1440, 52),
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
                new Point(295,42),
                new Point(630,10),
                new Point(770,10),
                new Point(1050,27),
                new Point(1188,27),
                new Point(1700,10),
                new Point(1910,10),
                new Point(2620,27),
                new Point(2830,27),

        };
        for(int i=0;i<myPointArrayBush.length;i++)
        {
            Bush b=new Bush(myPointArrayBush[i].x,myPointArrayBush[i].y);
            forest.add(b);
        }
        Point[] myPointArrayRuin=new Point[]{
                        new Point(980,27),
                        new Point(1835,10),
                        new Point(2550,27)
        };
        for(int i=0;i<myPointArrayRuin.length;i++)
        {
            Ruin r=new Ruin(myPointArrayRuin[i].x,myPointArrayRuin[i].y);
            forest.add(r);
        }

    }
    private void populateEnemies() {

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
            if(GamePanel.LoadState==true)
            {
                if(player.getx()<myPointArray[i].x)
                {
                    Mushroom m=new Mushroom(tileMap,player);
                    m.setPosition(myPointArray[i].x,myPointArray[i].y);
                    enemies.add(m);
                }
            }
            else {
                Mushroom m = new Mushroom(tileMap, player);
                m.setPosition(myPointArray[i].x, myPointArray[i].y);
                enemies.add(m);
            }
        }
        for(int i=0;i<myPointArray2.length;i++)
        {
            if(GamePanel.LoadState==true)
            {
                if(player.getx()<myPointArray2[i].x)
                {
                    Goblin g=new Goblin(tileMap,player);
                    g.setPosition(myPointArray2[i].x,myPointArray2[i].y);
                    enemies.add(g);

                }
            }
            else {
                Goblin g = new Goblin(tileMap, player);
                g.setPosition(myPointArray2[i].x, myPointArray2[i].y);
                enemies.add(g);
            }
        }
        DarkMagician DM=new DarkMagician(tileMap,player);
        DM.setPosition(3000,20);
        BossEnemies.put("Boss",DM);
        enemies.add(DM);

    }
    private void musicThings() {
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
            if (player.getx() > 2700 && MusicBg.get("Boss").hasStopped() && !stopBossMusic) {
                MusicBg.get("bg").stop();
                if(MusicBg.containsKey("Boss"))
                     MusicBg.get("Boss").play();
            }
        }

    }
    private void updateAllEnemies() {
        for(int i=0;i<enemies.size();i++)
        {
            Enemy e=enemies.get(i);
            e.update();
            if(e.isDead())
            {
                if(e instanceof DarkMagician)
                {
                    if(((DarkMagician) e).hasPlayed())
                    {
                        timeToSpawnPortal=true;
                        player.setScore(player.getScore()+ e.getScore());
                        enemies.remove(i);
                        i--;
                        explosions.add(new Explosion(e.getx(), e.gety()));
                        stopBossMusic=true;
                        if(MusicBg.containsKey("Boss")) {
                            MusicBg.get("Boss").stop();
                            MusicBg.remove("Boss");
                        }
                    }
                }else {

                    player.setScore(player.getScore()+ e.getScore());
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
    private void GameOver() {
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
    private void playerIntersectedHead() {
        if(!kenzoHeads.isEmpty())
            if(player.intersects(kenzoHeads.get(0)))
            {
                KHAudio.play();
                player.setHealth(5);
                kenzoHeads.get(0).setRemove(true);
            }
    }
    private void BossFight() {
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
    private void updateHeads() {
        for(int i=0;i<kenzoHeads.size();i++)
        {
            kenzoHeads.get(i).update();
            if(kenzoHeads.get(i).shouldRemove())
            {
                kenzoHeads.remove(i);
                i--;
            }
        }
    }
    private void portalSpawn() {
        if(timeToSpawnPortal)
        {
            level2Portal=new Portal(tileMap);
            level2Portal.setPosition(3120,100);
            timeToSpawnPortal=false;
        }
        if(level2Portal!=null)
        {
            level2Portal.update();
        }
        if(level2Portal!=null)
            if(player.intersects(level2Portal))
            {
                gsm.setState(GameStateManager.LOADINGSTATE2);
            }

    }
    private void TimeToSave() {
        if(timeToSave==true) {


            JSONObject obj=new JSONObject();
            obj.put("Health",player.getHealth());
            obj.put("Score",player.getScore());
            obj.put("PlayerX",player.getx());
            obj.put("PlayerY",player.gety());
            obj.put("PlayerEnergy",player.getEnergy());
            obj.put("LevelType","Level1State");


            try(FileWriter file =new FileWriter("save.json"))
            {

                file.write(obj.toString());
                file.flush();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

            timeToSave=false;
        }


    }
    public void update() {
        player.update();
        tileMap.setPosition(GamePanel.WIDTH/2-player.getx(),GamePanel.HEIGHT/2-player.gety());
        player.checkAttack(enemies);
        player.checkAttack2(Projectile.projectiles);
        playerIntersectedHead();
        updateAllEnemies();
        musicThings();
        GameOver();
        updateAllProjectiles();
        BossFight();
        updateHeads();
        portalSpawn();
        TimeToSave();
    }

    private void drawForest(Graphics2D g) {

        for(int i=0;i<forest.size();i++)
        {
            forest.get(i).setMapPosition((int)tileMap.getx(),(int)tileMap.gety());
            forest.get(i).draw(g);
        }
    }
    private void drawEnemies(Graphics2D g) {
        for(int i=0;i<enemies.size();i++)
        {
            enemies.get(i).draw(g);
        }
        for(int i=0;i<explosions.size();i++)
        {
            explosions.get(i).setMapPosition((int)tileMap.getx(), (int)tileMap.gety());
            explosions.get(i).draw(g);
        }
    }
    private void drawProjectiles(Graphics2D g) {
        for(int i=0;i<Projectile.projectiles.size();i++)
        {
            Projectile.projectiles.get(i).draw(g);
        }

    }
    private void drawTutorial(Graphics2D g) {
        if(player.getx()>110 && help==false&& player.getx()<200)
        {
            count++;
            g.setColor(Color.black);
            g.setFont(new Font("Courier New",Font.PLAIN,10));
            g.fillRect(0,170,320,100);
            g.fillRect(0,0,320,50);
            GamePanel.inGameFocus=false;
            player.setLeft(false);
            player.setRight(false);
            g.setColor(Color.white);
            if (count < 200) {
                g.drawString("You are in Aokigahara forest!", 30, 200);
            } else if (count > 200 && count < 600) {

                g.drawString("Press right,left arrows to move!", 30, 200);
                g.drawString("Press w for jump!", 30, 220);


            } else if (count > 600 && count < 1000) {
                g.drawString("Press F for the first attack!", 30, 200);
                g.drawString("Press R for the second attack!", 30, 220);



            } else if (count > 1000 && count < 1400) {
                g.drawString("The second attack will consume power", 30, 200);
                g.drawString("and will give more damage to the enemies!", 30, 220);

            }
            else if(count>1400)
            {
                GamePanel.inGameFocus=true;
                help=true;
                count=0;
            }

        }
    }
    private void drawSaving(Graphics2D g) {
        if(player.getx()>2500 && save==false)
        {   count++;
            if(count<200) {
                g.drawString("Saving...", 250, 30);
            }
            else if(count>200)
            {
                count=0;
                save=true;
                timeToSave=true;

            }
        }
    }
    private void drawHeads(Graphics2D g) {
        for(int i=0;i<kenzoHeads.size();i++)
        {
            kenzoHeads.get(i).draw(g);

        }
        if(level2Portal!=null)
        {
            level2Portal.draw(g);
        }
    }
    private void drawBossDialogue(Graphics2D g) {
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
                g.drawString("Kenzo:Here you are!Give me back my Brother!", 30, 200);
            } else if (count > 200 && count < 600) {
                g.drawString("Dark Magician:If you want your brother,", 30, 200);
                g.drawString("you have to beat me.", 30, 220);

            } else if (count > 600 && count < 1000) {
                g.drawString("Kenzo:I have been waiting for this moment", 30, 180);
                g.drawString("for a long time.", 30, 200);
                g.drawString("I trained for many years to beat you!", 30, 220);


            } else if (count > 1000 && count < 1400) {
                g.drawString("Dark Magician:Show me what you know!", 30, 200);

            }
            else if(count>1400)
            {
                GamePanel.inGameFocus=true;
                exit=true;
                count=0;
            }
        }

    }
    public void draw(Graphics2D g) {

        bg.draw(g);
        drawForest(g);
        tileMap.draw(g);
        player.draw(g);
        drawEnemies(g);
        hud.draw(g);
        g.drawString("Score:"+ player.getScore(),230,12);
        drawProjectiles(g);
        drawTutorial(g);
        drawSaving(g);
        drawHeads(g);
        drawBossDialogue(g);
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
