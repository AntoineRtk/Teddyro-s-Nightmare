package osoroshi.teddyro.game.utils;

public class Controler {
    
    private static boolean leftPressed, rightPressed, upPressed, downPressed, ctrlPressed, spacePressed, enterPressed, backspace, a, escape, f5, wPressed;
    
    public static void setLeft(boolean left) {
        Controler.leftPressed = left;
    }
    
    public static void setRight(boolean right) {
        Controler.rightPressed = right;
    }
    
    public static void setUp(boolean up) {
        Controler.upPressed = up;
    }
    
    public static void setDown(boolean down) {
        Controler.downPressed = down;
    }
    
    public static void setSpace(boolean space) {
        Controler.spacePressed = space;
    }
    
    public static void setControl(boolean ctrl) {
        Controler.ctrlPressed = ctrl;
    }
    
    public static void setW(boolean w) {
        Controler.wPressed = w;
    }
    
    public static void setEnter(boolean enter) {
        Controler.enterPressed = enter;
    }
    
    public static void setBackspace(boolean backspace) {
        Controler.backspace = backspace;
    }
    
    public static void setEscape(boolean escape) {
        Controler.escape = escape;
    }
    
    public static void setA(boolean a) {
        Controler.a = a;
    }
    
    public static void setF5(boolean f5) {
        Controler.f5 = f5;
    }
    
    public static boolean isLeft() {
        return Controler.leftPressed;
    }
    
    public static boolean isRight() {
        return Controler.rightPressed;
    }
    
    public static boolean isUp() {
        return Controler.upPressed;
    }
    
    public static boolean isDown() {
        return Controler.downPressed;
    }
    
    public static boolean isSpace() {
        return Controler.spacePressed;
    }
    
    public static boolean isControl() {
        return Controler.ctrlPressed;
    }
    
    public static boolean isW() {
        return Controler.wPressed;
    }
    
    public static boolean isEnter() {
        return Controler.enterPressed;
    }
    
    public static boolean isBackspace() {
        return Controler.backspace;
    }
    
    public static boolean isA() {
        return Controler.a;
    }
    
    public static boolean isEscape() {
        return Controler.escape;
    }
    
    public static boolean isF5() {
        return Controler.f5;
    }
}