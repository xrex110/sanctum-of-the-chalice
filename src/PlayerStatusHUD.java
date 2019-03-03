import java.awt.Graphics2D;
import java.awt.Color;

public class PlayerStatusHUD {
    
    public int health = 75;
    public int stamina = 25;
    public final int HEALTH_MAX = 100;
    public final int STAMINA_MAX = 100;
    private String key = "";
    CircleProgressBar nextTick;
    
    private int width, height;
    private TextDevice font;

    public PlayerStatusHUD(int width, int height, TextDevice font) {
        this.width = width;
        this.height = height;
        this.font = font;
        nextTick = new CircleProgressBar(width - 185, height - 95,50,50,5,Color.RED);
    }
    public void setKey(String s) { key = s; }
    
    private void fillProgressBar(Graphics2D rend, int x, int y, int w, int h, int current, int max, Color outline, Color fill, Color background) {
        rend.setColor(background);
        rend.fillRect(x,y,w,h);
        rend.setColor(fill);
        float fillRatio = (float) current / max;
        rend.fillRect(x,y,(int) (w * fillRatio),h);
        rend.setColor(outline);
        rend.drawRect(x,y,w,h);
    }
    
    private void drawCenteredText(Graphics2D rend, String text, int x, int y, int w, int h) {
        int textX = x + w/2 - font.getPixelWidth(rend, text) / 2;
        int textY = y + h/2 + font.getPixelHeight(rend)/4;
        font.drawOutlineText(rend, text, textX, textY);
    }

    public void draw(Graphics2D rend) {

        int barWidth = 100;
        int barHeight = 25;
        int barX = width - 25 - barWidth;
        int barY = height - 3*(barHeight + 10);
        
        String hp = health + "/" + HEALTH_MAX;
        String stam = stamina + "/" + STAMINA_MAX;

        fillProgressBar(rend, barX, barY, barWidth, barHeight, health,
                        HEALTH_MAX,  Color.white, new Color(0xbb0a1e), Color.black);
        drawCenteredText(rend, hp, barX, barY, barWidth, barHeight);

        fillProgressBar(rend, barX, barY+10+barHeight, barWidth, barHeight, stamina,
                        STAMINA_MAX, Color.white, new Color(0x228b22), Color.black);
        drawCenteredText(rend, stam, barX, barY+10+barHeight, barWidth, barHeight);
        nextTick.draw(rend);
        drawCenteredText(rend, key, width-185,height-95,50,50);
        
    }

}
