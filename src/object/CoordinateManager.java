package object;
import render.RenderLoop;
import java.io.Serializable;
import game.Coordinate;
public class CoordinateManager implements Serializable {

    private Coordinate pos;

    public CoordinateManager(Coordinate pos) {
        this.pos = pos;
    }
    public CoordinateManager(int x, int y) {
        this.pos = new Coordinate(y,x);
    }

    public Coordinate getPos() {
        return pos;
    }

    public int getX() {
        if(RenderLoop.invokedByRE()) {
            return pos.col * RenderLoop.tileSizeX;
        }
        return pos.col;
    }

    public void setX(int x) {
        if(RenderLoop.invokedByRE()) {
            pos.col = x * RenderLoop.tileSizeX;
        } else {
            pos.col = x;
        }
    }

    public int getY() {
        if(RenderLoop.invokedByRE()) {
            return pos.row * RenderLoop.tileSizeY;
        }
        return pos.row;
    }
    public void setY(int y) {
        if(RenderLoop.invokedByRE()) {
            pos.row = y * RenderLoop.tileSizeY;
        } else {
            pos.row = y;
        }
    }

    public int getXTile() {
        return pos.col;
    }

    public int getYTile() {
        return pos.row;
    }

}
