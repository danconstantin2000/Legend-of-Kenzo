package GameState;
import Entity.Player;
import Main.GamePanel;
import TileMap.Background;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class SettingsState extends GameState{
    private Background bg;
    private int currentChoice=0;
    private int currentChoice2=0;
    private String[] options={"1","2","3","4","5","Back"};
    private String[] options2={"Quit","Not now"};
    private Color titleColor;
    private Font font;
    private boolean selected;
    public SettingsState(GameStateManager gsm) {
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
        font=new Font("Courier New",Font.PLAIN,15);
        selected=false;

    }
    public void update(){ }
    public void draw(Graphics2D g){

        bg.draw(g);
        g.setColor(titleColor);
        g.setFont(font);
        g.drawString("SCALE:",20,50);
        for(int i=0;i<options.length-1;i++)
        {
            if (i == currentChoice) {
                g.setColor(Color.blue);
            } else {
                g.setColor(Color.BLACK);
            }
            g.drawString(options[i], 100+i*20, 50);
        }
        if(currentChoice==5)
        {
            g.setColor(Color.blue);
        } else {
            g.setColor(Color.BLACK);
        }
        g.drawString(options[5],140,210);
        if(selected==true)
        {
            g.setFont(new Font("Arial",Font.PLAIN,10));
            g.clearRect(30,80,270,100);
            g.drawRect(30,80,270,100);
            g.setColor(new Color(255,255,255));
            g.drawString("If you want to apply,you need to restart the game!",50,130);
            for(int i=0;i<options2.length;i++)
            {
                if(i==currentChoice2) {
                    g.setColor(Color.BLUE);
                }
                else {
                    g.setColor(Color.white) ;
                }
                g.drawString(options2[i],130+i*30,150);
            }
        }


    }
    private void select() {

        selected=true;
        Connection c = null;
        Statement stmt = null;
        String sql;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:data.db");
            c.setAutoCommit(false);
            stmt = c.createStatement();
            if(currentChoice!=5) {
                 sql = "UPDATE SETTINGS set Value=" + options[currentChoice] + " where ID='Scale';";
                 stmt.executeUpdate(sql);
            }
            else if(currentChoice==5)
            {
                selected=false;
                gsm.setState(GameStateManager.MENUSTATE);
            }
            stmt.close();
            c.commit();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Records updated successfully");

    }
    private void select2()
    {
        if(currentChoice2==0)
        {
            System.exit(0);
        }
        else if(currentChoice2==1)
        {
           selected=false;
        }
    }

    public void keyPressed(int k) {
        if (selected == false) {
            if (k == KeyEvent.VK_ENTER) {
                select();
            }

            if (currentChoice != 5) {
                if (k == KeyEvent.VK_LEFT) {
                    currentChoice--;
                    if (currentChoice == -1) {
                        currentChoice = options.length - 2;
                    }
                }
                if (k == KeyEvent.VK_RIGHT) {
                    currentChoice++;
                    if (currentChoice == options.length - 1) {
                        currentChoice = 0;
                    }

                }
            }
            if (k == KeyEvent.VK_UP && currentChoice == 5) {
                    currentChoice = 0;
                }
            else if (k == KeyEvent.VK_UP && currentChoice != 5) {
                    currentChoice = 5;
                }
            if (k == KeyEvent.VK_DOWN && currentChoice == 5) {
                    currentChoice = 0;
                }
            else if (k == KeyEvent.VK_DOWN && currentChoice != 5) {
                    currentChoice = 5;
                }
            }
            else {
                if (k == KeyEvent.VK_ENTER) {
                    select2();
                }
                if (k == KeyEvent.VK_LEFT) {
                    currentChoice2--;
                    if (currentChoice2 == -1) {
                        currentChoice2 = options2.length - 1;
                    }
                }
                if (k == KeyEvent.VK_RIGHT) {
                    currentChoice2++;
                    if (currentChoice2 == options2.length) {
                        currentChoice2 = 0;
                    }
                }
            }
    }
    public void keyReleased(int k) { }

}
