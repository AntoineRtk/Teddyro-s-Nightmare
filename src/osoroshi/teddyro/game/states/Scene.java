package osoroshi.teddyro.game.states;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import osoroshi.teddyro.game.states.scenes.Task;
import osoroshi.teddyro.game.states.scenes.TroupTask;
import osoroshi.teddyro.game.utils.JukeBox;

public abstract class Scene {
    
    private SceneManager sm;
    private BufferedImage redWave;
    private float opacity = 1.0f;
    private int textCount = 0, thunderCount = 0, maxThunderCount = 100, index = 0;
    private double textLength = 0;
    private ArrayList<Task> tasks = new ArrayList<>();
    
    public Scene(SceneManager sm) {
        this.sm = sm;
        redWave = new BufferedImage(30, 15, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = redWave.createGraphics();
        g.setColor(Color.red);
        for(double xp = 0; xp <= 15; xp += 0.1) {
            int ypoint = (int) (Math.sqrt(15 * 15 - xp * xp));
            g.fillRect(15 - (int) xp, 15 - ypoint, 1, 15 - (15 - ypoint));
            g.fillRect(15 + (int) xp, 15 - ypoint, 1, 15 - (15 - ypoint));
        }
        g.dispose();
    }
    
    public abstract void init();
    
    public void load(String scene) {
        JukeBox.stopAll();
        tasks.clear();
        index = 0;
        BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("osoroshi/teddyro/resources/texts/"+scene+".txt")));
        try {
            String line = "";
            while((line = br.readLine()) != null) {
                String firstWord = line.split(" ")[0];
                switch (firstWord) {
                    case "talk":
                        tasks.add(new Task(0, line.substring(line.indexOf(" ") + 1)));
                        break;
                    case "move":
                        tasks.add(new Task(1, line.substring(line.indexOf(" ") + 1)));
                        break;
                    case "troupcoming":
                        tasks.add(new TroupTask(2, "0"));
                        break;
                    case "talkv":
                        tasks.add(new Task(3, line.substring(line.indexOf(" ") + 1)));
                        break;
                    case "troupexiting":
                        tasks.add(new Task(4, "1"));
                        break;
                }
            }
            br.close();
        } catch (IOException ex) {
            Logger.getLogger(Scene.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void update() {
        if(!JukeBox.isRunning("cutscene")) {
            JukeBox.setLooping("cutscene", 88755, -1);
            opacity = 1f;
            thunderCount = 0;
            maxThunderCount = 300;
        }
        thunderCount++;
        if(thunderCount == maxThunderCount) {
            JukeBox.play("thunder");
        }
        if(thunderCount == maxThunderCount + 5) {
            thunderCount = 0;
            maxThunderCount = 300 + new java.util.Random().nextInt(300);
        }
        if(index < tasks.size()) {
            opacity -= 0.0075f;
            if(opacity < 0) opacity = 0;
        }
        if(index == tasks.size()) {
            opacity += 0.0075f;
            if(opacity > 1) opacity = 1;
        }
        if(index < tasks.size()) {
            for(int i = 0; i < tasks.size(); i++) {
                if(tasks.get(i) instanceof TroupTask) {
                    ((TroupTask) tasks.get(i)).update();
                }
            }
            switch(tasks.get(index).getInfo()) {
                case 0:
                    doActionWithText();
                    break;
                case 1:
                    break;
                case 2:
                    if(((TroupTask) tasks.get(index)).isFinish()) {
                        index++;
                    }
                    break;
                case 3:
                    doActionWithText();
                    break;
                case 4:
                    for(int i = 0; i < tasks.size(); i++) {
                        if(tasks.get(i).getInfo() == 2) {
                            ((TroupTask) tasks.get(i)).setGoingForward(false);
                            if(((TroupTask) tasks.get(i)).isFinish()) {
                                index++;
                            }
                        }
                    }
                    break;
            }
        }
    }
    
    public void doActionWithText() {
        if(textLength == 0) {
            JukeBox.play("text");
            textLength = 0;
        }
        textCount++;
        textLength += tasks.get(index).getParams().length() / (25.0 * JukeBox.clipLengthInSeconds("text"));
        if(textLength >= tasks.get(index).getParams().length()) textLength = tasks.get(index).getParams().length();
        if(textCount >= 187 && textLength == tasks.get(index).getParams().length()) {
            textLength = 0;
            textCount = 0;
            index++;
        }
    }
    
    public void paint(Graphics2D g) {
        for(int i = 0; i < sm.getHeight() / 2; i++) {
            g.setColor(new Color((int) (64.0 - 64.0 / (sm.getHeight() / 2.0) * (i + 1.0)), 0, (int) (128.0 - 128 / (sm.getHeight() / 2.0) * (i + 1.0))));
            g.fillRect(0, i, sm.getWidth(), 1);
        }
        if(thunderCount >= maxThunderCount) {
            g.setColor(new Color(255, 255, 255, (int) (255.0 / 5 * (maxThunderCount + 5 - thunderCount))));
            g.fillRect(0, 0, sm.getWidth() - 1, sm.getHeight() - 1);
        }
        if(index <= tasks.size()) {
            for(int i = 0; i < tasks.size(); i++) {
                if(tasks.get(i) instanceof TroupTask) {
                    ((TroupTask) tasks.get(i)).paint(g);
                }
            }
            if(index < tasks.size() && (tasks.get(index).getInfo() == 0 || tasks.get(index).getInfo() == 3)) {
                g.setColor(Color.white);
                int font = 30, style = tasks.get(index).getInfo() == 0 ? Font.PLAIN : Font.BOLD;
                do {
                    g.setFont(new Font("Lithos Pro Regular", style, font));
                    String text = tasks.get(index).getParams().substring(0, (int) textLength);
                    if(text.length() != 0) {
                        TextLayout txt = new TextLayout(text, g.getFont(), g.getFontRenderContext());
                        if(new TextLayout(tasks.get(index).getParams(), g.getFont(), g.getFontRenderContext()).getBounds().getWidth() > sm.getWidth()) {
                            font--;
                            continue;
                        }
                        if(tasks.get(index).getInfo() == 0) {
                            g.drawString(text, sm.getWidth() / 2 - (int) txt.getBounds().getWidth() / 2, sm.getHeight() - (int) txt.getBounds().getHeight() / 2);
                        }
                        else {
                            double totalWidth = 0;
                            for(int i = 0; i < text.length(); i++) {
                                if(text.charAt(i) == ' ') {
                                    totalWidth += g.getFontMetrics().stringWidth(" ");
                                    continue;
                                }
                                totalWidth += new TextLayout(text.charAt(i)+"", g.getFont(), g.getFontRenderContext()).getBounds().getWidth() + 2;
                            }
                            double xtext = sm.getWidth() / 2.0 - totalWidth / 2.0;
                            for(int i = 0; i < text.length(); i++) {
                                if(text.charAt(i) == ' ') {
                                    xtext += g.getFontMetrics().stringWidth(" ") + 2;
                                    continue;
                                }
                                g.drawString(text.charAt(i)+"", (int) xtext, sm.getHeight() - (int) txt.getBounds().getHeight() / 2 - 5 + new java.util.Random().nextInt(11));
                                xtext += new TextLayout(text.charAt(i)+"", g.getFont(), g.getFontRenderContext()).getBounds().getWidth() + 2;
                            }
                        }
                    }
                    break;
                } while(true);
            }
        }
        g.setColor(new Color(0, 0, 0, (int) (255.0f * opacity)));
        g.fillRect(0, 0, sm.getWidth() - 1, sm.getHeight() - 1);
        if(opacity == 1 && index == tasks.size()) {
            JukeBox.stop("cutscene");
            finish();
        }
    }
    
    public SceneManager getSceneManager() {
        return sm;
    }
    
    public abstract void finish();
}