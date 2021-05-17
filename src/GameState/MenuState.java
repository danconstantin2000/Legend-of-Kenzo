package GameState;
import Entity.Player;
import Main.Game;
import Main.GamePanel;
import TileMap.Background;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileReader;

public class MenuState extends GameState{
    private Background bg;//Imagine de Background a meniului.
    private int currentChoice=0;//Alegere curenta
    private String[] options={"Start","Help","Load","Quit"};//Vector de optiuni
    private Color titleColor;//Culoare titlu
    private Font titleFont;//Font titlu
    private Font font;//Font optiuni
    private boolean exists;
    private File myfile;
    public MenuState(GameStateManager gsm)
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

    public void init(){
        bg=new Background("/Backgrounds/MENUBG.png");
        titleColor=new Color(0,0,0);
        titleFont=new Font("Courier New",Font.BOLD,18);
        font=new Font("Courier New",Font.PLAIN,12);
         myfile=new File("save.json");
         if(myfile.exists())
         {
             exists=true;
         }
         else
         {
             exists=false;
         }

    }
    public void update(){
    }
    public void draw(Graphics2D g){
        //draw bg
        bg.draw(g);
        //draw title
        g.setColor(titleColor);
        g.setFont(titleFont);
        g.drawString("The legend of Kenzo",70,70);

        //optiune selectata-->albastra
        //optiune neselectata-->neagra

        g.setFont(font);
        for(int i=0;i<options.length;i++)
        {
            if(i==2)
            {

                if(exists==true)
                {

                    if (i == currentChoice) {
                        g.setColor(Color.blue);
                    } else {
                        g.setColor(Color.BLACK);
                    }
                    g.drawString(options[i], 145, 140 + i * 15);

                }
                else if(exists==false)
                {

                    if (i == currentChoice) {
                        g.setColor(Color.GRAY);
                    } else {
                        g.setColor(Color.GRAY);
                    }
                    g.drawString(options[i],145,140+i*15);
                }

            }
            else {

                    if (i == currentChoice) {
                        g.setColor(Color.blue);
                    } else {
                        g.setColor(Color.BLACK);
                    }
                    //Draw options
                    g.drawString(options[i], 145, 140 + i * 15);
                }


            }

    }
    private void select()
    {
        //In functie de optiune,putem face trecerea la un nou state etc;
        if(currentChoice==0)
        {
            //LoadingState-->lanseaza in executiel level1 state.
            Player.ResetScore();
            gsm.setState(GameStateManager.LOADINGSTATE);
        }
        if(currentChoice==1)
        {

            gsm.setState(GameStateManager.HELPSTATE);

        }
        if(currentChoice==2)
        {
            if(myfile.exists()) {

                String level = "";
                JSONParser parser = new JSONParser();
                try {
                    Object obj = parser.parse(new FileReader("save.json"));
                    JSONObject jsonObject = (JSONObject) obj;
                    String LevelSTR = jsonObject.get("LevelType").toString();
                    level = LevelSTR;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                GamePanel.LoadState = true;
                if (level.equals("Level1State")) {
                    gsm.setState(GameStateManager.LOADINGSTATE);
                } else if (level.equals("Level2State")) {
                    gsm.setState(GameStateManager.LOADINGSTATE2);
                }
            }
        }
        if(currentChoice==3)
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
