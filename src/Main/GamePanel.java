package Main;
import GameState.GameStateManager;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;
/*

Object<--Component<--Container<--JComponent<--JPanel.
JPanel=Container ce poate contine mai multe componente.
GamePanel=Clasa ce mosteneste clasa JPanel si implementeaza interfetele Runnable,KeyListener.
         =Clasa principala a intregului proiect

public interface Runnable {
            public void run();
        }

public interface KeyListener
{
    public void keyTyped(KeyEvent key);
    public void keyPressed(KeyEvent key);
    public void keyReleased(KeyEvent key);

}
Aceasta clasa trebuie sa aiba implementate toate aceste metode ale interfetelor.

        //Variabile

        + static final int WIDTH;
        + static final int HEIGHT;
        + static final int SCALE;
        - Thread thread;
        - boolean running;
        - int FPS;
        - BufferedImage image;
        - Graphics2D g;
        - GameStateManager gsm;

        //Metode

        + GamePanel()
        + void addNotify()
        - init()
        + run()
        - update()
        - draw()
        - drawToScreen()
        + keyTyped(KeyEvent key)
        + keyPressed(KeyEvent key)
        + keyReleased(KeyEvent key)

 */


public class GamePanel extends JPanel implements Runnable,KeyListener{
    //Dimensiuni predefinite
    public static final int WIDTH=320;
    public static final int HEIGHT=240;
    public static  int SCALE=3;
    public static boolean inGameFocus=true;
    public static boolean LoadState=false;
    private Thread thread;//Referinta catre thread-ul jocului.(Lanseaza jocul in executie prin metoda run())
    private boolean running;//Flag pentru starea exectutiei.
    private int FPS=60;//Numar de frame-uri pe secunda;
    private long targetTime=1000/FPS;//Durata unui frame in milisecunde
    //Referinta catre imagine
    private BufferedImage image;
    //Referinta catre un context grafic
    private Graphics2D g;
    //GameStateManager
    private GameStateManager gsm;
    private static GamePanel singleGamePanelInstance;

    //Constructor
    private GamePanel()
    {

        super();
        setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));//Dimensiuni
        setFocusable(true);//Seteaza flag-ul focusable pe true pentru a putea interactiona cu tastatura/mouse/etc
        requestFocus();

    }
    public static GamePanel getInstance()
    {
        if(singleGamePanelInstance==null)
        {
            return new GamePanel();
        }
        return singleGamePanelInstance;
    }

    public void addNotify()
    {
        //Aceasta metoda este apelata automat de catre program
        super.addNotify();
        if(thread==null)
        {
            thread=new Thread(this);//Instantierea firului de executie de unde va fi apelata metoda run()
            addKeyListener(this);//Manager de tastatura pentru a primi evenimente furnizate de aceasta
            thread.start();//Lansarea firului de executie
        }
    }
    private void init()
    {
        //Initializari
        image =new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
        g=(Graphics2D) image.getGraphics();
        running=true;
        gsm=new GameStateManager();
    }
    public void run() {

        init();
        long start;
        long elapsed;
        long wait;
        //Game Loop
        while(running) {

            start = System.nanoTime();
            update();


            draw();
            drawToScreen();
            elapsed = System.nanoTime() - start;
            //targetTime=ms
            //elapsed=nanosecond/1000000-->ms
            wait = targetTime - elapsed / 1000000;
            if(wait < 0) wait = 5;
            try {
                //Suspenda firul de executie pentru un anumit timp(wait)
                Thread.sleep(wait);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void update(){
        //Update the current gameState
        gsm.update();
        setFocusable(inGameFocus);
        requestFocus();

    }
    private void draw(){
        gsm.draw(g);
    }
    private void drawToScreen()
    {
        Graphics g2=getGraphics();
        g2.drawImage(image,0,0,WIDTH*SCALE,HEIGHT*SCALE,null);
        g2.dispose();
    }

    //Cele 3 metode din interfata KeyListener
    public void keyTyped(KeyEvent key){}
    public void keyPressed(KeyEvent key){
        gsm.keyPressed(key.getKeyCode());
    }
    public void keyReleased(KeyEvent key){
        gsm.keyReleased(key.getKeyCode());
    }

}
