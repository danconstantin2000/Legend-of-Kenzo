package GameState;

import Main.GamePanel;
import TileMap.TileMap;
import TileMap.Tile;
import java.awt.*;
import TileMap.Background;
public class Level1State extends GameState{

    private TileMap tileMap;
    private Background bg;
    public Level1State(GameStateManager gsm)
    {
        this.gsm=gsm;
        bg=new Background("/Backgrounds/BG4.png",1);

    }


    public void init() {
        tileMap=new TileMap(16);

        tileMap.loadTiles("/Tilesets/Tileset2.png");
        tileMap.loadMap("/Maps/myMap.map");

        tileMap.setPosition(0,0);
    }
    public void update() {

    }


    public void draw(Graphics2D g) {
        //clear screen;
        g.setColor(Color.WHITE);
        g.fillRect(0,0, GamePanel.WIDTH,GamePanel.HEIGHT);
        bg.draw(g);
        tileMap.draw(g);

    }


    public void keyPressed(int k) {

    }


    public void keyReleased(int k) {

    }
}
