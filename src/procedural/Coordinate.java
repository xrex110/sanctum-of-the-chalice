package procedural;

public class Coordinate {
	public int row, col;
	
	public Coordinate(int row, int col) {
		this.row = row;
		this.col = col;
	}

	public void setValue(int row, int col) {
		this.row = row;
		this.col = col;
	}

	public void translate(int rowOffset, int colOffset) {
		row += rowOffset;
		col += colOffset;
	}

	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(!(o instanceof Coordinate)) return false;
		Coordinate a = (Coordinate) o;
		return (this.row == a.row && this.col == a.col);
	}

	@Override 
	public String toString() {
		return String.format("(row: %d, col: %d)", row, col);
	}
}
