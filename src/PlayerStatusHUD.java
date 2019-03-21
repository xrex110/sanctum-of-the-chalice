import java.awt.Graphics2D;
import java.awt.Color;
import java.util.ArrayList;

public class PlayerStatusHUD {

    public int health = 75;
    public int stamina = 25;
    public final int HEALTH_MAX = 100;
    public final int STAMINA_MAX = 100;
    private String key = "";
    CircleProgressBar nextTick;

    private int width, height;
    private TextDevice font;

    ArrayList<Gradient> hurtFilter = new ArrayList<Gradient>();
    ArrayList<Gradient> timeFilter = new ArrayList<Gradient>();
    private int frames = 0;

    public PlayerStatusHUD(int width, int height, TextDevice font) {
        this.width = width;
        this.height = height;
        this.font = font;
        nextTick = new CircleProgressBar(width - 185, height - 95,50,50,5,Color.RED);
    }
    public void setKey(String s) { key = s; }
    public void reduceHP(int loss) {
        health -= loss;
        if(!hurtFilter.isEmpty()) return;
        int n = 20;
        for(int i = 0; i < n; ++i) {
            Color input = new Color(255,0,0,255 - (255/n) * i);
            hurtFilter.add(new Gradient(0,0,width,height,true,400,200,5,input));
            hurtFilter.add(new Gradient(0,0,width,height,false,400,200,5,input));
        }
    }

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

    private void drawHurt(Graphics2D rend) {
        if(hurtFilter.size() == 0) return;
        hurtFilter.remove(0).drawInverted(rend);
        hurtFilter.remove(0).drawInverted(rend);

    }
    boolean hasFilledTimeFilter = false;
    public void draw(Graphics2D rend) {
        int n = 20;
        int timeAlpha = 80;

        if(GameEngine.gameMode == GameEngine.MODE.REVERSION) {
            if(timeFilter.isEmpty() && !hasFilledTimeFilter){
                for(int i = 0; i < n; ++i) {
                    Color input = new Color(0,0,255,timeAlpha/n * i);
                    timeFilter.add(new Gradient(0,0,width,height,true,400,200,5,input));
                    timeFilter.add(new Gradient(0,0,width,height,false,400,200,5,input));
                }
                hasFilledTimeFilter = true;
            }else if(timeFilter.isEmpty()){ 
                Color gradColor = new Color(0,0,255,timeAlpha);

                Gradient revert = new Gradient(0,0,width,height,true,400,200,5,gradColor);
                Gradient revertHoriz = new Gradient(0,0,width,height,false,400,200,5,gradColor);
                revert.drawInverted(rend);
                revertHoriz.drawInverted(rend);
            }
        } 
        if(hasFilledTimeFilter && GameEngine.gameMode != GameEngine.MODE.REVERSION) {
            for(int i = 0; i < n; ++i) {
                Color input = new Color(0,0,255,timeAlpha-(timeAlpha/n * i));
                timeFilter.add(new Gradient(0,0,width,height,true,400,200,5,input));
                timeFilter.add(new Gradient(0,0,width,height,false,400,200,5,input));
            }

            hasFilledTimeFilter = false;
        }

        if(!timeFilter.isEmpty()){
            timeFilter.remove(0).drawInverted(rend);
            timeFilter.remove(0).drawInverted(rend);
        }
        drawHurt(rend);

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

        frames++;
    }

}
