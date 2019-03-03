import javax.swing.JPanel;
public abstract class Menu extends JPanel{
    public boolean isFocused = false;
    public abstract void invoke(String key);
    public void focus(Menu other) {
        RenderLoop re = Sanctum.ge.getRenderEngine();
        re.window.setWindowView(this);
        isFocused = true;
        other.isFocused = false;
        initializeFocus();
    }
    abstract void initializeFocus();
}
