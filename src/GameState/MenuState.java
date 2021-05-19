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

public class MenuState extends GameState{
    private Background bg;
    private int currentChoice=0;
    private String[] options={"Start","Help","Load","Quit"};
    private Color titleColor;
    private Font titleFont;
    private Font font;
    private boolean exists;
    private String level="";
    public MenuState(GameStateManager gsm) {
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
        font=new Font("Courier New",Font.PLAIN,12);

        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:data.db");
            c.setAutoCommit(false);
            stmt = c.createStatement();
            c.commit();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM PLAYERINFO WHERE ID='Player';" );
            level=rs.getString("LevelType");
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Operation done successfully");
         if(level.equals("-"))
         {

             exists=false;
         }
         else
         {
             exists=true;
         }
    }
    public void update(){ }
    public void draw(Graphics2D g){

        bg.draw(g);
        g.setColor(titleColor);
        g.setFont(titleFont);
        g.drawString("The legend of Kenzo",70,70);
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
                    g.drawString(options[i], 145, 140 + i * 15);
                }

            }

    }
    private void select() {

        if(currentChoice==0)
        {
            Player.ResetScore();
            gsm.setState(GameStateManager.LOADINGSTATE);
        }
        if(currentChoice==1)
        {
            gsm.setState(GameStateManager.HELPSTATE);
        }
        if(currentChoice==2)
        {
            if(exists) {
                Connection c = null;
                Statement stmt = null;
                String level="";
                try {
                    Class.forName("org.sqlite.JDBC");
                    c = DriverManager.getConnection("jdbc:sqlite:data.db");
                    c.setAutoCommit(false);
                    stmt = c.createStatement();
                    c.commit();
                    ResultSet rs = stmt.executeQuery( "SELECT * FROM PLAYERINFO WHERE ID='Player';" );
                    level=rs.getString("LevelType");
                    rs.close();
                    stmt.close();
                    c.close();
                } catch ( Exception e ) {
                    System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                    System.exit(0);
                }
                System.out.println("Operation done successfully");
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
