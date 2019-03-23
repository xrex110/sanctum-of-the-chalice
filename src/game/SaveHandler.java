package game;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.text.SimpleDateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

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
    }

}
