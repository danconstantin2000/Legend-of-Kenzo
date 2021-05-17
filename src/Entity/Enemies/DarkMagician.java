package Entity.Enemies;
import Audio.AudioPlayer;
import Entity.Animation;
import Entity.Enemy;
import Entity.Player;
import TileMap.TileMap;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;
import static java.lang.Math.abs;

public class DarkMagician extends Enemy {

    private HashMap<String, AudioPlayer> sfx;
    private ArrayList<BufferedImage[]> sprites;
    private final int[] numFrames = {
            8, 8, 7, 2, 8, 2,8
    };
    private static final int LONGATTACK = 0;
    private static final int SMALLATTACK=1;
    private static final int DEATH = 2;
    private static final int FALLING = 3;
    private static final int IDLE=4;
    private static final int JUMPING=5;
    private static final int RUNNING=6;
    private boolean smallAttacking;
    private boolean longAttacking;
    private boolean start;
    private boolean timeToAttack;
    private boolean drawBar;
    private long timer;
    private int rand;
    private long jumpTimer;
    private int smallAttackDamage;
    private int longAttackDamage;
    private int smallAttackRange;
    private int longAttackRange;
    private Player myPlayer;

    public DarkMagician(TileMap tm,Player p) {
        super(tm);
        width = 250;
        height = 250;
        cwidth =20;
        cheight =28;
        moveSpeed = 0.3;
        maxSpeed = 1.1;
        stopSpeed = 0.4;
        fallSpeed = 0.1;
        maxFallSpeed = 4.0;
        jumpStart = -3;
        stopJumpSpeed = 0.3;
        facingRight = true;
        health = maxHealth = 30;
        damage=1;
        Score=1500;
        myPlayer=p;
        dead=false;
        smallAttackDamage = 1;
        smallAttackRange=100;
        longAttackDamage = 2;
        longAttackRange = 100;
        timeToAttack=true;
        start=true;
        facingRight=false;
        drawBar=false;
        try {

            BufferedImage spritesheet = ImageIO.read(
                    getClass().getResourceAsStream(
                            "/Sprites/Enemies/DarkSprite.png"
                    )
            );

            sprites = new ArrayList<BufferedImage[]>();
            for(int i = 0; i < 7; i++) {

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
        animation.setDelay(150);

        sfx=new HashMap<String,AudioPlayer>();
        sfx.put("death",new AudioPlayer("/SFX/Boss_Death.wav"));
        sfx.put("jump",new AudioPlayer("/SFX/SFX_Jump_09.wav"));
        sfx.put("longattack",new AudioPlayer("/SFX/WB18.mp3"));
        sfx.put("smallattack",new AudioPlayer("/SFX/WB20.mp3"));
        timer=System.nanoTime();
        jumpTimer=System.nanoTime();

    }
    public boolean hasPlayed()
    {
        return animation.hasPlayedOnce();
    }
    private void setAnimations() {
        if(longAttacking) {
            if(currentAction != LONGATTACK) {
                sfx.get("longattack").play();
                currentAction = LONGATTACK;
                animation.setFrames(sprites.get(LONGATTACK));
                animation.setDelay(100);
            }
        }
        else if(smallAttacking) {
            if(currentAction != SMALLATTACK) {
                sfx.get("smallattack").play();
                currentAction = SMALLATTACK;
                animation.setFrames(sprites.get(SMALLATTACK));
                animation.setDelay(100);
            }
        }
        else if(dy > 0)
        {
            if(currentAction != FALLING) {
                currentAction = FALLING;
                animation.setFrames(sprites.get(FALLING));
                animation.setDelay(100);
            }
        }
        else if(dy < 0) {
            if(currentAction != JUMPING) {
                sfx.get("jump").play();
                currentAction = JUMPING;

                animation.setFrames(sprites.get(JUMPING));
                animation.setDelay(-1);

            }
        }
        else if(left || right) {
            if(currentAction != RUNNING) {
                currentAction = RUNNING;
                animation.setFrames(sprites.get(RUNNING));
                animation.setDelay(80);
            }
        }
        else  if(dead) {
            if (currentAction != DEATH)
            {
                sfx.get("death").play();
                currentAction=DEATH;
                animation.setFrames(sprites.get(DEATH));
                animation.setDelay(400);
            }
        }else
        {
            if(currentAction != IDLE) {
                currentAction = IDLE;
                animation.setFrames(sprites.get(IDLE));
                animation.setDelay(150);
            }
        }
    }
    private void turnOffAttacks() {

        if(currentAction==LONGATTACK)
        {
            if(animation.hasPlayedOnce())
            {
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

        if((currentAction == SMALLATTACK|| currentAction == LONGATTACK) && !(jumping || falling)) {
            dx = 0;
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
    private void turnOffFlinching() {
        if(flinching)
        {
            long elapsed=(System.nanoTime()-flinchTimer)/1000000;
            if(elapsed>1000)
            {
                flinching=false;
            }
        }
    }
    public void checkAttack() {
            if (longAttacking) {
                if (facingRight) {
                    if (myPlayer.getx() > x && myPlayer.getx() < x + longAttackRange && myPlayer.gety() > y - height / 2 && myPlayer.gety() < y + height / 2) {
                        myPlayer.hit(longAttackDamage);
                    }
                } else {
                    if (myPlayer.getx() < x && myPlayer.getx() > x - longAttackRange && myPlayer.gety() > y - height / 2 && myPlayer.gety() < y + height / 2) {
                        myPlayer.hit(longAttackDamage);
                    }
                }
            } else if (smallAttacking) {
                if (facingRight) {
                    if (myPlayer.getx() > x && myPlayer.getx() < x + smallAttackRange && myPlayer.gety() > y - height / 2 && myPlayer.gety() < y + height / 2) {
                        myPlayer.hit(smallAttackDamage);
                    }
                } else {
                    if (myPlayer.getx() < x && myPlayer.getx() > x - smallAttackRange && myPlayer.gety() > y - height / 2 && myPlayer.gety() < y + height / 2) {
                        myPlayer.hit(smallAttackDamage);
                    }
                }
        }

    }
    private void startAttack() {
        long elapsed = (System.nanoTime() - timer) / 1000000;
        long elapsedJump=(System.nanoTime()-jumpTimer)/1000000;

        if(!dead) {
            if (start) {
                if (abs(myPlayer.getx() - this.getx()) < 100) {
                    left = true;
                    start = false;
                }

            }
            if (!start) {
                if (myPlayer.getx() > this.x && !myPlayer.isJumping()) {
                    facingRight = true;
                    right = true;
                    left = false;

                } else if (myPlayer.getx() < this.x && !myPlayer.isJumping()) {
                    facingRight = false;
                    right = false;
                    left = true;
                }

                if (timeToAttack == true) {

                    if (rand == 1) {
                        if (abs(myPlayer.getx() - this.getx()) < 200) {

                            smallAttacking = true;
                        }
                    } else if (rand == 0) {
                        if (abs(myPlayer.getx() - this.getx()) < 200) {

                            longAttacking = true;
                        }
                    }
                }

            }

            if (longAttacking == true) {
                right = left = false;
                if (animation.hasPlayedOnce()) {
                    longAttacking = false;
                    timeToAttack = false;
                }
            }

            if (smallAttacking == true) {
                right = left = false;
                if (animation.hasPlayedOnce()) {
                    smallAttacking = false;
                    timeToAttack = false;
                }
            }

            if (elapsed < 6000 && elapsed > 5500) {
                rand = ThreadLocalRandom.current().nextInt(2);

            }
            if (elapsed > 6000) {
                timeToAttack = true;
            }
            if (elapsed > 6300) {
                timer = System.nanoTime();

            }
            if (elapsedJump > 8000 && abs(myPlayer.getx() - this.getx()) < 100) {
                jumping = true;
                jumpTimer = System.nanoTime();
            }
        }
        else
        {
            left=right=smallAttacking=longAttacking=false;
            dy=0;
        }
        if(myPlayer.isDead())
        {
            left=right=false;
        }
    }
    public void update() {
        getNextPosition();
        checkTileMapCollision();
        setPosition(xtemp, ytemp);
        checkAttack();
        startAttack();
        setAnimations();
        setDirection();
        animation.update();
        turnOffFlinching();
        turnOffAttacks();
    }
    private void drawBar(Graphics2D g){
        if(abs(myPlayer.getx()-this.getx())<100 )
        {
            drawBar=true;

        }
        if(drawBar==true)
        {
            g.setColor(Color.red);
            g.setFont(new Font("Courier New",Font.PLAIN,15));
            String bar="";

            for(int i=0;i<this.health;i++)
            {
                bar=bar+"_";


            }
            g.drawString(bar,30,200);
            g.drawString(bar,30,201);


            if(this.health==0)
            {
                drawBar=false;
            }
        }
    }
    public void draw(Graphics2D g) {

        setMapPosition();
        drawBar(g);
        if(flinching) {
            long elapsed =
                    (System.nanoTime() - flinchTimer) / 1000000;
            if(elapsed / 100 % 2 == 0) {
                return;
            }
        }

        if (facingRight) {
                g.drawImage(
                        animation.getImage(),
                        (int) (x + xmap - width / 2),
                        (int) (y + ymap - height / 2-27 ),
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
                        (int) (y + ymap - height / 2 -27 ),
                        -width,
                        height,
                        null
                );
        }
    }
}
