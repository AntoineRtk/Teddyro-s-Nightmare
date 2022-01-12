package osoroshi.teddyro.game.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import osoroshi.teddyro.game.states.Main;

public class Placement {
    
    private int x, y, color, transparency = 255;
    
    public Placement(double x, double y) {
        this.x = (int) x;
        this.y = (int) y;
        color = new java.util.Random().nextInt(2);
    }
    
    public void update() {
        y += 2 + new java.util.Random().nextInt(6);
        transparency -= 5;
        if(transparency < 0) {
            transparency = 0;
        }
    }
    
    public void paint(Graphics2D g) {
        switch(Main.getCursor()) {
            case 0:
                g.setColor(Color.blue);
                if(color == 1) g.setColor(Color.cyan);
                break;
            case 1:
                g.setColor(Color.red);
                if(color == 1) g.setColor(Color.orange);
                break;
            default:
                g.setColor(Color.yellow);
                if(color == 1) g.setColor(new Color(200, 100, 0));
                break;
        }
        g.setColor(new Color(g.getColor().getRed(), g.getColor().getGreen(), g.getColor().getBlue(), transparency));
        g.fillRect(x, y, 1, 1);
    }
    
    public boolean shouldBeRemoved() {
        return transparency == 0;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
}