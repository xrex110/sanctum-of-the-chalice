public class Room {
	public Coordinate origin;
	public int width, height;
	public int perimeter;
	public int area;

	private Coordinate[] left;
	private Coordinate[] right;
	private Coordinate[] top;
	private Coordinate[] bottom;
	private Coordinate[] corners;
	private boolean[] connectedSide;	//Order: left right top bottom

	public Room(Coordinate ul, int width, int height) {
		origin = ul;
		this.width = width;
		this.height = height;
		perimeter = (2 * width) + (2 * height);
		area = width * height;
		left = new Coordinate[height - 2];	//Subtract 2 for corner block exclusion
		right = new Coordinate[height - 2];
		top = new Coordinate[width - 2];
		bottom = new Coordinate[width - 2];
		//Origin clockwise
		corners = new Coordinate[4];		//Four corners allowed for now

		corners[0] = new Coordinate(origin.row, origin.col);
		corners[1] = new Coordinate(origin.row, origin.col + width - 1);
		corners[2] = new Coordinate(origin.row + height - 1, origin.col + width - 1);
		corners[3] = new Coordinate(origin.row + height - 1, origin.col);

		connectedSide = new boolean[] {false, false, false, false};

		for(int i = origin.row + 1, count = 0; i < origin.row + height - 1; i++, count++) {
			//origin.row + 1 ignores the corner blocks
			left[count] = new Coordinate(i, origin.col);
		}
		for(int i = origin.row + 1, count = 0; i < origin.row + height - 1; i++, count++) {
			//origin.row + 1 ignores the corner blocks
			int col = origin.col + width - 1;
			right[count] = new Coordinate(i, col);
		}
		for(int i = origin.col + 1, count = 0; i < origin.col + width - 1; i++, count++) {
			//origin.row + 1 ignores the corner blocks
			top[count] = new Coordinate(origin.row, i);
		}
		for(int i = origin.col + 1, count = 0; i < origin.col + width - 1; i++, count++) {
			//origin.row + 1 ignores the corner blocks
			int row = origin.row + height - 1;
			bottom[count] = new Coordinate(row, i);
		}	
	}

	public Coordinate[] getDirectionWall(Direction dir) {
		switch(dir) {
			case LEFT: return left;
			case RIGHT: return right;
			case UP: return top;
			case DOWN: return bottom;
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

	public Coordinate[] getCorners() {
		return corners;
	}

}
