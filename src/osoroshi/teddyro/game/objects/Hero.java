package osoroshi.teddyro.game.objects;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import osoroshi.teddyro.game.animations.JumpDust;
import osoroshi.teddyro.game.animations.ShootingStars;
import osoroshi.teddyro.game.objects.mobs.Monster;
import osoroshi.teddyro.game.powers.Power;
import osoroshi.teddyro.game.powers.PowerType;
import osoroshi.teddyro.game.states.GameLevel;
import osoroshi.teddyro.game.tilemap.TileMap;
import osoroshi.teddyro.game.utils.Controler;
import osoroshi.teddyro.game.utils.GameResource;

public class Hero extends MapObject {
    
    private ArrayList<BufferedImage[]> animations;
    private int jumpCount = 0, deadCount = 0, life = 0, starsLeft = 5, ssCount, ssTime, flinchCount, clothings;
    private ArrayList<JumpDust> jds = new ArrayList<>();
    private ArrayList<ShootingStars> ss = new ArrayList<>();
    private Power power;
    private boolean dead = false, flinching = false, rolling = false, shootingStars, canMove = true;
    private GameLevel gl;
    private BufferedImage deadImage;
    
    public Hero(GameLevel gl, TileMap tileMap, double x, double y) {
        super(tileMap, x, y);
        this.gl = gl;
        moveSpeed = 0.561159872615641651568746;
        maxMoveSpeed = 10.85;
        fallingSpeed = 0.71;
        maxFallingSpeed = 9.15;
        jumpStart = -13.71;
        stopSpeed = 1.25;
        width = 45;
        height = 60;
        life = 5;
        jumpCount = -1;
        ssCount = -1;
        animations = GameResource.getHeroSprites();
        animation.setFrames(animations.get(0));
        power = new Power(this.gl, this, this.tileMap);
    }
    
    public void update() {
        if(!dead) checkForKeys();
        else {
            dx = 0;
            dy = -2;
            deadCount++;
            animation.setFrames(animations.get(7));
            rotation = -90;
            fallingSpeed = 0;
            if(deadCount == 30) gl.restart();
        }
        super.update();
        super.checkCollisions();
        /*if(Controler.isDown()) {
            for(int i = 0; i < tileMap.platforms[getArea()].size(); i++) {
                if(tileMap.platforms[getArea()].get(i) instanceof SwitchEvent) {
                    tileMap.platforms[getArea()].remove(i);
                }
            }
            if(tileMap.platforms[getArea()].isEmpty()) tileMap.addPlatform(new SwitchEvent(gl, tileMap, getX(), tileMap.getY() - 64, "0"));
            //if(tileMap.platforms[getArea()].isEmpty()) tileMap.addPlatform(new Wall(gl, tileMap, getX(), tileMap.getY() - 64));
        }*/
        if(getY() + height == tileMap.getMapHeight()) {
            kill();
        }
        if(dx != 0 && !rolling && !isFalling() && !dead) {
            if(getDirX() == 1) setFlipped(false);
            else if(getDirX() == -1) setFlipped(true);
            if(clothings == 0) animation.setFrames(animations.get(1));
            else animation.setFrames(animations.get(4));
            animation.setDelay((int) (6.0 + (9.0 - (9.0 / 10.85 * Math.abs(dx)))));
            animation.setReverse(true);
        }
        else if(dx == 0 && !isFalling() && !dead) {
            if(clothings == 0) animation.setFrames(animations.get(0));
            else animation.setFrames(animations.get(3));
        }
        if(getDirY() == -1 && !isFalling() && dy == 0 && !dead && jumpCount == -1 && ssCount == -1) {
            jumpCount = 0;
        }
        if(jumpCount != -1 && !shootingStars) {
            if(getDirX() == 1) setFlipped(false);
            else if(getDirX() == -1) setFlipped(true);
            maxMoveSpeed = 0;
            if(clothings == 0) animation.setFrames(animations.get(2));
            else animation.setFrames(animations.get(5));
            if(jumpCount % 5.0 == 0 && height == 60) {
                /*for(int i = 0; i < width / 2; i += 10) {
                    double w = width / 2 - i - 1;
                    double h = Math.sqrt((width / 2 - 1) * (width / 2 - 1) - w * w);
                    jds.add(new JumpDust(tileMap, getX() + width / 2.0 - w, getY() + height / 2.0 - h));
                    jds.add(new JumpDust(tileMap, getX() + width / 2.0 - w, getY() + height / 2.0 + h));
                    jds.add(new JumpDust(tileMap, getX() + width / 2.0 + w, getY() + height / 2.0 - h));
                    jds.add(new JumpDust(tileMap, getX() + width / 2.0 + w, getY() + height / 2.0 + h));
                }*/
                for(int i = 0; i < 360; i += 360 / 8) {
                    double xj = width / 2.0 * Math.cos(i * Math.PI / 180.0);
                    double yj = height / 2.0 * Math.sin(i * Math.PI / 180.0);
                    jds.add(new JumpDust(tileMap, getX() + width / 2.0 + xj, getY() + height / 2.0 + yj));
                }
            }
            if(jumpCount >= 15 && getDirY() != -1) {
                jumpCount = -2;
                jumpStart = -13.71;
                setJumping(true);
                maxMoveSpeed = 10.85;
            }
            if(jumpCount >= 30-30+30) {
                jumpCount = -2;
                jumpStart = -18.71;
                setJumping(true);
                maxMoveSpeed = 10.85;
            }
            jumpCount++;
        }
        if(isFalling() && !dead) {
            if(getDirX() == 1) setFlipped(false);
            else if(getDirX() == -1) setFlipped(true);
            if(clothings == 0) animation.setFrames(animations.get(2));
            else animation.setFrames(animations.get(5));
            animation.setFrame(1);
        }
        if(dx != 0 && maxMoveSpeed != 0 && rolling && !isFalling() && !dead) {
            maxMoveSpeed = 10.85*2.5;
            animation.setFrames(animations.get(10));
            double r = 35.85 / maxMoveSpeed * Math.abs(dx);
            rotation += dx >= 0 ? r : -r;
            if(height != 41) {
                height = 41;
                y += 19;
            }
        }
        else if(!dead && height == 41) {
            if(maxMoveSpeed == 10.85*2.5) maxMoveSpeed = 10.85;
            rotation = 0;
            height = 60;
            y -= 19;
        }
        if(flinching) {
            flinchCount++;
            if(flinchCount == 61) {
                flinchCount = 0;
                flinching = false;
            }
        }
        if(shootingStars && ssCount == -1 && !isFalling() && !dead && starsLeft > 0) {
            jumpCount = -1;
            ssCount = 0;
            maxMoveSpeed = 0;
        }
        if(ssCount != -1) {
            animation.setFrames(animations.get(8));
            if(ssCount >= 24) {
                if(ssCount == 24) starsLeft--;
                animation.setFrame(1);
                width = 55;
                for(int i = 0; i < blocksHeight * 32; i += 32) {
                    if(tileMap.isSolid(getX() + width - 1, getY() + i)) {
                        setX(getX() - 10);
                    }
                    if(tileMap.isSolid(getX(), getY() + i)) {
                        setX(getX() + 10);
                    }
                }
                for(int i = 0; i < 5 + new java.util.Random().nextInt(6); i++) {
                    if(isFlipped()) {
                        ss.add(new ShootingStars(tileMap, getX(), getY() + height / 2, -1));
                    }
                    else {
                        ss.add(new ShootingStars(tileMap, getX() + width, getY() + height / 2, 1));
                    }
                }
                if(ssCount == 54) {
                    ssCount = -2;
                    width = 45;
                    maxMoveSpeed = 10.85;
                }
            }
            ssCount++;
        }
        for(int i = 0; i < jds.size(); i++) {
            jds.get(i).update();
            if(jds.get(i).shouldBeRemoved()) jds.remove(i);
        }
        for(int i = 0; i < ss.size(); i++) {
            ss.get(i).update();
            if(ss.get(i).shouldBeRemoved()) ss.remove(i);
        }
        ssTime++;
        if(ssTime > 1800) {
            if(starsLeft < 5) starsLeft++;
            ssTime = 0;
        }
    }
    
    public void checkForKeys() {
        if(!canMove) return;
        if(Controler.isRight()) {
            setDirX(1);
        }
        else if(Controler.isLeft()) {
            setDirX(-1);
        }
        else {
            setDirX(0);
        }
        if(Controler.isUp()) {
            setDirY(-1);
        }
        else if(Controler.isDown()) {
            setDirY(1);
        }
        else {
            setDirY(0);
        }
        if(Controler.isSpace()) {
            power.doAction();
        }
        if(Controler.isF5()) {
            setY(0);
        }
        shootingStars = Controler.isA() && clothings != 1;
        rolling = (Controler.isControl() || Controler.isW());
    }
    
    public void paint(Graphics2D g) {
        for(int i = 0; i < ss.size(); i++) {
            ss.get(i).paint(g);
        }
        if(!flinching || (flinching && (flinchCount % 6 == 0 || flinchCount % 6 == 3)) || dead) {
            if(dead) {
                this.rotation = Math.PI / 2;
            }
            super.paint(g);
        }
        /*g.setColor(Color.white);
        g.fill(new Ellipse2D.Double(70, 70, 30, 20));
        g.fill(new Arc2D.Double(70-15, 70+20-7, 30, 20, 45-45/2, 50, Arc2D.PIE));
        */
        for(int i = 0; i < jds.size(); i++) {
            jds.get(i).paint(g);
        }
        power.paint(g);
        if(GameResource.showOutlines()) {
            g.setColor(Color.yellow);
            g.setFont(new Font("Arial", Font.PLAIN, 10));
            g.drawString("Height : "+((((int) ((getY() + height - 1) / 32.0) - (int) (getY() / 32.0)) + 1) * 32), (int) getX() - tileMap.getX(), (int) getY() - tileMap.getY());
        }
        g.setColor(Color.black);
        g.setFont(new Font("Arial", Font.PLAIN, 30));
        //g.drawString(dy+" "+isFalling()+" "+isOnSlope(), 30, 60);
        //if(isOnSlope()) {
        //    g.drawString(getSX()+" "+getSY(), 30, 100);
        //}
    }
    
    public void setCanAction(boolean b) {
        canMove = b;
        if(!canMove) {
            setDirX(0);
            setDirY(0);
        }
    }
    
    public void setAnimation(int animation, int delay) {
        this.animation.setFrames(animations.get(animation));
        this.animation.setDelay(delay);
    }
    
    public void setAnimationIndex(int index) {
        this.animation.setFrame(index);
    }
    
    public void setArea(int area) {
        super.setArea(area);
        power.setPower(Power.BIRD_POWER);
    }
    
    public void relive() {
        dx = 0;
        dy = 0;
        width = 45;
        height = 60;
        rotation = 0;
        fallingSpeed = 0.71;
        maxFallingSpeed = 9.15;
        power.setPower(Power.NONE);
        setCanAction(true);
        ss.clear();
        dead = false;
        deadCount = 0;
        flinching = false;
        life = 5;
        jumpCount = -1;
        ssCount = -1;
        starsLeft = 5;
        moveSpeed = 0.46763322717970137630728833333333;
        moveSpeed = 0.561159872615641651568746;
        maxMoveSpeed = 10.85;
    }
    
    public void reinitValues() {
        width = 45;
        height = 60;
        fallingSpeed = 0.71;
        maxFallingSpeed = 9.15;
        moveSpeed = 0.46763322717970137630728833333333;
        moveSpeed = 0.561159872615641651568746;
        maxMoveSpeed = 10.85;
        jumpCount = -1;
        ssCount = -1;
        setCanAction(true);
    }
    
    public void kill() {
        setCanAction(false);
        dead = true;
        deadImage = GameResource.getHeroSprites().get(0)[0];
        setDirX(0);
        setDirY(0);
    }
    
    public void xAxisVerification() {
        for(int i = 0; i < tileMap.objects[area].size(); i++) {
            if(intersects(tileMap.objects[area].get(i).getShape())) {
                tileMap.objects[area].get(i).intersect(this);
            }
        }
    }
    
    public int getLife() {
        return life;
    }
    
    public int getMaxLife() {
        return 5;
    }
    
    public int getStarsLeft() {
        return starsLeft;
    }
    
    public boolean shootingStars() {
        return ssCount != -1;
    }
    
    public void launchStars() {
        
    }
    
    public void hit(int damage) {
        if(flinching) return;
        flinching = true;
        this.life -= damage;
        if(this.life <= 0) {
            kill();
        }
    }
    
    public void setPower(PowerType pt) {
        this.power.setPower(pt);
    }
    
    public Power getPower() {
        return this.power;
    }
    
    public void setClothings(int i) {
        this.clothings = i;
    }
    
    public boolean isRolling() {
        return rolling && maxMoveSpeed == 10.85 * 2.5 && animation.getFrames() == animations.get(10);
    }
    
    public boolean isDead() {
        return dead;
    }
    
    public void checkCollisionsWhileRolling() {
        for(int i = 0; i < tileMap.mobs[area].size(); i++) {
            Monster m = tileMap.mobs[area].get(i);
            if((getY() >= m.getY() && getY() < m.getY() + m.height) ||
                (m.getY() >= getY() && m.getY() < getY() + height)) {
                    if((getX() >= m.getX() && getX() < m.getX() + m.width) || (m.getX() >= getX() && m.getX() < getX() + width)) {
                        if(m.hit(2)) {
                            jump();
                            setY(m.getY() - height);
                        }
                        dx = 0;
                        dy = 0;
                        losePlatform(getPlatform());
                    }
            }
        }
    }
    
    public void checkCollisionsWhileFalling() {
        for(int i = 0; i < tileMap.mobs[area].size(); i++) {
            Monster m = tileMap.mobs[area].get(i);
            if((getY() + height >= m.getY() && getY() + height < m.getY() + m.height) ||
                (m.getY() >= getY() + height && m.getY() < getY() + height)) {
                    if((getX() >= m.getX() && getX() < m.getX() + m.width) || (m.getX() >= getX() && m.getX() < getX() + width)) {
                        if(m.canHitAtHead()) {
                            m.hit(2);
                            stopYIncreasing();
                            dy = -13.71;
                            setY(m.getY() - height);
                        }
                        else if(!m.isDead()) {
                            hit(m.getDamage());
                        }
                    }
            }
        }
    }
}