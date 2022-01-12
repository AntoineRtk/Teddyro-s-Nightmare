package osoroshi.teddyro.game.world;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.MultipleGradientPaint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;
import osoroshi.teddyro.game.utils.GameResource;

public class GameLevelSelector {
    
    private int x, y, xe, state, world = -1;
    private int[] from, levelToComplete, dirs;
    private boolean boss = false, finish = false, bonus = false, shown = true, finalAccess = false;
    private double rotation;
    private Path2D star;
    
    public GameLevelSelector(int[][] informations) {
        this.x = informations[0][0];
        this.y = informations[0][1];
        this.state = informations[0][2];
        this.from = new int[informations[1].length];
        for(int i = 0; i < informations[1].length; i++) {
            this.from[i] = informations[1][i];
        }
        this.levelToComplete = new int[informations[2].length];
        for(int i = 0; i < informations[2].length; i++) {
            this.levelToComplete[i] = informations[2][i];
        }
        this.dirs = new int[informations[3].length];
        for(int i = 0; i < informations[3].length; i++) {
            this.dirs[i] = informations[3][i];
        }
        this.star = GameResource.getStarPath(60, 60);
    }
    
    public void update() {
        xe++;
        if(xe%100==0)xe=0;
        rotation += 5;
        if(rotation == 360) {
            rotation = 0;
        }
    }
    
    public void setFinish(boolean finish) {
        this.finish = finish;
    }
    
    public void paint(Graphics2D g) {
        g.setColor(new Color(255, 0, 0, 191));
        if(finish) {
            float[] floats = new float[10];
            for(int i = 0; i < 10; i++) {
                floats[i] = i/10f;
            }
            Color[] colors = {Color.blue, Color.red, Color.yellow, Color.green, Color.pink, Color.orange, Color.magenta, Color.gray, Color.cyan, new Color(130, 65, 0)};
            g.setPaint(new LinearGradientPaint(xe, 0, xe + 10, 0, floats, colors, MultipleGradientPaint.CycleMethod.REPEAT));
        }
        if(!bonus) {
            g.fillOval(x, y, 60, 60);
        }
        else {
            AffineTransform af = g.getTransform();
            g.setTransform(AffineTransform.getRotateInstance(Math.toRadians(rotation), x + 30, y + 30));
            g.translate(x, y);
            g.fill(star);
            g.setTransform(af);
        }
        if(boss) {
            g.setColor(Color.red);
            GeneralPath gp = new GeneralPath();
            double[] ypoints = new double[10];
            for(int i = 0; i < ypoints.length; i++) {
                ypoints[i] = Math.sqrt(30 * 30 - (10 - (i + 1)) * (10 - (i + 1)));
            }
            gp.moveTo(getX() + 20, getY() + 30 - ypoints[0]);
            gp.lineTo(getX() + 30, getY() - 30);
            gp.lineTo(getX() + 40, getY() + 30 - ypoints[0]);
            gp.closePath();
            AffineTransform af = g.getTransform();
            for(double r = rotation; r < 360 + rotation; r += 90) {
                g.setTransform(AffineTransform.getRotateInstance(r * Math.PI / 180.0, getX() + 30, getY() + 30));
                g.fill(gp);
            }
            g.setTransform(af);
        }
        if(finalAccess) {
            AffineTransform af = g.getTransform();
            g.setTransform(AffineTransform.getRotateInstance(Math.toRadians(rotation), x + 30, y + 30));
            g.drawImage(GameResource.getBlackHole(), x, y, null);
            g.setTransform(af);
        }
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public Ellipse2D getShape() {
        return new Ellipse2D.Double(x, y, 60, 60);
    }
    
    public void setBoss(boolean b) {
        this.boss = b;
    }
    
    public boolean isBoss() {
        return boss;
    }
    
    public int getState() {
        return state;
    }
    
    public int[] getFrom() {
        return from;
    }
    
    public int[] getLevelToComplete() {
        return levelToComplete;
    }
    
    public int[] getDirs() {
        return dirs;
    }
    
    public boolean isFinish() {
        return finish;
    }
    
    public void setBonus(boolean b) {
        this.bonus = b;
    }
    
    public void setShown(boolean shown) {
        this.shown = shown;
    }
    
    public boolean isShown() {
        return shown;
    }
    
    public void setFinalAccess(boolean finalAccess) {
        this.finalAccess = finalAccess;
    }
    
    public boolean isFinalAccess() {
        return this.finalAccess;
    }
    
    public void setAccessToWorld(int world) {
        this.world = world;
    }
    
    public int getWorldAccess() {
        return world;
    }
}