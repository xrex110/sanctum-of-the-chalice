public class MapCoordinate {
	public int row, col;
	
	public MapCoordinate(int row, int col) {
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

	@Override 
	public String toString() {
		return String.format("(row: %d, col: %d)", row, col);
	}
}
