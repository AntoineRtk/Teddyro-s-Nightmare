package osoroshi.teddyro.game.objects.mobs.boss;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import osoroshi.teddyro.game.animations.DustExplosion;
import osoroshi.teddyro.game.objects.BlackScreen;
import osoroshi.teddyro.game.objects.Flower;
import osoroshi.teddyro.game.objects.Hero;
import osoroshi.teddyro.game.objects.MapElement;
import osoroshi.teddyro.game.objects.MapObject;
import osoroshi.teddyro.game.objects.Stone;
import osoroshi.teddyro.game.objects.mobs.Monster;
import osoroshi.teddyro.game.objects.platforms.InvisiblePlatform;
import osoroshi.teddyro.game.objects.platforms.SwitchEvent;
import osoroshi.teddyro.game.states.GameLevel;
import osoroshi.teddyro.game.tilemap.TileMap;
import osoroshi.teddyro.game.utils.GameResource;
import osoroshi.teddyro.game.utils.JukeBox;

public class Snado extends Monster {
    
    private boolean sleeping = true, hasJumped;
    private int z, zcount, e, emoteCount, maxy, act = 0,
            xp = 0, pcount, count = 0, life = 3;
    private InvisiblePlatform left, right;
    private double o;
    private Flower flower;
    
    public Snado(GameLevel gl, TileMap tileMap, double x, double y, String params) {
        super(gl, tileMap, x, y, 10, 10, 10, false, -1);
        animation.setFrames(GameResource.getSnadoSprites().get(0));
        fallingSpeed = 1.75;
        maxFallingSpeed = 10.5;
        moveSpeed = 0.12;
        maxMoveSpeed = 4.35;
        jumpStart = -12;
        width = 330;
        height = 300;
        if(params != null) {
            sleeping = false;
            e = 2;
            act = 6;
            pcount = 26;
            maxy = -1;
            life = 1;
        }
    }
    
    public void update() {
        if(left != null) {
            tileMap.removePlatform(left);
            tileMap.removePlatform(right);
        }
        if(isJumping()) {
            hasJumped = true;
        }
        super.update();
        if(getDirX() != 0) {
            setFlipped(getDirX() > -1);
        }
        if(dy == 0 && !isFalling() && hasJumped) {
            tileMap.setShaking(true, 121);
            hasJumped = false;
        }
        if(tileMap.isShaking()) {
            if(tileMap.getIntensity() > 0) {
                tileMap.setShaking(true, tileMap.getIntensity() - 1);
            }
            else {
                tileMap.setShaking(false, 0);
            }
        }
        if(left != null && act > 1) {
            tileMap.addPlatform(left);
            tileMap.addPlatform(right);
            left.setX(getX() - 1);
            left.setY(getY());
            right.setX(getX() + width);
            right.setY(getY());
        }
        if(!JukeBox.isRunning("boss") && e == 2 && act != 16) {
            JukeBox.stopAll();
            JukeBox.play(10);
        }
        if(dx != 0) {
            animation.setDelay((int) (6.0 - (3.75 / maxMoveSpeed * Math.abs(dx))));
        }
        else {
            animation.setDelay(0);
            animation.setFrame(0);
        }
        if(maxy == 0 && act == 0) {
            int i = 32;
            for(;;) {
                if(tileMap.isSolid(getX(), getY() + height - 1 + i)) {
                    maxy = (int) ((getY() + height - 1 + i) / 32.0) * 32 + 128 - gl.getHeight();
                    break;
                }
                i += 32;
            }
        }
        if(maxy != 0 && maxy != -1) {
            tileMap.setMaxY(maxy);
        }
        zcount++;
        if(zcount >= 10 && sleeping) {
            zcount = 0;
            z++;
            if(z > 2) z = 0;
        }
        emoteCount++;
        if((emoteCount == 60 || emoteCount == 90) && !sleeping) {
            if(e == 0 && emoteCount == 60) {
                setFlipped(true);
            }
            else if(e == 0 && emoteCount == 90) {
                e = 1;
                emoteCount = 0;
            }
            else if(e == 1 && emoteCount == 90) {
                for(int i = 0; i < tileMap.objects[area].size(); i++) {
                    if(!(tileMap.objects[area].get(i) instanceof BlackScreen)) {
                        tileMap.objects[area].remove(i);
                    }
                }
                e = 2;
                setDirX(-1);
                left = new InvisiblePlatform(gl, tileMap, getX(), getY(), 1, height) {
                    public boolean intersectX(MapObject mo, int dir) {
                        if(!(mo instanceof Hero)) return true;
                        mo.dx = -Math.abs(mo.dx) * 2.0;
                        pcount = 0;
                        return false;
                    }
                };
                right = new InvisiblePlatform(gl, tileMap, getX() + width - 1, getY(), 1, height) {
                    public boolean intersectX(MapObject mo, int dir) {
                        if(!(mo instanceof Hero)) return true;
                        mo.dx = Math.abs(mo.dx) * 2.0;
                        pcount = 0;
                        return false;
                    }
                };
            }
        }
        if(e == 2) {
            switch(act) {
                case 0:
                    gl.setCamera(this);
                    if(xp == 0) {
                        for(int i = (int) (getY() / 32.0) * 32; i > 0; i -= 32) {
                            if(tileMap.isSolid(getX(), i)) {
                                xp = (int) (getX() / 32.0) * 32;
                            }
                        }
                    }
                    else {
                        double nextDx = ((Math.abs(dx - moveSpeed) > maxMoveSpeed ? -maxMoveSpeed : dx - moveSpeed));
                        double nextX = getX() + width / 2.0 + nextDx;
                        if(nextX < xp && !isFalling()) {
                            addXImpulse(xp - getX() - width / 2.0 + moveSpeed);
                            setDirX(0);
                            setJumping(true);
                            dx = -moveSpeed * 2;
                        }
                    }
                    break;
                case 1:
                    setDirX(-1);
                    break;
                case 2:
                    gl.setCamera(tileMap.hero);
                    gl.setCameraTween(0.1);
                    if(gl.getCameraXAdj() == 0) {
                        tileMap.hero.setCanAction(true);
                        act++;
                        setDirX(0);
                    }
                    break;
                case 3:
                    setDirX(0);
                    setFlipped(true);
                    count++;
                    if(count == 1) {
                        setJumping(true);
                    }
                    gl.setCameraTween(1);
                    int areaw = (int) (tileMap.getMapWidth() - (getX() + width) - 62);
                    if(count > 1 && !isFalling()) {
                        if(life == 3) {
                            for(int i = 0; i < 5; i++) {
                                tileMap.addObject(new Stone(gl, tileMap, getX() + width + Math.random() * areaw, 0, (int) (i * 15)));
                            }
                            for(int i = 0; i < 5; i++) {
                                tileMap.addObject(new Stone(gl, tileMap, getX() + width + Math.random() * areaw, 0, 50 + (int) (i * 12)));
                            }
                            for(int i = 0; i < 10; i++) {
                                tileMap.addObject(new Stone(gl, tileMap, getX() + width + Math.random() * areaw, 0, 90 + (int) (i * 9.45)));
                            }
                            for(int i = 0; i < 5 + new java.util.Random().nextInt(6); i++) {
                                tileMap.addObject(new Stone(gl, tileMap, getX() + width + Math.random() * areaw, 0, 160 + (int) (i * 7.5)));
                            }
                        }
                        else {
                            for(int i = 0; i < 20; i++) {
                                tileMap.addObject(new Stone(gl, tileMap, getX() + width + Math.random() * areaw, 0, (int) (i * 8.25)));
                            }
                            for(int i = 0; i < 17 + new java.util.Random().nextInt(4); i++) {
                                tileMap.addObject(new Stone(gl, tileMap, getX() + width + Math.random() * areaw, 0, 130 + (int) (i * 7.5)));
                            }
                        }
                        JukeBox.play("fallingrocks");
                        act++;
                    }
                    break;
                case 4:
                    boolean a = false;
                    for(int i = 0; i < tileMap.objects[area].size(); i++) {
                        if(tileMap.objects[area].get(i) instanceof DustExplosion || tileMap.objects[area].get(i) instanceof Stone) {
                            a = true;
                        }
                    }
                    if(!a) {
                        tileMap.addPlatform(new SwitchEvent(gl, tileMap, getX() + width + 1, tileMap.getY() - 24, "7"));
                        act = -1;
                    }
                    break;
                case 5:
                    count++;
                    tileMap.setMaxY((int) (getY() + height - gl.getHeight() + 128));
                    if(count == 1) {
                        setJumping(true);
                    }
                    break;
                case 6:
                    if(o == 0) {
                        
                    }
                    count++;
                    o = 1.7 * count;
                    if(count == 150) {
                        o = 255;
                        act++;
                        count = 0;
                    }
                    break;
                case 7:
                    count++;
                    tileMap.setInTheDark(true, 25, 300);
                    o = 255.0 - 4.25 * count;
                    if(count == 60) {
                        o = 0;
                        act++;
                        count = 0;
                    }
                    break;
                case 8:
                    if(!tileMap.isShaking()) {
                        setJumping(true);
                    }
                    else {
                        setJumping(false);
                        for(int i = 0; i < 7; i++) {
                            tileMap.addObject(new Stone(gl, tileMap, 32 + Math.random() * (getX() - 62), 0, 120 + (int) (i * 15)));
                        }
                        for(int i = 0; i < 7; i++) {
                            tileMap.addObject(new Stone(gl, tileMap, 32 + Math.random() * (getX() - 62), 0, 210 + (int) (i * 12)));
                        }
                        for(int i = 0; i < 12; i++) {
                            tileMap.addObject(new Stone(gl, tileMap, 32 + Math.random() * (getX() - 62), 0, 282 + (int) (i * 10.8)));
                        }
                        for(int i = 0; i < 7 + new java.util.Random().nextInt(6); i++) {
                            tileMap.addObject(new Stone(gl, tileMap, 32 + Math.random() * (getX() - 62), 0, 402 + (int) (i * 9.6)));
                        }
                        for(int i = 0; i < 5 + new java.util.Random().nextInt(11); i++) {
                            tileMap.addObject(new Stone(gl, tileMap, 32 + Math.random() * (getX() - 62), 0, 480 + (int) (i * 10.2)));
                        }
                        JukeBox.play("fallingrocks");
                        act++;
                    }
                    break;
                case 9:
                    a = false;
                    for(int i = 0; i < tileMap.objects[area].size(); i++) {
                        if(tileMap.objects[area].get(i) instanceof DustExplosion || tileMap.objects[area].get(i) instanceof Stone) {
                            a = true;
                        }
                    }
                    if(!a) {
                        if(count < 60 && tileMap.isInTheDark()) {
                            tileMap.setInTheDark(true, 25, 300 + (int) (2.5 * (count + 1)));
                        }
                        else {
                            if(count >= 75 && count < 135 && tileMap.isInTheDark()) {
                                tileMap.setInTheDark(true, 25, 450 - (int) (2.5 * (count - 74)));
                            }
                            else if(count >= 135 && tileMap.isInTheDark()) {
                                o = 4.25 * (count - 135);
                                if(count == 195) {
                                    tileMap.setInTheDark(false, 0, 0);
                                }
                            }
                        }
                        count++;
                        if(!tileMap.isInTheDark()) {
                            o -= 5.1;
                            if(o < 0) {
                                o = 0;
                                act++;
                                count = 0;
                            }
                        }
                    }
                    else {
                        if(count < 60) {
                            tileMap.setInTheDark(true, 25, (int) (300 + 2.5 * (count + 1)));
                        }
                        else {
                            if(count >= 75 && count < 135) {
                                tileMap.setInTheDark(true, 25, 450 - (int) (2.5 * (count - 74)));
                            }
                            else if(count == 135) count = 0;
                        }
                        count++;
                    }
                    break;
                case 10:
                    if(count > 0) count++;
                    if(count == 0) {
                        height--;
                        y++;
                    }
                    if(height == 240) {
                        height = 300;
                        y -= 60;
                        count = 1;
                        fallingSpeed = 0;
                        jumpStart = 0;
                        setJumping(false);
                    }
                    if(count > 0) setY(1078.0 - 1078.0 / 10.0 * count);
                    if(count == 10) {
                        act++;
                        fallingSpeed = 1.75;
                        jumpStart = -12;
                        setX(tileMap.hero.getX() - width / 2.0 + tileMap.hero.width / 2.0);
                        if(getX() < 32) setX(32);
                        if(getX() + width > tileMap.getMapWidth() - 32) setX(tileMap.getMapWidth() - 32 - width);
                    }
                    break;
                case 11:
                    double dist = tileMap.hero.getX() + tileMap.hero.width / 2.0 - (getX() + width / 2.0);
                    if(dist > 0) setDirX(1);
                    else if(dist < 0) setDirX(-1);
                    else setDirX(0);
                    if(getDirX() != 0 && Math.abs(dist) < (Math.abs(dx) + 0.12 <= maxMoveSpeed ? Math.abs(dx) + moveSpeed : maxMoveSpeed)) {
                        dx = dist*2;
                        moveSpeed = Math.abs(dist);
                        maxMoveSpeed = moveSpeed;
                        setDirX(0);
                    }
                    else if(dist != 0) {
                        moveSpeed = 0.12;
                        maxMoveSpeed = 4.35;
                    }
                    if(!isFalling()) {
                        moveSpeed = 0.6;
                        maxMoveSpeed = 4.35;
                        jumpStart = -12;
                        fallingSpeed = 1.75;
                        count = 0;
                        setDirX(0);
                        act++;
                    }
                    break;
                case 12:
                    if(getX() < tileMap.hero.getX()) setDirX(-1);
                    else setDirX(1);
                    if(hasIntersectedInX()) {
                        if(getDirX() == 1) tileMap.addPlatform(new SwitchEvent(gl, tileMap, getX() - 32, tileMap.getY() - 24, "7"));
                        else tileMap.addPlatform(new SwitchEvent(gl, tileMap, getX() + width, tileMap.getY() - 24, "7"));
                        setDirX(0);
                        setFlipped(!isFlipped());
                        act++;
                    }
                    break;
                case 13:
                    if(life != 0) return;
                    tileMap.hero.setCanAction(false);
                    tileMap.hero.setFlipped(getX() < tileMap.hero.getX());
                    if(count > 60) {
                        int nh = 420 - 2 * count;
                        if(nh == 0) {
                            act++;
                            nh = 1;
                            tileMap.hero.setCanAction(true);
                        }
                        else {
                            for(int i = 0; i < width; i += 10) {
                                tileMap.addObject(new Sand(gl, tileMap, getX() + i, getY() + height - 3, getImage().getSubimage(i, height - 2, 4, 2), i > width / 2 ? 1 : -1));
                            }
                            JukeBox.play("sand");
                        }
                        BufferedImage newSnado = new BufferedImage(width, nh, BufferedImage.TYPE_INT_ARGB);
                        Graphics2D gn = newSnado.createGraphics();
                        gn.drawImage(GameResource.getSnadoSprites().get(0)[0], 0, 0, null);
                        gn.dispose();
                        height = nh;
                        y += 2;
                    }
                    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g = image.createGraphics();
                    g.drawImage(getImage(), 0, 0, null);
                    g.dispose();
                    for(int h = 0; h < image.getHeight(); h++) {
                        for(int w = 0; w < image.getWidth(); w++) {
                            Color color = new Color(image.getRGB(w, h));
                            Color snadoColor = new Color(GameResource.getSnadoSprites().get(0)[0].getRGB(w, h));
                            if(color.getRed() + color.getGreen() + color.getBlue() != 0) {
                                int mid = (int) ((color.getRed() + color.getGreen() + color.getBlue()) / 3.0);
                                int c = count;
                                if(count > 60) count = 60;
                                image.setRGB(w, h, new Color(snadoColor.getRed() + (int) (((mid - snadoColor.getRed()) / 60.0) * count), snadoColor.getGreen() + (int) (((mid - snadoColor.getGreen()) / 60.0) * count), snadoColor.getBlue() + (int) (((mid - snadoColor.getBlue()) / 60.0) * count)).getRGB());
                                count = c;
                            }
                        }
                    }
                    count++;
                    animation.setFrames(new BufferedImage[]{image});
                    break;
                case 14:
                    height = 0;
                    if(flower == null) {
                        flower = new Flower(gl, tileMap, tileMap.getMapWidth() / 2.0 - 22.5, tileMap.getY() - 38);
                        tileMap.addObject(flower);
                    }
                    if(flower.getY() < tileMap.getMapHeight() - 198) {
                        flower.setY(flower.getY() + 2.8);
                    }
                    else {
                        flower.setY(tileMap.getMapHeight() - 198);
                    }
                    break;
            }
        }
        pcount++;
        setDamage(5);
    }
    
    public void xAxisVerification() {
        boolean a = false;
        if(act == 1) {
            for(int i = 0; i < blocksHeight * 32; i += 32) {
                if(tileMap.isSolid(getX() + xIncreasing, getY() + i)) {
                    if(a) {
                        act = 2;
                        return;
                    }
                    BufferedImage image = tileMap.getTile(getX() + xIncreasing, getY() + i).getImage();
                    tileMap.setTile(GameResource.getTile(0), (getX() + xIncreasing) / 32.0, (getY() + i) / 32.0);
                    for(int i1 = 0; i1 < 8; i1++) {
                        for(int i2 = 0; i2 < 8; i2++) {
                            tileMap.addObject(new DustExplosion(gl, tileMap, image.getSubimage(i1 * 4, i2 * 4, 4, 4), (int) ((getX() + dx) / 32.0) * 32 + 31, (int) ((getY() + i) / 32.0) * 32 + i2 * 4, false, 4, 4));
                        }
                    }
                    a = true;
                }
            }
            JukeBox.play("brick");
        }
    }
    
    public void yAxisVerification() {
        if(getX() + width / 2.0 == xp && act == 0) {
            int blocks = (int) ((getX() + width - 1) / 32.0) - (int) (getX() / 32.0) + 1;
            boolean s = false;
            for(int bx = 0; bx < blocks * 32; bx += 32) {
                if(hasIntersectedInY() && tileMap.isSolid(getX() + bx, getY() - 1)) {
                    BufferedImage image = tileMap.getTile(getX() + bx, getY() - 1).getImage();
                    tileMap.setTile(GameResource.getTile(0), (getX() + bx) / 32.0, (getY() - 1) / 32.0);
                    for(int i = 0; i < 8; i++) {
                        for(int i1 = 0; i1 < 8; i1++) {
                            tileMap.addObject(new DustExplosion(gl, tileMap, image.getSubimage(i * 4, i1 * 4, 4, 4), (int) ((getX() + bx) / 32.0) * 32 + i * 4, (int) ((getY() - 1) / 32.0) * 32 + i1 * 4, false, 4, 4));
                        }
                    }
                    s = true;
                }
            }
            if(s) {
                xp = 0;
                JukeBox.play("brick");
                act = 1;
            }
        }
        if(act == 5 && dy > 0) {
            int wb = (int) ((getX() + width - 1) / 32.0) - (int) (getX() / 32.0) + 1;
            for(int i = 0; i < wb * 32; i += 32) {
                if((getY() + height) > tileMap.getMapHeight() - 32) break;
                if(tileMap.isSolid(getX() + i, getY() + height)) {
                    BufferedImage image = tileMap.getTile(getX() + i, getY() + height).getImage();
                    tileMap.setTile(GameResource.getTile(0), (getX() + i) / 32.0, (getY() + height) / 32.0);
                    for(int i1 = 0; i1 < 2; i1++) {
                        for(int i2 = 0; i2 < 2; i2++) {
                            tileMap.addObject(new DustExplosion(gl, tileMap, image.getSubimage(i1 * 16, i2 * 16, 16, 16), (int) ((getX() + i) / 32.0) * 32 + i1 * 16, (int) ((getY() + height) / 32.0) * 32 + i2 * 16, false, 4, 4));
                        }
                    }
                }
            }
        }
    }
    
    public void paint(Graphics2D g) {
        super.paint(g);
        if(sleeping) {
            double xp = 0, yp = height / 10.0 * 8.0;
            g.setColor(Color.black);
            for(int i = 0; i < 3; i++) {
                g.setFont(new Font("Gabriola", Font.PLAIN, 30 + 10 * i));
                TextLayout txt = new TextLayout("Z", g.getFont(), g.getFontRenderContext());
                if(i == z) g.drawString("Z", (int) (getX() + xp - txt.getBounds().getWidth()) - tileMap.getX(), (int) (getY() + yp) - tileMap.getY());
                xp -= txt.getBounds().getWidth() / 2.0;
                yp -= txt.getBounds().getHeight();
            }
        }
        else if(!sleeping && ((emoteCount < 100 && e == 0) || (emoteCount < 75 && e == 1))) {
            if(e == 0) {
                if(isFlipped()) g.drawImage(GameResource.getEmote1(), (int) getX() + width - tileMap.getX(), (int) getY() + (int) (height / 10.0 * 8.0) - tileMap.getY() - 77, 60, 77, null);
                else {
                    BufferedImage flipped = new BufferedImage(14, 18, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D fg = flipped.createGraphics();
                    fg.drawImage(GameResource.getEmote1().getSubimage(0, 0, 14, 14), 0, 0, null);
                    fg.drawImage(GameResource.getEmote1().getSubimage(5, 14, 4, 4), 9, 14, -4, 4, null);
                    fg.dispose();
                    g.drawImage(flipped, (int) getX() - tileMap.getX() - 60, (int) getY() + (int) (height / 10.0 * 8.0) - tileMap.getY() - 77, 60, 77, null);
                }
            }
            else if(e == 1 && emoteCount < 75) {
                g.drawImage(GameResource.getEmote2(), (int) getX() + width - tileMap.getX(), (int) getY() + (int) (height / 10.0 * 8.0) - tileMap.getY() - 77, 60, 77, null);
            }
        }
        else {
            if(pcount <= 25) {
                int size = width - (int) (width / 24.0 * (pcount - 1));
                int startX = (int) (width / 2.0 - 0.5 * size);
                int ovalWidth = width - startX * 2;
                g.setColor(new Color(150 + 105 - (int) (105.0 / width * size), 255, 255));
                g.fillOval((int) getX() - tileMap.getX() + startX, (int) getY() - tileMap.getY(), ovalWidth, height);
            }
        }
        if(act == 11) {
            g.setColor(Color.black);
            int yomber = 0;
            for(int i = (int) getY(); i < tileMap.getMapHeight(); i += 32) {
                if(tileMap.isSolid(getX(), i)) {
                    yomber = (int) (i / 32.0) * 32;
                    break;
                }
            }
            int w = (int) (1.0 * width / (yomber - height) * (getY() + height - height));
            g.fillRect((int) getX() - tileMap.getX() + width / 2 - w / 2, yomber - tileMap.getY() - 3, w, 3);
        }
        g.setColor(new Color(0, 0, 0, (int) o));
        g.fillRect(0, 0, gl.getWidth(), gl.getHeight());
    }
    
    public void hit() {
        if(sleeping) {
            JukeBox.stopAll();
            sleeping = false;
            e = 0;
            emoteCount = 0;
        }
        else {
            life--;
            if(life == 2) {
                count = -29;
                act = 3;
            }
            else if(life == 1) {
                act = 5;
                count = -29;
            }
            else {
                count = 0;
            }
            tileMap.hero.setCanAction(true);
        }
    }
    
    public boolean hit(int damage) {
        if(pcount > 25) pcount = 0;
        return false;
    }
    
    public class Sand extends MapElement {
        
        private int count;
        private BufferedImage image;
        
        public Sand(GameLevel gl, TileMap tileMap, double x, double y, BufferedImage image, int dir) {
            super(gl, tileMap, x, y, null);
            moveSpeed = maxMoveSpeed = 0.5 + Math.random() * 0.2;
            this.image = image;
            width = 10;
            height = 2;
            setDirX(dir);
        }
        
        public void update() {
            super.checkCollisions();
            count++;
            if(count > 15) {
                setOpacity((float) (1.0 - 1.0 / 15.0 * (count - 15)));
                if(getOpacity() <= 0) {
                    setOpacity(0);
                    tileMap.removeObject(Sand.this);
                }
            }
        }
        
        public void paint(Graphics2D g) {
            int nw = (int) (10.0 - 9.0 * getOpacity());
            g.drawImage(image, (int) getX() + 5 - nw / 2 - tileMap.getX(), (int) getY() - tileMap.getY(), nw, 3, null);
        }
    }
}