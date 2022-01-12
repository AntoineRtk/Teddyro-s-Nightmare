
package osoroshi.teddyro.game.states;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import osoroshi.teddyro.game.utils.Controler;
import osoroshi.teddyro.game.utils.GameResource;
import osoroshi.teddyro.game.utils.JukeBox;

public class MiniGame1 extends GameState {
    
    private BufferedImage[] images = new BufferedImage[2];
    private double bx, x, y, speed;
    private int count;
    private int[][][] tiles = new int[23*3][][];
    
    public MiniGame1(GameStateManager gsm) {
        super(gsm);
    }
    
    public void init() {
        JukeBox.play("minigame1");
        images[0] = GameResource.getHeroSprites().get(0)[0];
        images[1] = GameResource.getHeroSprites().get(10)[0];
        count = -1;
        for(int i = 0; i < tiles.length; i++) {
            tiles[i] = new int[][]{{0, getHeight() / 2 + 41 / 2}};
        }
    }
    
    public void update() {
        if(count == -1) {
            bx += 2;
            if(Controler.isSpace()) {
                count = 0;
                bx = 0;
                y = getHeight() / 2.0 - 41 / 2.0 - 19;
            }
        }
        else {
            x += speed;
            if(count < 90) {
                x += 10.85 / 90.0 * (count + 1);
                if(count == 89) {
                    y = getHeight() / 2.0 - 41 / 2.0;
                    speed = 10.85;
                }
            }
            bx = x + 45 / 2.0 - getWidth() / 4.0;
            if(bx < 0) bx = 0;
            for(int i = 0; i < tiles.length; i++) {
                for(int i1 = 0; i1 < tiles[i].length; i1++) {
                    if(i * 32 - (int) (bx % (tiles.length * 2.0/3.0 * 32)) <= -32) {
                        
                    }
                }
            }
            count++;
        }
    }
    
    public void paint(Graphics2D g) {
        BufferedImage image = GameResource.getBackground(0);
        double rx = (bx % image.getWidth());
        for(int i = 0; i < getWidth() / image.getWidth() + 2; i++) {
            g.drawImage(image, (int) (i * image.getWidth() - rx), 0, image.getWidth(), getHeight(), null);
        }
        if(count == -1) {
            g.drawImage(GameResource.getSpaceBar(), getWidth() / 2 - GameResource.getSpaceBar().getWidth() / 2, getHeight() / 2 - GameResource.getSpaceBar().getHeight() / 2, null);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            TextLayout txt = new TextLayout("Space Bar", g.getFont(), g.getFontRenderContext());
            g.drawString("Space Bar", getWidth() / 2 - (int) txt.getBounds().getX() - (int) txt.getBounds().getWidth() / 2, getHeight() / 2 - (int) txt.getBounds().getY() - (int) txt.getBounds().getHeight() / 2);
            g.setFont(new Font("Arial", Font.PLAIN, 30));
            txt = new TextLayout("Appuyez sur la barre d'espace pour commencer", g.getFont(), g.getFontRenderContext());
            g.drawString("Appuyez sur la barre d'espace pour commencer", getWidth() / 2 - (int) txt.getBounds().getX() - (int) txt.getBounds().getWidth() / 2, getHeight() / 2 - GameResource.getSpaceBar().getHeight() / 2 - (int) txt.getBounds().getY() - (int) txt.getBounds().getHeight());
        }
        else {
            AffineTransform af = g.getTransform();
            if(count >= 90) g.setTransform(AffineTransform.getRotateInstance(Math.toRadians(count * 12), x - bx + images[1].getWidth() / 2.0, y + images[1].getHeight() / 2.0));
            g.translate(x - bx, y);
            g.drawImage(count >= 90 ? images[1] : images[0], 0, 0, null);
            g.translate(-x + bx, -y);
            g.setTransform(af);
            for(int i = 0; i < tiles.length; i++) {
                for(int i1 = 0; i1 < tiles[i].length; i1++) {
                    g.drawImage(GameResource.getMinigame1Tile(tiles[i][i1][0]), i * 32 - (int) (bx % (tiles.length * 2.0/3.0 * 32)), tiles[i][i1][1], null);
                }
            }
        }
    }
}