import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
public class MenuView extends JPanel {
    private int volume = 5;
    private static int menuSelection = 0;
    private String[] options;
    private final String menuName;
    public boolean isFocused = false;
    public MenuView(String name) {
        if(name.equals("Main")) isFocused = true;
        menuName = name;
        options = new String[]{
            "Start",
            "Null",
            "Null",
            "Exit"
        };
    }

    public static void setMenuSelection(int id) {
        menuSelection = id;
    }
    public static int getMenuSelection() {
        return menuSelection;
    }

    public void invoke(String key) {
        if(!isFocused) return;
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
            rend.setBackground(Color.black);
            for(int i = 0; i < options.length; ++i) {
                if(menuSelection == i)
                    rend.setColor(Color.green);
                else
                    rend.setColor(Color.red);

                rend.fillRect(100,100 + 100 * i,50,50);
                rend.setColor(Color.black);
                rend.drawString(options[i], 110, 120 + 100*i);
            }
        }
}
