package Observer;

import java.awt.*;

public class ObserverB implements Observer{

    public void update(int sc, Graphics2D g){

        if(sc==0)
        {
            g.setColor(Color.BLACK);
            g.setFont(new Font("Courier New",Font.PLAIN,10));
            g.clearRect(0,210,400,30);
            g.drawRect(0,210,400,30);
            g.setColor(Color.WHITE);
            g.drawString("To improve your score,kill an enemy!",20,225);
        }
    }
}
