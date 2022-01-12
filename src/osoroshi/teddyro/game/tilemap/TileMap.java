package osoroshi.teddyro.game.tilemap;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import osoroshi.teddyro.game.objects.Hero;
import osoroshi.teddyro.game.objects.MapElement;
import osoroshi.teddyro.game.objects.MapObject;
import osoroshi.teddyro.game.objects.StartingPoint;
import osoroshi.teddyro.game.objects.mobs.Monster;
import osoroshi.teddyro.game.objects.platforms.Platform;
import osoroshi.teddyro.game.objects.platforms.Wall;
import osoroshi.teddyro.game.states.GameLevel;
import osoroshi.teddyro.game.utils.GameResource;
import osoroshi.teddyro.game.utils.JukeBox;

public class TileMap {
    
    private Tile[][][] tiles;
    private int x, y, tx, ty, minx, maxx, maxy, area = 0, intensity = 0, luminosity = 0, radius = 0;
    public ArrayList<MapElement>[] objects;
    public ArrayList<Platform>[] platforms;
    public ArrayList<Monster>[] mobs;
    public Hero hero;
    private GameLevel gl;
    private Background[][] bgs;
    private String mapText;
    private boolean shaking = false, noLuminosity = false, crackling = false, sunny = false, snowing = false;
    private double rotation = 0;
    private ArrayList<Flake> flakes = new ArrayList<>();
    
    public TileMap(GameLevel gl) {
        this.gl = gl;
    }
    
    public void reinit() {
        try {
            System.out.println("Map : "+mapText);
            BufferedReader sr = new BufferedReader(new StringReader(mapText));
            tiles = new Tile[mapText.split("&").length - 1][][];
            bgs = new Background[tiles.length][];
            this.objects = new ArrayList[tiles.length];
            this.mobs = new ArrayList[tiles.length];
            this.platforms = new ArrayList[tiles.length];
            for(int i = 0; i < tiles.length; i++) {
                this.objects[i] = new ArrayList<>();
                this.mobs[i] = new ArrayList<>();
                this.platforms[i] = new ArrayList<>();
                int mapWidth = Integer.parseInt(sr.readLine());
                int mapHeight = Integer.parseInt(sr.readLine());
                String bg = sr.readLine();
                int m = Integer.parseInt(sr.readLine());
                if(area == i) {
                    JukeBox.play(m);
                }
                bgs[i] = new Background[bg.split(" ").length];
                for(int b = 0; b < bgs[i].length; b++) {
                    bgs[i][b] = new Background(this, gl, b == 0);
                    int bi = Integer.parseInt(bg.split(" ")[b]);
                    bgs[i][b].load(bi);
                    if(bi == 12) setCastleBackground(true, i);
                }
                tiles[i] = new Tile[mapHeight][mapWidth];
                int col = 0;
                while(col < mapHeight) {
                    String[] split = sr.readLine().split(" ");
                    for(int row = 0; row < mapWidth; row++) {
                        String id = split[row];
                        if(id.contains("_")) {
                            String params = id.substring(id.indexOf("_") + 1);
                            MapObject mo = GameResource.getObject(gl, this, Integer.parseInt(id.substring(0, id.indexOf("_"))), row * 32, col * 32, params);
                            mo.setArea(i);
                            if(mo instanceof Monster) {
                                mobs[i].add((Monster) mo);
                            }
                            else if(mo instanceof Platform) {
                                platforms[i].add((Platform) mo);
                            }
                            else {
                                objects[i].add((MapElement) mo);
                            }
                            tiles[i][col][row] = GameResource.getTile(0);
                        }
                        else {
                            MapObject mo = GameResource.getObject(gl, this, Integer.parseInt(id), row * 32, col * 32, null);
                            if(mo == null) {
                                tiles[i][col][row] = GameResource.getTile(Integer.parseInt(id));
                            }
                            else {
                                tiles[i][col][row] = GameResource.getTile(0);
                                mo.setArea(i);
                                if(mo instanceof Monster) {
                                    mobs[i].add((Monster) mo);
                                }
                                else if(mo instanceof Platform) {
                                    platforms[i].add((Platform) mo);
                                }
                                else {
                                    objects[i].add((MapElement) mo);
                                }
                            }
                        }
                    }
                    col++;
                }
                sr.readLine();
            }
            setArea(area, 0);
            sr.close();
        } catch (FileNotFoundException ex) {
            System.exit(0);
        } catch (IOException ex) {
            System.exit(0);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Erreur lors de la lecture des identités de la carte.", "Teddyro's Nightmare", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }
    
    public void loadMap(String map, boolean in) {
        try {
            BufferedReader br = null;
            if(in) {
                br = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("osoroshi/teddyro/resources/maps/"+map+".mta")));
            }
            else {
                br = new BufferedReader(new FileReader(map));
            }
            mapText = "";
            String line = "";
            while((line = br.readLine()) != null) {
                mapText += line+"\n";
            }
            br.close();
            BufferedReader sr = new BufferedReader(new StringReader(mapText));
            tiles = new Tile[mapText.trim().split("&").length][][];
            this.objects = new ArrayList[tiles.length];
            this.mobs = new ArrayList[tiles.length];
            this.platforms = new ArrayList[tiles.length];
            bgs = new Background[tiles.length][];
            for(int i = 0; i < tiles.length; i++) {
                this.objects[i] = new ArrayList<>();
                this.mobs[i] = new ArrayList<>();
                this.platforms[i] = new ArrayList<>();
                int mapWidth = Integer.parseInt(sr.readLine());
                int mapHeight = Integer.parseInt(sr.readLine());
                String bg = sr.readLine();
                int m = Integer.parseInt(sr.readLine());
                if(i == 0) {
                    JukeBox.play(m);
                }
                bgs[i] = new Background[bg.split(" ").length];
                for(int b = 0; b < bgs[i].length; b++) {
                    bgs[i][b] = new Background(this, gl, b == 0);
                    int bi = Integer.parseInt(bg.split(" ")[b]);
                    bgs[i][b].load(bi);
                    if(bi == 12) setCastleBackground(true, i);
                }
                tiles[i] = new Tile[mapHeight][mapWidth];
                int col = 0;
                while(col < mapHeight) {
                    String[] split = sr.readLine().split(" ");
                    for(int row = 0; row < mapWidth; row++) {
                        String id = split[row];
                        if(id.contains("_")) {
                            String params = id.substring(id.indexOf("_") + 1);
                            MapObject mo = GameResource.getObject(gl, this, Integer.parseInt(id.substring(0, id.indexOf("_"))), row * 32, col * 32, params);
                            try {
                                mo.setArea(i);
                            } catch (java.lang.NullPointerException ex) {
                                JOptionPane.showMessageDialog(null, "Critical error reading line : "+(col + 4)+" at area "+i+" id "+id+" params "+params+" x : "+row+" y : "+col, "Teddyro's Nightmare", JOptionPane.ERROR_MESSAGE);
                                System.exit(0);
                            }
                            if(mo instanceof Monster) {
                                mobs[i].add((Monster) mo);
                            }
                            else if(mo instanceof Platform) {
                                platforms[i].add((Platform) mo);
                            }
                            else {
                                objects[i].add((MapElement) mo);
                            }
                            tiles[i][col][row] = GameResource.getTile(0);
                        }
                        else {
                            MapObject mo = GameResource.getObject(gl, this, Integer.parseInt(id), row * 32, col * 32, null);
                            if(mo == null) {
                                tiles[i][col][row] = GameResource.getTile(Integer.parseInt(id));
                            }
                            else {
                                tiles[i][col][row] = GameResource.getTile(0);
                                mo.setArea(i);
                                if(mo instanceof Monster) {
                                    mobs[i].add((Monster) mo);
                                }
                                else if(mo instanceof Platform) {
                                    platforms[i].add((Platform) mo);
                                }
                                else {
                                    objects[i].add((MapElement) mo);
                                }
                            }
                        }
                    }
                    col++;
                }
                sr.readLine();
            }
            sr.close();
            setArea(0, 0);
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
            System.exit(0);
        } catch (IOException ex) {
            System.out.println(ex);
            System.exit(0);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Erreur lors de la lecture des identités de la carte. Exception : "+(ex.getMessage().substring(ex.getMessage().indexOf(":") + 1)), "Teddyro's Nightmare", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }
    
    public int getX() {
        if(x + tx < minx && maxx > 0) return minx;
        if(x + tx > maxx && maxx > 0) return maxx;
        return x + tx;
    }
    
    public int getY() {
        if(y + ty < 0 && maxy > 0) return 0;
        if(y + ty > maxy && maxy > 0) return maxy;
        return y + ty;
    }
    
    public void setHero(Hero hero) {
        this.hero = hero;
    }
    
    public void paint(Graphics2D g) {
        if(tiles == null) {
            g.setColor(Color.red);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            TextLayout txt = new TextLayout("Votre carte est vide.", g.getFont(), g.getFontRenderContext());
            g.drawString("Votre carte est vide.", (gl.getWidth() - (int) txt.getBounds().getWidth()) / 2, gl.getHeight() / 2);
            return;
        }
        AffineTransform af = g.getTransform();
        for(int col = 0; col < tiles[area].length; col++) {
            for(int row = 0; row < tiles[area][col].length; row++) {
                if(tiles[area][col][row].getImage() != null && tileOnScreen(tiles[area][col][row], row, col)) {
                    Composite c = g.getComposite();
                    if(hero != null && tiles[area][col][row] instanceof AnimatedTile && !tiles[area][col][row].isSlope()) {
                        if(hero.getShape().intersects(row * 32, col * 32, 32, 32)) {
                            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                        }
                    }
                    if(rotation != 0) {
                        g.setTransform(AffineTransform.getRotateInstance(Math.toRadians(rotation), row * 32 - getX() + new java.util.Random().nextInt(33), col * 32 - getY() + new java.util.Random().nextInt(33)));
                    }
                    g.drawImage(tiles[area][col][row].getImage(), row * 32 - getX(), col * 32 - getY(), null);
                    g.setComposite(c);
                }
                if(GameResource.showOutlines() && tileOnScreen(tiles[area][col][row], row, col)) {
                    g.setColor(new Color(0, 0, 255, tiles[area][col][row].getImage() == null ? 63 : 255));
                    g.drawRect(row * 32 - getX() + tx, col * 32 - getY() + ty, 32, 32);
                }
                if(GameResource.showTilesLocations() && tileOnScreen(tiles[area][col][row], row, col)) {
                    g.setColor(Color.black);
                    g.setFont(new Font("Arial", Font.PLAIN, 15));
                    TextLayout txt1 = new TextLayout((row * 32)+" ", g.getFont(), g.getFontRenderContext());
                    TextLayout txt2 = new TextLayout(""+(col * 32), g.getFont(), g.getFontRenderContext());
                    TextLayout txt3 = new TextLayout(isSolid(row * 32, col * 32) ? "Solid" : "Not solid", g.getFont(), g.getFontRenderContext());
                    g.drawString((row * 32)+" ", row * 32 - getX(), col * 32 + (int) txt1.getBounds().getHeight() - getY());
                    g.drawString(""+(col * 32), row * 32 - getX(), col * 32 + (int) txt1.getBounds().getHeight() + (int) txt2.getBounds().getHeight() - getY());
                    g.setColor(isSolid(row * 32, col * 32) ? Color.red : new Color(0, 128, 0));
                    g.drawString(isSolid(row * 32, col * 32) ? "Solid" : "Not solid", row * 32 - getX(), col * 32 - getY() + (int) txt1.getBounds().getHeight() + (int) txt2.getBounds().getHeight() + (int) txt3.getBounds().getHeight());
                }
            }
        }
        g.setTransform(af);
        for(int i = 0; i < mobs[area].size(); i++) {
            mobs[area].get(i).paint(g);
        }
        for(int i = 0; i < objects[area].size(); i++) {
            objects[area].get(i).paint(g);
        }
        for(int i = 0; i < platforms[area].size(); i++) {
            platforms[area].get(i).paint(g);
        }
        for(int i = 0; i < flakes.size(); i++) {
            flakes.get(i).paint(g);
        }
        if(noLuminosity) {
            int i1 = 0;
            if(i1 == 1) {
            BufferedImage image = gl.getScreen();
            for(int h = 0; h < image.getHeight(); h++) {
                for(int w = 0; w < image.getWidth(); w++) {
                    Color color = new Color(image.getRGB(w, h));
                    int rc = color.getRed() - 75, gc = color.getGreen() - 75, bc = color.getBlue() - 75;
                    if(rc < 0) rc = 0;
                    if(gc < 0) gc = 0;
                    if(bc < 0) bc = 0;
                    image.setRGB(w, h, new Color(rc, gc, bc).getRGB());
                }
            }
            g.drawImage(image, 0, 0, null);
            g.setColor(new Color(0, 0, 0, 240));
            g.fillRect(0, 0, gl.getWidth() / 2 - 200, gl.getHeight());
            g.fillRect(gl.getWidth() / 2 + 200, 0, gl.getWidth() - (gl.getWidth() / 2 + 200), gl.getHeight());
            g.fillRect(gl.getWidth() / 2 - 200, 0, 400, gl.getHeight() / 2 - 200);
            g.fillRect(gl.getWidth() / 2 - 200, gl.getHeight() / 2 + 200, 400, gl.getHeight() - (gl.getHeight() / 2 + 200));
            for(int w = 0; w <= 200; w++) {
                double cos = (200 - w) / 200.0;
                double opp = 200 * Math.sin(Math.acos(cos));
                double topYValue = gl.getHeight() / 2 - opp;
                for(int h = 0; h <= 200; h++) {
                    if(gl.getHeight() / 2 - 200 + h < topYValue) {
                        g.fillRect(gl.getWidth() / 2 - 200 + w, gl.getHeight() / 2 - 200 + h, 1, 1);
                        g.fillRect(gl.getWidth() / 2 - 200 + w, gl.getHeight() / 2 + 200 - h, 1, 1);
                        g.fillRect(gl.getWidth() / 2 + 200 - w, gl.getHeight() / 2 - 200 + h, 1, 1);
                        g.fillRect(gl.getWidth() / 2 + 200 - w, gl.getHeight() / 2 + 200 - h, 1, 1);
                    }
                }
            }
            }
            else {
                BufferedImage image = gl.getScreen();
                g.setColor(new Color(0, 0, 0, 255 - luminosity));
                g.fillRect(0, 0, gl.getWidth(), gl.getHeight());
                int cx = (int) hero.getX() - getX() + hero.width / 2 - radius, cy = (int) hero.getY() - getY() + hero.height / 2 - radius;
                for(int h = 0; h < radius; h++) {
                    double width = Math.sqrt(radius * radius - (radius - (h + 1)) * (radius - (h + 1)));
                    int pxl = cx + radius - (int) width;
                    if(pxl < 0) pxl = 0;
                    if(pxl + width >= image.getWidth()) pxl = image.getWidth() - (int) width;
                    if(cy + h > - 1 && cy + h < image.getHeight()) g.drawImage(image.getSubimage(pxl, cy + h, (int) width, 1), pxl, cy + h, null);
                    if(cy + radius * 2 - (h + 1) > - 1 && cy + radius * 2 - (h + 1) < image.getHeight()) g.drawImage(image.getSubimage(pxl, cy + radius * 2 - (h + 1), (int) width, 1), pxl, cy + radius * 2 - (h + 1), null);
                    int pxr = cx + radius;
                    if(pxr < 0) pxr = 0;
                    if(pxr + (int) width >= image.getWidth()) pxr = image.getWidth() - (int) width;
                    if(cy + h > - 1 && cy + h < image.getHeight()) g.drawImage(image.getSubimage(pxr, cy + h, (int) width, 1), pxr, cy + h, null);
                    if(cy + radius * 2 - (h + 1) > - 1 && cy + radius * 2 - (h + 1) < image.getHeight()) g.drawImage(image.getSubimage(pxr, cy + radius * 2 - (h + 1), (int) width, 1), pxr, cy + radius * 2 - (h + 1), null);
                }
            }
        }
        if(crackling) {
            g.setColor(new Color(0, 0, 0, 255 / 2));
            g.fillRect(0, 0, gl.getWidth(), gl.getHeight());
            g.setColor(Color.gray);
            for(int h = 0; h < gl.getHeight(); h++) {
                for(int i = 0; i < new java.util.Random().nextInt(30) + 1; i++) g.fillRect(new java.util.Random().nextInt(gl.getWidth()), h, 1, 1);
            }
            int xl = new java.util.Random().nextInt(gl.getWidth() / 2), yl = new java.util.Random().nextInt(gl.getHeight());
            g.drawLine(xl, yl, xl + gl.getWidth() / 2, yl);
        }
        //g.setColor(Color.red);
        //g.drawLine(getMapWidth() - gl.getWidth() - getX(), 0, getMapWidth() - gl.getWidth() - getX(), getMapHeight());
        if(sunny) {
            g.setColor(new Color(255, 0, 0, 25));
            g.fillRect(0, 0, gl.getWidth(), gl.getHeight());
            for(int i = 0; i < 60; i++) {
                double xp = Math.sqrt(3600 - (60 - i) * (60 - i));
                for(int i1 = 0; i1 < (int) xp + 1; i1++) {
                    double dist = Math.sqrt(i1 * i1 + (59 - i) * (59 - i));
                    g.setColor(new Color(255, 255, 255, 255 - (int) (255.0 / 60.0 * dist)));
                    g.fillRect((int) i1, 59 - i, 1, 1);
                }
            }
        }
    }
    
    private boolean tileOnScreen(Tile tile, int row, int col) {
        int w = tile.getImage() == null ? 32 : tile.getImage().getWidth(), h = tile.getImage() == null ? 32 : tile.getImage().getHeight();
        return (row * 32 > getX() && row * 32 < getX() + gl.getWidth()) || (row * 32 + w > getX() && row * 32 + w < getX() + gl.getWidth()) &&
               (col * 32 > getY() && col * 32 < getY() + gl.getHeight()) || (col * 32 + h > getY() && col * 32 + h < getY() + gl.getHeight());
    }
    
    public int getMapWidth() {
        return tiles[area][0].length * 32;
    }
    
    public int getMapHeight() {
        return tiles[area].length * 32;
    }
    
    public int getMapHeight(int area) {
        return tiles[area].length * 32;
    }
    
    public Tile getTile(double x, double y) {
        if(tiles == null) return null;
        try {
            return tiles[area][(int) (y / 32.0)][(int) (x / 32.0)];
        } catch (ArrayIndexOutOfBoundsException ex) {
            return new Tile(null, true);
        }
    }
    
    public boolean isSolid(double x, double y) {
        if(tiles == null) return true;
        try {
            return tiles[area][(int) (y / 32.0)][(int) (x / 32.0)].isSolid();
        } catch (ArrayIndexOutOfBoundsException ex) {
            return true;
        }
    }
    
    public void setPosition(double x, double y) {
        this.x = (int) x;
        if(this.x < minx) {
            this.x = minx;
        }
        else if(this.x > maxx) {
            this.x = maxx;
        }
        if(this.getMapWidth() < gl.getWidth()) {
            this.x = -(gl.getWidth() / 2 - getMapWidth() / 2);
        }
        this.y = (int) y;
        if(this.y < 0) {
            this.y = 0;
        }
        else if(this.y > maxy) {
            this.y = maxy;
        }
        if(this.getMapHeight() < gl.getHeight()) {
            this.y = -(gl.getHeight() / 2 - getMapHeight() / 2);
        }
    }
    
    public void update() {
        if(tiles == null) return;
        for(int i = 0; i < tiles[area].length; i++) {
            for(int i1 = 0; i1 < tiles[area][i].length; i1++) {
                if(tiles[area][i][i1] instanceof SpecialTile || tiles[area][i][i1] instanceof AnimatedSlopeTile) {
                    if(tiles[area][i][i1] instanceof SpecialTile) ((SpecialTile) tiles[area][i][i1]).update();
                    else ((AnimatedSlopeTile) tiles[area][i][i1]).update();
                }
            }
        }
        if(snowing) {
            for(int i = 0; i < Math.round(5.0 / 9600.0 * getMapWidth()); i++) {
                flakes.add(new Flake(this, Math.random() * getMapWidth()));
            }
        }
        for(int i = 0; i < flakes.size(); i++) {
            flakes.get(i).update();
        }
    }
    
    public void updatePlatforms() {
        for(int i = 0; i < platforms[area].size(); i++) {
            platforms[area].get(i).update();
        }
    }
    
    public void updateMonsters() {
        for(int i = 0; i < mobs[area].size(); i++) {
            mobs[area].get(i).update();
        }
    }
    
    public void updateObjects() {
        for(int i = 0; i < objects[area].size(); i++) {
            objects[area].get(i).update();
        }
    }
    
    public void addPlatform(Platform p) {
        p.setArea(area);
        this.platforms[area].add(p);
    }
    
    public void addEnemy(Monster monster) {
        monster.setArea(area);
        this.mobs[area].add(monster);
    }
    
    public void removeEnemy(Monster monster) {
        for(int i = 0; i < mobs[area].size(); i++) {
            if(monster == mobs[area].get(i)) {
                mobs[area].remove(i);
            }
        }
    }
    
    public void removeObject(MapElement me) {
        for(int i = 0; i < objects[area].size(); i++) {
            if(me == objects[area].get(i)) {
                objects[area].remove(i);
            }
        }
    }
    
    public void removePlatform(Platform platform) {
        for(int i = 0; i < platforms[area].size(); i++) {
           if(platform == platforms[area].get(i)) {
               platforms[area].remove(i);
           }
        }
    }
    
    public void paintBackground(Graphics2D g) {
        if(bgs == null) return;
        for(int i = 0; i < bgs[area].length; i++) {
            bgs[area][i].paint(g);
        }
        if(shaking && intensity != 0) {
            tx = ty = 0;
            tx -= intensity;
            tx += new java.util.Random().nextInt(intensity * 2);
            ty -= intensity;
            ty += new java.util.Random().nextInt(intensity * 2);
        }
    }
    
    public void setArea(int area, int startingPoint) {
        setShaking(false, 0);
        setCrackling(false);
        setInTheDark(false, 0, 0);
        setSunny(false);
        flakes.clear();
        this.area = area;
        this.hero.setArea(area);
        this.hero.setCanAction(true);
        for(int i = 0; i < objects[area].size(); i++) {
            if(objects[area].get(i) instanceof StartingPoint) {
                if(((StartingPoint) objects[area].get(i)).getIndex() == startingPoint) {
                    this.hero.setX(objects[area].get(i).getX());
                    this.hero.setY(objects[area].get(i).getY());
                }
            }
        }
        minx = 0;
        maxx = getMapWidth() - gl.getWidth();
        maxy = getMapHeight() - gl.getHeight();
        JukeBox.stopSFX();
        hero.y += 4;
        hero.dx = 0;
        hero.dy = 0;
    }
    
    public int getArea() {
        return area;
    }
    
    public void setShaking(boolean shaking, int intensity) {
        this.shaking = shaking;
        this.intensity = intensity;
        if(!shaking) tx = ty = 0;
    }
    
    public boolean isShaking() {
        return this.shaking;
    }
    
    public int getIntensity() {
        return this.intensity;
    }
    
    public void setTile(Tile tile, double row, double col) {
        this.tiles[area][(int) col][(int) row] = tile;
    }
    
    public void addRotation(double r) {
        this.rotation += r;
    }
    
    public void setRotation(double r) {
        this.rotation = r;
    }
    
    public void addObject(MapElement me) {
        me.setArea(area);
        this.objects[area].add(me);
    }
    
    public void setBackgroundSize(int size) {
        for(int i = 0; i < bgs[area].length; i++) {
            bgs[area][i].setSize(size);
        }
    }
    
    public void setInTheDark(boolean inTheDark, int luminosity, int radius) {
        this.noLuminosity = inTheDark;
        this.luminosity = luminosity;
        this.radius = radius;
    }
    
    public boolean isInTheDark() {
        return this.noLuminosity;
    }
    
    public void setCrackling(boolean crakling) {
        this.crackling = crakling;
    }
    
    public void setBackground(BufferedImage image, int n) {
        bgs[area][n].setImage(image);
    }
    
    public void setCastleBackground(boolean b, int a) {
        bgs[a][0].setCastle(b);
    }
    
    public void setSunny(boolean b) {
        this.sunny = b;
    }
    
    public void setSnowing(boolean b) {
        this.snowing = b;
    }
    
    public void setMinX(int minx) {
        this.minx = minx;
    }
    
    public void setMaxX(int maxx) {
        this.maxx = maxx;
    }
    
    public void setMaxY(int maxy) {
        this.maxy = maxy;
    }
    
    public Wall getWallAt(double x, double y) {
        for(int i = 0; i < platforms[area].size(); i++) {
            if(platforms[area].get(i).getX() == x && platforms[area].get(i).getY() == y && platforms[area].get(i) instanceof Wall) {
                return (Wall) platforms[area].get(i);
            }
        }
        System.out.println("Returning null wall at "+x+" "+y);
        return null;
    }
    
    public boolean isSunny() {
        return this.sunny;
    }
}