package object;
import main.*;


import java.awt.Graphics2D;
import render.*;
import game.*;
import sound.*;
import java.util.ArrayList;

public class TriggerList extends GameObject {
	
	public ArrayList<Interactable> triggers;
	public ArrayList<GameObject> rendered;
	
	public void draw(Graphics2D rend) {}

	public TriggerList(int x, int y) {
		super(x, y, false);
		triggers = new ArrayList<Interactable>();
		rendered = new ArrayList<GameObject>();
	}
}
