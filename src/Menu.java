import javax.swing.JPanel;
import java.util.ArrayList;
public abstract class Menu extends JPanel{
    ArrayList<DynamicButton> options;
    DynamicButton selected;
    int selection = 0;
    boolean isFocused = false;
    Menu parent;

    long INTERACTION_DELAY = 150;
    long lastInputTime = 0;
    
    private String lastKey = null;

    public Menu(int width, int height, Menu parent) {
        this.setSize(width, height);
        this.parent = parent;
        this.setOpaque(true);
        this.setBackground(java.awt.Color.black);
        options = new ArrayList<DynamicButton>();
    }

    public void focus(Menu other) {
        RenderLoop re = Sanctum.ge.getRenderEngine();
        re.window.setWindowView(this);
        isFocused = true;
        other.isFocused = false;
        initializeFocus();
    }
    public void selectButton(int i) {
        
        if(selected != null)
            selected.isSelected = false;
        selection = i;
        selected = options.get(i);
        selected.isSelected = true;
    }

    boolean sanitizeInputTime(int delay, String key) {
        
        if(!isFocused) return false; 
        //Prevent double input when refocusing
        String oldKey = lastKey;
        lastKey = key;

        if(lastInputTime == 0) lastInputTime = System.nanoTime()+100;
        if(oldKey != null && oldKey.equals(lastKey)) {
            if((System.nanoTime() - lastInputTime) / 1e6 < delay) return false;
        }
        lastInputTime = System.nanoTime();
        return true;

    }

    void initializeFocus() {
        for(DynamicButton db : options) {
            db.isSelected = false;
        }
        if(options.size() > 0)
            selectButton(0);
        lastInputTime = 0;
    }
    public abstract void invoke(String key);
}
