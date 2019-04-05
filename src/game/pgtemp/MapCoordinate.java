public class MapCoordinate {
	public int row, col;
	
	public Coordinate(int row, int col) {
		this.row = row;
		this.col = col;
	}

	public void setValue(int row, int col) {
		this.row = row;
		this.col = col;
	}

	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(!(o instanceof MapCoordinate)) return false;
		MapCoordinate a = (MapCoordinate) o;
		return (this.row == a.row && this.col == a.col);
	}
}
