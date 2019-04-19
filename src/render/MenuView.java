package render;
import main.*;

import game.*;
import sound.*;
import object.*;


import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.util.ArrayList;
public class MenuView extends Menu {
    private TextDevice menuText;
    private TextDevice titleText;

    private ArrayList<Menu> children = new ArrayList<Menu>();
    private DynamicButton[] buttons;
    private FadingText savedNotification, loadNotification;

    public MenuView(int width, int height) {
        super(width, height, null);
        super.isFocused = true;

        menuText = new TextDevice("DPComic",20,Color.white, Color.black);
        titleText = new TextDevice("DPComic",40,Color.white, Color.black);

        Color selectedColor = new Color(0xbb0a1e);
        Color outline = Color.white;
        Color fill = new Color(0x002663);

        int BUTTON_HEIGHT = 50;
        int BUTTON_WIDTH = 200;
        int x = (getWidth() - BUTTON_WIDTH) / 2;

        int i = 0;
        buttons = new DynamicButton[] {
            new DynamicButton("New Game/Continue",x,200 + BUTTON_HEIGHT * 2 * i++,BUTTON_WIDTH,BUTTON_HEIGHT,fill,outline,selectedColor,menuText),
            new DynamicButton("Continue Last Save",x,200 + BUTTON_HEIGHT * 2 * i++,BUTTON_WIDTH,BUTTON_HEIGHT,fill,outline,selectedColor,menuText),
                new DynamicButton("Settings",x,200 + BUTTON_HEIGHT * 2 * i++,BUTTON_WIDTH,BUTTON_HEIGHT,fill,outline,selectedColor,menuText),
                new DynamicButton("Save",x,200 + BUTTON_HEIGHT * 2 * i++,BUTTON_WIDTH,BUTTON_HEIGHT,fill,outline,selectedColor,menuText),
                new DynamicButton("Exit",x,200 + BUTTON_HEIGHT * 2 * i++,BUTTON_WIDTH,BUTTON_HEIGHT,fill,outline,selectedColor,menuText),
        };
        for(DynamicButton b : buttons) options.add(b);
        selectButton(0);
        initializeChildren();
        
        TextDevice notifText = new TextDevice("DPComic",20,Color.white, Color.black);
        savedNotification = new FadingText("Saved game!", 50, 50,500,true,notifText);
        loadNotification = new FadingText("Loaded game!", 50, 50,500,true,notifText);


    }

    private void initializeChildren(){
        SettingsView settings = new SettingsView(getWidth(), getHeight(), this);
        children.add(settings);
        //ClassView cv = new ClassView(getWidth(), getHeight(), this);
        SaveView saveView = new SaveView(getWidth(), getHeight(), this);
        children.add(saveView);
    }

    public boolean recursiveIsFocused() {
        if(isFocused) return true;
        for(Menu m : children)
            if(m.isFocused) return true;
        return false;
    }
    public void invoke(String key) {
        if(!isFocused) {
            for(Menu m : children) {
                m.invoke(key);
            }
            return;
        }
        
        if(!sanitizeInputTime(key)) return;
        
        switch(key) {
            case "W":
                selection = selection == 0 ? buttons.length - 1 : selection - 1;
                selectButton(selection);
                break;
            case "S":
                selection = selection == buttons.length-1 ? 0 : selection + 1;
                selectButton(selection);
                break; 
            case "Enter": 
                RenderLoop re = Sanctum.ge.getRenderEngine();
                if(selection == 0) {
                    //Start new game
                    System.out.println("Loading game!");
                    re.gm.focus(this);
		            GameEngine.unPause();
                } else if(selection == 1) {
                    SaveHandler.loadGame("manual.save"); 
                    System.out.println("Loading game!");
                    re.gm.focus(this);
		            GameEngine.unPause();
                } else if(selection == 2) {
                    //Settings
                    children.get(0).focus(this);
                } else if(selection == 3) {
                    //Class Selection
                    game.SaveHandler.saveGame("manual.save");
                    savedNotification.start();
                    loadNotification.stop();

                } else if(selection == 4) {
                    //Exit
                    Sanctum.ge.getRenderEngine().window.killWindow();
                    System.exit(0);
                }
                break;
            
            }
    
    }

    @Override
        public void paint(Graphics g) {
            if(!isFocused) return;
            super.paint(g); //Clears screen before every paint
            Graphics2D rend = (Graphics2D) g;

            int titleX = (getWidth() - titleText.getPixelWidth(rend,"Sanctum of the Chalice")) / 2;
            int titleY = 100;
            titleText.drawOutlineText(rend, "Sanctum of the Chalice", titleX, titleY);

            for(int i = 0; i < options.size(); ++i) {
                options.get(i).draw(rend);
            }
            savedNotification.fadeIn(rend);
            loadNotification.fadeIn(rend);
        

        }
    public void setInputHandler(InputHandler ih) {
		this.addKeyListener(ih);
	}
    
}
