package TileMap;
import java.awt.image.BufferedImage;
//Tile Class

public class Tile {
    private BufferedImage image;
    private int type;
    public static final int NORMAL=0;//fara coliziune
    public static final int BLOCKED=1;//cu coliziune
    public Tile(BufferedImage image,int type)
    {
        this.image=image;
        this.type=type;

    }
    //Getters
    public BufferedImage getImage(){return image;}
    public int getType(){return type;}

}
