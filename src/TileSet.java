import java.awt.Graphics2D;
public class TileSet extends GameObject {
    private Tile[][] grid;
    private final int width, height, tileWidth, tileHeight;
    public TileSet(int x, int y, int width, int height, int tileWidth, int tileHeight, boolean debug) {
        super(x,y);
        this.width = width;
        this.height = height;
        grid = new Tile[width][height];
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        if(debug) execDebug();
    }

    private void execDebug() {
        for(int i = 0; i < width; ++i){
            grid[0][i] = new Tile(getX() + i*tileWidth, 0, "test_tile.png", 0);
            grid[height-1][i] = new Tile(getX() + i*tileWidth, (height-1)*tileHeight, "test_tile.png", 0);
        }
        for(int i = 0; i < height; ++i){
            grid[i][0] = new Tile(0, getY()+i*tileHeight, "test_tile.png", 0);
            grid[i][width-1] = new Tile((width-1)*tileWidth, getY()+i*tileHeight, "test_tile.png", 0);
        }

    }

    public void draw(Graphics2D rend) {
        for(int i = 0; i < width; ++i) {
            for(int j = 0; j < height; ++j) {
                int posX = i * tileWidth + getX();
                int posY = j * tileHeight + getY();
                if(grid[j][i] == null) {
                    //TODO: Report error
                    rend.fillRect(posX, posY, tileWidth, tileHeight);
                } else {
                    grid[j][i].draw(rend);
                }

            }
        }
    }

}
