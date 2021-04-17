package Entity.Enemies;

import Entity.MapObject;
import TileMap.TileMap;

import java.awt.*;
import java.util.ArrayList;

abstract public class Projectile extends MapObject {
    public static ArrayList<Projectile> projectiles;
    public Projectile(TileMap tm)
    {
        super(tm);

    }
    public abstract  void hit(int damage);
    public abstract void setHit();
    public abstract boolean shouldRemove();
    public abstract void update();
    public abstract void draw(Graphics2D g);
    public abstract int getDamage();

}
