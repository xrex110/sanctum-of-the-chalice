import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
public class SpriteLoader{
    
    private static final String RESOURCE_PATH = "../res/";

    /** Takes in a path in the form of "test.png", not "/test.png"
     *
     */
    public BufferedImage getImage(String path) {
        String location = /*RESOURCE_PATH + */path;
        File imgFile = new File(location);
        BufferedImage in = null;
        try{
            in = ImageIO.read(imgFile);
        }catch(IllegalArgumentException iae){
            System.out.println("Sprite loader: File input is null!");
            System.exit(1);
        }catch(IOException ioe){
            System.out.println("Sprite loader: File does not exist or is unreadable.");
            System.exit(1);
        }
        return in;
    }
    
}
