package render;
import main.*;

import game.*;
import sound.*;
import object.*;


import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class InventoryMenu extends Menu {
    //This is for debugging, hook in an Item[] later
    String[] inventory = new String[28];
    private DynamicButton[] inventoryButtons = new DynamicButton[28];
    private UsableItem[] geInventory;
    //For this array, set each equipment slot ID's image to the current equipped item slot's sprite!
    private DynamicButton[] equipmentRender = new DynamicButton[4];
    private Equipable[] geEquips;
    private SpriteLoader sp;
    //debugs for demonstration
    int top = -1, legs = -1, helm = -1, weapon = -1;

    final int INVENT_WIDTH = 4;
    final int INVENT_HEIGHT = 7;
    int inventX = 100;
    int inventY = 197;
    int BUTTON_WIDTH = 48;
    int BUTTON_HEIGHT = 48;
    int horizGap = 10;
    int vertGap = 10;
    int equipmentX = inventX;
    int equipmentY = inventY - 2*(BUTTON_HEIGHT+vertGap);
    TextDevice font = new TextDevice("DPComic",20,Color.white, Color.black);


    public InventoryMenu(int width, int height, Menu parent) {
        super(width, height, parent);
        this.setOpaque(true);
        this.setBackground(Color.black);
        sp = new SpriteLoader();
        
                /* Initialize inventory buttons */
        //Defines the top left corner of the inventory

        for(int i = 0; i < INVENT_WIDTH; ++i) {
            for(int j = 0; j < INVENT_HEIGHT; ++j) {
                int buttonX = inventX + (BUTTON_WIDTH + horizGap) * i;
                int buttonY = inventY + (BUTTON_HEIGHT + vertGap) * j;
                inventoryButtons[i*INVENT_HEIGHT+j] =
                    new DynamicButton(null, buttonX, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT, new Color(0x002663), Color.white, new Color(0xbb0a1e));

            }
        }

        for(DynamicButton b : inventoryButtons) options.add(b);

                for(int i = 0; i < equipmentRender.length; ++i) {
            int buttonX = equipmentX + (BUTTON_WIDTH  + horizGap) * i;
            int buttonY = equipmentY;
            equipmentRender[i] =
                new DynamicButton(null, buttonX, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT, new Color(0x002663), Color.white, new Color(0xbb0a1e));

        }
    }
    private void drawCenteredText(Graphics2D rend, String text, int x, int y, int w, int h) {
        int textX = x + w/2 - font.getPixelWidth(rend, text) / 2;
        int textY = y + h/2 + font.getPixelHeight(rend)/4;
        font.drawOutlineText(rend, text, textX, textY);
    }

    @Override
        public void paint(Graphics g) {
            if(!isFocused) return;
            //super.paint(g);

            Graphics2D rend = (Graphics2D) g;
            
            drawOutlinedRectangle(rend, Color.white, Color.black, inventX - horizGap, inventY - vertGap,4 * (BUTTON_WIDTH + horizGap) + horizGap, 7*(BUTTON_HEIGHT + vertGap) + vertGap);
            drawOutlinedRectangle(rend, Color.white, Color.black, equipmentX - horizGap, equipmentY - vertGap,4 * (BUTTON_WIDTH + horizGap) + horizGap, BUTTON_HEIGHT + vertGap + vertGap);

            drawStats(rend);
            drawLevelInfo(rend);
            drawItemInfo(rend);

            for(int i = 0; i < inventoryButtons.length; i++) {
                if (geInventory[i] == null) {
			inventoryButtons[i].image = null;
		}
		else {
			inventoryButtons[i].image = geInventory[i].getBufImage();
		}
		inventoryButtons[i].draw(rend);
            }
	    for(int i = 0; i < equipmentRender.length; i++) {
                if (geEquips[i] == null) {
			equipmentRender[i].image = null;
		}
		else {
			equipmentRender[i].image = geEquips[i].getBufImage();
		}
		equipmentRender[i].draw(rend);
            }

        }
    int statsW = 4 * (BUTTON_WIDTH + horizGap) + horizGap;
    int statsX = getWidth() - equipmentX - statsW + horizGap;
    int statsH = 128 + 26; //Bs magic number to fit text xd
    
    int levelY = statsH + equipmentY + BUTTON_HEIGHT;
    
    public void drawStats(Graphics2D rend) {
    
        drawOutlinedRectangle(rend, Color.white, Color.black, statsX-horizGap,equipmentY-vertGap, statsW, statsH);

        int textY = equipmentY + vertGap;
        String[] statText = new String[] {
            "Strength: " + Player.player.stat.getStr(),
            "Dexterity: " + Player.player.stat.getDex(),
            "Consitution: " + Player.player.stat.getCon(),
            "Intellect: " + Player.player.stat.getWis(),
            "Defense: " + Player.player.getDef(),
        };
        for(String s : statText) {
            font.drawOutlineText(rend, s, statsX, textY);
            textY += 30;
        }
        
        int barWidth = 100;
        int barHeight = 25;
        int barX = getWidth()-17- barWidth;
        int barY = getHeight() - 2*(barHeight + 10)-3;

		int health = Player.player.stat.getHP();
        int hpMax = Player.player.stat.getMaxHP();
        String hp = health + "/" + hpMax;
        fillProgressBar(rend, barX, barY, barWidth, barHeight, health,
                hpMax,  Color.white, new Color(0xbb0a1e), Color.black);
        drawCenteredText(rend, hp, barX, barY, barWidth, barHeight);

    }

    public void drawLevelInfo(Graphics2D rend) {
        drawOutlinedRectangle(rend, Color.white, Color.black, statsX-horizGap,levelY-vertGap, statsW, 120);
        int textY = levelY + vertGap;

        String[] levelText = new String[] {
            "Level: " + Player.player.stat.getLv(),
            "XP: " + Player.player.stat.getCurXP() + "/" + (Player.player.stat.getTotXP()),
            "Level Points: " + Player.player.getFreePoints(),
            "Gold: " + Player.player.getGold()
        };
        for(String s : levelText) {
            font.drawOutlineText(rend, s, statsX, textY);
            textY += 30;
        }
        
    }
    
    public void drawItemInfo(Graphics2D rend) {
        int itemInfoY = levelY + 120 + BUTTON_HEIGHT;
        int textY = itemInfoY + vertGap;

        String itemDesc = "";
        if(geInventory[selection] != null)
            itemDesc = geInventory[selection].name;

        drawOutlinedRectangle(rend, Color.white, Color.black, statsX-horizGap, itemInfoY-vertGap, statsW, 120);
        font.drawOutlineText(rend, itemDesc, statsX, textY);
         
    }
    
    public void drawOutlinedRectangle(Graphics2D rend, Color outline, Color fill, int x, int y, int width, int height) {
        rend.setColor(fill);
        rend.fillRect(x,y,width,height);
        rend.setColor(outline);
        rend.drawRect(x,y,width,height);
    }
    
    private void fillProgressBar(Graphics2D rend, int x, int y, int w, int h, int current, int max, Color outline, Color fill, Color background) {
        rend.setColor(background);
        rend.fillRect(x,y,w,h);
        rend.setColor(fill);
        float fillRatio = (float) current / max;
        rend.fillRect(x,y,(int) (w * fillRatio),h);
        rend.setColor(outline);
        rend.drawRect(x,y,w,h);
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
            case "INVENTORY":
                parent.focus(this);
                GameEngine.unPause();
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
		GameEngine.updateInventIndex(selection);
                break;
        }
    }

    public void setInvent(UsableItem[] inv, Equipable[] eq) {
	this.geInventory = inv;
	this.geEquips = eq;
    }
}
