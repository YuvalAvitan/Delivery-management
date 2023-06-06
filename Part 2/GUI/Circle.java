package GUI;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Circle
{
    private int centerX, centerY, radius;
    private Color color ;
    private int margin =10;

    private Ellipse2D shape;
    
    public Circle(int x, int y, int r)
    {
        centerX = x;
        centerY = y;
        radius = r;

        this.color  = Color.pink;
        this.shape = new Ellipse2D.Double(x,y,r*2,r*2);
    }

    public void drawCircle(Graphics g)
    {
        g.setColor(color);
        g.fillOval(centerX - radius, centerY - radius, radius * 2, radius * 2);
    }

    public void paint(Graphics2D g2d) {
    	g2d.setColor(Color.pink);
    	g2d.fill(shape);
    }

    public int getCenterX() {
        return centerX;
    }

    public void setCenterX(int centerX) {
        this.centerX = centerX;
    }

    public int getCenterY() {
        return centerY;
    }

    public void setCenterY(int centerY) {
        this.centerY = centerY;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getMargin() {
        return margin;
    }

    public void setMargin(int margin) {
        this.margin = margin;
    }

//
//
//    public boolean containsPoint(int x, int y)
//    {
//        int xSquared = (x - centerX) * (x - centerX);
//        int ySquared = (y - centerY) * (y - centerY);
//        int radiusSquared = radius * radius;
//        return xSquared + ySquared - radiusSquared <= 0;
//    }

    public void setColor(Color c)
    {
        this.color = c;
    }

}
