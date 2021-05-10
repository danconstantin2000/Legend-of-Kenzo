package GameState;

import Main.GamePanel;
import TileMap.Background;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.FileReader;

public class HelpState extends GameState{

    private Background bg;
    private Font titleFont;
    private Color titleColor;
    private Font font;
    private int currentChoice=0;//Alegere curenta
    private String[] options={"Back"};//Vector de optiuni
    public HelpState(GameStateManager gsm)
    {
        this.gsm=gsm;
        try{
            //Initializari
            init();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    public void init()
    {
        bg=new Background("/Backgrounds/BG4.png");
        titleColor=new Color(0,0,0);
        titleFont=new Font("Courier New",Font.BOLD,18);
        font=new Font("Courier New",Font.PLAIN,12);
    }
    public void update(){}
    public void draw(Graphics2D g)
    {
        bg.draw(g);
        g.setColor(titleColor);
        g.setFont(titleFont);
        g.drawString("Help State!",100,30);
        g.setFont(font);
        g.drawString("The legend of Kenzo is a 2D platformer",25,60);
        g.drawString("game,similar to the NES games of the 90s,",10,75);
        g.drawString("and your goal is to defeat the two evil ",10,90);
        g.drawString("magicians and save Kenzo's brother.",10,105);
        g.drawString("Controls:",10,120);
        g.drawString("Right Arrow = Move right;",10,135);
        g.drawString("Left Arrow = Move left;",10,150);
        g.drawString("W = Jump;",10,165);
        g.drawString("R = Attack 1;",10,180);
        g.drawString("F = Attack 2;",10,195);

        for(int i=0;i<options.length;i++)
        {


                if (i == currentChoice) {
                    g.setColor(Color.blue);
                } else {
                    g.setColor(Color.BLACK);
                }
                //Draw options
                g.drawString(options[i], 145, 200 + i * 15);



        }


    }
    private void select()
    {
        //In functie de optiune,putem face trecerea la un nou state etc;
        if(currentChoice==0)
        {
            gsm.setState(GameStateManager.MENUSTATE);

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
