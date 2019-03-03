import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Polygon;
import static java.lang.Math.*;

public class CircleProgressBar {

    public static CircleProgressBar tickTimer = new CircleProgressBar(25, 100, 50, 50, 5,new Color(0xFFD700));
    public static CircleProgressBar tickTimer2 = new CircleProgressBar(25, 175, 100, 50, Color.RED);

    private int GRANULARITY;    

    private Color fill;
    private int x, y, width, height;
    private boolean isHollow;
    private int thickness = -1;

    private Polygon[] states;
    private int frame = 0;

    public CircleProgressBar(int x, int y, int width, int height, int thickness, Color fill) {

        this.fill = fill;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isHollow = true;
        this.thickness = thickness;
        this.GRANULARITY = (int)(Sanctum.ge.SLOWRATE/RenderLoop.SLEEP_TIME);
        states = new Polygon[GRANULARITY];
        computePolygon();

    }

    public CircleProgressBar(int x, int y, int width, int height, Color fill) {

        this.fill = fill;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isHollow = false;
        this.GRANULARITY = (int)(Sanctum.ge.SLOWRATE/RenderLoop.SLEEP_TIME);
        states = new Polygon[GRANULARITY];
        computePolygon();

    }

    private void computePolygon() {
        int centerX = x + width / 2;
        int centerY = y + height / 2;

        final double ARC_ANGLE = 2 * PI / GRANULARITY; //In radians
        int semiMajorAxis = width / 2;
        int semiMinorAxis = height / 2;

        if(isHollow) {
            for(int i = 0; i < GRANULARITY; ++i) {
                Polygon current = new Polygon();
                //Do the outside
                for(int j = 0; j < i+2; ++j) {
                    double theta = -PI / 2 - j * ARC_ANGLE;
                    double xpos = -cos(theta) * semiMajorAxis;
                    double ypos = sin(theta) * semiMinorAxis;
                    current.addPoint((int)(centerX + xpos), (int)(centerY + ypos));
                }
                //Do the inside
                for(int j = i+2; j >= 0; --j) {
                    double theta = -PI / 2 - j * ARC_ANGLE;
                    double xpos = -cos(theta) * (semiMajorAxis - thickness);
                    double ypos = sin(theta) * (semiMinorAxis - thickness);
                    current.addPoint((int)(centerX + xpos), (int)(centerY + ypos));
                }
                states[i] = current;
            }
        } else {
            for(int i = 0; i < GRANULARITY; ++i) {
                Polygon current = new Polygon();
                current.addPoint(centerX, centerY);
                for(int j = 0; j < i+2; ++j) {
                    double theta = -PI / 2 - j * ARC_ANGLE;
                    double xpos = -cos(theta) * semiMajorAxis;
                    double ypos = sin(theta) * semiMinorAxis;
                    current.addPoint((int)(centerX + xpos), (int)(centerY + ypos));
                }
                states[i] = current;
            }
        }
    }

    public void draw(Graphics2D rend) {
        rend.setColor(fill);

        Polygon current = states[frame % GRANULARITY];
        rend.fillPolygon(current);

        frame++;
    }
}
