package GameState;

import Entity.Player;
import Main.GamePanel;
import TileMap.TileMap;
import TileMap.Tile;
import java.awt.*;
import java.awt.event.KeyEvent;

import TileMap.Background;
public class Level1State extends GameState{

    private TileMap tileMap;
    private Background bg;
    private Player player;
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
       this.bg=new Background("/Backgrounds/BG5.png",0.1);
       player=new Player(tileMap);
       player.setPosition(100,30);
    }
    public void update() {
    //update palyer
        player.update();
       tileMap.setPosition(GamePanel.WIDTH/2-player.getx(),GamePanel.HEIGHT/2-player.gety());
    }


    public void draw(Graphics2D g) {
        //clear screen;
        bg.draw(g);
        tileMap.draw(g);
        player.draw(g);

    }


    public void keyPressed(int k) {
        if(k == KeyEvent.VK_LEFT) player.setLeft(true);
        if(k == KeyEvent.VK_RIGHT) player.setRight(true);
        if(k == KeyEvent.VK_UP) player.setUp(true);
        if(k == KeyEvent.VK_DOWN) player.setDown(true);
        if(k == KeyEvent.VK_W) player.setJumping(true);
        if(k == KeyEvent.VK_E) player.setGliding(true);
        if(k == KeyEvent.VK_R) player.setScratching();
        if(k == KeyEvent.VK_F) player.setFiring();
    }

    public void keyReleased(int k) {
        if(k == KeyEvent.VK_LEFT) player.setLeft(false);
        if(k == KeyEvent.VK_RIGHT) player.setRight(false);
        if(k == KeyEvent.VK_UP) player.setUp(false);
        if(k == KeyEvent.VK_DOWN) player.setDown(false);
        if(k == KeyEvent.VK_W) player.setJumping(false);
        if(k == KeyEvent.VK_E) player.setGliding(false);
    }
}
