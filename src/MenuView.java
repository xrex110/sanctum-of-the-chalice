import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
public class MenuView extends JPanel {
    private int volume = 5;
    private static int menuSelection = 0;
    private String[] options;
    private final String menuName;
    private TextDevice menuText;
    private TextDevice titleText;
    public boolean isFocused = false;
    private long lastInputTime = 0;
    private final long INTERACTION_DELAY = 200; //In milliseconds
    public MenuView(String name) {
        if(name.equals("Main")) isFocused = true;
        menuName = name;
        this.setOpaque(true);
        this.setBackground(Color.black);
        options = new String[]{
            "Start",
                "Null",
                "Null",
                "Exit"
        };
        menuText = new TextDevice("DPComic",20,Color.white, Color.black);
        titleText = new TextDevice("DPComic",40,Color.white, Color.black);
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
                break;
            case "S":
                menuSelection = menuSelection == options.length-1 ? 0 : menuSelection + 1;
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

        final int BUTTON_WIDTH = 100;
        final int BUTTON_HEIGHT = 50;
        for(int i = 0; i < options.length; ++i) {
            Color fill = new Color(0x002663);
            if(menuSelection == i)
                fill = new Color(0xbb0a1e);
            int buttonX = (getWidth() - BUTTON_WIDTH) / 2;
            int buttonY = 200 + BUTTON_HEIGHT * 2 * i;
            Color outline = Color.white;

            drawOutlinedRectangle(rend, outline, fill, buttonX, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT);
            
            int textX = buttonX + BUTTON_WIDTH/2 - menuText.getPixelWidth(rend, options[i]) / 2;
            int textY = buttonY + BUTTON_HEIGHT/2 + menuText.getPixelHeight(rend)/4;
            menuText.drawOutlineText(rend, options[i], textX, textY);
        }
    }
    public void drawOutlinedRectangle(Graphics2D rend, Color outline, Color fill, int x, int y, int width, int height) {
        rend.setColor(fill);
        rend.fillRect(x,y,width,height);
        rend.setColor(outline);
        rend.drawRect(x,y,width,height);

    }

}
