package Entity;

import Audio.AudioPlayer;
import Entity.Enemies.MushroomProjectile;
import Entity.Enemies.Projectile;
import Main.GamePanel;
import TileMap.*;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

//Clasa personajului principal(Kenzo)
public class Player extends MapObject {

    //informatii player
    private int health;
    private int maxHealth;
    private int energy;
    private int maxEnergy;
    private boolean dead;
    private boolean flinching;
    private long flinchTimer;
    //longAttack
    private boolean longAttacking;
    private int energyCost;
    private int longAttackDamage;
    private int longAttackRange;
    //smallAttack
    private boolean smallAttacking;
    private int smallAttackDamage;
    private int smallAttackRange;

    //Variabila pentru a verifica necesitatea de a trece la gameoverState
    private  boolean switchState;
    //variabila pentru a verifica caderea in gol
    private boolean Void;


    // spritesheet
    private ArrayList<BufferedImage[]> sprites;
    private final int[] numFrames = {
            6, 6, 6, 2, 8, 2,8
    };

    //Animatii spritesheet
    private static final int LONGATTACK = 0;
    private static final int SMALLATTACK=1;
    private static final int DEATH = 2;
    private static final int FALLING = 3;
    private static final int IDLE=4;
    private static final int JUMPING=5;
    private static final int RUNNING=6;
    private HashMap<String, AudioPlayer> sfx;
    //constructor
    public Player(TileMap tm) {
        super(tm);
        //inaltime si latime de citire din spritesheet
        width = 200;
        height = 200;
        //inatime si latime de coliziune
        cwidth =20;
        cheight =29;
        //atributii
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
        longAttackDamage = 4;
        longAttackRange = 100;
        Score=0;
        // Incarca sprite-urile
        try {

            BufferedImage spritesheet = ImageIO.read(
                    getClass().getResourceAsStream(
                            "/Sprites/Player/spritesheet (2).png"
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
        animation.setDelay(400);
        sfx=new HashMap<String,AudioPlayer>();
        sfx.put("jump",new AudioPlayer("/SFX/jump2-1.mp3"));
        sfx.put("longattack",new AudioPlayer("/SFX/scratch.mp3"));
        sfx.put("smallattack",new AudioPlayer("/SFX/Sword2.mp3"));
        sfx.put("herodeath",new AudioPlayer("/SFX/Hero_Dies.mp3"));
        sfx.put("enemy_take_dmg",new AudioPlayer("/SFX/Enemy_Damage.mp3"));
        sfx.put("hero_take_dmg",new AudioPlayer("/SFX/Hero_TakesDMG.wav"));

    }

    //getters
    public boolean getVoid(){return Void;}
    public boolean getSwitchState(){return switchState;}
    public boolean isDead(){return dead;}
    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public int getEnergy() { return energy; }
    public int getMaxEnergy() { return maxEnergy; }
    //setters
    public void setSmallAttacking(boolean b) {
        smallAttacking = b;
    }
    public void setLongAttacking(boolean b) {
        longAttacking=b;
    }
    //Verifica atacul dintre player-proiectila ,proiectila-player
    public void checkAttack2(ArrayList<Projectile> projectiles)
    {
        for(int i=0;i<projectiles.size();i++) {
            Projectile mp = projectiles.get(i);
            //Cand ia proiectila damage.
            //Daca playerul folosest longattack-ul:
            if (longAttacking) {
                if (facingRight) {
                    //Daca proiectila este la dreapta player-ului,dar este in range-ul atacului,inseamna ca acesta s-a produs.
                    if (mp.getx() > x && mp.getx() < x + longAttackRange && mp.gety() > y - height / 2 && mp.gety() < y + height / 2) {
                        mp.hit(longAttackDamage);
                        sfx.get("enemy_take_dmg").play();
                    }
                } else {

                    if (mp.getx() < x && mp.getx() > x - longAttackRange && mp.gety() > y - height / 2 && mp.gety() < y + height / 2) {
                        mp.hit(longAttackDamage);
                        sfx.get("enemy_take_dmg").play();
                    }
                }
                //Daca Playerul foloseste smallattack-ul:
            } else if (smallAttacking) {
                if (facingRight) {
                    //Daca proiectila este la dreapta player-ului,dar este in range-ul atacului,inseamna ca acesta s-a produs.
                    if (mp.getx() > x && mp.getx() < x + smallAttackRange && mp.gety() > y - height / 2 && mp.gety() < y + height / 2) {
                        mp.hit(smallAttackDamage);
                        sfx.get("enemy_take_dmg").play();
                    }
                } else {
                    //Daca proiectila este la stanga playerului in timp ce el priveste spre stanga,dar este in range-ul atacului,inseamna ca acesta s-a produs.
                    if (mp.getx() < x && mp.getx() > x - smallAttackRange && mp.gety() > y - height / 2 && mp.gety() < y + height / 2) {
                        mp.hit(smallAttackDamage);
                        sfx.get("enemy_take_dmg").play();
                    }
                }
            }
            //Cand ia playerul damage:
            //Daca playerul se intersecteaza cu proiectila,aceasta se distruge si ia din viata playerului.
            if(intersects(mp))
            {
                Projectile.projectiles.get(i).setHit();
                hit(mp.getDamage());
            }
        }
    }
//Similar ca atacul cu proiectile dar este cu inamici
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

            }
            if(intersects(e))
            {
                hit(e.getDamage());

            }

        }


    }
    //Playerul a fost nimerit
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
//La fiecare longAttack,se scade 500 din totalul de 2500 de energie.Aceasta se incarca inapoi treptat.
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
        if((currentAction == SMALLATTACK|| currentAction == LONGATTACK) && !(jumping || falling)) {
            dx = 0;
        }
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

    private void shouldSwitchState()
    {
        if(currentAction==DEATH)
        {
            if(animation.hasPlayedOnce())
            {
                switchState=true;
            }
        }
    }
    //Daca animatia pentru atac s-a terminat,s-a terminat si atacul.
    private void turnOffAttacks()
    {

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
    }
    //Calcul pentru incetarea Flinching-ului(Cand un obiect ia damage,acesta are flinching-ul activat)
    private void turnOffFlinching()
    {
        if(flinching)
        {
            long elapsed=(System.nanoTime()-flinchTimer)/1000000;
            if(elapsed>1000)
            {
                flinching=false;
            }
        }
    }
    //Setaria animatiilor din spritesheet.In functie de setarea actiunii,sprite-ul corespuznator se va actualiza pe ecran.
    private void setAnimations()
    {
            if(longAttacking) {
                if(currentAction != LONGATTACK) {
                    sfx.get("longattack").play();
                    currentAction = LONGATTACK;
                    animation.setFrames(sprites.get(LONGATTACK));
                    animation.setDelay(50);


                }
            }
            else if(smallAttacking) {
                if(currentAction != SMALLATTACK) {
                    sfx.get("smallattack").play();
                    currentAction = SMALLATTACK;
                    animation.setFrames(sprites.get(SMALLATTACK));
                    animation.setDelay(30);


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
                    animation.setDelay(40);

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
    //Setaria directiei dreapta-stanga.
    private void setDirection()
    {

            if(right) facingRight = true;
            if(left) {
                facingRight = false;
            }
    }


    private void voidFall()
    {
        //Metoda pentru verificarea iesirii de pe ecran cand playerul cade in gol.
        if(  y+ymap+cheight> GamePanel.HEIGHT)
        {

            dead=true;
            Void=true;
        }
    }

    public void update() {

        // update position
        getNextPosition();
        checkTileMapCollision();
        setPosition(xtemp, ytemp);
        EnergyActions();
        shouldSwitchState();
        turnOffAttacks();
        turnOffFlinching();
        setAnimations();
        setDirection();
        animation.update();
        voidFall();


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

        if (facingRight) {
            if(longAttacking)
            {
                g.drawImage(
                        animation.getImage(),
                        (int) (x + xmap - width / 2),
                        (int) (y + ymap - height / 2 - 9),
                        width
                        , height,
                        null
                );
            }
            else if(smallAttacking)
            {
                g.drawImage(
                        animation.getImage(),
                        (int) (x + xmap - width / 2),
                        (int) (y + ymap - height / 2 -11),
                        width
                        , height,
                        null
                );
            }else if(dead)
            {
                g.drawImage(
                        animation.getImage(),
                        (int) (x + xmap - width / 2),
                        (int) (y + ymap - height / 2 -11),
                        width
                        , height,
                        null);
            }else {
                g.drawImage(
                        animation.getImage(),
                        (int) (x + xmap - width / 2),
                        (int) (y + ymap - height / 2 - 17),
                        width
                        , height,
                        null
                );
            }
        }
        else
        {
            if(longAttacking)
            {
                g.drawImage(
                        animation.getImage(),
                        (int) (x + xmap - width / 2+width),
                        (int) (y + ymap - height / 2 - 9),
                        -width
                        , height,
                        null
                );
            }
            else if(smallAttacking) {
                g.drawImage(
                        animation.getImage(),
                        (int) (x + xmap - width / 2+width),
                        (int) (y + ymap - height / 2 - 11),
                        -width
                        , height,
                        null
                );
            }else if(dead)
            {
                g.drawImage(
                        animation.getImage(),
                        (int) (x + xmap - width / 2+width),
                        (int) (y + ymap - height / 2 -11),
                        -width
                        , height,
                        null);
            }
           else g.drawImage(
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











