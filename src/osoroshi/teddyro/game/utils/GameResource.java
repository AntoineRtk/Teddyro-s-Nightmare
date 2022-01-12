package osoroshi.teddyro.game.utils;

import osoroshi.teddyro.game.objects.BlackScreen;
import osoroshi.teddyro.game.objects.mobs.boss.Snado;
import osoroshi.teddyro.game.objects.platforms.SpikeWall;
import osoroshi.teddyro.game.tilemap.SpeedTile;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import osoroshi.teddyro.game.animations.Animation;
import osoroshi.teddyro.game.objects.Capsule;
import osoroshi.teddyro.game.objects.Door;
import osoroshi.teddyro.game.objects.Elevator;
import osoroshi.teddyro.game.objects.FallingRockGenerator;
import osoroshi.teddyro.game.objects.FireworkGenerator;
import osoroshi.teddyro.game.objects.Flower;
import osoroshi.teddyro.game.objects.Launcher;
import osoroshi.teddyro.game.objects.MapObject;
import osoroshi.teddyro.game.objects.SnowGenerator;
import osoroshi.teddyro.game.objects.Star;
import osoroshi.teddyro.game.objects.StartingPoint;
import osoroshi.teddyro.game.objects.Sun;
import osoroshi.teddyro.game.objects.TV;
import osoroshi.teddyro.game.objects.Teleporter;
import osoroshi.teddyro.game.objects.camera.CameraControler;
import osoroshi.teddyro.game.objects.mobs.Bat;
import osoroshi.teddyro.game.objects.mobs.BlackHoleLauncher;
import osoroshi.teddyro.game.objects.mobs.Dinosaur;
import osoroshi.teddyro.game.objects.mobs.MeteoriteLauncher;
import osoroshi.teddyro.game.objects.mobs.RedSquid;
import osoroshi.teddyro.game.objects.mobs.Skeleton;
import osoroshi.teddyro.game.objects.mobs.Slug;
import osoroshi.teddyro.game.objects.mobs.Snail;
import osoroshi.teddyro.game.objects.mobs.Squid;
import osoroshi.teddyro.game.objects.platforms.CyanBlock;
import osoroshi.teddyro.game.objects.platforms.DissapearingPlatform;
import osoroshi.teddyro.game.objects.platforms.GreenBlock;
import osoroshi.teddyro.game.objects.platforms.MovingPlatform;
import osoroshi.teddyro.game.objects.platforms.SwitchEvent;
import osoroshi.teddyro.game.objects.platforms.Wall;
import osoroshi.teddyro.game.objects.pngs.Bunny;
import osoroshi.teddyro.game.objects.pngs.PNG;
import osoroshi.teddyro.game.states.GameLevel;
import osoroshi.teddyro.game.tilemap.AnimatedSlopeTile;
import osoroshi.teddyro.game.tilemap.AnimatedTile;
import osoroshi.teddyro.game.tilemap.CapsuleDirectionTile;
import osoroshi.teddyro.game.tilemap.CapsuleFireTile;
import osoroshi.teddyro.game.tilemap.IceTile;
import osoroshi.teddyro.game.tilemap.Ladder;
import osoroshi.teddyro.game.tilemap.LavaTile;
import osoroshi.teddyro.game.tilemap.Slope;
import osoroshi.teddyro.game.tilemap.SpikeTile;
import osoroshi.teddyro.game.tilemap.Tile;
import osoroshi.teddyro.game.tilemap.TileMap;
import osoroshi.teddyro.game.tilemap.TrampolineTile;
import osoroshi.teddyro.game.tilemap.WaterTile;

public class GameResource {
    
    private static ArrayList<BufferedImage> tileImages = new ArrayList<>();
    private static ArrayList<BufferedImage[]> heroSprites = new ArrayList<>();
    private static ArrayList<BufferedImage[]> dogSprites = new ArrayList<>();
    private static ArrayList<ArrayList<BufferedImage[]>> objectsSprites = new ArrayList<>();
    private static ArrayList<ArrayList<BufferedImage[]>> pngs = new ArrayList<>();
    private static ArrayList<BufferedImage> platformSprites = new ArrayList<>();
    private static BufferedImage[] smokes = new BufferedImage[15];
    private static BufferedImage tileImage, greenBlock, cyanBlock, starImage, world1Background, world2Background, world3Background, world4Background, world5Background, arrows, firework, blackhole, book, enter, spaceBar, flake, spikeBall, emote1, emote2, logo, mg1;
    private static ArrayList<BufferedImage> bgs = new ArrayList<>();
    private static boolean drawConvexShapes = false, showOutlines, showTilesLocations;
    private static Cursor cursor1, cursor2, cursor3;
    private static double x, y;
    private static String state = "Chargement du logo 0/8";
    private static double purcent = 0;
    
    public static long init() {
        try {
            logo = ImageIO.read(GameResource.class.getClassLoader().getResource("osoroshi/teddyro/resources/divers/logo2.png"));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Incorrect path.", "Teddyro's Nightmare", JOptionPane.ERROR_MESSAGE);
        }
        long ccTime, time = System.currentTimeMillis();
        state = "Chargement des tuiles 1/8";
        purcent = 0;
        try {
            tileImages.clear();
            tileImage = ImageIO.read(GameResource.class.getClassLoader().getResource("osoroshi/teddyro/resources/images/tileset/tiles.png"));
            int tileNumber = 0;
            for(int y = 0; y < tileImage.getHeight(); y += 32) {
                for(int x = 0; x < tileImage.getWidth(); x += 32) {
                    tileImages.add(tileImage.getSubimage(x, y, 32, 32));
                    purcent = 100.0 / 8.0 / (tileImage.getWidth() / 32.0 * (tileImage.getHeight() / 32.0)) * tileNumber;
                    tileNumber++;
                }
            }
            mg1 = ImageIO.read(GameResource.class.getClassLoader().getResource("osoroshi/teddyro/resources/images/tileset/minigame1.png"));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Incorrect path.", "Teddyro's Nightmare", JOptionPane.ERROR_MESSAGE);
        }
        System.out.println(System.currentTimeMillis() - time+" for tiles");
        ccTime = System.currentTimeMillis();
        state = "Chargement du héros 2/8";
        loadHeros();
        System.out.println(System.currentTimeMillis() - ccTime+" for hero");
        state = "Chargement des objets 3/8";
        loadObjects();
        ccTime = System.currentTimeMillis();
        state = "Chargement des mondes 5/8";
        loadWorlds();
        System.out.println(System.currentTimeMillis() - ccTime+" for world");
        ccTime = System.currentTimeMillis();
        state = "Chargement des utilitaires 6/8";
        loadDivers();
        System.out.println(System.currentTimeMillis() - ccTime+" for divers");
        ccTime = System.currentTimeMillis();
        state = "Chargement des arrières plans 7/8";
        loadBackgrounds();
        System.out.println(System.currentTimeMillis() - ccTime+" for backgrounds");
        ccTime = System.currentTimeMillis();
        state = "Chargement des musiques 8/8";
        loadMusic();
        purcent = 100.0;
        System.out.println(System.currentTimeMillis() - ccTime+" for musics");
        System.out.println("Chargement terminé en "+(System.currentTimeMillis() - time)+" ms en utilisant "+((Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory()) / (1024.0 * 1024.0))+" mo");
        return (System.currentTimeMillis() - time);
    }
    
    private static void loadBackgrounds() {
        bgs.clear();
        for(int i = 0; i < 13; i++) {
            if(i == 10) {
                BufferedImage image = new BufferedImage(684, 542, BufferedImage.TYPE_INT_RGB);
                double red = (208.0 - 168.0) / 406.5, green = (200.0 - 160.0) / 406.5, blue = (232.0 - 248.0) / 406.5;
                Graphics2D g = image.createGraphics();
                for(int h = 0; h < 406; h++) {
                    g.setColor(new Color(168 + (int) (red * h), 160 + (int) (green * h), 248 + (int) (blue * h)));
                    g.fillRect(0, h, 684, 1);
                }
                g.setColor(new Color(208, 200, 232));
                g.fillRect(0, 406, 684, 135);
                g.dispose();
                bgs.add(image);
                purcent += 100.0 / 8.0 / 12.0;
                continue;
            }
            try {
                bgs.add(ImageIO.read(GameResource.class.getClassLoader().getResource("osoroshi/teddyro/resources/images/backgrounds/bg"+i+".png")));
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "An error occured during background reading : "+ex, "Teddyro's Nightmare", JOptionPane.ERROR_MESSAGE);
                Logger.getLogger(GameResource.class.getName()).log(Level.SEVERE, null, ex);
                System.exit(0);
            }
            purcent += 100.0 / 8.0 / 12.0;
        }
    }
    
    private static void loadDivers() {
        try {
            cursor1 = Toolkit.getDefaultToolkit().createCustomCursor(new ImageIcon(GameResource.class.getClassLoader().getResource("osoroshi/teddyro/resources/divers/cursor1.png")).getImage(), new Point(16, 16), "X");
            cursor2 = Toolkit.getDefaultToolkit().createCustomCursor(new ImageIcon(GameResource.class.getClassLoader().getResource("osoroshi/teddyro/resources/divers/cursor2.png")).getImage(), new Point(16, 16), "X");
            cursor3 = Toolkit.getDefaultToolkit().createCustomCursor(new ImageIcon(GameResource.class.getClassLoader().getResource("osoroshi/teddyro/resources/divers/cursor3.png")).getImage(), new Point(16, 16), "X");
            arrows = ImageIO.read(GameResource.class.getClassLoader().getResource("osoroshi/teddyro/resources/divers/arrows.png"));
            firework = ImageIO.read(GameResource.class.getClassLoader().getResource("osoroshi/teddyro/resources/divers/fireworks.png"));
            blackhole = ImageIO.read(GameResource.class.getClassLoader().getResource("osoroshi/teddyro/resources/divers/blackhole.png"));
            book = ImageIO.read(GameResource.class.getClassLoader().getResource("osoroshi/teddyro/resources/divers/book.png"));
            enter = ImageIO.read(GameResource.class.getClassLoader().getResource("osoroshi/teddyro/resources/divers/enter.png"));
            spaceBar = ImageIO.read(GameResource.class.getClassLoader().getResource("osoroshi/teddyro/resources/divers/spacebar.png"));
            BufferedImage smoke = ImageIO.read(GameResource.class.getClassLoader().getResource("osoroshi/teddyro/resources/divers/smoke.png"));
            for(int h = 0; h < smoke.getHeight(); h += 256) {
                for(int w = 0; w < smoke.getWidth(); w += 256) {
                    smokes[h / 256 * 5 + w / 256] = smoke.getSubimage(w, h, 256, 256);
                }
            }
            drawEmote();
            starImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = starImage.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Path2D star = new Path2D.Double(); 
            star.moveTo(starImage.getWidth()/5, starImage.getHeight()-1); 
            star.lineTo(starImage.getWidth()/2, 0); 
            star.lineTo(starImage.getWidth()*4/5, starImage.getHeight()-1); 
            star.lineTo(0, 2*starImage.getHeight()/5); 
            star.lineTo(starImage.getWidth()-1, 2*starImage.getHeight()/5); 
            star.closePath();
            g.setColor(Color.blue);
            g.fill(star);
            g.dispose();
            purcent += 100.0 / 8.0;
        } catch (IOException ex) {
            Logger.getLogger(GameResource.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void drawEmote() {
        emote1 = new BufferedImage(14, 18, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = emote1.createGraphics();
        g.setColor(Color.white);
        g.fillRect(1, 1, 12, 13);
        g.fillRect(6, 14, 2, 1);
        g.fillRect(6, 15, 1, 1);
        g.setColor(new Color(85, 60, 0));
        g.fillRect(1, 0, 12, 1);
        g.fillRect(0, 1, 1, 12);
        g.fillRect(13, 1, 1, 12);
        g.fillRect(1, 13, 4, 1);
        g.fillRect(9, 13, 4, 1);
        g.fillRect(8, 14, 1, 1);
        g.fillRect(7, 15, 1, 1);
        g.fillRect(6, 16, 1, 1);
        g.fillRect(5, 14, 1, 4);
        g.setColor(new Color(177, 145, 0));
        g.fillRect(4, 3, 2, 3);
        g.fillRect(5, 2, 5, 2);
        g.fillRect(9, 3, 2, 3);
        for(int i = 0; i < 3; i++) {
            g.fillRect(8 - i, 6 + i, 2, 1);
        }
        g.fillRect(6, 10, 2, 2);
        g.dispose();
        emote2 = new BufferedImage(14, 18, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g1 = emote2.createGraphics();
        g1.drawImage(emote1, 0, 0, null);
        g1.setColor(Color.white);
        g1.fillRect(1, 1, 12, 12);
        g1.setColor(new Color(177, 145, 0));
        g1.fillRect(6, 2, 2, 7);
        g1.fillRect(6, 10, 2, 2);
        g1.dispose();
    }
    
    private static void loadWorlds() {
        try {
            world1Background = ImageIO.read(GameResource.class.getClassLoader().getResource("osoroshi/teddyro/resources/images/backgrounds/bgw1.png"));
            purcent += 100.0 / 8.0 / 5.0;
            world2Background = ImageIO.read(GameResource.class.getClassLoader().getResource("osoroshi/teddyro/resources/images/backgrounds/bgw2.png"));
            purcent += 100.0 / 8.0 / 5.0;
            world3Background = ImageIO.read(GameResource.class.getClassLoader().getResource("osoroshi/teddyro/resources/images/backgrounds/bgw3.png"));
            purcent += 100.0 / 8.0 / 5.0;
            world4Background = ImageIO.read(GameResource.class.getClassLoader().getResource("osoroshi/teddyro/resources/images/backgrounds/bgw4.png"));
            purcent += 100.0 / 8.0 / 5.0;
            world5Background = ImageIO.read(GameResource.class.getClassLoader().getResource("osoroshi/teddyro/resources/images/backgrounds/bgw5.png"));
            purcent += 100.0 / 8.0 / 5.0;
        } catch (IOException ex) {
            Logger.getLogger(GameResource.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void loadHeros() {
        try {
            heroSprites.clear();
            dogSprites.clear();
            pngs.clear();
            BufferedImage image = ImageIO.read(GameResource.class.getClassLoader().getResource("osoroshi/teddyro/resources/images/sprites/spritesheet.png"));
            heroSprites.add(new BufferedImage[]{image.getSubimage(0, 0, 45, 60)});
            BufferedImage[] walk = new BufferedImage[3];
            for(int i = 0; i < 45 * 3; i += 45) {
                walk[i / 45] = image.getSubimage(i, 60, 45, 60);
            }
            walk[0] = image.getSubimage(0, 60, 44, 60);
            walk[1] = image.getSubimage(44, 60, 43, 60);
            walk[2] = image.getSubimage(87, 60, 45, 60);
            heroSprites.add(walk);
            BufferedImage[] jump = new BufferedImage[2];
            for(int i = 0; i < 45 * 2; i += 45) {
                jump[i / 45] = image.getSubimage(i, 120, 45, 60);
            }
            heroSprites.add(jump);
            heroSprites.add(new BufferedImage[]{image.getSubimage(0, 180, 44, 71)});
            BufferedImage[] walkWithHat = new BufferedImage[3];
            for(int i = 0; i < 45 * 3; i += 45) {
                walkWithHat[i / 45] = image.getSubimage(i, 251, 45, 71);
            }
            heroSprites.add(walkWithHat);
            BufferedImage[] jumpWithHat = {image.getSubimage(44, 181, 43, 70), image.getSubimage(87, 181, 45, 70)};
            heroSprites.add(jumpWithHat);
            heroSprites.add(new BufferedImage[]{image.getSubimage(0, 322, 57, 60)});
            heroSprites.add(new BufferedImage[]{image.getSubimage(0, 382, 32, 32)});
            heroSprites.add(new BufferedImage[]{image.getSubimage(0, 414, 45, 60), image.getSubimage(45, 414, 55, 60)});
            heroSprites.add(new BufferedImage[]{image.getSubimage(100, 414, 45, 60), image.getSubimage(145, 414, 55, 60)});
            heroSprites.add(new BufferedImage[]{image.getSubimage(0, 474, 50, 41)});
            BufferedImage[] pickingUp = new BufferedImage[2];
            for(int i = 0; i < pickingUp.length; i++) {
                pickingUp[i] = image.getSubimage(i * 45, 515, 45, 60);
            }
            heroSprites.add(pickingUp);
            purcent += (100.0 / 8.0) / 2.0;
            BufferedImage image1 = ImageIO.read(GameResource.class.getClassLoader().getResource("osoroshi/teddyro/resources/images/sprites/dogssprites.png"));
            BufferedImage[] walkDog = new BufferedImage[4];
            for(int i = 0; i < 4; i++) {
                walkDog[i] = image1.getSubimage(i * 17, 0, 17, 31);
            }
            BufferedImage[] normalDog = new BufferedImage[2];
            for(int i = 0; i < 30; i += 15) {
                normalDog[i/15] = image1.getSubimage(i, 31, 15, 34);
            }
            BufferedImage[] walkDog2 = new BufferedImage[4];
            walkDog2[0] = image1.getSubimage(0, 65, 33, 25);
            walkDog2[1] = image1.getSubimage(33, 65, 38, 25);
            walkDog2[2] = image1.getSubimage(71, 65, 37, 25);
            walkDog2[3] = image1.getSubimage(108, 65, 37, 25);
            dogSprites.add(walkDog);
            dogSprites.add(normalDog);
            dogSprites.add(walkDog2);
            ArrayList<BufferedImage[]> pngDog = new ArrayList<>();
            pngDog.add(normalDog);
            pngDog.add(new BufferedImage[]{image1.getSubimage(0, 90, 34, 25)});
            pngs.add(pngDog);
            purcent += (100.0 / 8.0) / 2.0;
        } catch (IOException ex) {
            Logger.getLogger(GameResource.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void loadObjects() {
        try {
            objectsSprites.clear();
            platformSprites.clear();
            BufferedImage objectsImage = ImageIO.read(GameResource.class.getClassLoader().getResource("osoroshi/teddyro/resources/images/sprites/objectsspritessheet.png"));
            ArrayList<BufferedImage[]> snail = new ArrayList<>();
            BufferedImage[] idleSnail = new BufferedImage[3];
            for(int i = 0; i < 3; i++) { 
                idleSnail[i] = objectsImage.getSubimage(i * 22, 0, 22, 20);
            }
            snail.add(idleSnail);
            objectsSprites.add(snail);
            ArrayList<BufferedImage[]> bat = new ArrayList<>();
            BufferedImage[] movingBat = new BufferedImage[4];
            for(int i = 0; i < 4; i++) {
                movingBat[i] = objectsImage.getSubimage(i * 30, 20, 30, 15);
            }
            bat.add(movingBat);
            objectsSprites.add(bat);
            ArrayList<BufferedImage[]> door = new ArrayList<>();
            door.add(new BufferedImage[]{objectsImage.getSubimage(0, 35, 34, 60)});
            BufferedImage[] doorAnimation = new BufferedImage[3];
            for(int i = 0; i < 3; i++) {
                doorAnimation[i] = objectsImage.getSubimage(i * 34, 35, 34, 60);
            }
            door.add(doorAnimation);
            objectsSprites.add(door);
            platformSprites.add(objectsImage.getSubimage(256, 0, 64, 64));
            BufferedImage dirtPlatform = new BufferedImage(64, 64, BufferedImage.TYPE_INT_RGB);
            Graphics2D dirtGraphics = dirtPlatform.createGraphics();
            dirtGraphics.drawImage(objectsImage.getSubimage(256, 32, 64, 32), 0, 0, null);
            dirtGraphics.drawImage(objectsImage.getSubimage(256, 32, 64, 32), 0, 32, null);
            dirtGraphics.dispose();
            platformSprites.add(dirtPlatform);
            platformSprites.add(objectsImage.getSubimage(256, 64, 64, 64));
            platformSprites.add(objectsImage.getSubimage(128, 0, 128, 128));
            objectsSprites.add(null);
            ArrayList<BufferedImage[]> slug = new ArrayList<>();
            slug.add(new BufferedImage[]{objectsImage.getSubimage(41, 115, 23, 18)});
            slug.add(new BufferedImage[]{objectsImage.getSubimage(21, 115, 20, 18)});
            slug.add(new BufferedImage[]{objectsImage.getSubimage(0, 115, 21, 18)});
            objectsSprites.add(slug);
            ArrayList<BufferedImage[]> tv = new ArrayList<>();
            tv.add(new BufferedImage[]{objectsImage.getSubimage(0, 133, 48, 37)});
            objectsSprites.add(tv);
            ArrayList<BufferedImage[]> squid = new ArrayList<>();
            squid.add(new BufferedImage[]{objectsImage.getSubimage(0, 170, 14, 15), objectsImage.getSubimage(14, 170, 16, 14)});
            squid.add(new BufferedImage[]{objectsImage.getSubimage(30, 170, 16, 15), objectsImage.getSubimage(46, 170, 19, 14)});
            BufferedImage[] explosion = new BufferedImage[12];
            for(int i = 0; i < 192; i += 16) {
                explosion[i / 16] = objectsImage.getSubimage(65 + i, 170, 16, 16);
            }
            squid.add(explosion);
            objectsSprites.add(squid);
            ArrayList<BufferedImage[]> redSquid = new ArrayList<>();
            redSquid.add(new BufferedImage[]{objectsImage.getSubimage(0, 185, 14, 15), objectsImage.getSubimage(14, 185, 16, 14)});
            redSquid.add(new BufferedImage[]{objectsImage.getSubimage(30, 185, 16, 15), objectsImage.getSubimage(46, 185, 19, 14)});
            redSquid.add(explosion);
            redSquid.add(new BufferedImage[]{objectsImage.getSubimage(65, 185, 32, 32)});
            objectsSprites.add(redSquid);
            ArrayList<BufferedImage[]> switchEvent = new ArrayList<>();
            switchEvent.add(new BufferedImage[]{objectsImage.getSubimage(0, 217, 32, 24)});
            objectsSprites.add(switchEvent);
            ArrayList<BufferedImage[]> birds = new ArrayList<>();
            BufferedImage[] blueBird = {objectsImage.getSubimage(0, 241, 8, 7)}, greenBird = {objectsImage.getSubimage(8, 241, 8, 7)};
            birds.add(blueBird);
            birds.add(greenBird);
            objectsSprites.add(birds);
            ArrayList<BufferedImage[]> skeleton = new ArrayList<>();
            BufferedImage[] idleSkeleton = new BufferedImage[]{objectsImage.getSubimage(0, 249, 23, 32)};
            BufferedImage[] movingSkeleton = new BufferedImage[]{objectsImage.getSubimage(23, 249, 24, 32), objectsImage.getSubimage(47, 249, 25, 32)};
            skeleton.add(idleSkeleton);
            skeleton.add(movingSkeleton);
            objectsSprites.add(skeleton);
            ArrayList<BufferedImage[]> bluePNG = new ArrayList<>();
            bluePNG.add(new BufferedImage[]{objectsImage.getSubimage(0, 281, 30, 30), objectsImage.getSubimage(30, 281, 31, 31), objectsImage.getSubimage(61, 281, 30, 30)});
            bluePNG.add(new BufferedImage[]{objectsImage.getSubimage(91, 281, 22, 31), objectsImage.getSubimage(113, 281, 22, 32), objectsImage.getSubimage(135, 281, 22, 31)});
            pngs.add(bluePNG);
            ArrayList<BufferedImage[]> bearPNG = new ArrayList<>();
            bearPNG.add(new BufferedImage[]{objectsImage.getSubimage(0, 313, 33, 52)});
            bearPNG.add(new BufferedImage[]{objectsImage.getSubimage(33, 313, 55, 39)});
            pngs.add(bearPNG);
            ArrayList<BufferedImage[]> iceCreamPNG = new ArrayList<>();
            iceCreamPNG.add(new BufferedImage[]{objectsImage.getSubimage(0, 365, 41, 49)});
            iceCreamPNG.add(new BufferedImage[]{objectsImage.getSubimage(41, 365, 41, 49)});
            pngs.add(iceCreamPNG);
            ArrayList<BufferedImage[]> shamallowPNG = new ArrayList<>();
            shamallowPNG.add(new BufferedImage[]{objectsImage.getSubimage(0, 414, 26, 30)});
            shamallowPNG.add(new BufferedImage[]{objectsImage.getSubimage(26, 414, 24, 30)});
            pngs.add(shamallowPNG);
            ArrayList<BufferedImage[]> chocolatePNG = new ArrayList<>();
            chocolatePNG.add(new BufferedImage[]{objectsImage.getSubimage(0, 444, 21, 23)});
            chocolatePNG.add(new BufferedImage[]{objectsImage.getSubimage(21, 444, 17, 23)});
            pngs.add(chocolatePNG);
            ArrayList<BufferedImage[]> unicornPNG = new ArrayList<>();
            unicornPNG.add(new BufferedImage[]{objectsImage.getSubimage(0, 467, 27, 67)});
            unicornPNG.add(new BufferedImage[]{objectsImage.getSubimage(27, 467, 78, 63)});
            pngs.add(unicornPNG);
            ArrayList<BufferedImage[]> guard = new ArrayList<>();
            guard.add(new BufferedImage[]{objectsImage.getSubimage(0, 534, 31, 55)});
            guard.add(new BufferedImage[]{objectsImage.getSubimage(31, 534, 22, 56), objectsImage.getSubimage(53, 534, 22, 56)});
            pngs.add(guard);
            ArrayList<BufferedImage[]> horse = new ArrayList<>();
            horse.add(new BufferedImage[]{objectsImage.getSubimage(0, 589, 18, 32)});
            horse.add(new BufferedImage[]{objectsImage.getSubimage(18, 590, 32, 30)});
            pngs.add(horse);
            ArrayList<BufferedImage[]> chocolateVanillaPNG = new ArrayList<>();
            chocolateVanillaPNG.add(new BufferedImage[]{objectsImage.getSubimage(0, 621, 26, 58)});
            chocolateVanillaPNG.add(new BufferedImage[]{objectsImage.getSubimage(26, 621, 20, 58)});
            pngs.add(chocolateVanillaPNG);
            ArrayList<BufferedImage[]> chocolateBiscuitPNG = new ArrayList<>();
            chocolateBiscuitPNG.add(new BufferedImage[]{objectsImage.getSubimage(0, 679, 29, 60)});
            chocolateBiscuitPNG.add(new BufferedImage[]{objectsImage.getSubimage(29, 679, 15, 60)});
            pngs.add(chocolateBiscuitPNG);
            ArrayList<BufferedImage[]> strawberryPNG = new ArrayList<>();
            strawberryPNG.add(new BufferedImage[]{objectsImage.getSubimage(0, 740, 33, 49)});
            strawberryPNG.add(new BufferedImage[]{objectsImage.getSubimage(33, 740, 19, 49)});
            pngs.add(strawberryPNG);
            ArrayList<BufferedImage[]> blackHole = new ArrayList<>();
            blackHole.add(new BufferedImage[]{objectsImage.getSubimage(0, 789, 90, 90)});
            objectsSprites.add(blackHole);
            ArrayList<BufferedImage[]> meteorite = new ArrayList<>();
            meteorite.add(new BufferedImage[]{objectsImage.getSubimage(0, 879, 89, 148), objectsImage.getSubimage(89, 879, 83, 156), objectsImage.getSubimage(172, 879, 82, 158)});
            objectsSprites.add(meteorite);
            ArrayList<BufferedImage[]> bunnyPNG = new ArrayList<>();
            bunnyPNG.add(new BufferedImage[]{objectsImage.getSubimage(79, 1036, 16, 22)});
            bunnyPNG.add(new BufferedImage[]{objectsImage.getSubimage(29, 1036, 23, 15)});
            pngs.add(bunnyPNG);
            ArrayList<BufferedImage[]> bunny = new ArrayList<>();
            bunny.add(new BufferedImage[]{objectsImage.getSubimage(0, 1036, 29, 23), objectsImage.getSubimage(29, 1036, 23, 15), objectsImage.getSubimage(52, 1036, 27, 19)});
            objectsSprites.add(bunny);
            greenBlock = objectsImage.getSubimage(0, 1059, 64, 64);
            cyanBlock = objectsImage.getSubimage(64, 1059, 64, 64);
            ArrayList<BufferedImage[]> dinosaur = new ArrayList<>();
            BufferedImage[] dinosaurSprites = new BufferedImage[2];
            for(int i = 0; i < 60; i += 30) {
                dinosaurSprites[i / 30] = objectsImage.getSubimage(i, 1123, 30, (i == 0) ? 29 : 28);
            }
            dinosaur.add(dinosaurSprites);
            objectsSprites.add(dinosaur);
            ArrayList<BufferedImage[]> rock = new ArrayList<>();
            rock.add(new BufferedImage[]{objectsImage.getSubimage(0, 1152, 15, 15)});
            objectsSprites.add(rock);
            ArrayList<BufferedImage[]> flower = new ArrayList<>();
            flower.add(new BufferedImage[]{objectsImage.getSubimage(0, 1167, 45, 38)});
            objectsSprites.add(flower);
            ArrayList<BufferedImage[]> elevator = new ArrayList<>();
            elevator.add(new BufferedImage[]{objectsImage.getSubimage(0, 1205, 63, 63)});
            BufferedImage[] opening = new BufferedImage[3];
            for(int i = 0; i < 3; i++) {
                opening[i] = objectsImage.getSubimage(63 * i, 1205, 63, 63);
            }
            elevator.add(opening);
            objectsSprites.add(elevator);
            ArrayList<BufferedImage[]> pngSign = new ArrayList<>();
            pngSign.add(new BufferedImage[]{objectsImage.getSubimage(0, 1268, 32, 32)});
            pngSign.add(new BufferedImage[]{objectsImage.getSubimage(0, 1268, 32, 32)});
            pngs.add(pngSign);
            ArrayList<BufferedImage[]> wall = new ArrayList<>();
            wall.add(new BufferedImage[]{objectsImage.getSubimage(0, 1300, 32, 64)});
            objectsSprites.add(wall);
            ArrayList<BufferedImage[]> capsule = new ArrayList<>();
            capsule.add(new BufferedImage[]{objectsImage.getSubimage(0, 1364, 48, 64)});
            objectsSprites.add(capsule);
            BufferedImage bossDoor = ImageIO.read(GameResource.class.getClassLoader().getResource("osoroshi/teddyro/resources/divers/door.png"));
            ArrayList<BufferedImage[]> bossDoorAnimation = new ArrayList<>();
            BufferedImage[] bossDoors = new BufferedImage[30];
            int c = 0;
            for(int h = 0; h < bossDoor.getHeight(); h += 128) {
                for(int w = 0; w < bossDoor.getWidth(); w += 128) {
                    bossDoors[c] = bossDoor.getSubimage(w, h, 128, 128);
                    c++;
                }
            }
            bossDoorAnimation.add(new BufferedImage[]{bossDoor.getSubimage(0, 0, 128, 128)});
            bossDoorAnimation.add(bossDoors);
            objectsSprites.add(bossDoorAnimation);
            flake = objectsImage.getSubimage(0, 1428, 30, 30);
            spikeBall = objectsImage.getSubimage(0, 1458, 64, 64);
            BufferedImage snado = ImageIO.read(GameResource.class.getClassLoader().getResource("osoroshi/teddyro/resources/images/sprites/snadosprites.png"));
            ArrayList<BufferedImage[]> snadoAnimations = new ArrayList<>();
            BufferedImage[] idleSnado = new BufferedImage[3];
            for(int i = 0; i < 3; i++) { 
                idleSnado[i] = snado.getSubimage(330 * i, 0, 330, 300);
            }
            snadoAnimations.add(idleSnado);
            objectsSprites.add(snadoAnimations);
            BufferedImage castleDoor = ImageIO.read(GameResource.class.getClassLoader().getResource("osoroshi/teddyro/resources/divers/castledoor.png"));
            ArrayList<BufferedImage[]> castleDoorAnimation = new ArrayList<>();
            BufferedImage[] castleDoors = new BufferedImage[6];
            for(int i = 0; i < 6; i++) {
                castleDoors[i] = castleDoor.getSubimage(170 * i, 0, 170, 244);
            }
            castleDoorAnimation.add(new BufferedImage[]{castleDoor.getSubimage(0, 0, 170, 244)});
            castleDoorAnimation.add(castleDoors);
            objectsSprites.add(castleDoorAnimation);
            BufferedImage castleDoor2 = ImageIO.read(GameResource.class.getClassLoader().getResource("osoroshi/teddyro/resources/divers/castledoor1.png"));
            ArrayList<BufferedImage[]> castleDoorAnimation2 = new ArrayList<>();
            BufferedImage[] castleDoors2 = new BufferedImage[6];
            for(int i = 0; i < 6; i++) {
                castleDoors2[i] = castleDoor2.getSubimage(170 * i, 0, 170, 244);
            }
            castleDoorAnimation2.add(new BufferedImage[]{castleDoor2.getSubimage(0, 0, 170, 244)});
            castleDoorAnimation2.add(castleDoors2);
            objectsSprites.add(castleDoorAnimation2);
            purcent += 100.0 / 8.0;
        } catch (IOException ex) {
            Logger.getLogger(GameResource.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void loadMusic() {
        JukeBox.dischargeAll();
        JukeBox.load("winter", "winter");
        JukeBox.load("warning", "warning");
        JukeBox.load("world1", "world1");
        JukeBox.load("bbjump", "bbjump");
        JukeBox.load("menu", "menu");
        JukeBox.load("switch", "switch");
        JukeBox.load("death", "death");
        JukeBox.load("click", "click");
        JukeBox.load("platform", "platform");
        JukeBox.load("breath", "breath");
        JukeBox.load("menuaction", "menuaction");
        JukeBox.load("start", "start");
        JukeBox.load("shootingstars", "stars");
        JukeBox.load("elevator", "elevator");
        JukeBox.load("castle", "castle");
        JukeBox.load("bonusroom", "bonusroom");
        JukeBox.load("lightswitch", "lightswitch");
        JukeBox.load("woof", "woof");
        JukeBox.load("fallingspikeball", "fallingspikeball");
        JukeBox.load("endbonusroom", "endbonusroom");
        JukeBox.load("secretdiscovered", "secretdiscovered");
        JukeBox.load("flap", "flap");
        JukeBox.load("sand", "sand");
        JukeBox.load("dream", "dream");
        JukeBox.load("brick", "brick");
        JukeBox.load("win", "win");
        JukeBox.load("climatechange", "climatechange");
        JukeBox.load("firework", "firework");
        JukeBox.load("boss", "boss");
        JukeBox.load("smokeexplosion", "smokeexplosion");
        JukeBox.load("desert", "desert");
        JukeBox.load("capsule", "capsule");
        JukeBox.load("text", "text");
        JukeBox.load("thunder", "thunder");
        for(int i = 0; i < 3; i++) {
            JukeBox.load("applause"+i, "applause"+i);
        }
        for(int i = 0; i < 3; i++) {
            JukeBox.load("erupt"+i, "erupt"+i);
        }
        JukeBox.load("plains", "plains");
        JukeBox.load("catastrophe", "catastrophe");
        JukeBox.load("city", "city");
        JukeBox.load("festival", "festival");
        JukeBox.load("swamp", "swamp");
        JukeBox.load("volcano", "world3");
        JukeBox.load("tick", "tick");
        JukeBox.load("sadstory", "sadstory");
        JukeBox.load("fallingrocks", "fallingrocks");
        JukeBox.load("cutscene", "cutscene");
        JukeBox.load("abovethesky", "abovethesky");
        JukeBox.load("explosion", "explosion");
    }
    
    private static Tile forIndex(BufferedImage image, BufferedImage completeImage, int tileIndex) {
        switch(tileIndex) {
            case 0:
                return new Tile(image, false);
            case 3:
                return new Tile(image, false);
            case 4:
                return new Tile(image, false);
            case 5:
                return new Tile(image, false);
            case 6:
                return new Tile(image, false);
            case 7:
                return new Ladder(image);
            case 8:
                return new Ladder(image);
            case 9:
                return new TrampolineTile(image);
            case 10:
                BufferedImage[] waterTiles = new BufferedImage[9];
                for(int i = 0; i < 9; i++) {
                    waterTiles[i] = completeImage.getSubimage(i * 32, 32, 32, 32);
                }
                Animation animation = new Animation();
                animation.setFrames(waterTiles);
                animation.setDelay(1);
                return new WaterTile(animation);
            case 19:
                return new Tile(image, false);
            case 23:
                return new IceTile(image);
            case 25:
                return new Slope(image, 32, 16);
            case 26:
                return new Slope(image, 16, 0);
            case 27:
                return new Slope(image, 0, 16);
            case 28:
                return new Slope(image, 16, 32);
            case 30:
                return new Slope(image, 32, 0);
            case 31:
                return new Slope(image, 0, 32);
            case 34:
                return new IceTile(image);
            case 35:
                return new Tile(completeImage.getSubimage(160, 96, 32, 128), false);
            case 40:
                animation = new Animation();
                BufferedImage[] lavaTiles = new BufferedImage[4];
                for(int i = 0; i < 4; i++) {
                    lavaTiles[i] = completeImage.getSubimage(i * 32, 128, 32, 32);
                }
                animation.setFrames(lavaTiles);
                animation.setDelay(9);
                return new LavaTile(animation);
            case 44:
                animation = new Animation();
                animation.setDelay(-1);
                animation.setFrames(new BufferedImage[]{image});
                return new LavaTile(animation);
            case 50:
                return new SpikeTile(image, true);
            case 53:
                return new Tile(completeImage.getSubimage(96, 160, 64, 64), false);
            case 80:
                animation = new Animation();
                BufferedImage[] waterFallTiles = new BufferedImage[8];
                for(int i = 0; i < 8; i++) {
                    waterFallTiles[i] = completeImage.getSubimage(i * 32, 256, 32, 32);
                }
                animation.setFrames(waterFallTiles);
                animation.setDelay(9);
                return new AnimatedTile(false, animation);
            case 92:
                return new Tile(image, false);
            case 96:
                return new Slope(image, 0, 16);
            case 97:
                return new Slope(image, 16, 32);
            case 98:
                return new Slope(image, 32, 16);
            case 99:
                return new Slope(image, 16, 0);
            case 100:
                animation = new Animation();
                BufferedImage[] greenToRed = new BufferedImage[2];
                for(int i = 0; i < 2; i++) {
                    greenToRed[i] = completeImage.getSubimage(i * 32, 320, 32, 32);
                }
                animation.setFrames(greenToRed);
                animation.setDelay(12);
                return new AnimatedTile(false, animation);
            case 101:
                animation = new Animation();
                BufferedImage[] redToGreen = new BufferedImage[2];
                for(int i = 1; i > -1; i--) {
                    redToGreen[i - ((i == 1) ? i : -1)] = completeImage.getSubimage(i * 32, 320, 32, 32);
                }
                animation.setFrames(redToGreen);
                animation.setDelay(12);
                return new AnimatedTile(false, animation);
            case 102:
                animation = new Animation();
                BufferedImage[] redToGreenFlipped = new BufferedImage[2];
                for(int i = 0; i < 2; i++) {
                    redToGreenFlipped[i] = completeImage.getSubimage(i * 32 + 64, 320, 32, 32);
                }
                animation.setFrames(redToGreenFlipped);
                animation.setDelay(12);
                return new AnimatedTile(false, animation);
            case 103:
                animation = new Animation();
                BufferedImage[] greenToRedFlipped = new BufferedImage[2];
                for(int i = 1; i > -1; i--) {
                    greenToRedFlipped[i - ((i == 1) ? i : -1)] = completeImage.getSubimage(i * 32 + 64, 320, 32, 32);
                }
                animation.setFrames(greenToRedFlipped);
                animation.setDelay(12);
                return new AnimatedTile(false, animation);
            case 104:
                animation = new Animation();
                BufferedImage[] asteroidTiles = new BufferedImage[360/10];
                BufferedImage rotatedAsteroid;
                for(int i = 0; i < asteroidTiles.length; i++) {
                    rotatedAsteroid = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g = rotatedAsteroid.createGraphics();
                    g.setTransform(AffineTransform.getRotateInstance(Math.toRadians(i * 10), 16, 16));
                    g.drawImage(image, 0, 0, null);
                    g.dispose();
                    asteroidTiles[i] = rotatedAsteroid;
                }
                animation.setFrames(asteroidTiles);
                animation.setDelay(0);
                return new AnimatedTile(true, animation);
            case 105:
                BufferedImage galaxyTile = new BufferedImage(32, 32, BufferedImage.TYPE_INT_RGB);
                Graphics2D g = galaxyTile.createGraphics();
                g.setColor(Color.black);
                g.fillRect(0, 0, 32, 32);
                g.setColor(Color.white);
                for(int i = 0; i < 2; i++) {
                    g.fillRect(new java.util.Random().nextInt(32), new java.util.Random().nextInt(32), 1, 1);
                }
                g.dispose();
                return new Tile(galaxyTile, true);
            case 106:
                return new Slope(image, 32, 0);
            case 107:
                return new Slope(image, 0, 32);
            case 117:
                return new Tile(completeImage.getSubimage(224, 352, 96, 96), true);
            case 150:
                return new Slope(image, 32, 16);
            case 151:
                return new Slope(image, 16, 0);
            case 152:
                return new Slope(image, 32, 0);
            case 153:
                return new Slope(image, 0, 32);
            case 154:
                return new Slope(image, 0, 16);
            case 155:
                return new Slope(image, 16, 32);
            case 160:
                animation = new Animation();
                BufferedImage[] discoTile = new BufferedImage[8];
                for(int i = 0; i < 8; i++) {
                    discoTile[i] = completeImage.getSubimage(i * 32, 512, 32, 32);
                }
                animation.setFrames(discoTile);
                animation.setDelay(6);
                return new AnimatedTile(true, animation);
            case 168:
                animation = new Animation();
                discoTile = new BufferedImage[8];
                for(int i = 0; i < 8; i++) {
                    discoTile[i] = get45DegreesImage(completeImage.getSubimage(i * 32, 512, 32, 32), true);
                }
                animation.setFrames(discoTile);
                animation.setDelay(6);
                return new AnimatedSlopeTile(animation, 32, 0);
            case 169:
                animation = new Animation();
                discoTile = new BufferedImage[8];
                for(int i = 0; i < 8; i++) {
                    discoTile[i] = get45DegreesImage(completeImage.getSubimage(i * 32, 512, 32, 32), false);
                }
                animation.setFrames(discoTile);
                animation.setDelay(6);
                return new AnimatedSlopeTile(animation, 0, 32);
            case 170:
                return new Tile(completeImage.getSubimage(0, 544, 256, 32), false);
            case 179:
                return new SpikeTile(image, true);
            case 180:
                return new Tile(completeImage.getSubimage(0, 576, 256, 64), false);
            case 188:
                return new Tile(image, false);
            case 189:
                return new SpikeTile(image, true);
            case 198:
                return new CapsuleDirectionTile(null, 0);
            case 199:
                return new Tile(image, false);
            case 210:
                return new Tile(completeImage.getSubimage(0, 672, 256, 256), false);
            case 218:
                return new Tile(completeImage.getSubimage(256, 672, 64, 128), false);
            case 258:
                return new CapsuleDirectionTile(null, -1);
            case 259:
                return new CapsuleDirectionTile(null, 1);
            case 268:
                return new Tile(completeImage.getSubimage(256, 832, 64, 96), false);
            case 301:
                return new Slope(image, 0, 32);
            case 302:
                return new Slope(image, 32, 0);
            case 303:
                return new CapsuleFireTile(image, true);
            case 304:
                BufferedImage i = new BufferedImage(32, 64, BufferedImage.TYPE_INT_ARGB);
                g = i.createGraphics();
                g.drawImage(image, 0, 0, null);
                g.drawImage(completeImage.getSubimage(160, 960, 32, 32), 0, 32, null);
                g.dispose();
                return new CapsuleDirectionTile(i, 2);
            case 305:
                return new CapsuleDirectionTile(null, -2);
            case 306:
                return new Tile(completeImage.getSubimage(192, 960, 32, 64), false);
            case 307:
                return new Slope(image, 32, 16);
            case 308:
                return new Slope(image, 16, 0);
            case 309:
                return new SpeedTile();
            case 311:
                return new Tile(image, false);
            case 312:
                return new Tile(image, false);
            case 313:
                return new Tile(image, false);
            case 314:
                return new Tile(image, false);
            case 317:
                return new Slope(image, 0, 16);
            case 318:
                return new Slope(image, 16, 32);
            case 320:
                return new Tile(completeImage.getSubimage(0, 1024, 64, 96), false);
            case 325:
                return new Tile(completeImage.getSubimage(192, 1024, 128, 128), true);
            case 345:
                return new Tile(completeImage.getSubimage(128, 1280, 128, 96), true);
            /*case 350:
                return new Tile(image, false);
            case 351:
                return new Tile(image, false);
            case 352:
                return new Tile(image, false);*/
            case 353:
                return new Slope(image, 32, (int) (32.0 - 32.0 / 6.0 * 32.0));
            case 354:
                return new Slope(image, (int) (32.0 - 32.0 / 6.0 * 32.0), 32);
            /*case 355:
                return new Tile(image, false);
            case 356:
                return new Tile(image, false);
            case 357:
                return new Tile(image, false);*/
            /*case 360:
                return new Tile(image, false);
            case 361:
                return new Tile(image, false);*/
            case 362:
                return new Slope(image, 32, (int) (32.0 - 32.0 / 25.0 * 32.0));
            case 365:
                return new Slope(image, (int) (32.0 - 32.0 / 25.0 * 32.0), 32);
            /*case 366:
                return new Tile(image, false);
            case 367:
                return new Tile(image, false);
            case 370:
                return new Tile(image, false);*/
            case 371:
                return new Slope(image, 32, (int) (32.0 - 32.0 / 25.0 * 32.0));
            case 376:
                return new Slope(image, (int) (32.0 - 32.0 / 25.0 * 32.0), 32);
            /*case 377:
                return new Tile(image, false);*/
            case 380:
                return new Slope(image, 32, (int) (32.0 - 32.0 / 25.0 * 32.0));
            case 387:
                return new Slope(image, (int) (32.0 - 32.0 / 25.0 * 32.0), 32);
            case 434:
                return new SpikeTile(completeImage.getSubimage(128, 1376, 128, 128), false);
            case 435:
                return new SpikeTile(null, true);
            case 476:
                return new Slope(image, 32, 16);
            case 477:
                return new Slope(image, 16, 0);
            case 484:
                return new Slope(image, 32, 16);
            case 485:
                return new Slope(image, 16, 0);
            case 492:
                return new Slope(image, 32, 16);
            case 493:
                return new Slope(image, 16, 0);
            case 500:
                return new Slope(image, 32, 16);
            case 501:
                return new Slope(image, 16, 0);
            case 510:
                return new Slope(image, 0, 16);
            case 511:
                return new Slope(image, 16, 32);
            case 522:
                return new Slope(image, 0, 16);
            case 523:
                return new Slope(image, 16, 32);
            case 534:
                return new Slope(image, 0, 16);
            case 535:
                return new Slope(image, 16, 32);
            case 546:
                return new Slope(image, 0, 16);
            case 547:
                return new Slope(image, 16, 32);
            case 553:
                return new Slope(image, 32, 0);
            case 562:
                return new Slope(image, 32, 0);
            case 571:
                return new Slope(image, 32, 0);
            case 580:
                return new Slope(image, 32, 0);
            case 594:
                return new Slope(image, 0, 32);
            case 605:
                return new Slope(image, 0, 32);
            case 616:
                return new Slope(image, 0, 32);
            case 627:
                return new Slope(image, 0, 32);
            case 527:
                return new Slope(image, 0, 32);
            case 670:
                return new Tile(completeImage.getSubimage(0, 2144, 128, 96), false);
            default:
                return new Tile(image, true);
        }
    }
    
    private static BufferedImage get45DegreesImage(BufferedImage image, boolean leftToRight) {
        BufferedImage newImage = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = newImage.createGraphics();
        GeneralPath gp = new GeneralPath();
        if(leftToRight) {
            gp.moveTo(0, 32);
            gp.lineTo(32, 0);
            gp.lineTo(32, 32);
        }
        else {
            gp.moveTo(0, 0);
            gp.lineTo(32, 32);
            gp.lineTo(0, 32);
        }
        gp.closePath();
        g.setClip(gp);
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return newImage;
    }
    
    public static Tile getTile(int index) {
        return forIndex(tileImages.get(index), tileImage, index);
        /*if(index == 105) {
            return forIndex(null, null, 105);
        }
        return tiles.get(index).clone();*/
    }
    
    public static ArrayList<BufferedImage[]> getHeroSprites() {
        return (ArrayList<BufferedImage[]>) heroSprites.clone();
    }
    
    public static BufferedImage getHeroPortrait() {
        return heroSprites.get(0)[0].getSubimage(0, 0, 45, 40);
    }
    
    public static BufferedImage getStarImage(int w, int h) {
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.drawImage(starImage, 0, 0, w, h, null);
        g.dispose();
        return image;
    }
    
    public static Path2D getStarPath(int w, int h) {
        Path2D star = new Path2D.Double(); 
        star.moveTo(w/5.0, h-1.0); 
        star.lineTo(w/2.0, 0); 
        star.lineTo(w*4.0/5.0, h-1); 
        star.lineTo(0, 2.0*h/5.0); 
        star.lineTo(w-1.0, 2.0*h/5.0); 
        star.closePath();
        return star;
    }
    
    public static ArrayList<BufferedImage[]> getSnailSprites() {
        return objectsSprites.get(0);
    }
    
    public static ArrayList<BufferedImage[]> getBatSprites() {
        return objectsSprites.get(1);
    }
    
    public static ArrayList<BufferedImage[]> getDoorSprites() {
        return objectsSprites.get(2);
    }
    
    public static BufferedImage getPlatformSprites(int sprites) {
        return platformSprites.get(sprites);
    }
    
    public static ArrayList<BufferedImage[]> getSlugSprites() {
        return objectsSprites.get(4);
    }
    
    public static ArrayList<BufferedImage[]> getTVSprites() {
        return objectsSprites.get(5);
    }
    
    public static ArrayList<BufferedImage[]> getSquidSprites() {
        return objectsSprites.get(6);
    }
    
    public static ArrayList<BufferedImage[]> getRedSquidSprites() {
        return objectsSprites.get(7);
    }
    
    public static ArrayList<BufferedImage[]> getSwitchEventSprites() {
        return objectsSprites.get(8);
    }
    
    public static ArrayList<BufferedImage[]> getBirdsSprites() {
        return objectsSprites.get(9);
    }
    
    public static ArrayList<BufferedImage[]> getSkeletonSprites() {
        return objectsSprites.get(10);
    }
    
    public static ArrayList<BufferedImage[]> getBlackHoleSprites() {
        return objectsSprites.get(11);
    }
    
    public static ArrayList<BufferedImage[]> getMeoritSprites() {
        return objectsSprites.get(12);
    }
    
    public static ArrayList<BufferedImage[]> getBunnySprites() {
        return objectsSprites.get(13);
    }
    
    public static ArrayList<BufferedImage[]> getDinosaurSprites() {
        return objectsSprites.get(14);
    }
    
    public static ArrayList<BufferedImage[]> getRockSprites() {
        return objectsSprites.get(15);
    }
    
    public static ArrayList<BufferedImage[]> getFlowerSprites() {
        return objectsSprites.get(16);
    }
    
    public static ArrayList<BufferedImage[]> getElevatorSprites() {
        return objectsSprites.get(17);
    }
    
    public static ArrayList<BufferedImage[]> getWallSprites() {
        return objectsSprites.get(18);
    }
    
    public static ArrayList<BufferedImage[]> getCapsuleSprites() {
        return objectsSprites.get(19);
    }
    
    public static ArrayList<BufferedImage[]> getBossDoorSprites() {
        return objectsSprites.get(20);
    }
    
    public static ArrayList<BufferedImage[]> getSnadoSprites() {
        return objectsSprites.get(21);
    }
    
    public static ArrayList<BufferedImage[]> getCastleDoorSprites() {
        return objectsSprites.get(22);
    }
    
    public static ArrayList<BufferedImage[]> getCastleDoorSprites2() {
        return objectsSprites.get(23);
    }
    
    public static ArrayList<BufferedImage[]> getDogSprites() {
        return dogSprites;
    }
    
    public static BufferedImage getFirework(int f) {
        return firework.getSubimage(f * 170, 0, 170, 170);
    }
    
    public static BufferedImage getBackground(int number) {
        try {
            return bgs.get(number);
        } catch (Exception ex) {
            return null;
        }
    }
    
    public static MapObject getObject(GameLevel gl, TileMap tileMap, int id, double x, double y, String params) {
        if(id == 70) {
            return new Snail(gl, tileMap, x, y);
        }
        if(id == 71) {
            return new Bat(gl, tileMap, x, y);
        }
        if(id == 72) {
            return new Slug(gl, tileMap, x, y);
        }
        if(id == 73) {
            return new Star(gl, tileMap, x, y, params);
        }
        if(id == 74) {
            return new MovingPlatform(gl, tileMap, x, y, params, 0);
        }
        if(id == 75) {
            return new FireworkGenerator(gl, tileMap, x, y, params);
        }
        if(id == 76) {
            return new TV(gl, tileMap, x, y, params);
        }
        if(id == 77) {
            return new Door(gl, tileMap, x, y, params);
        }
        if(id == 78) {
            return new Squid(gl, tileMap, x, y);
        }
        if(id == 79) {
            return new RedSquid(gl, tileMap, x, y);
        }
        if(id == 140) {
            return new Teleporter(gl, tileMap, x, y, params);
        }
        if(id == 141) {
            return new SwitchEvent(gl, tileMap, x, y, params);
        }
        if(id == 142) {
            return new Skeleton(gl, tileMap, x, y);
        }
        if(id == 143) {
            return new PNG(gl, tileMap, x, y, params);
        }
        if(id == 144) {
            return new StartingPoint(gl, tileMap, x, y, params);
        }
        if(id == 145) {
            return new BlackHoleLauncher(gl, tileMap, x, y, params);
        }
        if(id == 146) {
            return new MeteoriteLauncher(gl, tileMap, x, y, params);
        }
        if(id == 147) {
            return new Bunny(gl, tileMap, x, y, params);
        }
        if(id == 148) {
            return new GreenBlock(gl, tileMap, x, y, params);
        }
        if(id == 149) {
            return new CyanBlock(gl, tileMap, x, y, params);
        }
        if(id == 200) {
            return new Dinosaur(gl, tileMap, x, y);
        }
        if(id == 201) {
            return new FallingRockGenerator(gl, tileMap, x, y);
        }
        if(id == 202) {
            return new Launcher(gl, tileMap, x, y, params);
        }
        if(id == 203) {
            return new Flower(gl, tileMap, x, y);
        }
        if(id == 204) {
            return new Sun(gl, tileMap, x, y);
        }
        if(id == 205) {
            return new CameraControler(gl, tileMap, x, y, params);
        }
        if(id == 206) {
            return new Elevator(gl, tileMap, x, y, params);
        }
        if(id == 207) {
            return new MovingPlatform(gl, tileMap, x, y, params, 0);
        }
        if(id == 208) {
            return new MovingPlatform(gl, tileMap, x, y, params, 1);
        }
        if(id == 209) {
            return new MovingPlatform(gl, tileMap, x, y, params, 2);
        }
        if(id == 290) {
            return new DissapearingPlatform(gl, tileMap, x, y, params);
        }
        if(id == 291) {
            return new Wall(gl, tileMap, x, y);
        }
        if(id == 292) {
            return new Capsule(gl, tileMap, x, y, params);
        }
        if(id == 293) {
            return new SnowGenerator(gl, tileMap, x, y);
        }
        if(id == 294) {
            return new Door(gl, tileMap, x, y, params, 0);
        }
        if(id == 295) {
            return new SpikeWall(gl, tileMap, x, y, params);
        }
        if(id == 296) {
            return new Snado(gl, tileMap, x, y, params);
        }
        if(id == 297) {
            return new BlackScreen(gl, tileMap, x, y);
        }
        if(id == 335) {
            return new MovingPlatform(gl, tileMap, x, y, params, 3);
        }
        return null;
    }
    
    public static BufferedImage getGreenBlock() {
        return greenBlock;
    }
    
    public static BufferedImage getCyanBlock() {
        return cyanBlock;
    }
    
    public static BufferedImage getWorld1Background() {
        return world1Background;
    }
    
    public static BufferedImage getWorld2Background() {
        return world2Background;
    }
    
    public static BufferedImage getWorld3Background() {
        return world3Background;
    }
    
    public static BufferedImage getWorld4Background() {
        return world4Background;
    }
    
    public static BufferedImage getWorld5Background() {
        return world5Background;
    }
    
    public static BufferedImage getArrows() {
        return arrows;
    }
    
    public static BufferedImage getBook() {
        return book;
    }
    
    public static BufferedImage getEnter() {
        return enter;
    }
    
    public static BufferedImage getSpaceBar() {
        return spaceBar;
    }
    
    public static BufferedImage getFlake() {
        return flake;
    }
    
    public static BufferedImage getSpikeBall() {
        return spikeBall;
    }
    
    public static void setDrawConvexShapes(boolean b) {
        GameResource.drawConvexShapes = b;
    }
    
    public static boolean drawConvexShapes() {
        return GameResource.drawConvexShapes;
    }
    
    public static Cursor getCursor(int c) {
        switch(c) {
            case 0:
                return cursor1;
            case 1:
                return cursor2;
            default:
                return cursor3;
        }
    }
    
    public static ArrayList<BufferedImage[]> getPNG(int png) {
        return pngs.get(png);
    }
    
    public static BufferedImage getBlackHole() {
        return blackhole;
    }
    
    public static BufferedImage getEmote1() {
        return emote1;
    }
    
    public static BufferedImage getEmote2() {
        return emote2;
    }
    
    public static void displayOutlines() {
        GameResource.showOutlines = !GameResource.showOutlines;
    }
    
    public static void displayTilesLocations() {
        GameResource.showTilesLocations = !GameResource.showTilesLocations;
    }
    
    public static boolean showOutlines() {
        return showOutlines;
    }
    
    public static boolean showTilesLocations() {
        return showTilesLocations;
    }
    
    public static void setPosition(double x, double y) {
        GameResource.x = x;
        GameResource.y = y;
    }
    
    public static int getX() {
        return (int) GameResource.x;
    }
    
    public static int getY() {
        return (int) GameResource.y;
    }
    
    public static BufferedImage getXFlippedInstance(BufferedImage image) {
        BufferedImage flipped = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        Graphics2D g = flipped.createGraphics();
        g.drawImage(image, flipped.getWidth(), 0, -flipped.getWidth(), flipped.getHeight(), null);
        g.dispose();
        return flipped;
    }
    
    public static BufferedImage getYFlippedInstance(BufferedImage image) {
        BufferedImage flipped = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        Graphics2D g = flipped.createGraphics();
        g.drawImage(image, 0, flipped.getHeight(), flipped.getWidth(), -flipped.getHeight(), null);
        g.dispose();
        return flipped;
    }
    
    public static BufferedImage[] getSmoke() {
        return smokes;
    }
    
    public static String getState() {
        return state;
    }
    
    public static void setPurcent(double purcent) {
        GameResource.purcent = purcent;
    }
    
    public static int getPurcent() {
        return (int) Math.round(purcent);
    }
    
    public static BufferedImage getLogo() {
        return logo;
    }
    
    public static BufferedImage getMinigame1Tile(int i) {
        return mg1.getSubimage(i * 32, 0, 32, 32);
    }
}