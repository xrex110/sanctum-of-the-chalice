import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
public class MenuView extends JPanel {
    private int volume = 5;
    private static int menuSelection = 0;
    private DynamicButton[] options;
    private DynamicButton currentSelection;
    private final String menuName;
    private TextDevice menuText;
    private TextDevice titleText;
    public boolean isFocused = false;
    private long lastInputTime = 0;
    private final long INTERACTION_DELAY = 200; //In milliseconds
    public MenuView(String name, int width, int height) {

        if(name.equals("Main")) isFocused = true;

        this.setOpaque(true);
        this.setBackground(Color.black);
        this.setSize(width, height);

        menuText = new TextDevice("DPComic",20,Color.white, Color.black);
        titleText = new TextDevice("DPComic",40,Color.white, Color.black);
        menuName = name;
        
        Color selectedColor = new Color(0xbb0a1e);
        Color outline = Color.white;
        Color fill = new Color(0x002663);
        
        int BUTTON_HEIGHT = 50;
        int BUTTON_WIDTH = 100;
        int x = (getWidth() - BUTTON_WIDTH) / 2;

        int i = 0;
        options = new DynamicButton[]{
            new DynamicButton("Start",x,200 + BUTTON_HEIGHT * 2 * i++,100,BUTTON_HEIGHT,fill,outline,selectedColor,menuText),
            new DynamicButton("Nothing",x,200 + BUTTON_HEIGHT * 2 * i++,100,BUTTON_HEIGHT,fill,outline,selectedColor,menuText),
            new DynamicButton("Nothing",x,200 + BUTTON_HEIGHT * 2 * i++,100,BUTTON_HEIGHT,fill,outline,selectedColor,menuText),
            new DynamicButton("Exit",x,200 + BUTTON_HEIGHT * 2 * i++,100,BUTTON_HEIGHT,fill,outline,selectedColor,menuText),
        };

        currentSelection = options[0];
        currentSelection.isSelected = true;

    }

    public static void setMenuSelection(int id) {
        menuSelection = id;
    }
    public static int getMenuSelection() {
        return menuSelection;
    }

    public void invoke(String key) {
        if(!isFocused) return;
        
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
            case "Enter":
                if(getMenuSelection() == 0) {
                    if(menuName.equals("Main")){
                        System.out.println("Loading game!");
                        Sanctum.ge.getRenderEngine().window
                            .setWindowView(Sanctum.ge.getRenderEngine().gm);
                        isFocused = false;
                    }
                }else if(getMenuSelection() == 3) {
                    if(menuName.equals("Main")){
                        Sanctum.ge.getRenderEngine().window.killWindow();
                        System.exit(0);
                    }
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

        for(int i = 0; i < options.length; ++i) {
            options[i].draw(rend);
            
        }
    }
    
}
