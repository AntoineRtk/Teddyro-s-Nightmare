package osoroshi.teddyro.game.states;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.TexturePaint;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import osoroshi.teddyro.game.utils.Controler;
import osoroshi.teddyro.game.utils.GameResource;
import osoroshi.teddyro.game.utils.JukeBox;
import osoroshi.teddyro.game.utils.Placement;

public class Game extends JPanel implements Runnable {
    
    public GameStateManager gsm;
    private BufferedImage image;
    private Graphics2D g;
    private boolean running = false, showFPS = false, fallingStars = true, screenshot;
    private long loadTime = -1, screenshotTime;
    private int pc, count = 0, FPS, screenshotNumber;
    private ArrayList<Placement> placements = new ArrayList<>();
    private Random random;
    private String screenshotPath = "";
    private Thread thread;
    
    public void init(long time) {
        count = 0;
        pc = 1;
        this.loadTime = time;
        random = new Random();
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
            }
        });
        addMouseMotionListener(new MouseMotionListener() {
            public void mouseMoved(MouseEvent e) {
                if(fallingStars) {
                    for(int i = 0; i < 20 + random.nextInt(20); i++) {
                        placements.add(new Placement(e.getX() * (684.0 / Game.super.getWidth()) - 10 + new java.util.Random().nextInt(20), e.getY() * (662.0 / Game.super.getHeight())));
                    }
                }
                GameResource.setPosition(1.0 * getWidth() / Game.super.getWidth() * e.getX(), 1.0 * getHeight() / Game.super.getHeight() * e.getY());
            }
            
            public void mouseDragged(MouseEvent e) {
                if(fallingStars) {
                    for(int i = 0; i < 20 + random.nextInt(20); i++) {
                        placements.add(new Placement(e.getX() * (684.0 / Game.super.getWidth()) - 10 + new java.util.Random().nextInt(20), e.getY() * (662.0 / Game.super.getHeight())));
                    }
                }
                GameResource.setPosition(1.0 * getWidth() / Game.super.getWidth() * e.getX(), 1.0 * getHeight() / Game.super.getHeight() * e.getY());
            }
        });
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                switch(e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        Controler.setLeft(true);
                        break;
                    case KeyEvent.VK_RIGHT:
                        Controler.setRight(true);
                        break;
                    case KeyEvent.VK_UP:
                        Controler.setUp(true);
                        break;
                    case KeyEvent.VK_DOWN:
                        Controler.setDown(true);
                        break;
                    case KeyEvent.VK_SPACE:
                        Controler.setSpace(true);
                        break;
                    case KeyEvent.VK_CONTROL:
                        Controler.setControl(true);
                        break;
                    case KeyEvent.VK_W:
                        Controler.setW(true);
                        break;
                    case KeyEvent.VK_ENTER:
                        Controler.setEnter(true);
                        break;
                    case KeyEvent.VK_BACK_SPACE:
                        Controler.setBackspace(true);
                        break;
                    case KeyEvent.VK_A:
                        Controler.setA(true);
                        break;
                    case KeyEvent.VK_ESCAPE:
                        Controler.setEscape(true);
                        break;
                    case KeyEvent.VK_1:
                        FPS = 1;
                        break;
                    case KeyEvent.VK_2:
                        FPS = 30;
                        break;
                    case KeyEvent.VK_3:
                        FPS = (new java.util.Random().nextBoolean()) ? 60 : 300;
                        break;
                    case KeyEvent.VK_4:
                        FPS = 1000;
                        break;
                    case KeyEvent.VK_5:
                        FPS = -1;
                        break;
                    case KeyEvent.VK_6:
                        FPS = -1;
                        break;
                    case KeyEvent.VK_F1:
                        showFPS = !showFPS;
                        break;
                    case KeyEvent.VK_F2:
                        JukeBox.mute();
                        break;
                    case KeyEvent.VK_F3:
                        screenshot = true;
                        break;
                    case KeyEvent.VK_F5:
                        Controler.setF5(true);
                        break;
                }
                gsm.keyPressed(e.getKeyCode());
            }
            
            public void keyReleased(KeyEvent e) {
                switch(e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        Controler.setLeft(false);
                        break;
                    case KeyEvent.VK_RIGHT:
                        Controler.setRight(false);
                        break;
                    case KeyEvent.VK_UP:
                        Controler.setUp(false);
                        break;
                    case KeyEvent.VK_DOWN:
                        Controler.setDown(false);
                        break;
                    case KeyEvent.VK_SPACE:
                        Controler.setSpace(false);
                        break;
                    case KeyEvent.VK_CONTROL:
                        Controler.setControl(false);
                        break;
                    case KeyEvent.VK_W:
                        Controler.setW(false);
                        break;
                    case KeyEvent.VK_ENTER:
                        Controler.setEnter(false);
                        break;
                    case KeyEvent.VK_BACK_SPACE:
                        Controler.setBackspace(false);
                        break;
                    case KeyEvent.VK_A:
                        Controler.setA(false);
                        break;
                    case KeyEvent.VK_ESCAPE:
                        Controler.setEscape(false);
                        break;
                    case KeyEvent.VK_F4:
                        Main.setFullScreen(!Main.isFullScreen());
                        break;
                    case KeyEvent.VK_F5:
                        Controler.setF5(false);
                        break;
                    case KeyEvent.VK_F6:
                        fallingStars = !fallingStars;
                        break;
                }
                gsm.keyReleased(e.getKeyCode());
            }
        });
        verifyScreenshotNumber();
        setFocusable(true);
        requestFocus();
    }
    
    public void verifyScreenshotNumber() {
        File file = new File(System.getProperty("user.home")+"\\Documents\\Teddyro's Nightmare");
        if(!file.exists()) { try {
            Files.createDirectories(file.toPath());
        } catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        } }
        for(int i = 0; i < file.listFiles().length; i++) {
            if(file.listFiles()[i].getName().startsWith("Screenshot")) {
                screenshotNumber++;
            }
        }
    }
    
    public void initImage() {
        gsm = new GameStateManager(this);
        image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }
    
    public void start() {
        running = true;
        thread = new Thread(this);
        thread.start();
    }
    
    public void stop() {
        running = false;
        while(true) {
            if(!thread.isAlive()) break;
        }
    }
    
    public int getWidth() {
        return 684;
    }
    
    public int getHeight() {
        return 662;
    }
    
    public void run() {
        FPS = 30;
        int frames = 0, totalFrames = 0;
        long time = System.currentTimeMillis();
        while(running) {
            long nano = System.nanoTime();
            gsm.update();
            g.setColor(Color.black);
            g.fillRect(0, 0, getWidth(), getHeight());
            gsm.paint(g);
            count++;
            if(count <= 60 && loadTime != -1) {
                g.setColor(Color.orange);
                g.setFont(new Font("Arial", Font.PLAIN, 10));
                TextLayout txt = new TextLayout("Loaded in "+loadTime+" ms", g.getFont(), g.getFontRenderContext());
                g.drawString("Loaded in "+loadTime+" ms", getWidth() - (int) txt.getBounds().getWidth()-5, getHeight() - (int) txt.getBounds().getHeight()+5);
            }
            if(loadTime == -1) {
                if(count % 10 == 0) {
                    pc++;
                    if(pc > 3) pc = 1;
                }
                g.setColor(Color.blue);
                g.setFont(new Font("Arial", Font.BOLD, 45));
                g.drawString("Chargement"+repeat(".", pc), getWidth() - 305, getHeight() - 15);
                g.setFont(new Font("Arial", Font.BOLD, 30));
                g.drawString(GameResource.getState()+" "+GameResource.getPurcent()+"%", 10, 30);
                drawTextWithColor();
                if(GameResource.getLogo() != null) {
                    g.setFont(new Font("Gabriola", Font.PLAIN, 100));
                    TextLayout txt = new TextLayout("Teddyro's Nightmare", g.getFont(), g.getFontRenderContext());
                    g.setPaint(new TexturePaint(GameResource.getLogo(), new Rectangle(count * 10 % 200, 0, GameResource.getLogo().getWidth(), (int) txt.getBounds().getHeight())));
                    g.drawString("Teddyro's Nightmare", (int) (getWidth() / 2.0 - txt.getBounds().getX() - txt.getBounds().getWidth() / 2.0), (int) (getHeight() / 2.0 - txt.getBounds().getY() - txt.getBounds().getHeight() / 2.0));
                }
            }
            if(showFPS) {
                g.setColor(Color.yellow);
                g.setFont(new Font("Times New Roman", Font.BOLD, 23));
                g.drawString("T.E. : "+(System.nanoTime() - nano) / 1000000.0+" ms (time elapsed between updating and drawing)", 0, 20);
                if(FPS != -1) {
                    g.drawString("FPS : "+totalFrames+"/"+FPS, 0, 45);
                }
                else {
                    g.drawString("FPS : "+totalFrames+"/INFINITE", 0, 45);
                }
            }
            if(System.currentTimeMillis() - screenshotTime < 3000) {
                g.setColor(Color.yellow);
                g.setFont(new Font("Times New Roman", Font.BOLD, 15));
                g.drawString("Screenshot : "+screenshotPath, 0, getHeight() - 30);
            }
            if(screenshot) {
                screenshot = false;
                screenshotTime = System.currentTimeMillis();
                screenshotPath = System.getProperty("user.home")+"\\Documents\\Teddyro's Nightmare\\Screenshot"+screenshotNumber+".png";
                try {
                    File file = new File(System.getProperty("user.home")+"\\Documents\\Teddyro's Nightmare");
                    if(!file.exists()) file.mkdir();
                    ImageIO.write(image, "PNG", new File(screenshotPath));
                    screenshotNumber++;
                } catch (IOException ex) {
                    screenshotPath = "Erreur lors de l'écriture de l'image : "+ex.getLocalizedMessage();
                }
            }
            for(int i = 0; i < placements.size(); i++) {
                try {
                    placements.get(i).update();
                    placements.get(i).paint(g);
                    if(placements.get(i).shouldBeRemoved()) {
                        placements.remove(i);
                    }
                } catch (java.lang.NullPointerException ex) {}
            }
            try {
                getGraphics().drawImage(image, 0, 0, super.getWidth(), super.getHeight(), this);
            } catch (java.lang.NullPointerException ex) {
                frames = -999999;
            }
            frames++;
            try {
                if(FPS != -1) {
                    long sleep = 1000 / FPS - (long) ((System.nanoTime() - nano) / 1000000.0);
                    if(sleep > 0) {
                        thread.sleep(sleep);
                    }
                }
            } catch (InterruptedException | java.lang.IllegalArgumentException ex) {
                Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            }
            if((System.currentTimeMillis() - time) > 1000 || (FPS != -1 && frames == FPS)) {
                totalFrames = frames;
                frames = 0;
                if(FPS != -1 && frames == FPS) {
                    long timeToSleep = 1000 - (System.currentTimeMillis() - time);
                    if(timeToSleep > 0) {
                        try {
                            thread.sleep(timeToSleep);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                time = System.currentTimeMillis();
            }
        }
    }
    
    public void drawTextWithColor() {
        Color[] colors = new Color[]{Color.blue, Color.red, Color.green, Color.yellow, Color.pink, Color.magenta, Color.orange, Color.cyan};
        int red, green, blue, index = 0, c = 0;
        red = colors[0 % colors.length].getRed();
        green = colors[0 % colors.length].getGreen();
        blue = colors[0 % colors.length].getBlue();
        for(int x = 0; x < getWidth(); x++) {
            c++;
            if(c == 5) {
                c = 0;
                index++;
                if(index == colors.length) index = 0;
                red = colors[index].getRed();
                green = colors[index].getGreen();
                blue = colors[index].getBlue();
            }
            g.setColor(new Color(red, green, blue));
            for(int h = 0; h < 100; h++) {
                if(image.getRGB(x, h) != Color.black.getRGB()) {
                    g.fillRect(x, h, 1, 1);
                }
            }
        }
    }
    
    public BufferedImage getScreen() {
        BufferedImage screen = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D sg = screen.createGraphics();
        sg.drawImage(image, 0, 0, this);
        sg.dispose();
        return screen;
    }
    
    public void setState(int i) {
        gsm.setState(i);
    }
    
    public void setFPS(int fps) {
        FPS = fps;
    }
    
    public void displayFPS() {
        showFPS = !showFPS;
    }
    
    public String repeat(String string, int time) {
        String toReturn = "";
        for(int i = 0; i < time; i++) {
            toReturn += string;
        }
        return toReturn;
    }
}