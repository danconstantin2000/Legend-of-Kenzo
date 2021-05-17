package GameState;
import Audio.AudioPlayer;
import Main.GamePanel;
import TileMap.Background;
import java.awt.*;

public class EndState extends GameState{

    private Background bg;
    private Background bg2;
    private Background bg3;
    private Font titleFont;
    private Color titleColor;
    private Font font;
    private int contor;
    private boolean exit;
    private AudioPlayer end;

    public EndState(GameStateManager gsm) {
        this.gsm=gsm;
        try{
            init();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    public void init() {
        bg=new Background("/Backgrounds/IMAGE1.png");
        bg2=new Background("/Backgrounds/IMAGE2.png");
        bg3=new Background("/Backgrounds/IMAGE3.png");
        titleColor=new Color(255,255,255);
        titleFont=new Font("Courier New",Font.BOLD,18);
        font=new Font("Courier New",Font.PLAIN,12);
        exit=false;
        contor=0;
        end=new AudioPlayer("/Music/Ending.mp3");
        end.play();

    }
    public void update(){}
    private void drawDialogue(Graphics2D g) {
        if(exit==false)
        {
            if(contor<800)
            {
                bg.draw(g);
                g.setFont(titleFont);
                g.drawString("Kenzo",125,140);
                g.setFont(new Font("Courier New",Font.PLAIN,10));
                g.drawString("     He became the best fighter in his land!",0,160);
                g.drawString("  In addition to teaching people the martial arts ",0,180);
                g.drawString("  techniques,he continued to fight for peace",0,200);
                g.drawString("  and to defeat any evil existing in Aokigahara!",0,220);


            }
            else if(contor>800 && contor<1600)
            {
                bg2.draw(g);
                g.setFont(titleFont);
                g.drawString("Ryzen",125,140);
                g.setFont(new Font("Courier New",Font.PLAIN,10));
                g.drawString("     Ryzen became the leader of the Aokigahara land. ",0,160);
                g.drawString(" He organizes fighting contests to prepare a large",0,180);
                g.drawString(" part of the population for self-defense techniques.",0,200);



            }
            else if(contor>1600 && contor<2400)
            {
                bg3.draw(g);
                g.setFont(titleFont);
                g.drawString("Aokigahara",120,145);
                g.setFont(new Font("Courier New",Font.PLAIN,10));
                g.drawString("       Aokigahara has become one of the most famous",0,160);
                g.drawString(" lands in the world.People from all over the world",0,180);
                g.drawString(" come there every year to show their talent in",0,200);
                g.drawString(" martial arts.",0,220);

            }
            else if(contor>2400 && contor<3000){
                g.setFont(titleFont);
                g.clearRect(0,0,320,240);
                g.drawString("Thanks for playing!",60,130);
                if(contor>2870)
                {
                    if(!end.hasStopped())
                        end.stop();
                }
            }
            else if(contor>3000)
            {
                contor=0;
                exit=true;
                GamePanel.inGameFocus=true;
                gsm.setState(GameStateManager.MENUSTATE);
            }
        }

    }
    public void draw(Graphics2D g) {
        g.setColor(titleColor);
        contor++;
        drawDialogue(g);
    }
    public void keyPressed(int k) { }
    public void keyReleased(int k) { }

}
