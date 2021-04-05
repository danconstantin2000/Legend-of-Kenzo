package GameState;

import TileMap.Background;

import java.awt.*;
import java.awt.event.KeyEvent;

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
            bg=new Background("/Backgrounds/MENUBG.png",1);
            bg.setVector(-0.1,0);
            titleColor=new Color(0,0,0);
            titleFont=new Font("Courier New",Font.BOLD,18);
            timer=System.nanoTime();


        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    public void init(){}
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


        //draw menu options


    }


    public void keyPressed(int k) {

    }

    public void keyReleased(int k) { }

}
