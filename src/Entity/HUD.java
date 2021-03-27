package Entity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class HUD {

    private Player player;
    private BufferedImage image;
    private Font font;
    public HUD(Player p)
    {
        player=p;
        try
        {
            image= ImageIO.read(getClass().getResourceAsStream("/HUD/hud3.png"));
            font=new Font("Courier new",Font.BOLD,14);

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    public void draw(Graphics2D g)
    {
        g.drawImage(image,0,0,null);
        g.setFont(font);
        g.setColor(Color.BLACK);
        g.drawString(
                player.getHealth() + "/" + player.getMaxHealth(),
                30,
                35
        );
        g.drawString(
                player.getFire() / 100 + "/" + player.getMaxFire() / 100,
                80,
                35
        );

    }


}
