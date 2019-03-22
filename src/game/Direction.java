package game;
import main.*;

import render.*;
import sound.*;
import object.*;


public enum Direction {
	LEFT(0),
	RIGHT(1),
	UP(2),
	DOWN(3);

	private final int value;
	private Direction(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
