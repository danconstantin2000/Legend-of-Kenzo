package GameState;

import Entity.Enemies.Mushroom;
import Entity.Enemy;
import Entity.HUD;
import Entity.Player;
import Main.GamePanel;
import TileMap.TileMap;
import TileMap.Tile;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import TileMap.Background;
public class Level1State extends GameState{

    private TileMap tileMap;
    private Background bg;
    private Player player;
    private ArrayList<Enemy> enemies;
    private HUD hud;
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
       enemies=new ArrayList<Enemy>();
       Mushroom r=new Mushroom(tileMap);
       r.setPosition(400,100);
       enemies.add(r);
       hud=new HUD(player);
    }
    public void update() {
    //update palyer
        player.update();
       tileMap.setPosition(GamePanel.WIDTH/2-player.getx(),GamePanel.HEIGHT/2-player.gety());
       //update all enemies;
        for(int i=0;i<enemies.size();i++)
        {
            enemies.get(i).update();
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
