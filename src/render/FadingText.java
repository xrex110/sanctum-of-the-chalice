package render;

import java.awt.Graphics2D;
import java.awt.Color;
import game.*;

public class FadingText {

    private String text;
    private int x,y,delay;
    private TextDevice font;
    private Timer timer;
    private Color defaultOuter, defaultInner;
    private boolean fadeInAndOut = true;

    public FadingText(String text, int x, int y, int delay, boolean fadeInAndOut, TextDevice font) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.delay = delay;
        this.font = font;
        defaultOuter = font.getOutlineColor();
        defaultInner = font.getInnerColor();
        timer = new Timer();
    }

    public void fadeIn(Graphics2D rend) {
        if(timer.getMillis() >= delay) {
            
            if(fadeInAndOut) fadeOut(rend);
            return;
        }
        float percentage = (timer.getMillis() / (float)(delay));
        Color out = new Color(defaultOuter.getRed(), defaultOuter.getGreen(), defaultOuter.getBlue(), (int)(defaultOuter.getAlpha() * percentage));
        Color in = new Color(defaultInner.getRed(), defaultInner.getGreen(), defaultInner.getBlue(), (int)(defaultInner.getAlpha() * percentage));
        font.setColors(in,out);

        font.drawOutlineText(rend, text, x,y); 

    }
    public void fadeOut(Graphics2D rend) {
        if(timer.getMillis() >= delay*2) {
            font.drawOutlineText(rend, text, x,y); 
            return;
        }
        float percentage; 
        if(fadeInAndOut)
            percentage = 1-(timer.getMillis()/2 / (float)(delay));
        else
            percentage = 1-(timer.getMillis() / (float)(delay));

        Color out = new Color(defaultOuter.getRed(), defaultOuter.getGreen(), defaultOuter.getBlue(), (int)(defaultOuter.getAlpha() * percentage));
        Color in = new Color(defaultInner.getRed(), defaultInner.getGreen(), defaultInner.getBlue(), (int)(defaultInner.getAlpha() * percentage));
        font.setColors(in,out);

        font.drawOutlineText(rend, text, x,y); 

    }

    
    public void start() {
        timer.stop();
        timer.reset();
        timer.start();
    }
}
