
package osoroshi.teddyro.game.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JOptionPane;

public class SaveManager {
    
    private static Cipher cipher;
    private static SecretKeySpec skc;
    private static File file;
    private static boolean initialized = false;
    
    public static void init() {
        file = new File("save.bin");
        try {
            cipher = Cipher.getInstance("Blowfish");
            skc = new SecretKeySpec(Arrays.copyOf("Teddyro".getBytes(), 16), "Blowfish");
            if(!file.exists()) {
                file.createNewFile();
                cipher.init(Cipher.ENCRYPT_MODE, skc);
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                String t = "TeddyroSave\n";
                for(int i = 0; i < 5 * 3 + 2; i++) {
                    t += "0\n";
                }
                bos.write(cipher.doFinal(t.getBytes("UTF-8")));
                bos.flush();
                bos.close();
            }
            else {
                if(!getSave().startsWith("TeddyroSave")) {
                    int option = JOptionPane.showConfirmDialog(null, "It seems your save file is not correct. Do you want to create one ?", "Teddyro's Nightmare", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(option == JOptionPane.YES_OPTION) {
                        file.delete();
                        SaveManager.init();
                        JOptionPane.showMessageDialog(null, "Save file re created.", "Teddyro's Nightmare", JOptionPane.INFORMATION_MESSAGE);
                    }
                    else {
                        System.exit(0);
                    }
                }
            }
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IOException | BadPaddingException | IllegalBlockSizeException ex) {
            JOptionPane.showMessageDialog(null, "It seems your save file is corrupt. A new one will be created.", "Teddyro's Nightmare", JOptionPane.ERROR_MESSAGE);
            file.delete();
            SaveManager.init();
        }
        initialized = true;
    }
    
    public static void removeInitialization() {
        initialized = false;
    }
    
    public static boolean isInitialized() {
        return initialized;
    }
    
    public static void finish(int level) {
        try {
            String[] save = getSave().split("\n");
            System.out.println(level);
            save[level + 1] = "1";
            cipher.init(Cipher.ENCRYPT_MODE, skc);
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bos.write(cipher.doFinal(Arrays.toString(save).replace("[", "").replace("]", "").replace(", ", "\n").getBytes("UTF-8")));
            bos.flush();
            bos.close();
        } catch (IOException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(SaveManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static boolean isFinish(int level) {
        try {
            String save = getSave();
            return save.split("\n")[level + 1].equals("1");
        } catch (IOException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(SaveManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public static String getSave() throws IOException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        byte[] bytes = new byte[(int) file.length()], tempBytes = new byte[bytes.length];
        int total = 0, r = 0;
        cipher.init(Cipher.DECRYPT_MODE, skc);
        while((r = bis.read(tempBytes, 0, tempBytes.length)) != -1) {
            for(int i = 0; i < r; i++) {
                bytes[total + i] = tempBytes[i];
            }
            total += r;
        }
        bis.close();
        return new String(cipher.doFinal(bytes), "UTF-8")+"\n";
    }
}