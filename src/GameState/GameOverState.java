package GameState;


import TileMap.Background;

import java.awt.*;
import java.awt.event.KeyEvent;
//Similar menuState
public class GameOverState extends GameState{

    private Background bg;
    private int currentChoice=0;
    private String[] options={"YES","NO"};
    private Font font;
    private Color titleColor;
    private Font titleFont;
    public GameOverState(GameStateManager gsm)
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
        bg=new Background("/Backgrounds/GOBG.png");
        titleColor=new Color(0,0,0);
        titleFont=new Font("Courier New",Font.BOLD,18);
        font=new Font("Courier New",Font.PLAIN,12);
    }
    public void update(){

    }
    public void draw(Graphics2D g){
        //draw bg
        bg.draw(g);
        //draw title
        g.setColor(titleColor);
        g.setFont(titleFont);
        g.drawString("GAME OVER",110,70);
        g.drawString("TRY AGAIN?",110,90);

        //draw menu options
        g.setFont(font);
        for(int i=0;i<options.length;i++)
        {
            if(i==currentChoice)
            {
                g.setColor(Color.blue);
            }
            else
            {
                g.setColor(Color.BLACK);
            }

            if(i==1)
            {
                g.drawString(options[i],152,140+i*15);

            }
            else {
                g.drawString(options[i], 150, 140 + i * 15);
            }

        }
    }
    private void select()
    {
        if(currentChoice==0)
        {
            gsm.setState(GameStateManager.MENUSTATE);
        }
        if(currentChoice==1)
        {
            System.exit(0);
        }

    }
    public void keyPressed(int k) {
        if(k== KeyEvent.VK_ENTER)
        {
            select();
        }
        if(k==KeyEvent.VK_UP){
            currentChoice--;
            if(currentChoice==-1)
            {
                currentChoice=options.length-1;
            }
        }
        if(k==KeyEvent.VK_DOWN)
        {
            currentChoice++;
            if(currentChoice==options.length){
                currentChoice=0;
            }

        }
    }
    public void keyReleased(int k) { }
}
