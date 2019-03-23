package game;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.text.SimpleDateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import game.*;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class SaveHandler {

    /* This class should contain ONLY static saving methods and fields */
    private static final String BIN_PATH = SaveHandler.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SS"); 
    public static void saveScreenshot(BufferedImage image) {
        new Thread(new Runnable() {
            @Override
                public void run() {
                    try {
                        File directory = new File(BIN_PATH + "screenshots");
                        if(!directory.exists())
                            directory.mkdir();
                        String fileName = formatter.format(new Date()) + ".png";
                        File outputFile = new File(directory.getPath() + "/" + fileName);
                        ImageIO.write(image, "png", outputFile);
                        System.out.println("Saved a screenshot to " + outputFile);
                        //System.out.println(format.format(LocalDate.now()));
                    } catch(Exception e) {
                        System.out.println("Failed to take screenshot.");
                        return;
                    }
                }
        }).start();
        new Settings();
    }
    public static Settings loadSettings() {
        try {
        File directory = new File(BIN_PATH + "data");
        if(!directory.exists())
            directory.mkdir();
        String fileName = "settings.save";
        
        FileInputStream fis = new FileInputStream(directory.getPath() + "/" + fileName);
        ObjectInputStream ois = new ObjectInputStream(fis);
        
        Settings settings = (Settings) ois.readObject();

        ois.close();
        fis.close();
        
        System.out.println("Successfully loaded settings");
        System.out.println(settings);
        return settings;

        } catch(Exception e) {
            System.out.println("Failed to load settings, initializing default values");
            return new Settings();
        } 
    }
    public static void saveSettings(Settings target) {
        try {
            System.out.println(target);
            File directory = new File(BIN_PATH + "data");
            if(!directory.exists())
                directory.mkdir();
            String fileName = "settings.save";
            
            FileOutputStream os = new FileOutputStream(directory.getPath() + "/" + fileName, false);
            ObjectOutputStream oos = new ObjectOutputStream(os);
            
            oos.writeObject(target);
            oos.flush();
            oos.close();
            os.close();
            System.out.println("Successfully saved settings.");

        } catch(Exception e) {
            System.out.println("Unable to save settings.");
        }
        
    }

}
