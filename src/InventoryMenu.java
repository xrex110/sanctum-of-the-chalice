import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class InventoryMenu extends Menu {
    //This is for debugging, hook in an Item[] later
    String[] inventory = new String[28];
    private DynamicButton[] inventoryButtons = new DynamicButton[28];
    //For this array, set each equipment slot ID's image to the current equipped item slot's sprite!
    private DynamicButton[] equipmentRender = new DynamicButton[4];
    private SpriteLoader sp;
    //debugs for demonstration
    int top = -1, legs = -1, helm = -1, weapon = -1;

    final int INVENT_WIDTH = 4;
    final int INVENT_HEIGHT = 7;

    public InventoryMenu(int width, int height, Menu parent) {
        super(width, height, parent);
        this.setOpaque(true);
        this.setBackground(Color.black);
        sp = new SpriteLoader();
        /* Initialize inventory buttons */
        //Defines the top left corner of the inventory
        int inventX = 100;
        int inventY = 197;
        int BUTTON_WIDTH = 48;
        int BUTTON_HEIGHT = 48;
        int horizGap = 10;
        int vertGap = 10;

        for(int i = 0; i < INVENT_WIDTH; ++i) {
            for(int j = 0; j < INVENT_HEIGHT; ++j) {
                int buttonX = inventX + (BUTTON_WIDTH + horizGap) * i;
                int buttonY = inventY + (BUTTON_HEIGHT + vertGap) * j;
                inventoryButtons[i*INVENT_HEIGHT+j] =
                    new DynamicButton(null, buttonX, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT, new Color(0x002663), Color.white, new Color(0xbb0a1e));

            }
        }

        for(DynamicButton b : inventoryButtons) options.add(b);
        
        int equipmentX = inventX;
        int equipmentY = inventY - 2*(BUTTON_HEIGHT+vertGap);
        for(int i = 0; i < equipmentRender.length; ++i) {
            int buttonX = equipmentX + (BUTTON_WIDTH  + horizGap) * i;
            int buttonY = equipmentY;
            equipmentRender[i] =
                    new DynamicButton(null, buttonX, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT, new Color(0x002663), Color.white, new Color(0xbb0a1e));

        }
    }
    @Override
        public void paint(Graphics g) {
            if(!isFocused) return;
            //super.paint(g);
            Graphics2D rend = (Graphics2D) g;
            for(DynamicButton b : inventoryButtons) {
                b.draw(rend);
            }
            for(DynamicButton b : equipmentRender) {
                b.draw(rend);
            }

        }

    public void invoke(String key) {
        if(!sanitizeInputTime(key)) return;
        switch(key) {
            case "W":
                selection -= 1;
                if(selection%INVENT_HEIGHT == INVENT_HEIGHT-1 || selection < 0) selection += INVENT_HEIGHT;
                selectButton(selection); 
                break;
            case "D":
                selection += INVENT_HEIGHT;
                if(selection > inventoryButtons.length-1)
                    selection -= inventoryButtons.length;
                selectButton(selection); 
                break;
            case "A":
                selection -= INVENT_HEIGHT;
                if(selection < 0) selection += inventoryButtons.length;
                selectButton(selection); 
                break;
            case "S":
                selection += 1;
                if(selection%INVENT_HEIGHT == 0)
                    selection -= INVENT_HEIGHT;
                selectButton(selection); 

                break;
            case "I":
                //focus gameview cba to do on plane pls
                parent.focus(this);
                break;
            case "Enter":
                //Pls handle item equipping FEETBUS or JONTRON or SHOEMAN or TEA
                //In each conditional handle your unequip request
                //and in the else handle an equip request :)
                if(selection == helm) helm = -1;
                else if(selection == legs) legs = -1;
                else if(selection == top) top = -1;
                else if(selection == weapon) weapon = -1;
                else helm = selection;
                //Set highlight for equipped items!
                for(int i = 0; i < inventoryButtons.length; ++i) {
                    if(helm == i || top == i || legs == i || weapon == i) {
                        inventoryButtons[i].outline = Color.green;
                    }else {
                        inventoryButtons[i].outline = inventoryButtons[i].originalOutline;
                    }
                }
                break;
        }
    }
}
