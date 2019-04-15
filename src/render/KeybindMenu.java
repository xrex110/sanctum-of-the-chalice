package render;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.util.ArrayList;
import static main.Sanctum.settings;

public class KeybindMenu extends Menu {
    private boolean isBinding = false;
    public KeybindMenu(int width, int height, Menu parent) {
        super(width, height, parent);

        TextDevice menuText = new TextDevice("DPComic",20,Color.white, Color.black);
        int BUTTON_WIDTH = 250;
        int BUTTON_HEIGHT = 50;

        int x = (width - BUTTON_WIDTH) / 2;
        int y = 100;
        Color selectedColor = new Color(0xbb0a1e);
        Color outline = Color.white;
        Color fill = new Color(0x002663);

        for(int i = 0; i <= 6;) {
            DynamicButton b;
            b = new DynamicButton("",x, y + 2*BUTTON_HEIGHT*i++, BUTTON_WIDTH, BUTTON_HEIGHT,fill,outline,selectedColor,menuText);
            options.add(b);
        }
        options.get(6).text = "Back";

    }

    public void invoke(String key) {
        if(!sanitizeInputTime(key)) return;
        
        if(isBinding && !key.equals("Enter")) {
        System.out.println(key); 
            switch(selection) {
                case 0: settings.UP = key.charAt(0); break;
                case 1: settings.DOWN = key.charAt(0); break;
                case 2: settings.LEFT = key.charAt(0); break;
                case 3: settings.RIGHT = key.charAt(0); break;
                case 4: settings.REVERT = key.charAt(0); break;
                case 5: settings.INVENTORY = key.charAt(0); break;
            }
            isBinding = false;
            return; 
        } else {
            isBinding = false;
        }
        switch(key) {
            case "Enter":
                if(selection == 6) {
                    isBinding = false;
                    parent.focus(this);
                } else {
                    isBinding = true;
                }
                break;
            case "W":
                selectButton(selection - 1 >= 0 ? selection-1 : options.size()-1);
                break;
            case "S":
                selectButton(selection + 1 < options.size() ? selection+1 : 0);
                break;
        }

    }

    @Override
        public void paint(Graphics g) {
            if(!isFocused) return;
            super.paint(g);
            Graphics2D rend = (Graphics2D) g;
            options.get(0).text = "Up: " + (char)settings.UP;
            options.get(0).draw(rend);
            options.get(1).text = "Down: " + (char) settings.DOWN;
            options.get(1).draw(rend);
            options.get(2).text = "Left: " + (char)settings.LEFT;
            options.get(2).draw(rend);
            options.get(3).text = "Right: " + (char)settings.RIGHT;
            options.get(3).draw(rend);
            options.get(4).text = "Revert: " + (char)settings.REVERT;
            options.get(4).draw(rend);
            options.get(5).text = "Inventory: " + (char)settings.INVENTORY;
            options.get(5).draw(rend);
            options.get(6).draw(rend);
        }

}
