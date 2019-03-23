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

public class SettingsView extends Menu{
    
    static {
        if(Sanctum.settings == null) {
            System.out.println("Settings view is loading settings...");
            Sanctum.settings = SaveHandler.loadSettings();

            GameView.setInterpRate(Sanctum.settings.interpRate);
			if(Sanctum.settings.soundOn)
                SoundEngine.setVolume(Sanctum.settings.volume);
            
        }
    } 
	
    public SettingsView(int width, int height, Menu parent) {
        super(width, height, parent);
        this.setOpaque(true);
        this.setBackground(Color.black);
        TextDevice menuText = new TextDevice("DPComic",20,Color.white, Color.black);
        int BUTTON_WIDTH = 250;
        int BUTTON_HEIGHT = 50;
        int leftX = (getWidth()/2 - BUTTON_WIDTH) / 2;
        int rightX = getWidth() - BUTTON_WIDTH - leftX;
        //if(Sanctum.settings == null) Sanctum.settings = new Settings();
        String[] leftText = new String[] {
            "Difficulty: Normal",
                "Sound: " + Sanctum.settings.soundOn,
                "Interp Rate: " + Sanctum.settings.interpRate,
                "Player Sprite: Wizard.png",
                "Back"
        };
        String[] rightText = new String[] {
            "Volume: " + Sanctum.settings.volume,
        };
        Color selectedColor = new Color(0xbb0a1e);
        Color outline = Color.white;
        Color fill = new Color(0x002663);

        int i = 0;
        for(String s : leftText) {
            DynamicButton b;
            b = new DynamicButton(s,leftX, 100 + 2*BUTTON_HEIGHT*i++, BUTTON_WIDTH, BUTTON_HEIGHT,fill,outline,selectedColor,menuText);
            options.add(b);
        }
        i = 0;
        for(String s : rightText) {
            DynamicButton b;
            b = new DynamicButton(s,rightX, 100 + 2*BUTTON_HEIGHT*i++, BUTTON_WIDTH, BUTTON_HEIGHT,fill,outline,selectedColor,menuText);
            options.add(b);
        }

        selectButton(0);

    }
    //TODO: remove me and make a volume call
    int VOLUME_INCREMENT = 5;
    public void invoke(String key) {
        if(!sanitizeInputTime(key)) return;
        switch(key) {
            case "W":
                selection = selection == 0 ? options.size()-1 : selection-1;
                selected.isSelected = false;
                selected = options.get(selection);
                selected.isSelected = true;
                break;
            case "S":
                selection = selection == options.size()-1 ? 0 : selection+1;
                selected.isSelected = false;
                selected = options.get(selection);
                selected.isSelected = true;
                break;
            case "A":
                if(selection == 2) {
                    if(GameView.getInterpRate() - 5 > 0)
                        GameView.setInterpRate(GameView.getInterpRate()-5);
                    else GameView.setInterpRate(0);
                    selected.text = "Interp Rate: " + GameView.getInterpRate();
                } else if(selection == 5) {
                    //TODO: Refactor to alter a volume var
                    if(Sanctum.settings.volume - VOLUME_INCREMENT >= 0) {
                        Sanctum.settings.volume -= VOLUME_INCREMENT;
                        selected.text = "Volume: " + Sanctum.settings.volume;
						// start 50
						// volume / 50 
						// increase 10 -> 10 times louder.
						SoundEngine.setVolume(Sanctum.settings.volume);
                    }
                }


                break;
            case "D":
                if(selection == 2) {
                    if(GameView.getInterpRate()+5 < (int)(Sanctum.ge.SLOWRATE / RenderLoop.SLEEP_TIME))
                        GameView.setInterpRate(GameView.getInterpRate()+5);
                    else GameView.setInterpRate((int)(Sanctum.ge.SLOWRATE / RenderLoop.SLEEP_TIME)-1);
                    selected.text = "Interp Rate: " + GameView.getInterpRate();
                } else if(selection == 5) {
                    //TODO: Refactor to alter a volume var
                    if(Sanctum.settings.volume + VOLUME_INCREMENT <= 100) {
                        Sanctum.settings.volume += VOLUME_INCREMENT;
                        selected.text = "Volume: " + Sanctum.settings.volume;
						SoundEngine.setVolume(Sanctum.settings.volume);
                    }
                }
                break;
            case "Enter":
                System.out.println(selection);
                switch(selection) {
                    case 0:                        
                        break; 
                    case 1:
                        Sanctum.settings.soundOn = !Sanctum.settings.soundOn;
                        selected.text = "Sound: " + Sanctum.settings.soundOn;
                        if(Sanctum.settings.soundOn) {
						    SoundEngine.setVolume(Sanctum.settings.volume);
                        } else {
						    SoundEngine.setVolume(0);
                        }
                        break;
                    case 2:
                        
                        break;
                    case 3:
                        break;
                    case 4:
                        parent.focus(this);
                        break;
                }
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
            for(DynamicButton b : options) b.draw(rend);
        }
    @Override
    void initializeFocus() {
        selection = 0;
        selected = options.get(0);
        for(DynamicButton db : options) {
            db.isSelected = false;
        }
        selected.isSelected = true;
        lastInputTime = 0;
    }
}   
