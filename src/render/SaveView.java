package render;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
public class SaveView extends Menu {
    DynamicButton saveButton, loadButton, backButton;
    TextDevice menuText = new TextDevice("DPComic",20,Color.white, Color.black);
    private FadingText savedNotification, loadNotification;
    public SaveView(int w, int h, Menu parent) {
        super(w,h,parent);

        Color selectedColor = new Color(0xbb0a1e);
        Color outline = Color.white;
        Color fill = new Color(0x002663);

        int BUTTON_WIDTH = 200;
        int BUTTON_HEIGHT = 100;
        int buttonX = getWidth() / 2 - BUTTON_WIDTH/2;
        int buttonY = getHeight() / 2 - 3*BUTTON_HEIGHT;
        saveButton = new DynamicButton("Save current game", buttonX, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT, fill, outline, selectedColor, menuText);
        loadButton = new DynamicButton("Load previous game", buttonX, buttonY+2*BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT, fill, outline, selectedColor, menuText);
        backButton = new DynamicButton("Back", buttonX, buttonY+4*BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT, fill, outline, selectedColor, menuText);
        options.add(saveButton);
        options.add(loadButton);
        options.add(backButton);
        
        TextDevice notifText = new TextDevice("DPComic",20,Color.white, Color.black);
        savedNotification = new FadingText("Saved game!", 50, 50,500,true,notifText);
        loadNotification = new FadingText("Loaded game!", 50, 50,500,true,notifText);

    }

    @Override
    public void invoke(String key) {
        if(!sanitizeInputTime(key)) return;
        switch(key) {
        case "Enter":
            if(selection == 0) {
                //Call a save pls
                savedNotification.start();
                loadNotification.stop();
            }else if(selection == 1) {
                //Call a load pls
                savedNotification.stop();
                loadNotification.start();
            }else if(selection == 2) {
                parent.focus(this);
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
        saveButton.draw(rend);
        loadButton.draw(rend);
        backButton.draw(rend);
        savedNotification.fadeIn(rend);
        loadNotification.fadeIn(rend);
        
    }

}
