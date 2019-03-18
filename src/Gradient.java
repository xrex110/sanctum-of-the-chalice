import java.awt.Graphics2D;
import java.awt.Color;
public class Gradient {
    int x,y,w,h,step;
    boolean isHorizontal;
    int axis, axisWindow;
    Color color;
    public Gradient(int x, int y, int w, int h, boolean horizontal, int axis, int axisWindow, int step, Color color) {
        this.x=x;
        this.y=y;
        this.w=w;
        this.h=h;
        isHorizontal = horizontal;
        this.axis = axis;
        this.axisWindow = axisWindow;
        this.step = step;
        this.color = color;
    }
    public void draw(Graphics2D rend) {
        for(int i = 0; i < w; i += step) {
            int alpha = 255;
            if(i < axis-axisWindow) {
                alpha = (int)(((float)i/(axis-axisWindow)) * 255);
            } else if(i > axis+axisWindow){
                alpha = 255 - (int)(((float)(i-axis-axisWindow) / (w-axis-axisWindow)) * 255);
            }
            Color current =  new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
            rend.setColor(current);
            rend.fillRect(i + x, y, step, h);
        }
    }
    public void drawInverted(Graphics2D rend) {
        for(int i = 0; i < w; i += step) {
            int alpha = 0;
            if(i < axis-axisWindow) {
                alpha = 255-(int)(((float)i/(axis-axisWindow)) * 255);
            } else if(i > axis+axisWindow) {
                alpha = (int)(((float)(i-axis-axisWindow) / (w-axis-axisWindow)) * 255);
            }
            Color current =  new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
            rend.setColor(current);
            rend.fillRect(i + x, y, step, h);
        }

    }
    
}
