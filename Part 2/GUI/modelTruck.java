package GUI;

import javax.swing.*;
import java.awt.*;

public class modelTruck
{
    private Color color;
    private int x;
    private int y;

    public modelTruck(int x, int y, Color c)
    {
        this.x = x;
        this.y =y;
        this.color = c;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void drawTruck(Graphics g)
    {
        g.setColor(this.color);
        g.fillRect(x,y-4,10,10);
        g.setColor(Color.BLACK);
        g.fillOval(x-3,y-9,7,7);
        g.fillOval(x+7,y-9,7,7);
        g.fillOval(x-3,y+3,7,7);
        g.fillOval(x+7,y+3,7,7);
    }


}
