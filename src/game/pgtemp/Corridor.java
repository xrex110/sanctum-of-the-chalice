import java.awt.Rectangle;
import java.util.ArrayList;

/*
 *	For fat corridors, like:
 *		211112
 *		211112
 *		211112
 *		211112
 *		211112   heading = UP
 *		2X1112
 *	heading.pos is always the position X asa shown
 *	i.e. it is the first non wall block adjacent to the left wall, and at the start of the corridor
 *
 */

public class Corridor {
	public Rectangle bounds;	//Stores x,y,width,height
	//private Heading heading;	//Heading.pos stores the beginning of corridor in terms of generation.
	Direction direction;		//Direction of the corridor

	public int row, col;		//Row col of origin (upper left corner)
	public int breadth, length;

	//Right and left are based off of heading.dir
	private ArrayList<MapCoordinate> firstWall;
	private ArrayList<MapCoordinate> secondWall;

	private boolean[] sidesConnected;	//{firstWall, secondWall}

	public Corridor(MapCoordinate origin, Direction dir, int breadth, int length) {
		this.row = origin.row;
		this.col = origin.col;
		this.breadth = breadth;
		this.length = length;
		direction = dir;

		//Setting proper width and height for bounds
		if(direction == Direction.UP || direction == Direction.DOWN) {
			bounds = new Rectangle(col, row, breadth, length);
		}
		else {
			bounds = new Rectangle(col, row, length, breadth);	
		}
		//Now we populate the walls
		firstWall = new ArrayList<>();
		secondWall = new ArrayList<>();

		if(direction == Direction.UP || direction == Direction.DOWN) {
			for(int i = row; i < row + length; i++) {
				firstWall.add(new MapCoordinate(i, col));
				secondWall.add(new MapCoordinate(i, col + breadth - 1));
			}
		}
		else {
			for(int i = col; i < col + length; i++) {
				firstWall.add(new MapCoordinate(row, i));
				secondWall.add(new MapCoordinate(row + breadth - 1, i));
			}
		}
		firstWall.trimToSize();
		secondWall.trimToSize();
		sidesConnected = new boolean[] {false, false};
	}

	//Same as translate in Room.java
	//Note offsetY = rowOffset, offsetX = colOffset
	public void translate(int offsetX, int offsetY) {
		bounds.x += offsetX;
		bounds.y += offsetY;
		row += offsetY;
		col += offsetX;

		for(MapCoordinate pt : firstWall) pt.translate(offsetY, offsetX);
		for(MapCoordinate pt : secondWall) pt.translate(offsetY, offsetX);
	}

	public ArrayList<MapCoordinate> getFirstWall() {
		return firstWall;
	}

	public ArrayList<MapCoordinate> getSecondWall() {
		return secondWall;
	}

	public boolean isFirstSideConnected() {
		return sidesConnected[0];
	}

	public boolean isSecondSideConnected() {
		return sidesConnected[1];
	}

	public boolean isAllConnected() {
		return isFirstSideConnected() && isSecondSideConnected();
	}

	public void setFirstSideConnected() {
		sidesConnected[0] = true;
	}

	public void setSecondSideConnected() {
		sidesConnected[1] = true;
	}
}

// Code that is potentially usable much later.
/*	
		//Determining origin and bounds based on Heading pos and dir
		if(heading.direction == Direction.UP) {
			row = heading.position.row - length + 1;
			col = heading.position.col - 1;
			bounds = new Rectangle(col, row, breadth, length);
		}
		if(heading.direction == Direction.DOWN) {
			row = heading.position.row;
			col = heading.position.col - breadth - 2;		//Right next to the left wall
			bounds = new Rectangle(col, row, breadth, length);
		}
		if(heading.direction == Direction.RIGHT) {
			row = heading.position.row - 1;
			col = heading.position.col;
			bounds = new Rectangle(col, row, length, breadth);
		}
		if(heading.direction == Direction.LEFT) {
			row = heading.position.row - breadth - 2;
			col = heading.position.col - length + 1;;
			bounds = new Rectangle(col, row, length, breadth);
		}
*/
