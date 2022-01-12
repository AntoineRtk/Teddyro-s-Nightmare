package osoroshi.teddyro.game.states;

import java.awt.Desktop;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.html.HTMLEditorKit;
import osoroshi.teddyro.game.utils.GameResource;
import osoroshi.teddyro.game.utils.JukeBox;

public class GameMenuAction extends GameMenu {
    
    private int action = 0;
    private boolean frameShown = false;
    private GameMenu gm;
    
    public GameMenuAction(GameStateManager gsm, Menu menu, String text, int action, GameMenu gm) {
        super(gsm, menu, text);
        this.action = action;
        this.gm = gm;
    }
    
    public void doAction(int i) {
        String pass = "";
        switch(action) {
            case -1:
                JukeBox.play("click");
                back(gm);
                break;
            case 0:
                JukeBox.play("start");
                JukeBox.stop("menu");
                ((Menu)gsm.getState()).start();
                break;
            case 1:
                JukeBox.play("click");
                System.exit(0);
                break;
            case 2:
                JukeBox.play("click");
                JFileChooser fc = new JFileChooser();
                fc.setAcceptAllFileFilterUsed(false);
                fc.setFileFilter(new FileNameExtensionFilter("Cartes", "mta"));
                if(fc.showOpenDialog(Main.getFrame()) == JFileChooser.APPROVE_OPTION) {
                    JukeBox.play("start");
                    JukeBox.stop("menu");
                    gsm.setState(6);
                    ((CustomLevel) gsm.getState()).setPath(fc.getSelectedFile());
                }
                break;
            case 3:
                JukeBox.play("click");
                Main.setFullScreen(false);
                break;
            case 4:
                JukeBox.play("click");
                Main.setFullScreen(true);
                break;
            case 5:
                JukeBox.play("click");
                JukeBox.setMute(false);
                break;
            case 6:
                JukeBox.play("click");
                JukeBox.setMute(true);
                break;
            case 7:
                JukeBox.play("click");
                Main.setFullScreen(false);
                pass = JOptionPane.showInputDialog(null, "Enter password", null);
                if(pass != null && pass.equals("OsoroshiPassword")) {
                    gsm.setFPS(25);
                }
                else {
                    JOptionPane.showMessageDialog(null, "Wrong password", "Teddyro's Nightmare", JOptionPane.ERROR_MESSAGE);
                }
                break;
            case 8:
                JukeBox.play("click");
                Main.setFullScreen(false);
                pass = JOptionPane.showInputDialog(null, "Enter password", null);
                if(pass != null && pass.equals("OsoroshiPassword")) {
                    gsm.setFPS(50);
                }
                else {
                    JOptionPane.showMessageDialog(null, "Wrong password", "Teddyro's Nightmare", JOptionPane.ERROR_MESSAGE);
                }
                break;
            case 9:
                JukeBox.play("click");
                Main.setFullScreen(false);
                pass = JOptionPane.showInputDialog(null, "Enter password", null);
                if(pass != null && pass.equals("OsoroshiPassword")) {
                    gsm.setFPS(60);
                }
                else {
                    JOptionPane.showMessageDialog(null, "Wrong password", "Teddyro's Nightmare", JOptionPane.ERROR_MESSAGE);
                }
                break;
            case 10:
                JukeBox.play("click");
                Main.setFullScreen(false);
                pass = JOptionPane.showInputDialog(null, "Enter password", null);
                if(pass != null && pass.equals("OsoroshiPassword")) {
                    gsm.setFPS(100);
                }
                else {
                    JOptionPane.showMessageDialog(null, "Wrong password", "Teddyro's Nightmare", JOptionPane.ERROR_MESSAGE);
                }
                break;
            case 11:
                JukeBox.play("click");
                Main.setFullScreen(false);
                GameResource.setDrawConvexShapes(true);
                JOptionPane.showMessageDialog(null, "Les formes convexes seront tracées lors des attaques (peut causer des grosses chutes de FPS)", "Teddyro's Nightmare", JOptionPane.INFORMATION_MESSAGE);
                break;
            case 12:
                JukeBox.play("click");
                Main.setFullScreen(false);
                GameResource.setDrawConvexShapes(false);
                JOptionPane.showMessageDialog(null, "Les formes convexes ne seront pas tracées lors des attaques (évite des chutes de FPS)", "Teddyro's Nightmare", JOptionPane.INFORMATION_MESSAGE);
                break;
            case 13:
                JukeBox.play("click");
                Main.setFullScreen(false);
                if(frameShown) return;
                frameShown = true;
                JFrame frame = new JFrame("Crédits");
                frame.setSize(400, 600);
                frame.setResizable(false);
                frame.setLocationRelativeTo(null);
                final JTextPane text = new JTextPane();
                text.setEditorKit(new HTMLEditorKit());
                BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("osoroshi/teddyro/resources/texts/credits.txt")));
                String totalText = "", readingText;
                try {
                    while((readingText = br.readLine()) != null) {
                        totalText += readingText+"\n";
                    }
                    br.close();
                } catch (IOException ex) {
                    Logger.getLogger(GameMenuAction.class.getName()).log(Level.SEVERE, null, ex);
                }
                text.setText(totalText);
                final JScrollPane scroll = new JScrollPane(text);
                frame.add(scroll);
                frame.setIconImage(new ImageIcon(getClass().getClassLoader().getResource("osoroshi/teddyro/resources/divers/icon.png")).getImage());
                frame.setVisible(true);
                frame.addWindowListener(new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        frameShown = false;
                    }
                });
                break;
            case 14:
                JukeBox.play("click");
                Main.setCursor(0);
                break;
            case 15:
                JukeBox.play("click");
                Main.setCursor(1);
                break;
            case 16:
                JukeBox.play("click");
                Main.setCursor(2);
                break;
            case 17:
                File file = new File(System.getProperty("user.home")+"\\Documents\\Teddyro's Nightmare");
                if(!file.exists()) file.mkdir();
                try {
                    Desktop.getDesktop().open(file);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Erreur lors de l'ouverture du dossier des captures d'écran : "+ex.getLocalizedMessage(), "Teddyro's Nightmare", JOptionPane.ERROR_MESSAGE);
                }
                break;
            case 18:
                JukeBox.play("click");
                pass = JOptionPane.showInputDialog(null, "Enter password", null);
                if(pass != null && pass.equals("OsoroshiPassword")) {
                    gsm.setFPS(-1);
                }
                else {
                    JOptionPane.showMessageDialog(null, "Wrong password", "Teddyro's Nightmare", JOptionPane.ERROR_MESSAGE);
                }
                break;
            case 19:
                gsm.setState(27);
                break;
        }
    }
}