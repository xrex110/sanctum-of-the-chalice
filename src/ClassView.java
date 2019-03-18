import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.util.ArrayList;
import java.awt.geom.AffineTransform;

public class ClassView extends Menu{
    
    ArrayList<Integer> interp = new ArrayList<Integer>();
    int interpFrames = 35;

    SpriteLoader sp = new SpriteLoader();
    String[] classNames;
    int y = getHeight() / 2 - 92;
    int yGap = 10;
    public ClassView(int width, int height, Menu parent) {
        super(width, height, parent);
        sp.cacheSheet("class_icons.png",128,128);
        
        Color selectedColor = new Color(0xbb0a1e);
        Color outline = Color.white;
        Color fill = new Color(0x002663);

        int xGap = getWidth() / 6;
        int x = xGap - 64;
        TextDevice menuText = new TextDevice("DPComic",20,Color.white, Color.black);
        for(ClassTypeEnum type : ClassTypeEnum.values()) {
            DynamicButton b;
            b = new DynamicButton(type.name, x,y+128+yGap, 128, 64, fill, outline, selectedColor, menuText);
            options.add(b);
            x += xGap*2;
        }
        

        selectButton(1);
         
    }
    void initializeFocus() {
        
    }

    public void invoke(String key) {
       if(!sanitizeInputTime(key) || interp.size() > 0) return;
       int initX = (options.size() - selection - 3) * getWidth() / 3;
       switch(key) {
            case "D":
                for(int i = 0; i <= getWidth() / 3; i += (getWidth() / 3) /interpFrames) {
                    interp.add(initX-i);
                }
                selectButton(selection < options.size()-1 ? selection+1 : 0);
                break;
            case "A":
                for(int i = 0; i <= getWidth() / 3; i += (getWidth() / 3) /interpFrames) {
                    interp.add(initX+i);
                }
                selectButton(selection > 0 ? selection-1 : options.size()-1);
                break;
            case "Q":
                parent.focus(this);
                break;
       }
    }
    @Override
    public void paint(Graphics g) {
        if(!isFocused) return;
        super.paint(g);
        Graphics2D rend = (Graphics2D) g;
        
        AffineTransform oldAt = rend.getTransform();
        AffineTransform at = new AffineTransform();
        if(interp.size() == 0)
            at.translate((options.size() - selection - 3) * getWidth() / 3, 0);
        else
            at.translate(interp.remove(0), 0);
        rend.setTransform(at);

        int i = 0;
        for(DynamicButton db : options) {
            db.draw(rend);
            ClassTypeEnum cte = ClassTypeEnum.values()[i++];
            rend.drawImage(sp.getSprite("class_icons.png",cte.spriteTile,128,128),null,db.x,db.y - yGap - 128);
        }

        DynamicButton illusionFront = options.get(options.size() - 1).copy();
        DynamicButton illusionFront2 = options.get(options.size() - 2).copy();
        DynamicButton illusionBack = options.get(0).copy();
        DynamicButton illusionBack2 = options.get(1).copy();

        if(options.get(0).isSelected) illusionBack.isSelected = true;
        if(options.get(options.size() -1).isSelected) illusionFront.isSelected = true;

        illusionFront.x = -getWidth() / 6 - 64;
        illusionFront2.x = -3*getWidth() / 6 - 64;
        illusionBack.x = options.get(options.size() - 1).x + getWidth() / 3;
        illusionBack2.x = options.get(options.size() - 2).x + getWidth();
        
        illusionFront.draw(rend);
        illusionFront2.draw(rend);
        illusionBack.draw(rend);
        illusionBack2.draw(rend);

        rend.drawImage(sp.getSprite("class_icons.png",ClassTypeEnum.values()[ClassTypeEnum.values().length-1].spriteTile,128,128),null,illusionFront.x,illusionFront.y - yGap - 128);
        rend.drawImage(sp.getSprite("class_icons.png",ClassTypeEnum.values()[0].spriteTile,128,128),null,illusionBack.x,illusionBack.y - yGap - 128);

        rend.drawImage(sp.getSprite("class_icons.png",ClassTypeEnum.values()[ClassTypeEnum.values().length-2].spriteTile,128,128),null,illusionFront2.x,illusionFront2.y - yGap - 128);
        rend.drawImage(sp.getSprite("class_icons.png",ClassTypeEnum.values()[1].spriteTile,128,128),null,illusionBack2.x,illusionBack2.y - yGap - 128);

        
        rend.setTransform(oldAt);

        Gradient grad;
        grad = new Gradient(0,0,getWidth(),getHeight(),true, getWidth()/2, 50, 1,  Color.black);
        grad.drawInverted(rend);
    }
}
