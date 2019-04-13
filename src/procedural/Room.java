package procedural;

import java.awt.Rectangle;
import java.util.ArrayList;

public class Room {
	//All Rooms are represented as awt.Rectangle objects under the hood
	public Rectangle bounds; //Stores x,y,width,height;
	//IMPORTANT: x and y of the top left corner, not row and col
	public int row, col;
	
	private ArrayList<Coordinate> leftWall;
	private ArrayList<Coordinate> rightWall;
	private ArrayList<Coordinate> topWall;
	private ArrayList<Coordinate> bottomWall;
	private ArrayList<Coordinate> corners;

	private boolean[] sidesConnected; //left right top bot

	public Room(Coordinate origin, int width, int height) {
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
		corners.add(new Coordinate(row, col));
		corners.add(new Coordinate(row, col + bounds.width - 1));
		corners.add(new Coordinate(row + bounds.height - 1, col + bounds.width - 1));
		corners.add(new Coordinate(row + bounds.height - 1, col));

		//Populating the Wall coordinate arraylists
		for(int i = bounds.y + 1, count = 0; i < bounds.y + bounds.height - 1; i++, count++) {
			//bounds.y + 1 ignores the corner blocks
			leftWall.add(new Coordinate(i, bounds.x));
		}
		for(int i = bounds.y + 1, count = 0; i < bounds.y + bounds.height - 1; i++, count++) {
			int col = bounds.x + bounds.width - 1;
			rightWall.add(new Coordinate(i, col));
		}
		for(int i = bounds.x + 1, count = 0; i < bounds.x + bounds.width - 1; i++, count++) {
			topWall.add(new Coordinate(bounds.y, i));
		}
		for(int i = bounds.x + 1, count = 0; i < bounds.x + bounds.width - 1; i++, count++) {
			int row = bounds.y + bounds.height - 1;
			bottomWall.add(new Coordinate(row, i));
		}
		
		leftWall.trimToSize();
		rightWall.trimToSize();
		topWall.trimToSize();
		bottomWall.trimToSize();
	}

	//This function accepts a x and y offset, and then translate ALL the
	//coordinate associated with this object according to the offset
	public void translate(int offsetX, int offsetY) {
		bounds.x += offsetX;
		bounds.y += offsetY;
		row += offsetY;
		col += offsetX;

		for(Coordinate pt : leftWall) pt.translate(offsetY, offsetX);
		for(Coordinate pt : rightWall) pt.translate(offsetY, offsetX);
		for(Coordinate pt : topWall) pt.translate(offsetY, offsetX);
		for(Coordinate pt : bottomWall) pt.translate(offsetY, offsetX);
		for(Coordinate pt : corners) pt.translate(offsetY, offsetX);

	}

	public ArrayList<Coordinate> getDirectionWall(Direction dir) {
		switch(dir) {
			case LEFT: return leftWall;
			case RIGHT: return rightWall;
			case UP: return topWall;
			case DOWN: return bottomWall;
		}
		return null;
	}

	public ArrayList<Coordinate> getCorners() {
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
