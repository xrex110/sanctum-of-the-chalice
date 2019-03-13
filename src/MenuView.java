import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.util.ArrayList;
public class MenuView extends Menu {
    private int volume = 5;
    private static int menuSelection = 0;
    private DynamicButton[] options;
    private DynamicButton currentSelection;
    private TextDevice menuText;
    private TextDevice titleText;
    private long lastInputTime = 0;
    private static final long INTERACTION_DELAY = 200; //In milliseconds

    private ArrayList<Menu> children = new ArrayList<Menu>();

    public MenuView(int width, int height) {


        this.setOpaque(true);
        this.setBackground(Color.black);
        this.setSize(width, height);
        super.isFocused = true;
        menuText = new TextDevice("DPComic",20,Color.white, Color.black);
        titleText = new TextDevice("DPComic",40,Color.white, Color.black);

        Color selectedColor = new Color(0xbb0a1e);
        Color outline = Color.white;
        Color fill = new Color(0x002663);

        int BUTTON_HEIGHT = 50;
        int BUTTON_WIDTH = 100;
        int x = (getWidth() - BUTTON_WIDTH) / 2;

        int i = 0;
        options = new DynamicButton[]{
            new DynamicButton("Start",x,200 + BUTTON_HEIGHT * 2 * i++,100,BUTTON_HEIGHT,fill,outline,selectedColor,menuText),
                new DynamicButton("Settings",x,200 + BUTTON_HEIGHT * 2 * i++,100,BUTTON_HEIGHT,fill,outline,selectedColor,menuText),
                new DynamicButton("Credits",x,200 + BUTTON_HEIGHT * 2 * i++,100,BUTTON_HEIGHT,fill,outline,selectedColor,menuText),
                new DynamicButton("Exit",x,200 + BUTTON_HEIGHT * 2 * i++,100,BUTTON_HEIGHT,fill,outline,selectedColor,menuText),
        };

        currentSelection = options[0];
        currentSelection.isSelected = true;
        
        initializeChildren();

    }

    private void initializeChildren(){
        SettingsView settings = new SettingsView(getWidth(), getHeight(), this);
        children.add(settings);
    }

    public boolean recursiveIsFocused() {
        if(isFocused) return true;
        for(Menu m : children)
            if(m.isFocused) return true;
        return false;
    }

    public static void setMenuSelection(int id) {
        menuSelection = id;
    }
    public static int getMenuSelection() {
        return menuSelection;
    }

    public void invoke(String key) {
        if(!isFocused) {
            for(Menu m : children) {
                m.invoke(key);
            }
            return;
        }

        if(lastInputTime == 0) lastInputTime = System.nanoTime() + (long)3e8;
        if((System.nanoTime() - lastInputTime) / 1e6 < INTERACTION_DELAY) return;
        else {
            lastInputTime = System.nanoTime(); 
        }

        switch(key) {
            case "W":
                menuSelection = menuSelection == 0 ? options.length - 1 : menuSelection - 1;
                currentSelection.isSelected = false;
                currentSelection = options[menuSelection];
                currentSelection.isSelected = true;
                break;
            case "S":
                menuSelection = menuSelection == options.length-1 ? 0 : menuSelection + 1;
                currentSelection.isSelected = false;
                currentSelection = options[menuSelection];
                currentSelection.isSelected = true;
                break; 
            case "Enter": {
                RenderLoop re = Sanctum.ge.getRenderEngine();
                if(getMenuSelection() == 0) {
                    //Start new game
                    System.out.println("Loading game!");
                    re.gm.focus(this);
		    GameEngine.unPause();
                } else if(getMenuSelection() == 1) {
                    //Settings
                    children.get(0).focus(this);
                } else if(getMenuSelection() == 2) {
                    //Unused
                    isFocused = false;

                } else if(getMenuSelection() == 3) {
                    //Exit
                    Sanctum.ge.getRenderEngine().window.killWindow();
                    System.exit(0);
                }
                break;
            }
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

            for(int i = 0; i < options.length; ++i) {
                options[i].draw(rend);
            }
        }
    public void setInputHandler(InputHandler ih) {
		this.addKeyListener(ih);
	}
    @Override
    void initializeFocus() {
        lastInputTime = 0;
    }

}
