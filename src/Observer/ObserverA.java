package Observer;

import java.awt.*;

public class ObserverA implements Observer{

    public void update(int sc, Graphics2D g)
    {
        g.drawString("Score:"+ sc,230,12);
    }


}
