package Main;
import javax.swing.JFrame;

//Object<--Component<--Container<--Window<--Frame<--JFrame.

public class Game
{
    public static void main(String[] args)
    {
        //Instanta fereastra JFrame <--java.swing.
        JFrame window=new JFrame("The legend of Kenzo!");


        /*
        public void setContentPane(Container contentPane);
        Instanta GamePanel*/
        window.setContentPane(new GamePanel());


        /*
            public void setDefaultCloseOperation(int operation);
            Operation:DO_NOTHING_ON_CLOSE,HIDE_ON_CLOSE,DISPOSE_ON_CLOSE,EXIT_ON_CLOSE
            EXIT_ON_CLOSE->La inchidere apeleaza System.exit();

         */
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Fereastra nu-si poate modifica dimensiunile.
        window.setResizable(false);


        //Fereastra sa fie de dimensiunea setPreferredSize<---GamePanel.
        window.pack();

        //Fereastra vizibila.
        window.setVisible(true);
    }
}
