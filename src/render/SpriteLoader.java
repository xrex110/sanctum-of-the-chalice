package render;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.io.Serializable;
public class SpriteLoader implements Serializable {

    private static final String RESOURCE_PATH = "../res/";

    /* Stores all image files loaded */
    private transient HashMap<String, BufferedImage> cache;

    public SpriteLoader(){
        cache = new HashMap<String, BufferedImage>();
    }

	/**
	 *	Searches the cache for the requested image and its frame (0-indexed) and returns it
	 *	If not found within cache, attempts to load the image from its spritesheet (a cacheSheet call) and
	 *	then return it
	 *
	 */
    
    public BufferedImage getSprite(String requested, int frame, int xSize, int ySize) {
        if(cache == null) cache = new HashMap<String, BufferedImage>();
		String cacheQuery = requested + frame;
		//System.out.println("Searching for " + cacheQuery + " within cache");
		if(cache.containsKey(cacheQuery)) {
			//System.out.println(cacheQuery + " was successfully found within cache");
			return cache.get(cacheQuery);	
		}

		cacheSheet(requested, xSize, ySize);
		if(cache.containsKey(cacheQuery)) {
			//System.out.println(cacheQuery + " had to be loaded into the cache and returned");
			return cache.get(cacheQuery);	
		}
		else {
			System.out.println("Invalid frame requested.\n");
			System.exit(1);
		}
		return null;
    } 

    /** Loads a sprite map from memory (same path format as getImage()) and splits it
     *  Stores output in a HashMap with keys "FileName0,1,2..."
     *
     *  X and Y specify the width and height of each cut
     *
     */
    public int cacheSheet(String path, int x, int y) {
        if(cache == null) cache = new HashMap<String, BufferedImage>();
        BufferedImage sheet = getImage(RESOURCE_PATH + path);
        
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

		//TODO: This needs a check to ensure we dont try to cache things that are already cached
		//It can occur if the player calls cacheSheet manually
        int result = 0;
        for(int i = 0; i < width; i += x) {
            for(int j = 0; j < height; j += y, count++, result++) {
                BufferedImage temp = sheet.getSubimage(i, j, x, y);
				String imgKey = path + count;
				if(cache.containsKey(imgKey)) {
					System.out.println("Image " + imgKey + " already in cache");
					continue;
				}
				System.out.println("Put " + imgKey + " in cache");
                cache.put(imgKey, temp);
            }
        }
        return result;
    }

	/**
	 *	Caches and returns the requested image
	 *
	 */
	
	public BufferedImage cacheImage(String path) {
        if(cache == null) cache = new HashMap<String, BufferedImage>();
		BufferedImage img = getImage(path);
		String imgKey = path + "0";
		//System.out.println("Put " + imgKey + " in cache");
		cache.put(imgKey, img);
		return img;
	}


    /** Takes in a path in the form of "test.png", not "/test.png"
     *  Purpose: Loads a sprite from the disk and returns it as a BufferedImage.
     */
    public BufferedImage getImage(String path) {
        if(cache == null) cache = new HashMap<String, BufferedImage>();

        if(path == null){
            System.out.printf("Sprite loader: path provided to getImage() is null!%n");
            System.exit(1);
        }

        String location = RESOURCE_PATH + path;
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
        if(cache == null) cache = new HashMap<String, BufferedImage>();
        return cache;
    }


}
