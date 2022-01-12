package osoroshi.teddyro.game.states;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import osoroshi.teddyro.game.utils.GameResource;

public class Main {
    
    private static JFrame frame;
    private static GraphicsDevice gd;
    private static int c;
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            JOptionPane.showMessageDialog(null, "Error loading look and feel settings for Windows OS "+ex.getMessage(), "Teddyro's Nightmare", JOptionPane.ERROR_MESSAGE);
        }
        frame = new JFrame("Initing GUI");
        frame.setIconImage(new ImageIcon(Main.class.getClassLoader().getResource("osoroshi/teddyro/resources/divers/icon.png")).getImage());
        frame.setVisible(true);
        frame.setMinimumSize(new Dimension(150, 150));
        frame.setSize(700, 700);
        frame.setDefaultCloseOperation(0);
        frame.setLocationRelativeTo(null);
        Game game = new Game();
        frame.add(game);
        game.initImage();
        game.start();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        gd = ge.getDefaultScreenDevice();
        frame.setIconImage(new ImageIcon(Main.class.getClassLoader().getResource("osoroshi/teddyro/resources/divers/icon.png")).getImage());
        frame.setTitle("Initing textures and objects");
        final long time = GameResource.init();
        frame.setCursor(GameResource.getCursor(0));
        game.init(time);
        game.setState(0);
        final long titleTime = System.currentTimeMillis();
        new Thread(new Thread() {
            public void run() {
                while(true) {
                    frame.setTitle("Initialized textures and objects in "+time+" ms");
                    if(System.currentTimeMillis() - titleTime > 3000-3001) {
                        frame.setTitle("Teddyro's Nightmare");
                        break;
                    }
                }
            }
        }).start();
        frame.setDefaultCloseOperation(3);
    }
    
    public static void setCursor(int c) {
        frame.setCursor(GameResource.getCursor(c));
        Main.c = c;
    }
    
    public static int getCursor() {
        return Main.c;
    }
    
    public static void setWidth(int w) {
        frame.setSize(w, frame.getHeight());
        frame.setLocationRelativeTo(null);
    }
    
    public static void setHeight(int h) {
        frame.setSize(frame.getWidth(), h);
        frame.setLocationRelativeTo(null);
    }
    
    public static void setFullScreen(boolean fs) {
        if(fs) {
            frame.setVisible(false);
            frame.dispose();
            frame.setUndecorated(true);
            frame.setVisible(true);
            gd.setFullScreenWindow(frame);
        }
        else {
            frame.setVisible(false);
            frame.dispose();
            frame.setUndecorated(false);
            gd.setFullScreenWindow(null);
            frame.setVisible(true);
        }
    }
    
    public static boolean isFullScreen() {
        return frame.isUndecorated();
    }
    
    public static JFrame getFrame() {
        return frame;
    }
}