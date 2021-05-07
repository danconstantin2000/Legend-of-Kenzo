package Entity;
import Audio.AudioPlayer;
import Entity.Enemies.DarkMagician;
import Entity.Enemies.Projectile;
import Main.GamePanel;
import TileMap.*;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

//Clasa personajului principal(Kenzo)
public class Ryzen extends MapObject {

    private ArrayList<BufferedImage[]> sprites;
    private final int[] numFrames = {
            4,8
    };

    private static final int IDLE=0;
    private static final int RUNNING=1;
    //constructor
    public Ryzen(TileMap tm) {
        super(tm);
        //inaltime si latime de citire din spritesheet
        width = 200;
        height = 200;
        //inatime si latime de coliziune
        cwidth =20;
        cheight =26;
        //atributii
        moveSpeed = 0.3;
        maxSpeed = 1.6;
        stopSpeed = 0.4;
        facingRight = false;
        fallSpeed = 0.1;
        maxFallSpeed = 4.0;
        // Incarca sprite-urile
        try {

            BufferedImage spritesheet = ImageIO.read(
                    getClass().getResourceAsStream(
                            "/Sprites/Player/Ryzen.png"
                    )
            );

            sprites = new ArrayList<BufferedImage[]>();
            for(int i = 0; i < 2; i++) {

                BufferedImage[] bi =
                        new BufferedImage[numFrames[i]];

                for(int j = 0; j < numFrames[i]; j++) {

                    bi[j] = spritesheet.getSubimage(
                            j * width,
                            i * height,
                            width,
                            height);
                }
                sprites.add(bi);
            }

        }
        catch(Exception e) {
            e.printStackTrace();
        }

        animation = new Animation();
        currentAction = IDLE;
        animation.setFrames(sprites.get(IDLE));
        animation.setDelay(400);

    }







    private void setAnimations()
    {
         if(left || right) {
             if (currentAction != RUNNING) {
                 currentAction = RUNNING;
                 animation.setFrames(sprites.get(RUNNING));
                 animation.setDelay(40);

             }
         }
         else
        {
            if(currentAction != IDLE) {
                currentAction = IDLE;
                animation.setFrames(sprites.get(IDLE));
                animation.setDelay(150);

            }
        }

    }
    //Setaria directiei dreapta-stanga.
    private void setDirection()
    {

        if(right) facingRight = true;
        if(left) {
            facingRight = false;
        }
    }


    private void getNextPosition() {

        //Daca se apasa sageata stanga playerul se misca inapoi pe harta.
        if(left) {
            dx -= moveSpeed;
            //Se atinge viteza maxima treptat si aceasta nu mai poate fi depasita.
            if(dx < -maxSpeed) {
                dx = -maxSpeed;
            }
        }
        //Daca se apasa sageata dreapta ,playerul se misca in fata.
        else if(right) {
            dx += moveSpeed;
            //Se atinge viteza maxima treptat si aceasta nu mai poate fi depasita.
            if(dx > maxSpeed) {
                dx = maxSpeed;
            }
        }
        else {
            //Daca tasta nu mai este apasata,playerul nu se mai poate misca si trebuie incetinit
            if(dx > 0) {
                dx -= stopSpeed;
                if(dx < 0) {
                    dx = 0;
                }
            }
            else if(dx < 0) {
                dx += stopSpeed;
                if(dx > 0) {
                    dx = 0;
                }
            }
        }

        //In timp ce playerul ataca si nu se afla in aer,el nu se poate misca.

        //Daca playerul sare,trebuie setata si aterizarea.
        if(jumping && !falling) {
            dy = jumpStart;
            falling = true;
        }

        // Playerul aterizeaza cu viteza fallSpeed.
        if(falling) {
            dy += fallSpeed ;
            //Daca dy>0,acesta nu mai poate sari.
            if(dy > 0) jumping = false;
            //Daca playerul se afla in aer ,el aterizeaza cu viteza stopJumpSpeed.
            if(dy < 0 && !jumping) dy += stopJumpSpeed;
            //Cand atinge viteza maxFallSpeed,acesta nu o poate depasi.
            if(dy > maxFallSpeed) dy = maxFallSpeed;
        }


    }
    //Metoda de update.Se apeleaza toate metodele de mai sus
    public void update() {

        getNextPosition();
        checkTileMapCollision();
        setPosition(xtemp, ytemp);
        setAnimations();
        setDirection();
        animation.update();



    }
    //Metoda de draw.Am tratat mai multe cazuri pentru a fi desenat corect player-ul pe ecran in functie de mai multe sprite-uri.

    public void draw(Graphics2D g) {

        setMapPosition();


        if (facingRight) {


            g.drawImage(
                    animation.getImage(),
                    (int) (x + xmap - width / 2),
                    (int) (y + ymap - height / 2-15 ),
                    width
                    , height,
                    null
            );

        }
        else
        {

            g.drawImage(
                    animation.getImage(),
                    (int) (x + xmap - width / 2 + width),
                    (int) (y + ymap - height / 2 -15 ),
                    -width,
                    height,
                    null
            );

        }

    }


}










