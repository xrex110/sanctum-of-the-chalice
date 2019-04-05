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

	private boolean[] sidesConnected; //left right top bot

	public Room(MapCoordinate origin, int width, int height) {
		row = origin.row;
		col = origin.col;
		bounds = new Rectangle(origin.col, origin.row, width, height);
		sidesConnected = new boolean[] {false, false, false, false};

		//Populating the Wall coordinate arraylists
		for(int i = bounds.getY() + 1, count = 0; i < bounds.getY() + bounds.getHeight - 1; i++, count++) {
			//bounds.getY() + 1 ignores the corner blocks
			leftWall.add(new MapCoordinate(i, bounds.getX());
		}
		for(int i = bounds.getY() + 1, count = 0; i < bounds.getY() + bounds.getHeight - 1; i++, count++) {
			int col = bounds.getX() + bounds.getWidth - 1;
			rightWall.add(new MapCoordinate(i, col);
		}
		for(int i = bounds.getX() + 1, count = 0; i < bounds.getX() + bounds.getWidth - 1; i++, count++) {
			topWall.add(new MapCoordinate(bounds.getY(), i);
		}
		for(int i = bounds.getX() + 1, count = 0; i < bounds.getX() + bounds.getWidth - 1; i++, count++) {
			int row = bounds.getY() + bounds.getHeight - 1;
			bottomWall.add(new MapCoordinate(row, i);
		}
		
		leftWall.trimToSize();
		rightWall.trimToSize();
		topWall.trimToSize();
		bottomWall.trimToSize();
	}

	public ArrayList getDirectionWall(Direction dir) {
		switch(dir) {
			case LEFT: return leftWall;
			case RIGHT: return rightWall;
			case UP: return topWall;
			case DOWN: return bottomWall;
		}
		return null;
	}

	public void setConnectedSide(Direction dir) {
		connectedSide[dir.getValue()] = true;
	}

	public boolean isSideConnected(Direction dir) {
		return connectedSide[dir.getValue()];
	}

	public boolean isAllConnected() {
		return connectedSide[0] && connectedSide[1] && connectedSide[2] && connectedSide[3];
	}
}
