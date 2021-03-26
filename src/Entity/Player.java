package Entity;

import TileMap.*;

import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends MapObject {

    // player stuff
    private int health;
    private int maxHealth;
    private int fire;
    private int maxFire;
    private boolean dead;
    private boolean flinching;
    private long flinchTimer;

    // fireball
    private boolean firing;
    private int fireCost;
    private int fireBallDamage;
    //private ArrayList<FireBall> fireBalls;

    // scratch
    private boolean scratching;
    private int scratchDamage;
    private int scratchRange;

    // gliding
    private boolean gliding;

    // animations
    private ArrayList<BufferedImage[]> sprites;
    private final int[] numFrames = {
            6, 6, 6, 2, 8, 2,8,4
    };

    // animation actions
    private static final int ATTACK1 = 0;
    private static final int ATTACK2=1;
    private static final int DEATH = 2;
    private static final int FALLING = 3;
    private static final int IDLE=4;
    private static final int JUMPING=5;
    private static final int RUNNING=6;
    private static final int TAKEHIT=7;

    public Player(TileMap tm) {

        super(tm);

        width = 200;
        height = 200 ;
        cwidth =20;
        cheight =27;

        moveSpeed = 0.3;
        maxSpeed = 1.6;
        stopSpeed = 0.4;
        fallSpeed = 0.3;
        maxFallSpeed = 4.0;
        jumpStart = -4.5;
        stopJumpSpeed = 0.3;

        facingRight = true;

        health = maxHealth = 5;
        fire = maxFire = 2500;

        fireCost = 200;
        fireBallDamage = 5;
        //fireBalls = new ArrayList<FireBall>();

        scratchDamage = 8;
        scratchRange = 40;

        // load sprites
        try {

            BufferedImage spritesheet = ImageIO.read(
                    getClass().getResourceAsStream(
                            "/Sprites/Player/spritesheet (2).png"
                    )
            );

            sprites = new ArrayList<BufferedImage[]>();
            for(int i = 0; i < 8; i++) {

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

    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public int getFire() { return fire; }
    public int getMaxFire() { return maxFire; }

    public void setFiring() {
        firing = true;
    }
    public void setScratching() {
        scratching = true;
    }
    public void setGliding(boolean b) {
        gliding = b;
    }

    private void getNextPosition() {

        // movement
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

        // cannot move while attacking, except in air
        if(
                (currentAction == ATTACK1 || currentAction == ATTACK2) &&
                        !(jumping || falling)) {
            dx = 0;
        }

        // jumping
        if(jumping && !falling) {
            dy = jumpStart;
            falling = true;
        }

        // falling
        if(falling) {

            if(dy > 0 ) dy += fallSpeed * 0.1;
            else dy += fallSpeed;

            if(dy > 0) jumping = false;
            if(dy < 0 && !jumping) dy += stopJumpSpeed;

            if(dy > maxFallSpeed) dy = maxFallSpeed;

        }

    }

    public void update() {

        // update position
        getNextPosition();
        checkTileMapCollision();
        setPosition(xtemp, ytemp);

        // set animation
        if(scratching) {
            if(currentAction != ATTACK1) {
                currentAction = ATTACK1;
                animation.setFrames(sprites.get(ATTACK1));
                animation.setDelay(50);
                width = 200;//60;
            }
        }
        else if(firing) {
            if(currentAction != ATTACK2) {
                currentAction = ATTACK2;
                animation.setFrames(sprites.get(ATTACK2));
                animation.setDelay(100);
                width =200; //30;
            }
        }
        else if(dy > 0)
        {
             if(currentAction != FALLING) {
                currentAction = FALLING;
                animation.setFrames(sprites.get(FALLING));
                animation.setDelay(100);
                width = 200;//30;
            }
        }
        else if(dy < 0) {
            if(currentAction != JUMPING) {
                currentAction = JUMPING;
                animation.setFrames(sprites.get(JUMPING));
                animation.setDelay(-1);
                width = 200;//30;
            }
        }
        else if(left || right) {
            if(currentAction != RUNNING) {
                currentAction = RUNNING;
                animation.setFrames(sprites.get(RUNNING));
                animation.setDelay(40);
                width = 200;//30;
            }
        }
        else {
            if(currentAction != IDLE) {
                currentAction = IDLE;
                animation.setFrames(sprites.get(IDLE));
                animation.setDelay(400);
                width = 200;//30;
            }
        }

        animation.update();

        // set direction
        if(currentAction !=ATTACK1 && currentAction !=ATTACK2) {
            if(right) facingRight = true;
            if(left) facingRight = false;
        }



    }

    public void draw(Graphics2D g) {

        setMapPosition();

        // draw player
        if(flinching) {
            long elapsed =
                    (System.nanoTime() - flinchTimer) / 1000000;
            if(elapsed / 100 % 2 == 0) {
                return;
            }
        }

        if(facingRight) {
            g.drawImage(
                    animation.getImage(),
                    (int)(x + xmap - width / 2),
                    (int)(y + ymap - height / 2),
                    null
            );
        }
        else {
            g.drawImage(
                    animation.getImage(),
                    (int)(x + xmap - width / 2 + width),
                    (int)(y + ymap - height / 2),
                    -width,
                    height,
                    null
            );

        }

    }

}











