package Entity;

import Audio.AudioPlayer;
import Main.GamePanel;
import TileMap.*;

import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class Player extends MapObject {

    // player's info
    private int health;
    private int maxHealth;
    private int energy;
    private int maxEnergy;
    private boolean dead;
    private boolean flinching;
    private long flinchTimer;

    // long Attack
    private boolean longAttacking;
    private int energyCost;
    private int longAttackDamage;
    private int longAttackRange;
    //small Attack
    private boolean smallAttacking;
    private int smallAttackDamage;
    private int smallAttackRange;
    public int score;
    public boolean switchState;
    public boolean Gol;

    // animations
    private ArrayList<BufferedImage[]> sprites;
    private final int[] numFrames = {
            6, 6, 6, 2, 8, 2,8,4
    };

    // animation actions
    private static final int LONGATTACK = 0;
    private static final int SMALLATTACK=1;
    private static final int DEATH = 2;
    private static final int FALLING = 3;
    private static final int IDLE=4;
    private static final int JUMPING=5;
    private static final int RUNNING=6;
    private static final int TAKEHIT=7;

    private HashMap<String, AudioPlayer> sfx;


    public Player(TileMap tm) {

        super(tm);

        width = 200;
        height = 200 ;
        cwidth =20;
        cheight =27;

        moveSpeed = 0.3;
        maxSpeed = 1.6;
        stopSpeed = 0.4;
        fallSpeed = 0.1;
        maxFallSpeed = 4.0;
        jumpStart = -3;
        stopJumpSpeed = 0.3;

        facingRight = true;

        health = maxHealth = 5;
        energy = maxEnergy = 2500;
        dead=false;
        energyCost = 500;
        smallAttackDamage = 1;
        smallAttackRange=100;
        longAttackDamage = 8;
        longAttackRange = 100;
        score=0;
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
        sfx=new HashMap<String,AudioPlayer>();
        sfx.put("jump",new AudioPlayer("/SFX/jump2-1.mp3"));
        sfx.put("longattack",new AudioPlayer("/SFX/scratch.mp3"));
        sfx.put("smallattack",new AudioPlayer("/SFX/Sword2.mp3"));
        sfx.put("herodeath",new AudioPlayer("/SFX/Hero_Dies.mp3"));
        sfx.put("enemy_take_dmg",new AudioPlayer("/SFX/Enemy_Damage.mp3"));
        sfx.put("hero_take_dmg",new AudioPlayer("/SFX/Hero_TakesDMG.wav"));

    }

    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public int getEnergy() { return energy; }
    public int getMaxEnergy() { return maxEnergy; }

    public void setSmallAttacking(boolean b) {
        smallAttacking = b;
    }
    public void setLongAttacking(boolean b) {
        longAttacking=b;
    }
    public void checkAttack(ArrayList<Enemy> enemies)
    {
        for(int i=0;i<enemies.size();i++) {
            Enemy e = enemies.get(i);
            //long attack
            if (longAttacking) {
                if (facingRight) {
                    if (e.getx() > x && e.getx() < x + longAttackRange && e.gety() > y - height / 2 && e.gety() < y + height / 2) {
                        e.hit(longAttackDamage);
                        sfx.get("enemy_take_dmg").play();

                    }
                } else {
                    if (e.getx() < x && e.getx() > x - longAttackRange && e.gety() > y - height / 2 && e.gety() < y + height / 2) {

                        e.hit(longAttackDamage);
                        sfx.get("enemy_take_dmg").play();

                    }
                }
            } else if (smallAttacking) {
                if (facingRight) {
                    if (e.getx() > x && e.getx() < x + smallAttackRange && e.gety() > y - height / 2 && e.gety() < y + height / 2) {
                        e.hit(smallAttackDamage);
                        sfx.get("enemy_take_dmg").play();

                    }
                } else {
                    if (e.getx() < x && e.getx() > x - smallAttackRange && e.gety() > y - height / 2 && e.gety() < y + height / 2) {

                        e.hit(smallAttackDamage);
                        sfx.get("enemy_take_dmg").play();

                    }
                }
                //check enemy collision

            }
            if(intersects(e))
            {

                hit(e.getDamage());

            }

        }


    }
    public void hit(int damage)
    {
        if(flinching)return;
        health-=damage;
        if(health==0)flinching=false;
        if(health<0)health=0;

        if(health==0){dead=true;}
        flinching=true;
        flinchTimer=System.nanoTime();
    }


    public void EnergyActions()
    {
        if(longAttacking) {

            if (energy < energyCost) {
                longAttacking = false;

            }


            if (energy < 0) {
                energy = 0;
            }

        }
        if (energy > maxEnergy) {
            energy = maxEnergy;
        }

        energy++;

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
                (currentAction == SMALLATTACK|| currentAction == LONGATTACK) &&
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

            dy += fallSpeed ;


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

        EnergyActions();


        if(currentAction==DEATH)
        {
            if(animation.hasPlayedOnce())
            {
                switchState=true;
            }

        }

        if(currentAction==LONGATTACK)
        {
            if(animation.hasPlayedOnce())
            {
                energy=energy-energyCost;
                longAttacking=false;
                sfx.get("longattack").stop();

            }
        }
        if(currentAction==SMALLATTACK)
        {
            if(animation.hasPlayedOnce())
            {
                smallAttacking=false;
                sfx.get("smallattack").stop();

            }
        }

        if(flinching)
        {

            long elapsed=(System.nanoTime()-flinchTimer)/1000000;
            if(elapsed>1000)
            {
                flinching=false;
            }
        }
        // set animation

        if(longAttacking) {


            if(currentAction != LONGATTACK) {
                sfx.get("longattack").play();
                currentAction = LONGATTACK;
                animation.setFrames(sprites.get(LONGATTACK));
                animation.setDelay(50);
                width =200;
                height=185;


            }

        }
        else if(smallAttacking) {

            if(currentAction != SMALLATTACK) {
               sfx.get("smallattack").play();
                currentAction = SMALLATTACK;
                animation.setFrames(sprites.get(SMALLATTACK));
                animation.setDelay(30);
                width =200; //30;
                height=190;

            }

        }
        else if(dy > 0)
        {
             if(currentAction != FALLING) {
                currentAction = FALLING;
                animation.setFrames(sprites.get(FALLING));
                animation.setDelay(100);
                width = 200;
                height=200;

             }
        }
        else if(dy < 0) {

                if(currentAction != JUMPING) {
                    sfx.get("jump").play();
                currentAction = JUMPING;
                animation.setFrames(sprites.get(JUMPING));
                animation.setDelay(-1);
                width = 200;//30;
                height=200;

            }
        }
        else if(left || right) {

            if(currentAction != RUNNING) {
                currentAction = RUNNING;
                animation.setFrames(sprites.get(RUNNING));
                animation.setDelay(40);
                width = 200;//30
                height=200;

            }
        }
        else  if(dead)
        {

            if(currentAction!=DEATH)
            {

                sfx.get("herodeath").play();
                currentAction=DEATH;
                animation.setFrames(sprites.get(DEATH));
                animation.setDelay(400);
                width=200;
                height=190;


            }

        }else
            {

            if(currentAction != IDLE) {
                currentAction = IDLE;
                animation.setFrames(sprites.get(IDLE));
                animation.setDelay(150);
                width = 200;//30;
                height=200;

            }
        }




        // set direction
        if(currentAction !=LONGATTACK && currentAction !=SMALLATTACK) {
            if(right) facingRight = true;
            if(left) {
                facingRight = false;
            }
        }


        if(smallAttacking)
        {
            LongAttack=false;
            SmallAttack=true;

        }
        else if(longAttacking)
        {
            LongAttack=true;
            SmallAttack=false;

        }
        else
        {

            LongAttack=false;
            SmallAttack=false;
        }

        animation.update();
       if( y > 224)
       {
           health=0;
           Gol=true;

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

        super.draw(g);

    }


}











