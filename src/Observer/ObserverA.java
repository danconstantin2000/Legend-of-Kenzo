package Observer;

import java.awt.*;

public class ObserverA implements Observer{

    public void update(int sc,double x, Graphics2D g)
    {
        if(x>0) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("Courier New", Font.BOLD, 14));
            g.drawString("Score:" + sc, 230, 12);
        }
    }


}
