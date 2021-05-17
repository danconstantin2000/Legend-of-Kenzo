package Entity;
import TileMap.*;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Ryzen extends MapObject {

    private ArrayList<BufferedImage[]> sprites;
    private final int[] numFrames = {
            4,8
    };
    private static final int IDLE=0;
    private static final int RUNNING=1;
    public Ryzen(TileMap tm) {
        super(tm);
        width = 200;
        height = 200;
        cwidth =20;
        cheight =26;
        moveSpeed = 0.3;
        maxSpeed = 1.6;
        stopSpeed = 0.4;
        facingRight = false;
        fallSpeed = 0.1;
        maxFallSpeed = 4.0;
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
    private void setAnimations() {
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
    private void setDirection() {

        if(right) facingRight = true;
        if(left) {
            facingRight = false;
        }
    }
    private void getNextPosition() {
        if(left) {
            dx -= moveSpeed;
            if(dx < -maxSpeed) {
                dx = -maxSpeed;
            }
        }
        else if(right) {
            dx += moveSpeed;
            if(dx > maxSpeed) {
                dx = maxSpeed;
            }
        }
        else {
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

        if(jumping && !falling) {
            dy = jumpStart;
            falling = true;
        }

        if(falling) {
            dy += fallSpeed ;
            if(dy > 0) jumping = false;
            if(dy < 0 && !jumping) dy += stopJumpSpeed;
            if(dy > maxFallSpeed) dy = maxFallSpeed;
        }


    }
    public void update() {
        getNextPosition();
        checkTileMapCollision();
        setPosition(xtemp, ytemp);
        setAnimations();
        setDirection();
        animation.update();

    }
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










