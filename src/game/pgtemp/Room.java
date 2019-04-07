import java.awt.Rectangle;
import java.util.ArrayList;

public class Room {
	//All Rooms are represented as awt.Rectangle objects under the hood
	public Rectangle bounds; //Stores x,y,width,height;
	//IMPORTANT: x and y of the top left corner, not row and col
	public int row, col;
	
	private ArrayList<MapCoordinate> leftWall;
	private ArrayList<MapCoordinate> rightWall;
	private ArrayList<MapCoordinate> topWall;
	private ArrayList<MapCoordinate> bottomWall;
	private ArrayList<MapCoordinate> corners;

	private boolean[] sidesConnected; //left right top bot

	public Room(MapCoordinate origin, int width, int height) {
		row = origin.row;
		col = origin.col;
		bounds = new Rectangle(origin.col, origin.row, width, height);
		sidesConnected = new boolean[] {false, false, false, false};

		leftWall = new ArrayList<>();
		rightWall = new ArrayList<>();
		topWall = new ArrayList<>();
		bottomWall = new ArrayList<>();
		corners = new ArrayList<>();

		//Populate corners
		corners.add(new MapCoordinate(row, col));
		corners.add(new MapCoordinate(row, col + bounds.width - 1));
		corners.add(new MapCoordinate(row + bounds.height - 1, col + bounds.width - 1));
		corners.add(new MapCoordinate(row + bounds.height - 1, col));

		//Populating the Wall coordinate arraylists
		for(int i = bounds.y + 1, count = 0; i < bounds.y + bounds.height - 1; i++, count++) {
			//bounds.y + 1 ignores the corner blocks
			leftWall.add(new MapCoordinate(i, bounds.x));
		}
		for(int i = bounds.y + 1, count = 0; i < bounds.y + bounds.height - 1; i++, count++) {
			int col = bounds.x + bounds.width - 1;
			rightWall.add(new MapCoordinate(i, col));
		}
		for(int i = bounds.x + 1, count = 0; i < bounds.x + bounds.width - 1; i++, count++) {
			topWall.add(new MapCoordinate(bounds.y, i));
		}
		for(int i = bounds.x + 1, count = 0; i < bounds.x + bounds.width - 1; i++, count++) {
			int row = bounds.y + bounds.height - 1;
			bottomWall.add(new MapCoordinate(row, i));
		}
		
		leftWall.trimToSize();
		rightWall.trimToSize();
		topWall.trimToSize();
		bottomWall.trimToSize();
	}

	public ArrayList<MapCoordinate> getDirectionWall(Direction dir) {
		switch(dir) {
			case LEFT: return leftWall;
			case RIGHT: return rightWall;
			case UP: return topWall;
			case DOWN: return bottomWall;
		}
		return null;
	}

	public ArrayList<MapCoordinate> getCorners() {
		return corners;
	}

	public void setSideConnected(Direction dir) {
		sidesConnected[dir.getValue()] = true;
	}

	public boolean isSideConnected(Direction dir) {
		return sidesConnected[dir.getValue()];
	}

	public boolean isAllConnected() {
		return sidesConnected[0] && sidesConnected[1] && sidesConnected[2] && sidesConnected[3];
	}
}
