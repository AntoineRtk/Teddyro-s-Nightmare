package osoroshi.teddyro.game.objects;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import osoroshi.teddyro.game.states.GameLevel;
import osoroshi.teddyro.game.tilemap.TileMap;
import osoroshi.teddyro.game.utils.GameResource;
import osoroshi.teddyro.game.utils.JukeBox;

public class Launcher extends MapElement {
    
    private GeneralPath arrow;
    private double[][][] locations;
    private int count = 0, turn;
    private double gy = 32;
    private boolean heroInLauncher = false;
    
    public Launcher(GameLevel gl, TileMap tileMap, double x, double y, String params) {
        super(gl, tileMap, x, y, params);
        BufferedImage image = new BufferedImage(64, 32, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        arrow = new GeneralPath();
        arrow.moveTo(0, 32);
        arrow.lineTo(32, 0);
        arrow.lineTo(64, 32);
        arrow.lineTo(32, 16);
        arrow.closePath();
        g.setColor(Color.blue);
        g.fill(arrow);
        g.fillRect(63, 0, 1, 1);
        animation.setFrames(new BufferedImage[]{image});
        width = 64;
        height = 32;
        if(params == null) return;
        locations = new double[params.split("/").length][][];
        for(int i = 0; i < params.split("/").length; i++) {
            String location = params.split("/")[i];
            double lx = (i > 0) ? Double.parseDouble(params.split("/")[i - 1].split("-")[0]) : getX() + 32;
            double ly = (i > 0) ? Double.parseDouble(params.split("/")[i - 1].split("-")[1]) : getY() + 16;
            double xx = Double.parseDouble(location.split("-")[0]);
            double yy = Double.parseDouble(location.split("-")[1]);
            double dist = Math.sqrt(Math.abs((xx - lx) * (xx - lx) + (yy - ly) * (yy - ly)));
            if(i == params.split("/").length - 1) {
                double centerX = (xx + lx) / 2.0, centerY = (yy + ly) / 2.0,
                    radius = Math.max(Math.abs(centerX - xx), Math.abs(centerY - yy));
                locations[i] = new double[180 / 4][];
                for(int a = 0; a < 180 / 4; a++) {
                    locations[i][a] = new double[]{centerX + Math.cos(a * 4 * Math.PI / 180.0) * radius, centerY - Math.sin(a * 4 * Math.PI / 180.0) * radius};
                }
                locations[i][44] = new double[]{xx, yy};
            }
            else {
                double blocks = dist / 16.0;
                locations[i] = new double[(int) blocks][];
                double distx = (xx - lx) / blocks, disty = (yy - ly) / blocks;
                for(int p = 0; p < (int) blocks; p++) {
                    locations[i][p] = new double[]{lx + distx * p, ly + disty * p};
                }
            }
        }
    }
    
    public void update() {
        super.checkCollisions();
        if((int) gy <= -5) {
            gy = 27;
        }
        gy -= 0.3;
        if(heroInLauncher) {
            tileMap.hero.setCanAction(false);
            tileMap.hero.setX(locations[turn][count][0] - tileMap.hero.width / 2.0);
            tileMap.hero.setY(locations[turn][count][1] - tileMap.hero.height / 2.0);
            tileMap.hero.maxFallingSpeed = 0;
            tileMap.hero.setFlipped(locations[turn][locations[turn].length - 1][0] < locations[turn][0][0]);
            count++;
            if(count >= locations[turn].length) {
                turn++;
                count = 0;
                if(turn >= locations.length) {
                    heroInLauncher = false;
                    tileMap.hero.reinitValues();
                }
            }
        }
    }
    
    public void intersect(MapObject mo) {
        if(mo instanceof Hero && !heroInLauncher) {
            heroInLauncher = true;
            count = 0;
            turn = 0;
        }
    }
    
    public void paint(Graphics2D g) {
        super.paint(g);
        Color cyan = Color.cyan, white = Color.white;
        double redRatio = (white.getRed() - cyan.getRed()) / 4.0;
        double greenRatio = (white.getGreen() - cyan.getGreen()) / 4.0;
        double blueRatio = (white.getBlue() - cyan.getBlue()) / 4.0;
        Shape s = g.getClip();
        g.setClip(arrow.createTransformedShape(AffineTransform.getTranslateInstance(getX() - tileMap.getX(), getY() - tileMap.getY())));
        for(int i = 0; i < 5; i++) {
            int py = (int) (getY() - tileMap.getY() + gy - i);
            int pfy = (int) (getY() - tileMap.getY() + gy + i);
            if(gy - i < 0) py = (int) (getY() + 32 - tileMap.getY() + gy - i);
            if(gy + i < 0) pfy = (int) (getY() + 32 - tileMap.getY() + gy + i);
            Color c = new Color(cyan.getRed() + (int) (redRatio * i), cyan.getGreen() + (int) (greenRatio * i), cyan.getBlue() + (int) (blueRatio * i), 255 / 2);
            g.setColor(c);
            g.drawLine((int) getX() - tileMap.getX(), py, (int) getX() - tileMap.getX() + width, py);
            g.drawLine((int) getX() - tileMap.getX(), pfy, (int) getX() - tileMap.getX() + width, pfy);
        }
        g.setClip(s);
        if(getX() == 6272) {
            g.setColor(new Color(0, 255, 0, alpha));
            double[][] pos1 = locations[locations.length - 2];
            double[][] pos2 = locations[locations.length - 1];
            double centerX = (pos2[pos2.length - 1][0] + pos1[pos1.length - 1][0]) / 2.0, centerY = (pos2[pos2.length - 1][1] + pos1[pos1.length - 1][1]) / 2.0;
            if(alpha != 255) g.fillRect((int) centerX - tileMap.getX(), (int) centerY - tileMap.getY(), 1, 1);
            g.drawOval((int) centerX - 176 - tileMap.getX(), (int) centerY - 176 - tileMap.getY(), 176*2, 176*2);
            g.setFont(new Font("Arial", Font.ITALIC, 20));
            TextLayout txt1 = new TextLayout("X' = cx + r * cos(θ) - Y' = cy - r * sin(θ)", g.getFont(), g.getFontRenderContext());
            g.drawString("X' = cx + r * cos(θ) - Y' = cy - r * sin(θ)", (int) (centerX - txt1.getBounds().getWidth() / 2.0) - tileMap.getX(), (int) (centerY - txt1.getBounds().getHeight() / 2.0) - tileMap.getY());
            g.setFont(new Font("Arial", Font.ITALIC, 25));
            TextLayout txt2 = new TextLayout("LOVE MATHEMATICS", g.getFont(), g.getFontRenderContext());
            g.setColor(new Color(255, 0, 0, alpha));
            g.drawString("LOVE MATHEMATICS", (int) (centerX - txt2.getBounds().getWidth() / 2.0) - tileMap.getX(), (int) (centerY + txt2.getBounds().getHeight() * 2.0) - tileMap.getY());
            if(GameResource.getX() + tileMap.getX() > centerX - 5 && GameResource.getX() + tileMap.getX() < centerX + 5 && GameResource.getY() + tileMap.getY() > centerY - 5 && GameResource.getY() + tileMap.getY() < centerY + 5 && alpha != 255) {
                alpha = 255;
                JukeBox.play("secretdiscovered");
            }
        }
    }
    
    private int alpha = (int) (10.0*25.5) - 247;
}