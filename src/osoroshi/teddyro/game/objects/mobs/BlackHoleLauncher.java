package osoroshi.teddyro.game.objects.mobs;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import osoroshi.teddyro.game.objects.MapElement;
import osoroshi.teddyro.game.states.GameLevel;
import osoroshi.teddyro.game.tilemap.TileMap;
import osoroshi.teddyro.game.utils.GameResource;
import osoroshi.teddyro.game.utils.JukeBox;

public class BlackHoleLauncher extends MapElement {
    
    private long time, totalTime, starsTime;
    private int flipCount, count = 0, pressedCount = 0, d, spaceBarWidth, spaceBarHeight, w, h, red;
    private boolean pressed = false, blackHoleLaunched = false, starsLaunched = false, looping = true;
    private double blackHoleX = 0, herodx = 0, r;
    private BufferedImage blackHole, spaceBar, redScreen;
    private ArrayList<BlackHoleImage> bhimages;
    private ArrayList<BlackParticle> particles = new ArrayList<>();
    
    public BlackHoleLauncher(GameLevel gl, TileMap tileMap, double x, double y, String params) {
        super(gl, tileMap, x, y, params);
        blackHole = GameResource.getBlackHoleSprites().get(0)[0];
        spaceBar = GameResource.getSpaceBar();
        spaceBarWidth = spaceBar.getWidth();
        spaceBarHeight = spaceBar.getHeight();
        w = blackHole.getWidth();
        h = blackHole.getHeight();
        bhimages = new ArrayList<>();
        redScreen = new BufferedImage(gl.getWidth(), gl.getHeight(), BufferedImage.TYPE_INT_RGB);
    }
    
    public void update() {
        gl.setCameraTween(1);
        for(int i = 0; i < bhimages.size(); i++) {
            bhimages.get(i).update();
        }
        count++;
        r = (r + 10) % 360;
        tileMap.hero.setCanAction(false);
        tileMap.hero.setDirX(1);
        if(System.currentTimeMillis() - time > 400 + new java.util.Random().nextInt(150)) {
            d = (pressedCount > 2 + ((new java.util.Random().nextBoolean()) ? ((new java.util.Random().nextInt(5) == 0) ? 1 : 0) : 0)) ? 1 : 0;
            pressedCount = 0;
            time = System.currentTimeMillis();
        }
        if(JukeBox.isRunning("festival")) {
            JukeBox.stop("festival");
            JukeBox.setLooping("catastrophe", 52575, 326634);
        }
        if(!blackHoleLaunched) {
            boolean solid = false;
            for(int xs = 0; xs > -gl.getWidth() / 2; xs -= 32) {
                for(int ht = 0; ht < tileMap.hero.height; ht++) {
                    if(tileMap.isSolid(tileMap.hero.getX() + xs, getY() + ht)) {
                        solid = true;
                    }
                }
            }
            totalTime = System.currentTimeMillis();
            if(!solid) {
                blackHoleLaunched = true;
                blackHoleX = 0;
                herodx = 0;
                totalTime = System.currentTimeMillis();
            }
        }
        if(blackHoleLaunched) {
            if(System.currentTimeMillis() - totalTime > 1000 && !tileMap.hero.isDead()) {
                blackHoleX = gl.getWidth() / 2 - tileMap.hero.width / 2 - blackHole.getWidth();
                herodx = 0;
                tileMap.hero.setDirX(0);
                if(!starsLaunched) {
                    starsLaunched = true;
                    starsTime = System.currentTimeMillis();
                    tileMap.hero.launchStars();
                }
                if(System.currentTimeMillis() - starsTime < 750) {
                    tileMap.hero.dx = 0;
                    tileMap.hero.setFlipped(true);
                }
                if(w > 0 && starsLaunched && System.currentTimeMillis() - starsTime > 750) {
                    w--;
                    h--;
                    blackHoleX += (90 - w) / 2.0;
                }
                else {
                    flipCount++;
                    red += 3;
                    if(red > 255) {
                        red = 255;
                        particles.add(new BlackParticle(Math.random() * (gl.getWidth() - 5)));
                    }
                    BufferedImage rs = new BufferedImage(gl.getWidth(), gl.getHeight(), BufferedImage.TYPE_INT_RGB);
                    Graphics2D g = rs.createGraphics();
                    g.setColor(new Color(red, 0, 0));
                    g.fillRect(0, 0, rs.getWidth(), rs.getHeight());
                    g.dispose();
                    tileMap.setBackground(rs, 0);
                    if(flipCount % (1000.0 / 25.0 / 2.0) == 0) {
                        if(flipCount > (1000.0 / 25.0 / 2.0) * 4) {
                            if(looping) {
                                looping = false;
                                JukeBox.cancelLooping("catastrophe");
                                int pos = JukeBox.getPosition("catastrophe");
                                
                            }
                        }
                        else if(flipCount < (1000.0 / 25 / 2) * 8) {
                            tileMap.hero.setFlipped(!tileMap.hero.isFlipped());
                        }
                    }
                }
            }
            else {
                tileMap.hero.setFlipped(false);
                blackHoleX += 6.075 + (Math.random() * 4.25);
                herodx += tileMap.hero.dx;
                double res = tileMap.hero.getX() - (tileMap.hero.getX() - gl.getWidth() / 2 + (int) (blackHoleX + blackHole.getWidth() / 2 - herodx));
                if(res < blackHole.getWidth() / 2) {
                    tileMap.hero.kill();
                }
            }
        }
        if(count < 5) return;
        count = 0;
        if(spaceBarWidth == spaceBar.getWidth()) {
            spaceBarWidth -= 10;
            spaceBarHeight -= 10;
        }
        else {
            spaceBarWidth = spaceBar.getWidth();
            spaceBarHeight = spaceBar.getHeight();
        }
    }
    
    public void paint(Graphics2D g) {
        for(int i = 0; i < bhimages.size(); i++) {
            AffineTransform af = g.getTransform();
            g.setTransform(AffineTransform.getRotateInstance(Math.toRadians(bhimages.get(i).getRotation()), bhimages.get(i).getX() + bhimages.get(i).getWidth() / 2 - tileMap.getX(), bhimages.get(i).getY() + bhimages.get(i).getHeight() / 2 - tileMap.getY()));
            g.drawImage(blackHole, (int) bhimages.get(i).getX() - tileMap.getX(), (int) bhimages.get(i).getY() - tileMap.getY(), bhimages.get(i).getWidth(), bhimages.get(i).getHeight(), null);
            g.setTransform(af);
        }
        for(int i = 0; i < particles.size(); i++) {
            g.setColor(Color.black);
            g.fillOval(particles.get(i).getX(), particles.get(i).getY(), 3, 3);
            particles.get(i).y -= 5;
            if(particles.get(i).getY() < 0) {
                particles.remove(i);
            }
        }
        if(System.currentTimeMillis() - totalTime < 10000) {
            g.drawImage(spaceBar, gl.getWidth() / 2 - spaceBarWidth / 2, gl.getHeight() / 4 * 3 - spaceBarHeight / 2, spaceBarWidth, spaceBarHeight, null);
            g.setColor(Color.black);
            g.setFont(new Font("Arial", Font.PLAIN, (spaceBarWidth == spaceBar.getWidth()) ? 20 : 18));
            TextLayout txt = new TextLayout("Space Bar", g.getFont(), g.getFontRenderContext());
            g.drawString("Space Bar", gl.getWidth() / 2 - (int) txt.getBounds().getWidth() / 2, gl.getHeight() / 4 * 3 - spaceBarHeight / 2 + (int) txt.getBounds().getHeight() * 2);
        }
        if(blackHoleLaunched && !tileMap.hero.isDead()) {
            AffineTransform af = g.getTransform();
            //g.setTransform(AffineTransform.getRotateInstance(Math.toRadians(r), tileMap.hero.getX() + tileMap.hero.width / 2 - gl.getWidth() / 2 + (blackHoleX - herodx) + w / 2 - tileMap.getX(), tileMap.hero.getY() + tileMap.hero.height / 2 - tileMap.getY()));
            g.drawImage(blackHole, (int) tileMap.hero.getX() + tileMap.hero.width / 2 - gl.getWidth() / 2 + (int) (blackHoleX - herodx) - tileMap.getX(), (int) tileMap.hero.getY() + tileMap.hero.height / 2 - h / 2 - tileMap.getY(), w, h, null);
            g.setTransform(af);
        }
    }
    
    public void keyPressed(int keyCode) {
        if(!pressed) {
            pressed = true;
            if(keyCode == KeyEvent.VK_SPACE) {
                pressedCount++;
            }
        }
    }
    
    public void keyReleased() {
        pressed = false;
    }
    
    private class BlackParticle {
        
        private double x, y = gl.getHeight();
        
        public BlackParticle(double x) {
            this.x = x;
        }
        
        public int getX() {
            return (int) x;
        }
        
        public int getY() {
            return (int) y;
        }
    }
    
    private class BlackHoleImage {
        
        private double x, y, xp, yp, ratio, width, height, r;
        private int pos, deadCount, count, totalCount;
        
        public BlackHoleImage(double x, double y, int pos) {
            this.x = x;
            this.y = y;
            this.pos = pos;
            ratio = 90.0 / (326634.0 - pos);
        }
        
        public void update() {
            r += 5 + Math.random() * 5;
            r = r % 360;
            width = height = (JukeBox.getPosition("catastrophe") - pos) * ratio;
            if(JukeBox.getPosition("catastrophe") > 326634) {
                width = 90;
                height = 90;
            }
            if(JukeBox.getPosition("catastrophe") > 670000) {
                if(xp == 0 && yp == 0) {
                    xp = (getNearestX() - x) / 5.0;
                    yp = (getNearestY() - y) / 5.0;
                }
            }
            if((xp != 0 || yp != 0) && count != 5) {
                this.x += xp;
                this.y += yp;
                count++;
            }
            if(count == 5) {
                JukeBox.play("death");
                deadCount++;
                if(deadCount == 10) {
                    xp = (tileMap.hero.getX() + tileMap.hero.width / 2 - x) / 5.0;
                    yp = (tileMap.hero.getY() + tileMap.hero.height / 2 - y) / 5.0;
                    count = 0;
                }
            }
            if(x == tileMap.hero.getX() + tileMap.hero.width / 2 && y == tileMap.hero.getY() + tileMap.hero.height / 2) {
                totalCount++;
                if(totalCount == 60) {
                    gl.setState(8);
                }
            }
        }
        
        public double getNearestX() {
            double nearestX = x;
            if(x < tileMap.hero.getX()) {
                nearestX = tileMap.hero.getX() - width / 2;
            }
            if(x > tileMap.hero.getX() + tileMap.hero.width) {
                nearestX = tileMap.hero.getX() + tileMap.hero.width + width / 2;
            }
            return nearestX;
        }
        
        public double getNearestY() {
            double nearestY = y;
            if(y < tileMap.hero.getY()) {
                nearestY = tileMap.hero.getY() - height / 2;
            }
            if(y > tileMap.hero.getY() + tileMap.hero.height) {
                nearestY = tileMap.hero.getY() + tileMap.hero.height + height / 2;
            }
            return nearestY;
        }
        
        public double getX() {
            return x - width / 2.0;
        }
        
        public double getY() {
            return y - height / 2.0;
        }
        
        public int getWidth() {
            return (int) width;
        }
        
        public int getHeight() {
            return (int) height;
        }
        
        public double getRotation() {
            return r;
        }
    }
}