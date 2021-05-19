package Main;
import javax.swing.JFrame;
import java.io.File;
import java.sql.*;
public class Game
{
    public static void main(String[] args)
    {
        Connection c = null;
        Statement stmt = null;
        if (!new File("data.db").exists()) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:data.db");
                stmt = c.createStatement();
                String sql = "CREATE TABLE PLAYERINFO " +
                        "(ID CHAR(50)," +
                        "x REAL NOT NULL, " +
                        "y REAL NOT NULL, " +
                        "LevelType CHAR(50), " +
                        "Health INT NOT NULL,"+
                        "Score INT NOT NULL,"+
                        "Energy INT NOT NULL)";
                stmt.executeUpdate(sql);
                stmt.close();
                c.close();
            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:data.db");
                c.setAutoCommit(false);
                stmt = c.createStatement();
                String sql = "INSERT INTO PLAYERINFO(ID,x,y,LevelType,Health,Score,Energy) " +
                        "VALUES ('Player',0,0,'-',0,0,0)";
                stmt.executeUpdate(sql);
                stmt.close();
                c.commit();
                c.close();
            } catch ( Exception e ) {
                System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                System.exit(0);
            }
            System.out.println("Table created successfully");
            System.out.println("Record created successfully");

        }
        JFrame window=new JFrame("The legend of Kenzo!");
        window.setContentPane(GamePanel.getInstance());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.pack();
        window.setVisible(true);
    }
}
