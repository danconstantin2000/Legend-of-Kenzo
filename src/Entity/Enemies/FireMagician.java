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
import static java.lang.Math.abs;

public class FireMagician extends Enemy {

    private HashMap<String, AudioPlayer> sfx;
    private ArrayList<BufferedImage[]> sprites;
    private final int[] numFrames = {
            8, 5, 8, 8,
    };
    private static final int ATTACK = 0;
    private static final int DEATH = 1;
    private static final int IDLE=2;
    private static final int RUNNING=3;
    private boolean Attacking;
    private boolean start;
    private boolean timeToAttack;
    private boolean drawBar;
    private long timer;
    private int AttackDamage;
    private int AttackRange;
    private Player myPlayer;

    public FireMagician(TileMap tm,Player p) {
        super(tm);
        width = 200;
        height = 200;
        cwidth =20;
        cheight =28;
        moveSpeed = 0.3;
        maxSpeed = 1.3;
        stopSpeed = 0.4;
        fallSpeed = 0.1;
        maxFallSpeed = 4.0;
        jumpStart = -3;
        stopJumpSpeed = 0.3;
        facingRight = true;
        health = maxHealth = 40;
        damage=1;
        Score=1500;
        myPlayer=p;
        dead=false;
        drawBar=false;
        AttackDamage = 2;
        AttackRange=100;
        timeToAttack=true;
        start=true;
        facingRight=false;
        try {

            BufferedImage spritesheet = ImageIO.read(
                    getClass().getResourceAsStream(
                            "/Sprites/Enemies/FireSprite3.png"
                    )
            );

            sprites = new ArrayList<BufferedImage[]>();
            for(int i = 0; i < 4; i++) {

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
        sfx.put("longattack",new AudioPlayer("/SFX/Fire bolt 6.wav"));
        timer=System.nanoTime();

    }
    public boolean hasPlayed()
    {
        return animation.hasPlayedOnce();
    }
    private void setAnimations() {
        if(Attacking) {
            if(currentAction != ATTACK) {
                sfx.get("longattack").play();
                currentAction = ATTACK;
                animation.setFrames(sprites.get(ATTACK));
                animation.setDelay(200);
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
        if(currentAction==ATTACK)
        {
            if(animation.hasPlayedOnce())
            {
                Attacking=false;
                sfx.get("longattack").stop();
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

        if((currentAction== ATTACK) && !(jumping || falling)) {
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
        if (Attacking) {
            if (facingRight) {
                if (myPlayer.getx() > x && myPlayer.getx() < x + AttackRange && myPlayer.gety() > y - height / 2 && myPlayer.gety() < y + height / 2) {
                    myPlayer.hit(AttackDamage);
                }
            } else {
                if (myPlayer.getx() < x && myPlayer.getx() > x - AttackRange && myPlayer.gety() > y - height / 2 && myPlayer.gety() < y + height / 2) {
                    myPlayer.hit(AttackDamage);

                }
            }
        }
    }
    private void startAttack() {
        long elapsed = (System.nanoTime() - timer) / 1000000;
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

                    if (abs(myPlayer.getx() - this.getx()) < 200) {
                        Attacking = true;
                    }

                }

            }

            if (Attacking == true) {
                right = left = false;
                if (animation.hasPlayedOnce()) {
                    Attacking = false;
                    timeToAttack = false;
                }

            }


            if (elapsed > 6000) {
                timeToAttack = true;


            }
            if (elapsed > 6100) {
                timer = System.nanoTime();

            }

        }
        else
        {
            left=right=Attacking=false;
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
    private void drawBar(Graphics2D g) {
        if(abs(myPlayer.getx()-this.getx())<100 )
        {
            drawBar=true;

        }
        if(drawBar==true)
        {
            g.setColor(Color.red);
            g.setFont(new Font("Courier New",Font.PLAIN,11));
            String bar="";

            for(int i=0;i<this.health;i++)
            {
                bar=bar+"_";

            }
            g.drawString(bar,25,200);
            g.drawString(bar,25,201);
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
                    (int) (y + ymap - height / 2-17 ),
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
                    (int) (y + ymap - height / 2 -17 ),
                    -width,
                    height,
                    null
            );

        }

    }

}
