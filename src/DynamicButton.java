import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.image.BufferedImage;
public class DynamicButton {

    public boolean isSelected;

    private TextDevice font;
    private int x, y, width, height;
    String text;
    public Color originalOutline;
    public Color fill, outline, selectedColor;
    public BufferedImage image;

    public DynamicButton(BufferedImage img, int x, int y, int width, int height, Color fill, Color outline, Color selectedColor) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.fill = fill;
        this.outline = outline;
        originalOutline = outline;
        this.selectedColor = selectedColor;
        image = img;
        isSelected = false;
    }

    public void draw(Graphics2D rend) {
        Color background;
        if(isSelected) background = selectedColor;
        else background = fill;
        drawOutlinedRectangle(rend, outline, background, x, y, width, height);
        if(text != null) {
            int textX = x + width/2 - font.getPixelWidth(rend, text) / 2;
            int textY = y + height/2 + font.getPixelHeight(rend)/4;
            font.drawOutlineText(rend, text, textX, textY);
        }else if(image != null){
            int imgX = x + width/2 - image.getWidth() / 2;
            int imgY = y + height/2 - image.getHeight() / 2;
            rend.drawImage(image, null, imgX, imgY);
        }

    }

    public DynamicButton(String text, int x, int y, int width, int height, Color fill, Color outline, Color selectedColor, TextDevice font) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.fill = fill;
        this.outline = outline;
        this.selectedColor = selectedColor;
        this.font = font;
        isSelected = false;
    }

    public void drawOutlinedRectangle(Graphics2D rend, Color outline, Color fill, int x, int y, int width, int height) {
        rend.setColor(fill);
        rend.fillRect(x,y,width,height);
        rend.setColor(outline);
        rend.drawRect(x,y,width,height);
        //System.out.printf("(%d,%d,%d,%d)\n",x,y,width,height);
    }

}
