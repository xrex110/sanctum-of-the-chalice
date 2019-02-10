import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
public class SpriteLoader{

    private static final String RESOURCE_PATH = "../res/";

    /* Stores all image files loaded */
    private final HashMap<String, BufferedImage> cache;

    public SpriteLoader(){
        cache = new HashMap<String, BufferedImage>();
    }


    /** Loads a sprite map from memory (same path format as getImage()) and splits it
     *  Stores output in a HashMap with keys "FileName0,1,2..."
     *
     *  X and Y specify the width and height of each cut
     *
     */
    public void loadSheet(String path, int x, int y) {
        BufferedImage sheet = getImage(path);
        
        if(sheet == null){
            System.out.printf("Unexpected null pointer returned after loading %s%n", path);
            System.exit(1);
        }

        int height = sheet.getHeight();
        int width = sheet.getWidth();
        int count = 0;

        if(width == 0 || height == 0){
            System.out.printf("Sprite Loader: loading %s resulted in an image with 0 as a dimension!%n");
            System.exit(1);
        }
        
        /* Check if the provided bounds evenly divide the image 
         * Also check if the bounds are <= 0 */
        if(height % y != 0 || width % x != 0 || x <= 0 || y <= 0){
            System.out.printf("Sprite Loader: invalid sheet intervals of %d and %d.%n", x, y);
            System.out.printf("\tCaused when loading file: %s (%dx%d)%n", path, width, height);
            System.exit(1);
        }

        for(int i = 0; i < width; i += x) {
            for(int j = 0; j < height; j += y) {
                BufferedImage temp = sheet.getSubimage(i, j, x, y);
                cache.put(path + count++, temp);
            }
        }
                
    }


    /** Takes in a path in the form of "test.png", not "/test.png"
     *  Purpose: Loads a sprite from the disk and returns it as a BufferedImage.
     */
    public BufferedImage getImage(String path) {

        if(path == null){
            System.out.printf("Sprite loader: path provided to getImage() is null!%n");
            System.exit(1);
        }

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

    public HashMap<String, BufferedImage> getCache(){
        return cache;
    }


}
