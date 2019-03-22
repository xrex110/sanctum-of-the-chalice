package render;
import main.*;

import game.*;
import sound.*;
import object.*;


import java.awt.Graphics2D;
import java.awt.Color;
public class Gradient {
    int x,y,w,h,step;
    boolean isHorizontal;
    int axis, axisWindow;
    float density = 1.0f;
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
    public void setDensity(float mult) {
        density = mult;
    }
    public void draw(Graphics2D rend) {
        int GOAL_ALPHA = color.getAlpha();
        for(int i = 0; i < w; i += step) {
            int alpha = GOAL_ALPHA;
            if(i < axis-axisWindow) {
                alpha = (int)(((float)i/(axis-axisWindow)) * GOAL_ALPHA);
            } else if(i > axis+axisWindow){
                alpha = GOAL_ALPHA - (int)(((float)(i-axis-axisWindow) / (w-axis-axisWindow)) * GOAL_ALPHA);
            }
            Color current =  new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
            rend.setColor(current);
            rend.fillRect(i + x, y, step, h);
        }
    }
    public void drawInverted(Graphics2D rend) {
        int GOAL_ALPHA = color.getAlpha();
        for(int i = 0; i < w; i += step) {
            float alpha = 0;
            if(i < axis-axisWindow) {
                alpha = density * (GOAL_ALPHA-(int)(((float)i/(axis-axisWindow)) * GOAL_ALPHA));
            } else if(i > axis+axisWindow) {
                alpha = density * (int)(((float)(i-axis-axisWindow) / (w-axis-axisWindow)) * GOAL_ALPHA);
            }
            if(alpha > GOAL_ALPHA) alpha = GOAL_ALPHA;
            if(alpha < 0) alpha = 0;
            Color current =  new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)alpha);
            rend.setColor(current);
            if(!isHorizontal) {
                rend.fillRect(x, i+y, w, step);
            }
            else {
                rend.fillRect(i + x, y, step, h);
            }
        }

    }
    
}
