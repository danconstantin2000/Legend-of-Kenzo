package GameState;
import TileMap.Background;
import java.awt.*;
//GameState intermediar.De aici se face intrarea in Level1 State;
/*
    -Backgroud bg;
    -int currentChoice;
    -Color titleColor;
    -Font titleFont;
    -long timer;
    Metode
    +LoadingState(gsm)
    +void init()
    +void update()
    +void draw(Graphics2D g)
    +keyPressed(int k)
    +keyReleased(int k)

 */

public class LoadingState extends GameState{

    private Background bg;
    private int currentChoice=0;
    private Color titleColor;
    private Font titleFont;
    private long timer;


    public LoadingState(GameStateManager gsm)
    {
        this.gsm=gsm;
        try{
            init();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    public void init(){
        bg=new Background("/Backgrounds/MENUBG.png");
        titleColor=new Color(0,0,0);
        titleFont=new Font("Courier New",Font.BOLD,18);
        timer=System.nanoTime();
    }
    public void update(){
        bg.update();
        long elapsed=(System.nanoTime()-timer)/1000000;

        if(elapsed>1000)
        {
            gsm.setState(GameStateManager.LEVEL1STATE);
        }

    }
    public void draw(Graphics2D g){
        //draw bg
        bg.draw(g);
        //draw title
        g.setColor(titleColor);
        g.setFont(titleFont);
        g.drawString("LOADING...",120,120);

    }

    public void keyPressed(int k) { }
    public void keyReleased(int k) { }

}
