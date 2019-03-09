import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class InventoryMenu extends Menu {
    private static final long INTERACTION_DELAY = 150; //In milliseconds
    private long lastInputTime = 0; 
    //This is for debugging, hook in an Item[] later
    String[] inventory = new String[28];
    private DynamicButton[] inventoryButtons = new DynamicButton[28];;
    private int selection = 0;
    private DynamicButton selected;
    private Menu parent;
    private SpriteLoader sp;
    //debugs for demonstration
    int top = -1, legs = -1, helm = -1, weapon = -1;

    final int INVENT_WIDTH = 4;
    final int INVENT_HEIGHT = 7;

    public InventoryMenu(int width, int height, Menu parent) {
        this.setSize(width, height);
        this.setOpaque(true);
        this.setBackground(Color.black);
        this.parent = parent;
        sp = new SpriteLoader();
        /* Initialize inventory buttons */
        //Defines the top left corner of the inventory
        int inventX = 150;
        int inventY = 150;
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
        inventoryButtons[0].isSelected = true;
        selected = inventoryButtons[0];
    }
    @Override
        public void paint(Graphics g) {
            if(!isFocused) return;
            super.paint(g);
            Graphics2D rend = (Graphics2D) g;
            for(DynamicButton b : inventoryButtons) {
                b.draw(rend);
            }
        }

    void initializeFocus() {
        selection = 0;
        selected = inventoryButtons[0];
        for(DynamicButton db : inventoryButtons) {
            db.isSelected = false;
        }
        selected.isSelected = true;
        lastInputTime = 0;

    }
    public void invoke(String key) {
        if(!isFocused) return;
        if(lastInputTime == 0) lastInputTime = System.nanoTime() + (long)3e8;
        if((System.nanoTime() - lastInputTime) / 1e6 < INTERACTION_DELAY) return;
        else {
            lastInputTime = System.nanoTime(); 
        }
        switch(key) {
            case "W":
                selection -= 1;
                if(selection%INVENT_HEIGHT == INVENT_HEIGHT-1 || selection < 0) selection += INVENT_HEIGHT;
                selected.isSelected = false;
                selected = inventoryButtons[selection];
                selected.isSelected = true;
                break;
            case "D":
                selection += INVENT_HEIGHT;
                if(selection > inventoryButtons.length-1)
                    selection -= inventoryButtons.length;
                selected.isSelected = false;
                selected = inventoryButtons[selection];
                selected.isSelected = true;
                break;
            case "A":
                selection -= INVENT_HEIGHT;
                if(selection < 0) selection += inventoryButtons.length;
                selected.isSelected = false;
                selected = inventoryButtons[selection];
                selected.isSelected = true;
                break;
            case "S":
                selection += 1;
                if(selection%INVENT_HEIGHT == 0)
                    selection -= INVENT_HEIGHT;
                selected.isSelected = false;
                selected = inventoryButtons[selection];
                selected.isSelected = true;

                break;
            case "I":
                //focus gameview cba to do on plane pls
                break;
            case "Enter":
                //Pls handle item equipping FEETBUS or JONTRON or SHOEMAN or TEA
                helm = selection;
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
