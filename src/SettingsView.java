import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.util.ArrayList;

public class SettingsView extends Menu{

    ArrayList<DynamicButton> options; 
    private int selection = 0;
    private DynamicButton selected;
    private long lastInputTime = 0; 
    private Menu parent;
    private static final long INTERACTION_DELAY = 200; //In milliseconds
    public SettingsView(int width, int height, Menu parent) {
        this.setSize(width, height);
        this.setOpaque(true);
        this.setBackground(Color.black);
        this.parent = parent;
        TextDevice menuText = new TextDevice("DPComic",20,Color.white, Color.black);
        int BUTTON_WIDTH = 250;
        int BUTTON_HEIGHT = 50;
        int leftX = (getWidth()/2 - BUTTON_WIDTH) / 2;
        int rightX = 3 * leftX;

        String[] leftText = new String[] {
            "Difficulty: Normal",
                "Sound: On",
                "Interp Rate: 30",
                "Player Sprite: Wizard.png",
                "Back"
        };

        Color selectedColor = new Color(0xbb0a1e);
        Color outline = Color.white;
        Color fill = new Color(0x002663);

        options = new ArrayList<DynamicButton>();
        int i = 0;
        for(String s : leftText) {
            DynamicButton b;
            b = new DynamicButton(s,leftX, 100 + 2*BUTTON_HEIGHT*i++, BUTTON_WIDTH, BUTTON_HEIGHT,fill,outline,selectedColor,menuText);
            options.add(b);
        }

        selected = options.get(selection);
        selected.isSelected = true;

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
                }

                break;
            case "D":
                if(selection == 2) {
                    if(GameView.getInterpRate()+5 < (int)(Sanctum.ge.SLOWRATE / RenderLoop.SLEEP_TIME))
                        GameView.setInterpRate(GameView.getInterpRate()+5);
                    else GameView.setInterpRate((int)(Sanctum.ge.SLOWRATE / RenderLoop.SLEEP_TIME)-1);
                    selected.text = "Interp Rate: " + GameView.getInterpRate();
                }
                break;
            case "Enter":
                System.out.println(selection);
                switch(selection) {
                    case 0:
                        
                        if(selected.text.equals("Difficulty: Normal"))
                            selected.text =     "Difficulty: Hard";
                        else if(selected.text.equals("Difficulty: Hard"))
                            selected.text =     "Difficulty: Easy";
                        else if(selected.text.equals("Difficulty: Easy"))
                            selected.text =     "Difficulty: Normal";
                        break; 
                    case 1:
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